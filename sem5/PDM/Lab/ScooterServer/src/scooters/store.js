import dataStore from 'nedb-promise';

export class ScooterStore {
    constructor({filename, autoload}) {
        this.store = dataStore({filename, autoload});
    }

    async find(props) {
        return this.store.find(props);
    }

    async findOne(props) {
        return this.store.findOne(props);
    }

    async insert(scooter) {
        if (!scooter.locked || typeof scooter.locked !== 'boolean') {
            throw new Error('Invalid locked property')
        }
        if (!scooter.batteryLevel || typeof scooter.batteryLevel !== 'number' || scooter.batteryLevel < 0 || scooter.batteryLevel > 100) {
            throw new Error('Invalid batteryLevel property')
        }
        return this.store.insert(scooter);
    };

    async update(props, scooter) {
        return this.store.update(props, scooter);
    }

    async remove(props) {
        return this.store.remove(props);
    }
}

export default new ScooterStore({filename: './db/scooters.json', autoload: true});