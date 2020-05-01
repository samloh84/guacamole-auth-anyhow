import {handler as guacamole_config} from './src/index'

 guacamole_config()
    .then(function(data){
        console.log(JSON.stringify(data, null, 4));
    });
