###
Module dependencies.
###
express = require("express")
http = require("http")
path = require("path")

app = express()
app.configure ->
  public_path = path.join(__dirname, "public")
  views_path  = path.join(__dirname, "views")
  production  = process.env.PRODUCTION
  app.set "port", process.env.PORT or 5000
  app.set "views", views_path
  app.set "view engine", "jade"
  app.use express.favicon()
  app.use express.logger("dev")
  app.use express.bodyParser()
  app.use express.methodOverride()
  app.use app.router
  app.use require("less-middleware")(src: public_path)
  app.use require("express-coffee")(path: public_path, live: not production, uglify: production)
  app.use express.static(public_path)

app.configure "development", ->
  app.use express.errorHandler()

server = http.createServer(app)
io = require("socket.io").listen(server)
server.listen app.get("port")

class WaitList
  constructor: (@io) ->
    @wait_list = {}
    @count = 0
  log: (message) ->
    (socket) -> console.log message
  add: (uuid) ->
    return if @wait_list[uuid]
    @count = @count += 1
    @wait_list[uuid] = @io.of(uuid).on 'connection', @log("#{uuid} connected")
    @io.sockets.emit('count', count: @count)
  call: (uuid) ->
    @wait_list[uuid]?.emit('call')

wait_list = new WaitList(io)

app.get "/", (req, res) ->
  res.render 'index', title: 'Denwaly', count: wait_list.count

app.get "/wait/:uuid", (req, res) ->
  {uuid} = req.params
  console.log "add #{uuid} to wait_list"
  wait_list.add(uuid)
  res.render 'wait', title: uuid, uuid: uuid

app.get "/call/:uuid", (req, res) ->
  {uuid} = req.params
  console.log "call from #{uuid}"
  wait_list.call(uuid)
  res.end()
