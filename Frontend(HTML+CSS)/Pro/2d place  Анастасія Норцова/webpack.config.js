const webpack = require('webpack');
module.exports = {
    entry: './src/app.js',
    output: {
        filename: './bundle.js'
    },
    module: {
        rules:[
            {
                test: /\.js$/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            presets: [
                                ['es2015']
                            ]
                        }
                    }
                ]
            }
        ]
    }
};
