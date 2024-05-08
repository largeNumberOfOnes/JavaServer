function main() {
    console.log("start main");
    // let socket = new WebSocket("ws://localhost:8080/");
    window.WebSocket = window.WebSocket || window.MozWebSocket;
    // let socket = new WebSocket("ws://localhost:8080/socket");
    let socket = new WebSocket("ws://localhost:8080");
    // let socket = new WebSocket("ws://10.55.134.49:8080");
    // let socket = new WebSocket("localhost:8080");
    socket.onopen = function(e) {
        console.log("[open] Соединение установлено");
        console.log("Отправляем данные на сервер");
        socket.send("Меня зовут Джон");
    };
    socket.onerror = function(error) {
        console.log("[error]");
    };
    socket.onclose = function(event) {
        if (event.wasClean) {
            alert("[close] Соединение закрыто чисто, код=${event.code} причина=${event.reason}");
        } else {
        // например, сервер убил процесс или сеть недоступна
        // обычно в этом случае event.code 1006
            alert('[close] Соединение прервано');
        }
    };

}
function sendfn() {
    console.log("send function");
    socket.send("11111");

}