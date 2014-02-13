package com.example.myapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() && mobile.isAvailable()) {
        	Toast toast = Toast.makeText(context, "Wifi and Mobile is available", Toast.LENGTH_SHORT);
			toast.show();
        }else if(mobile.isAvailable()){
        	Toast toast = Toast.makeText(context, "Mobile is available", Toast.LENGTH_SHORT);
			toast.show();
        }else if(wifi.isAvailable()){
        	Toast toast = Toast.makeText(context, "Wifi is available", Toast.LENGTH_SHORT);
			toast.show();
        }else{
        	Toast toast = Toast.makeText(context, "Internet is unavailable", Toast.LENGTH_SHORT);
			toast.show();
        }
    }
}