const assert = require('assert');
const ignition = require('./resources/igniteUtil');

const testValues = require('./resources/testValues');

const igniteBridge = ignition();

describe('Ignite', () => {

    describe('Cache', () => {
        const defaultCache = igniteBridge.getCache('default');

        it('should cache be Java.isJavaObject', () => {
            assert.equal(true, Java.isJavaObject(defaultCache));
        });

        it(`should cache be Java.typeName ${testValues.CACHE_TYPE}`, () => {
            assert.equal(testValues.CACHE_TYPE, defaultCache.getClass().getName());
        });

        it('should set cache value', () => {
            assert.doesNotThrow(() => {
                defaultCache.put(testValues.CACHE_KEY, testValues.CACHE_VALUE);
            });
        });

        it('should get cache value', () => {
            let val = defaultCache.get(testValues.CACHE_KEY);
            assert.equal(testValues.CACHE_VALUE, val);
        });
    });

});