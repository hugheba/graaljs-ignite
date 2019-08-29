const assert = require('assert');
const ignition = require('./resources/igniteUtil');

const igniteBridge = ignition();

describe('Ignite', () => {

    let igniteBridge;

    describe('Lifecycle', () => {
        // it('should instanciate and call lifecycle events', (done) => {
        //     igniteConfig.afterIgniteStart = () => {
        //         console.log(`AfterIgniteStart executed!`);
        //         done();
        //     };
        //     igniteBridge = new IgniteBridge(igniteConfig);
        //     done();
        // });

        after(() => {
            try {
                if (igniteBridge) igniteBridge.shutdown();
            } catch(e) {
                console.log(`Error shutting down: ${e.message}`);
            }
        })
    });



});