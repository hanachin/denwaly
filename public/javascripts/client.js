
  $(function() {
    var socket, wait_id;
    wait_id = location.pathname.split('/').pop();
    socket = io.connect();
    socket.on('connect', function() {
      return console.log('connected');
    });
    socket.on('disconnect', function() {
      return console.log('disconnected');
    });
    socket.on('count', function(data) {
      return $('#count').text(data.count);
    });
    if (wait_id !== '') {
      return socket.of(wait_id).on('call', function() {
        return alert('calling');
      });
    }
  });
