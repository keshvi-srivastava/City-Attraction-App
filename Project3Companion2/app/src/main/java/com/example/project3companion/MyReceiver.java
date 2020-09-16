package com.example.project3companion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

// Custom Receiver to retrieve activity
public class MyReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        // Holds the ID of the activity to be opened
        String name = bundle.getString("ID");

        Intent i = new Intent();

        if(name.equalsIgnoreCase("att")){

            Log.i("Receiver", "In receiver for button2");
            i.setClass(context.getApplicationContext(), WebViewerActivity.class);
            i.putExtra("Name", "attraction");
            context.startActivity(i);
        }
        else{
            Log.i("Receiver", "In receiver for button2");
            i.setClass(context.getApplicationContext(), WebViewerActivity.class);
            i.putExtra("Name", "restaurant");
            context.startActivity(i);
        }

    }
}

