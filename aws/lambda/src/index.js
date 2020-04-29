const EC2 = require('aws-sdk/clients/EC2');
const Promise = require('bluebird');
const _ = require('lodash');
const _each = require('lodash/each');
const _map = require('lodash/map');
const _get = require('lodash/get');
const _set = require('lodash/set');
const _flatten = require('lodash/flatten');
const _isEmpty = require('lodash/isEmpty');
const _find = require('lodash/find');
const _lowerCase = require('lodash/lowerCase');
const _has = require('lodash/has');
const _split = require('lodash/split');
const _filter = require('lodash/filter');
const _concat = require('lodash/concat');

const instancesAttributeMap = {
    'image_id': 'ImageId',
    'instance_id': 'InstanceId',
    'instance_type': 'InstanceType',
    'key_name': 'KeyName',
    'launch_time': 'LaunchTime',
    'placement_availability_zone': 'Placement.AvailabilityZone',
    'placement_group_name': 'Placement.GroupName',
    'placement_tenancy': 'Placement.Tenancy',
    'private_dns_name': 'PrivateDnsName',
    'private_ip_address': 'PrivateIpAddress',
    'public_dns_name': 'PublicDnsName',
    'public_ip_address': 'PublicIpAddress',
    'subnet_id': 'SubnetId',
    'vpc_id': 'VpcId',
    'owner_id': 'OwnerId',
    'reservation_id': 'ReservationId'
}

const subnetAttributeMap = {
    'image_id': 'ImageId',
    'instance_id': 'InstanceId',
    'instance_type': 'InstanceType',
    'key_name': 'KeyName',
    'launch_time': 'LaunchTime',
    'placement_availability_zone': 'Placement.AvailabilityZone',
    'placement_group_name': 'Placement.GroupName',
    'placement_tenancy': 'Placement.Tenancy',
    'private_dns_name': 'PrivateDnsName',
    'private_ip_address': 'PrivateIpAddress',
    'public_dns_name': 'PublicDnsName',
    'public_ip_address': 'PublicIpAddress',
    'subnet_id': 'SubnetId',
    'vpc_id': 'VpcId',
    'owner_id': 'OwnerId',
    'reservation_id': 'ReservationId'
}


export const handler = function () {

    let ec2 = new EC2({apiVersion: '2016-11-15', region: 'ap-southeast-1'});

    let vpcIds = _get(process.env, 'VPC_IDS', "");
    if (!_isEmpty(vpcIds)) {
        vpcIds = _split(vpcIds, /[ ,]+/);
    }

    let debug = _get(process.env, 'DEBUG', "false");


    let filters = [];

    if (!_isEmpty(vpcIds)) {
        filters.push({
            Name: "vpc-id",
            Values: vpcIds
        })
    }

    let params = {};

    if (!_isEmpty(filters)) {
        _set(params, 'Filters', filters);
    } else {
        params = null;
    }

    return Promise.resolve(ec2.describeInstances(params).promise())
        .then(function (instancesResponse) {
            let instances = _flatten(_map(_get(instancesResponse, 'Reservations'), 'Instances'));
            let vpcIds = _map(instances, 'VpcId');
            let subnetIds = _map(instances, 'SubnetId');
            //let securityGroupIds = _map(_flatten(_map(instances, 'SecurityGroups')), 'GroupId');

            let describeVpcsPromise = _isEmpty(vpcIds) ? null : Promise.resolve(ec2.describeVpcs({VpcIds: vpcIds}).promise());
            let describeSubnetsPromise = _isEmpty(subnetIds) ? null : Promise.resolve(ec2.describeSubnets({SubnetIds: subnetIds}).promise());
            //let describeSecurityGroupsPromise = _isEmpty(securityGroupIds) ? null : Promise.resolve(ec2.describeSecurityGroups({GroupIds: securityGroupIds}).promise());

            return Promise.props({
                instances: instancesResponse,
                vpcs: describeVpcsPromise,
                subnets: describeSubnetsPromise,
              //  securityGroups: describeSecurityGroupsPromise
            });
        })
        .then(function (response) {
            let instancesResponse = _get(response, 'instances');
            let instances = _flatten(_map(_get(instancesResponse, 'Reservations'), 'Instances'));

            let connections = _map(instances, function (instance) {
                let connection = {};
                let instanceTags = _get(instance, 'Tags');

                let instanceId = _get(instance, 'InstanceId');
                let instanceName = _get(_find(instanceTags, {Key: 'Name'}), 'Value', instanceId);

                _set(connection, 'identifier', instanceId);
                _set(connection, 'name', instanceName);
                if (_lowerCase(instance.Platform) === "windows") {
                    _set(connection, 'protocol', 'rdp');
                    _set(connection, ['parameters', 'hostname'], instance.PrivateIpAddress);
                    _set(connection, ['parameters', 'port'], '3389');
                    _set(connection, ['parameters', 'security'], 'tls');
                    _set(connection, ['parameters', 'ignore-cert'], 'true');
                    _set(connection, ['parameters', 'disable-auth'], 'true');

                } else {
                    _set(connection, 'protocol', 'ssh');
                    _set(connection, ['parameters', 'hostname'], instance.PrivateIpAddress);
                    _set(connection, ['parameters', 'port'], '22');
                }

                _each(instancesAttributeMap, function (path, key) {
                    if (_has(instance, path)) {
                        _set(connection, ['attributes', key], _get(instance, path));
                    }
                });

                if (!_isEmpty(instanceTags)) {
                    _each(instance.Tags, function (tag) {
                        _set(connection, ['attributes', `tag:${tag.Key}`], tag.Value);
                    });
                }


                return connection;
            });


            let subnetsResponse = _get(response, 'subnets');
            let subnets = _get(subnetsResponse, 'Subnets');
            let vpcsResponse = _get(response, 'vpcs');
            let vpcs = _get(vpcsResponse, 'Vpcs');
            //let securityGroupsResponse = _get(response, 'securityGroups');
            //let securityGroups = _get(securityGroupsResponse, 'SecurityGroups');


            let subnetConnectionGroups = _map(subnets, function (subnet) {

                let connectionGroup = {};
                let subnetTags = _get(subnet, 'Tags');


                let subnetId = _get(subnet, 'SubnetId');
                let subnetName = _get(_find(subnetTags, {Key: 'Name'}), 'Value', subnetId);

                _set(connectionGroup, 'identifier', subnetId);
                _set(connectionGroup, 'name', subnetName);

                let connections = _map(_filter(instances, {SubnetId: subnetId}), 'InstanceId');
                _set(connectionGroup, 'connections', connections);

                return connectionGroup;
            });


            // let securityGroupConnectionGroups = _map(securityGroups, function (securityGroup) {
            //
            //     let connectionGroup = {};
            //
            //     let securityGroupId = _get(securityGroup, 'GroupId');
            //     let securityGroupName = _get(securityGroup, 'GroupName');
            //
            //     _set(connectionGroup, 'identifier', securityGroupId);
            //     _set(connectionGroup, 'name', securityGroupName);
            //
            //     let connections = _map(_filter(instances, function (instance) {
            //         return !_isEmpty(_find(_get(instance, 'SecurityGroups'), {GroupId: securityGroupId}));
            //     }), 'InstanceId');
            //     _set(connectionGroup, 'connections', connections);
            //
            //     return connectionGroup;
            // });


            let vpcConnectionGroups = _map(vpcs, function (vpc) {

                let connectionGroup = {};
                let vpcTags = _get(vpc, 'Tags');


                let vpcId = _get(vpc, 'VpcId');
                let vpcName = _get(_find(vpcTags, {Key: 'Name'}), 'Value', vpcId);

                _set(connectionGroup, 'identifier', vpcId);
                _set(connectionGroup, 'name', vpcName);

                let subnetIds = _map(_filter(subnets, {VpcId: vpcId}), 'SubnetId');
                //let securityGroupIds = _map(_filter(securityGroups, {VpcId: vpcId}), 'GroupId');
                //let connectionGroups = _concat([], subnetIds, securityGroupIds);
                let connectionGroups = _concat([], subnetIds);
                _set(connectionGroup, 'connectionGroups', connectionGroups);

                return connectionGroup;
            });

            //let connectionGroups = _concat([], subnetConnectionGroups, securityGroupConnectionGroups, vpcConnectionGroups);
            let connectionGroups = _concat([], subnetConnectionGroups, vpcConnectionGroups);
            return {
                "connections": connections,
                "connectionGroups": connectionGroups
            };
        })
        .tap(function (configurations) {
            if (debug === "true") {
                console.debug(configurations);
            }
        });
};

