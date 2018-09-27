const assert = require('assert');
const IgniteBridge = require('../index.js');
const igniteConfig = require('./resources/igniteConfig');

const CACHE_KEY         = 1,
      CACHE_VALUE       = 'test_value',
      CACHE_TYPE        = 'com.hugheba.graal.js.ignite.Cache',
      COUNTER_NAME      = 'test_counter',
      COUNTER_TYPE      = 'com.hugheba.graal.js.ignite.Counter',
      TOPIC_NAME        = 'test_topic',
      TOPIC_MSG         = 'This is an event message',
      RECORD_NAME       = 'record1',
      RECORD_TYPE       = 'com.hugheba.graal.js.ignite.Record';

describe('Ignite', () => {
    const igniteBridge = new IgniteBridge(igniteConfig);

    describe('Cache', () => {
        const defaultCache = igniteBridge.getCache('default');

        it('should cache be Java.isJavaObject', () => {
            assert.equal(true, Java.isJavaObject(defaultCache));
        });

        it(`should cache be Java.typeName ${CACHE_TYPE}`, () => {
            assert.equal(CACHE_TYPE, defaultCache.getClass().getName());
        });

        it('should set cache value', () => {
            assert.doesNotThrow(() => {
                defaultCache.put(CACHE_KEY, CACHE_VALUE);
            });
        });

        it('should get cache value', () => {
            let val = defaultCache.get(CACHE_KEY);
            assert.equal(CACHE_VALUE, val);
        });
    });

    describe('Counter', () => {
        const counter = igniteBridge.getCounter(COUNTER_NAME);

        it('should counter be Java.isJavaObject', () => {
            assert.equal(true, Java.isJavaObject(counter));
        });

        it(`should counter be Java.typeName ${COUNTER_TYPE}`, () => {
            assert.equal(COUNTER_TYPE, counter.getClass().getName());
        });

        it('should counter initially be 0', () => {
            assert.equal(0, counter.get());
        });

        it('should counter incrementAndGet = 1', () => {
            assert.equal(1, counter.incrementAndGet());
        });

        it('should counter decrementAndGet = 0', () => {
            assert.equal(0, counter.decrementAndGet());
        });

    });

    describe('EventBus', () => {
        const eventBus = igniteBridge.getEventBus();
        it ('should subscribe and broadcas events', (done) => {
            eventBus.subscribe(TOPIC_NAME, (msg) => {
                assert.equal(TOPIC_MSG, msg);
                done();
            });
            eventBus.broadcast(TOPIC_NAME, TOPIC_MSG);
        });
    });

    describe('Record', () => {
        let record1 = igniteBridge.getRecord(RECORD_NAME),
            PROP_FIRSTNAME = 'firstname';

        it (`should record be Java.typeName ${RECORD_TYPE}`, () => {
            assert.equal(RECORD_TYPE, record1.getClass().getName());
        });

        it ('should record set and get', () => {
            let FIRSTNAME_BOB = 'Bob';
            record1.put(PROP_FIRSTNAME, FIRSTNAME_BOB);
            assert.equal(FIRSTNAME_BOB, record1.get(PROP_FIRSTNAME));
        });

        it ('should record.subscribe', (done) => {
            let FIRSTNAME_SAM = 'Sam';
            record1.subscribe(PROP_FIRSTNAME, (firstname) => {
                console.log(`Got should record.subscribe message ${firstname}`);
                assert.equal(FIRSTNAME_SAM, firstname);
                done();
            });
            record1.put(PROP_FIRSTNAME, FIRSTNAME_SAM);
        });

    });
});