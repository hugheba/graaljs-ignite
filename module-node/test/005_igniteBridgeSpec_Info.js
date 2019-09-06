const assert = require('assert');
const ignition = require('./resources/igniteUtil');

const igniteBridge = ignition();

describe('Ignite', () => {

    describe('Info', () => {
        it('should first clusterNode be local', () => {
            const info = JSON.parse(igniteBridge.getInfo());
            assert(info != undefined, `Ignite info receive`);
            const clusterNodes = info.clusterNodes;
            const node = clusterNodes[0];

            assert(node.local, `Ignite node 0 is local`);
            assert(!node.client, `Ignite node 0 is NOT client`);
        })
    });

});