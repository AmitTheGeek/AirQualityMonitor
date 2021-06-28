package com.amit.airqualitymonitor.websocket

interface WebSocketCallback {

    fun onMessageReceived(message: String?)
}