$ ->
	wait_id = location.pathname.split('/').pop()
	socket = io.connect()
	socket.on 'connect', ->
		console.log 'connected'
	socket.on 'disconnect', ->
		console.log 'disconnected'
	socket.on 'count', (data) ->
		$('#count').text(data.count)
	if wait_id isnt ''
		socket.of(wait_id).on 'call', ->
			alert 'calling'
