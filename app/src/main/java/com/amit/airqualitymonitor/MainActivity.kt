package com.amit.airqualitymonitor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.ssl.SSLSocketFactory


class MainActivity : AppCompatActivity() {
    private lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        initWebSocket()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        webSocketClient.close()
        Log.d(TAG, "onPause: ")
    }

    private fun initWebSocket() {
        val airQualityUri: URI? = URI(WEB_SOCKET_URL)

        createWebSocketClient(airQualityUri)
        Log.d(TAG, "initWebSocket: ")

        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun createWebSocketClient(coinbaseUri: URI?) {
        Log.d(TAG, "createWebSocketClient: ")
        webSocketClient = object : WebSocketClient(coinbaseUri) {

            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                subscribe()
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose  $code $reason $remote")
                unsubscribe()
            }

            override fun onError(ex: Exception?) {
                Log.e("createWebSocketClient", "onError: ${ex?.message}")
            }

        }
    }

    private fun subscribe() {
        webSocketClient.send(
                "{\n" +
                        "    \"type\": \"subscribe\"}"
        )
    }

    private fun unsubscribe() {
        webSocketClient.send(
                "{\n" +
                        "    \"type\": \"unsubscribe\"}"
        )
    }
    companion object {
        const val WEB_SOCKET_URL = "wss://city-ws.herokuapp.com/"
        const val TAG = "MainActivity"
    }

}