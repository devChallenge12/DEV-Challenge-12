
const data = {}
const deletedKeys = {}

const find = (key, val, by = null, ts = null) => data[key].find(item => item.key === key && item.val === val && item.by === by && item.ts === ts)

const last = key => data.hasOwnProperty(key)
    ? data[key][data[key].length - 1]
    : {val: null, by: null, ts: 0}

const lastValue = key => last(key).val
const lastTimestamp = key => last(key).ts


const save = (key, val, by = null, ts = null) => {
    if (!data.hasOwnProperty(key)) {
        data[key] = []
    }

    const item = {key, val, by, ts: ts || Date.now()}

    if (!ts) {
        data[key].push(item)
        return item
    } else {
        const found = find(key, val, by, ts)
        if (!found) {
            data[key].push(item)
            return item
        }
    }

    return null
}

const remove = key => {
    delete data[key]
    deletedKeys[key] = Date.now()
}

const dump = () => Object.keys(data).map(key => `${key}: ${lastValue(key)}`).join('\n') + '\n'

const keys = () => {
    const items = {}
    Object.keys(data).forEach(key => items[key] = lastTimestamp(key))
    return items
}

const deletions = () => deletedKeys

const all = () => data

module.exports = {save, remove, lastValue, all, keys, dump, deletions}