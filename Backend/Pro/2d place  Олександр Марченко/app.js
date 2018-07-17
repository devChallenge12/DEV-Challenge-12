const express = require('express')
const bodyParser = require('body-parser')
const dgram = require('dgram')
const {save, remove, lastValue, dump, all} = require('./storage')

const app = express()
app.use(bodyParser.text())

let socket = null
let lastpong = 0

const broadcast = (payload, to = null) => {
    payload.from = process.env.PRIVATE_IP
    const message = Buffer.from(JSON.stringify(payload))
    socket.send(message, 0, message.length, process.env.PRIVATE_PORT, to || process.env.PRIVATE_BROADCAST)
}

if (process.env.hasOwnProperty('PRIVATE_BROADCAST')) {
    const onSave = ({key, val, by, ts}) => save(key, val, by, ts)
    const onDelete = ({key}) => remove(key)
    const onHello = ({from}) => {
        broadcast({type: 'SYNC', data: all()}, from)
    }
    const onSync = ({data}) => {
        Object.keys(data).forEach(key => {
            data[key].forEach(({key, val, by, ts}) => save(key, val, by, ts))
        })
    }
    const onPing = ({from}) => {
        broadcast({type: 'PONG'}, from)
    }
    const onPong = () => {
        lastpong = Date.now()

        if (Date.now() - lastpong > 60000) {
            process.exit(1)
        }
    }

    socket = dgram.createSocket('udp4')

    socket.on('message', message => {
        let payload = null
        try {
            payload = JSON.parse(message.toString())
        } catch (error) {
            console.log(error)
        }
        if (!payload) {
            return
        }

        const isMessageFromMyself = payload.from === process.env.PRIVATE_IP
        if (isMessageFromMyself) {
            return
        }

        console.log(JSON.stringify(payload))
        switch(payload.type) {
            case 'HELLO': return onHello(payload)
            case 'DELETE': return onDelete(payload)
            case 'SAVE': return onSave(payload)
            case 'SYNC': return onSync(payload)
            case 'PING': return onPing(payload)
            case 'PONG': return onPong(payload)
        }
    })

    socket.on('listening', () => {
        socket.setBroadcast(true)
        broadcast({type: 'HELLO'})
    })

    socket.bind(process.env.PRIVATE_PORT, () => socket.setBroadcast(true))
    setInterval(() => broadcast({type: 'PING'}), 10000)
}

app.get('/', (req, res) => {
    res.set('Content-Type', 'application/x-yaml').send(dump())
})

app.get('/:key/history', (req, res) => {
    const {key} = req.params
    console.log(JSON.stringify({type: 'HTTP', method: 'GET', key}))
    const data = all(key)
    return data.length > 0
        ? res.json(data)
        : res.sendStatus(404)
})

app.get('/:key', (req, res) => {
    const {key} = req.params
    console.log(JSON.stringify({type: 'HTTP', method: 'GET', key}))
    const item = lastValue(key)
    return item 
        ? res.set('Content-Type', 'text/plain').send(item)
        : res.sendStatus(404)
})

app.delete('/:key', (req, res) => {
    const {key} = req.params
    console.log(JSON.stringify({type: 'HTTP', method: 'DELETE', key}))
    remove(key)
    if (socket) {
        broadcast({type: 'DELETE', key})
    }
    res.sendStatus(200)
})

app.post('/:key', (req, res) => {
    const {key} = req.params
    const val = req.body
    const by = 'user1' // TODO: take sub from JWT token
    const ts = Date.now()
    const item = save(key, val, by, ts)
    console.log(JSON.stringify({type: 'HTTP', method: 'POST', key, val}))
    if (socket && item) {
        broadcast({type: 'SAVE', key, val, by, ts})
    }
    res.sendStatus(200)
})

module.exports = app
