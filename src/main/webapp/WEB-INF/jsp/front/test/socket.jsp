<%--
  Created by IntelliJ IDEA.
  User: gang.wang
  Date: 2019/5/16
  Time: 14:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>websocket</h1>
<script>
    var websocket = window.WebSocket || window.MozWebSocket;
    var isConnected = false;
    initWebSocket("ws://localhost:" + location.port + "/xxxx/websocket/111");

    function onOpen() {
        isConnected = true;
    }

    function onClose() {
        isConnected = false;
    }

    function onError() {
        isConnected = false;
    }

    function onSend(message) {
        if (websocket != null) {
            websocket.send(JSON.stringify(message));
        } else {
            console.log("您已经掉线，无法与服务器通信!");
        }
    }

    function initWebSocket(wcUrl) {
        if (window.WebSocket) {
            websocket = new WebSocket(encodeURI(wcUrl));
            websocket.onopen = onOpen;
            websocket.onerror = onError;
            websocket.onclose = onClose;
            websocket.onmessage = onMessage;
        } else {
            console.log("您的设备不支持websocket");
        }
    }

    function onMessage(message) {
        console.log("websocket message:", message);
    }
</script>
</body>
</html>
