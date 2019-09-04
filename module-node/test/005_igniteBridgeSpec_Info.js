const assert = require('assert');
const ignition = require('./resources/igniteUtil');

const igniteBridge = ignition();

describe('Ignite', () => {

    describe('Info', () => {
        it('should first clusterNode be local', () => {
            const info = igniteBridge.getInfo();
            assert(info != undefined, `Ignite info receive`);
            const clusterNodes = info.getClusterNodes();
            const node = clusterNodes[0];

            assert(node.getLocal(), `Ignite node 0 is local`);
            assert(!node.getClient(), `Ignite node 0 is NOT client`);
        })
    });

});