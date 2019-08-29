const assert = require('assert');
const ignition = require('./resources/igniteUtil');

const testValues = require('./resources/testValues');

const igniteBridge = ignition();

describe('Ignite', () => {

    describe('EventBus', () => {
        const eventBus = igniteBridge.getEventBus();
        it ('should subscribe and broadcas events', (done) => {
            eventBus.subscribe(testValues.TOPIC_NAME, (msg) => {
                assert.equal(testValues.TOPIC_MSG, msg);
                done();
            });
            eventBus.broadcast(testValues.TOPIC_NAME, testValues.TOPIC_MSG);
        });
    });

});