package com.example.project3companion;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

// Main Activity Page to allow application to be run as a stand alone application as well
// Registers the receiver on start
public class MainActivity extends AppCompatActivity {

    BroadcastReceiver mReceiver = new MyReceiver();

    private static final String TOAST_INTENT =
            "edu.example.project3";

    IntentFilter mFilter = new IntentFilter() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFilter.addAction(TOAST_INTENT);
        registerReceiver(mReceiver, mFilter, "edu.uic.cs478.sp2020.project3", null);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent();
        intent.setClass(this, WebViewerActivity.class);

        // Condition to check which page to be opened
        if (id == R.id.attraction) {
            Log.i("Checked", "in attarctions");
            intent.putExtra("Name", "attraction");
            startActivity(intent);
        }
        else {
            intent.putExtra("Name", "restaurant");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Un-register the receiver on shutting the activity
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("Main Activity", "On destroy called");
        unregisterReceiver(mReceiver);
    }

}
