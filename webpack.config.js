var path = require('path');

module.exports = {
    entry: { app: './src/main/js/index.js', css: './src/main/resources/scss/_custom.scss'},
    devtool: 'sourcemaps',
    cache: true,
    output: {
        path: __dirname + '/src/main/resources/static/built',
        filename: '[name].js'
    },
    module: {
        rules: [
            {
                test: /\.(scss)$/,
                exclude: /(node_modules|bower_components)/,
                use: [{
                    loader: 'style-loader', // inject CSS to page
                }, {
                    loader: 'css-loader', // translates CSS into CommonJS modules
                }, {
                    loader: 'postcss-loader', // Run post css actions
                    options: {
                        plugins: function () { // post css plugins, can be exported to postcss.config.js
                            return [
                                require('precss'),
                                require('autoprefixer')
                            ];
                        }
                    }
                }, {
                    loader: 'sass-loader' // compiles Sass to CSS
                }]
            },
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['babel-preset-env', 'react']
                    }
                }
            }
        ]
    }
};