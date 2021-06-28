package com.amit.airqualitymonitor.websocket

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class WebSocketHandler(val callback: WebSocketCallback) {

    private lateinit var webSocketClient: WebSocketClient

    companion object {
        const val WEB_SOCKET_URL = "wss://city-ws.herokuapp.com/"
        const val TAG = "WebSocketHandler"
    }


    fun startConnection() {
        Log.d(TAG, "initWebSocket: ")
        val airQualityUri: URI? = URI(WEB_SOCKET_URL)
        createWebSocketClient(airQualityUri)
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()

    }


    fun stopConnection() {
        webSocketClient.close()
    }

    private fun createWebSocketClient(airQualityUri: URI?) {
        Log.d(TAG, "createWebSocketClient: ")
        webSocketClient = object : WebSocketClient(airQualityUri) {

            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
                callback.onMessageReceived(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose  $code $reason ")
            }

            override fun onError(ex: Exception?) {
                Log.e("createWebSocketClient", "onError: ${ex?.message}")
            }

        }
    }

}