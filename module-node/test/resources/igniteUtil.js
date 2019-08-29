const IgniteBridge = require('../../index.js');
const igniteConfig = require('./igniteConfig');

var ignite ;

const ignition = () => {
    if (!ignite) {
        ignite = new IgniteBridge(igniteConfig);
    }
    return ignite;
};

module.exports = ignition;