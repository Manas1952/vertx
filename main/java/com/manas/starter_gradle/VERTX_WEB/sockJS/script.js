var sock = new SockJS('http://localhost:8080/myApp');

sock.onopen = function() {
 console.log('open');
 sock.send('test');
};

sock.onmessage = function(e) {
 console.log(e.data);
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
