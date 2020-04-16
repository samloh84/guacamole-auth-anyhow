const path = require('path');

module.exports = {
    mode: 'production',
    entry: {
        handler: path.resolve(__dirname, 'src', 'index.js')
    },
    externals: [
        'aws-sdk'
    ],
    output: {
        path: path.resolve(__dirname, 'output'),
        filename: 'index.js',
        libraryTarget: 'commonjs'
    },
    target: 'node',
    module: {
        rules: [
            {test: /\.js$/, exclude: /node_modules/, loader: "babel-loader"}
        ]
    }
};