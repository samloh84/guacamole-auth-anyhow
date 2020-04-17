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



const attributeMap = {

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
        .then(function (response) {
            let instances = _flatten(_map(response.Reservations, 'Instances'));
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

                _each(attributeMap, function (path, key) {
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

            return {"connections": connections};
        })
        .tap(function (configurations) {
            if (debug === "true") {
                console.debug(configurations);
            }
        });
};

