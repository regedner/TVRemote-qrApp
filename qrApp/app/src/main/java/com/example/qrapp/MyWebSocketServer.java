package com.example.qrapp;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.Collection;

public class MyWebSocketServer extends WebSocketServer {

    public MyWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }



    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d("buttonPress", "bağlantı sağlandı");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d("websocket", "Bağlantı kapatıldı: " + reason);
        if (conn != null) {
            for (WebSocket socket : getConnections()) {
                socket.close();
            }
            conn.close();

        }
    }

    public void onMessage(WebSocket conn, String message) {
        String keyCode = "";

        if (message.startsWith("TEXT:")) {
            try {
                Log.d("buttonPress", formatMessage(message));
                Runtime.getRuntime().exec("input text " + formatMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }

        switch (message) {
            case "POWER":
                keyCode = "KEYCODE_POWER";
                break;

            case "MUTE":
                keyCode = "KEYCODE_VOLUME_MUTE";
                break;
            case "HOME":
                keyCode = "KEYCODE_HOME";
                break;
            case "SETTING":
                keyCode = "KEYCODE_QUICK_START5";
                break;
            case "MENU":
                keyCode = "KEYCODE_MENU";
                break;

            case "BACK":
                keyCode = "KEYCODE_BACK";
                break;

            case "UP":
                keyCode = "KEYCODE_DPAD_UP";
                break;

            case "LEFT":
                keyCode = "KEYCODE_DPAD_LEFT";
                break;

            case "ENTER":
                keyCode = "KEYCODE_DPAD_CENTER";
                break;

            case "RIGHT":
                keyCode = "KEYCODE_DPAD_RIGHT";
                break;

            case "DOWN":
                keyCode = "KEYCODE_DPAD_DOWN";
                break;

            case "VOLUMEUP":
                keyCode = "KEYCODE_VOLUME_UP";
                break;

            case "VOLUMEDOWN":
                keyCode = "KEYCODE_VOLUME_DOWN";
                break;

            case "CHANNELUP":
                keyCode = "KEYCODE_CHANNEL_UP";
                break;

            case "CHANNELDOWN":
                keyCode = "KEYCODE_CHANNEL_DOWN";
                break;

            case "BROWSER":
                keyCode = "KEYCODE_QUICK_START6";
                break;

            case "INPUT":
                keyCode = "KEYCODE_TV_INPUT";
                break;

            case "1":
                keyCode = "KEYCODE_1";
                break;

            case "2":
                keyCode = "KEYCODE_2";
                break;

            case "3":
                keyCode = "KEYCODE_3";
                break;

            case "4":
                keyCode = "KEYCODE_4";
                break;

            case "5":
                keyCode = "KEYCODE_5";
                break;

            case "6":
                keyCode = "KEYCODE_6";
                break;

            case "7":
                keyCode = "KEYCODE_7";
                break;

            case "8":
                keyCode = "KEYCODE_8";
                break;

            case "9":
                keyCode = "KEYCODE_9";
                break;

            case "0":
                keyCode = "KEYCODE_0";
                break;

            case "NETFLIX":
                keyCode = "KEYCODE_QUICK_START2";
                break;

            case "YOUTUBE":
                keyCode = "KEYCODE_QUICK_START3";
                break;

            case "PRIME_VIDEO":
                keyCode = "KEYCODE_QUICK_START1";
                break;
        }

        try {
            Log.d("buttonPress", keyCode);
            Runtime.getRuntime().exec("input keyevent " + keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String formatMessage(String message) {
        return message
            .substring(6)
            .replace(" ", "%s")
            .replace("Ç", "C")
            .replace("ç", "c")
            .replace("Ğ", "G")
            .replace("ğ", "g")
            .replace("İ", "I")
            .replace("ı", "i")
            .replace("Ö", "O")
            .replace("ö", "o")
            .replace("Ş", "S")
            .replace("ş", "s")
            .replace("Ü", "U")
            .replace("ü", "u");
    }



    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d("websocket", "Hata var: " + ex.getMessage());
        if (conn != null) {
            conn.close();
        }

    }

    @Override
    public void onStart() {
    }
}
