'use strict';
var messageForm = document.querySelector('#messageForm');
var textInput = document.querySelector('#text');
var userInput = document.querySelector('#user');
var companyInput = document.querySelector('#company');
var stompClient = null;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
function connect() {

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    // Tell your username to the server;
}
function onError(error) {

}

function sendMessage(event) {
    if(stompClient && textInput.value.trim() != '') {
        var chatComment = {
            text: textInput.value,
            companyId: 1,
            userId: 1
        };
        stompClient.send("/app/sendMessage", {}, JSON.stringify(chatComment));
        textInput.value = '';
    }
    event.preventDefault();
}
function onMessageReceived(payload) {
    $("div.msg_history").empty();
    generateChat(payload);
}

function generateChat(payload) {
    $.each(JSON.parse(payload.body),function(index,value){
        var html = '<div>' + value['text'] +
                '</div>';
        $("div.msg_history").append(html);
    });

}
connect();
messageForm.addEventListener('submit', sendMessage, true);
