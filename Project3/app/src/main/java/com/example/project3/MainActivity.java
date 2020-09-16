package com.example.project3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected Button button1;
    protected Button button2;
    private static final String TOAST_INTENT =
            "edu.example.project3";

    public String ID= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("Main Activity", "In main activity");

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Main Activity", "Button 1 clicked");
                Toast.makeText(MainActivity.this,
                        "Sending broadcast for Attractions Activity", Toast.LENGTH_LONG).show();

                ID = "att";
                checkPermission();

        }});

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Main Activity", "Button 2 clicked");
                Toast.makeText(MainActivity.this,
                        "Sending broadcast for Restaurant Activity", Toast.LENGTH_LONG).show();
                ID = "res";
                checkPermission();

            }
        });
    }


    public void checkPermission(){

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                "edu.uic.cs478.sp2020.project3") ==
                PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent();
            intent.setAction(TOAST_INTENT);
            intent.putExtra("ID", ID);
            sendBroadcast(intent) ;

        }
        else {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{"edu.uic.cs478.sp2020.project3"},
                    0);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,
                            "Permission granted by user", Toast.LENGTH_LONG).show() ;
                    Intent intent = new Intent();
                    intent.setAction(TOAST_INTENT);
                    intent.putExtra("ID", ID);
                    sendBroadcast(intent) ;

                }
                else {
                    Toast.makeText(this,
                            "Permission not granted by user", Toast.LENGTH_LONG).show() ;
                }
            }
            default: {
                // do nothing
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
