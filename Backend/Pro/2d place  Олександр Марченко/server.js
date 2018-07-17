const app = require('./app')

const port = process.env.PUBLIC_PORT || 3000
const host = '0.0.0.0'

app.listen(port, host, () => console.log({type: 'STARTED', host, port}))