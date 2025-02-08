package com.example.qrapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import android.util.Log;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;



import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private String tvWebSocketUrl = null;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        updateIpAddress();
        generateAndDisplayQRCode(tvWebSocketUrl);

        // QR kodunuzu oluşturacak metodu çağırın

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateIpAddress();
    }

    private void updateIpAddress() {
        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    // IP adresini al ve QR kodunu güncelle
                    tvWebSocketUrl = getIPAddress();
                    generateAndDisplayQRCode(tvWebSocketUrl);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    // Bağlantı kaybedildi, IP adresi ve QR kodu null yap
                    tvWebSocketUrl = "";
                    generateAndDisplayQRCode(tvWebSocketUrl);
                }
            });
        }
    }

    private String getIPAddress() {
        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                LinkAddress linkAddress = getIPv4Address(network);
                if (linkAddress != null) {
                    InetAddress address = linkAddress.getAddress();
                    SharedPreferences preferences = getSharedPreferences("webSocket", MODE_PRIVATE);
                    int qrServerPort = preferences.getInt("port", 8080);
                    Log.d("websocket", String.valueOf(qrServerPort));
                    return "ws://" + address.getHostAddress() + ":"+ qrServerPort + "/websocket";
                }
            }
        }
        return null;
    }

    private LinkAddress getIPv4Address(Network network) {

        LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
        for (LinkAddress linkAddress : linkProperties.getLinkAddresses()) {
            InetAddress address = linkAddress.getAddress();
            if (address != null && address.getAddress().length == 4) { // IPv4 adresi kontrolü
                return linkAddress;
            }
        }
        return null;
    }

    private void generateAndDisplayQRCode(String data) {
        String qrText;
        if (data != null && !data.isEmpty()) {
            qrText = data;
        } else {

            qrText = "Televizyonu ağa bağlayın."; // varsayılan metin
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 0);

            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 216, 216, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(216, 216, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? ContextCompat.getColor(this, R.color.black) : ContextCompat.getColor(this, R.color.white));
                }
                
            }

            qrCodeImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
