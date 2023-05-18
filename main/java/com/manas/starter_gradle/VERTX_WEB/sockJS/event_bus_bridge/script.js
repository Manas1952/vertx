var sock = new SockJS('http://localhost:8080/myApp');
var eb = new EventBus('http://localhost:8080/myApp');

eb.enableReconnect(true);

eb.onopen = function() {
 console.log('open');

 var count = 0;
 eb.registerHandler('hello.publish', (error, message) => {
    count++;
    console.log(count + ', received a message: ' + JSON.stringify(message));
  });

  eb.send('hello.consume', {name: 'tim', age: 587});
};

sock.onmessage = function(e) {
 console.log('onmessage -> ', e.data);
};

sock.onevent = function(event, message) {
 console.log('event: %o, message:%o', event, message);
 return true; // in order to signal that the message has been processed
};

sock.onunhandled = function(json) {
 console.log('this message has no address:', json);
};

sock.onclose = function() {
 console.log('close');
};

//sock.close();
