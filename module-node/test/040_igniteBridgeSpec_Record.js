const assert = require('assert');
const ignition = require('./resources/igniteUtil');

const testValues = require('./resources/testValues');

const igniteBridge = ignition();

describe('Ignite', () => {

    describe('Record', () => {
        let record1 = igniteBridge.getRecord(testValues.RECORD_NAME),
            PROP_FIRSTNAME = 'firstname';

        it (`should record be Java.typeName ${testValues.RECORD_TYPE}`, () => {
            assert.strictEqual(testValues.RECORD_TYPE, record1.getClass().getName());
        });

        it ('should record set and get', () => {
            let FIRSTNAME_BOB = 'Bob';
            record1.put(PROP_FIRSTNAME, FIRSTNAME_BOB);
            assert.strictEqual(FIRSTNAME_BOB, record1.get(PROP_FIRSTNAME));
        });

        it ('should record.subscribe', (done) => {
            let FIRSTNAME_SAM = 'Sam';
            record1.subscribe(PROP_FIRSTNAME, (firstname) => {
                console.log(`Got should record.subscribe message ${firstname}`);
                assert.strictEqual(FIRSTNAME_SAM, firstname);
                done();
            });
            record1.put(PROP_FIRSTNAME, FIRSTNAME_SAM);
        }).timeout(1000 * 10);

    });
});