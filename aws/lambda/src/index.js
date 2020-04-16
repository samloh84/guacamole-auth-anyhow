import EC2 from 'aws-sdk/clients/ec2';
import * as Promise from 'bluebird';
import {
    find as _find,
    flatten as _flatten,
    get as _get,
    isEmpty as _isEmpty,
    lowerCase as _lowerCase,
    map as _map,
    set as _set,
    split as _split
} from 'lodash';

export const handler = function () {

    let ec2 = new EC2({apiVersion: '2016-11-15', region: 'ap-southeast-1'});
    ec2.config.setPromisesDependency(Promise);

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

    return ec2.describeInstances(params)
        .promise()
        .then(function (response) {

            let instances = _flatten(_map(response.Reservations, 'Instances'));
            let connections = _map(instances, function (instance) {
                let configuration = {};
                let configurationId = _get(_find(instance.Tags, {Key: 'Name'}), 'Value', _get(instance, 'InstanceId'));
                _set(configuration, 'identifier', configurationId);
                _set(configuration, 'name', configurationId);
                if (_lowerCase(instance.Platform) === "windows") {
                    _set(configuration, 'protocol', 'rdp');
                    _set(configuration, ['parameters', 'hostname'], instance.PrivateIpAddress);
                    _set(configuration, ['parameters', 'port'], '3389');
                    _set(configuration, ['parameters', 'security'], 'tls');
                    _set(configuration, ['parameters', 'ignore-cert'], 'true');
                    _set(configuration, ['parameters', 'disable-auth'], 'true');

                } else {
                    _set(configuration, 'protocol', 'ssh');
                    _set(configuration, ['parameters', 'hostname'], instance.PrivateIpAddress);
                    _set(configuration, ['parameters', 'port'], '22');
                }

                return configuration;
            });

            return {"connections": connections};
        })
        .tap(function (configurations) {
            if (debug === "true") {
                console.debug(configurations);
            }
        });
};

