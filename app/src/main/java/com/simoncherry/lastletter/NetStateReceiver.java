package com.simoncherry.lastletter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetStateReceiver extends BroadcastReceiver{
    public interface BRInteraction {
        public void showCheckNetStateBtn(Boolean isConnected);
    }

    private BRInteraction brInteraction;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        Boolean isConnected;

        if (mNetworkInfo == null || !mNetworkInfo.isConnected()){
            //Toast.makeText(context, "You are not currently online", Toast.LENGTH_LONG).show();
            isConnected = false;
        }else{
            //Toast.makeText(context, "You are currently online", Toast.LENGTH_LONG).show();
            isConnected = true;
        }

        brInteraction.showCheckNetStateBtn(isConnected);
    }

    public void setBRInteractionListener(BRInteraction brInteraction) {
        this.brInteraction = brInteraction;
    }
}