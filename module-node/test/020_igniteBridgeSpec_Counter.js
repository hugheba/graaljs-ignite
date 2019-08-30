const assert = require('assert');
const ignition = require('./resources/igniteUtil');

const testValues = require('./resources/testValues');

const igniteBridge = ignition();

describe('Ignite', () => {

    describe('Counter', () => {
        const counter = igniteBridge.getCounter(testValues.COUNTER_NAME);

        it('should counter be Java.isJavaObject', () => {
            assert.strictEqual(true, Java.isJavaObject(counter));
        });

        it(`should counter be Java.typeName ${testValues.COUNTER_TYPE}`, () => {
            assert.strictEqual(testValues.COUNTER_TYPE, counter.getClass().getName());
        });

        it('should counter initially be 0', () => {
            assert.strictEqual(0, counter.get());
        });

        it('should counter incrementAndGet = 1', () => {
            assert.strictEqual(1, counter.incrementAndGet());
        });

        it('should counter decrementAndGet = 0', () => {
            assert.strictEqual(0, counter.decrementAndGet());
        });

    });

});