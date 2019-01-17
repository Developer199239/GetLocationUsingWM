package com.surroundapps.getlocationusingwm;

import android.arch.lifecycle.LiveData;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {
    Button getLocationBtn,getDataBtn;
    final static int PERMISSION_GPS_CODE = 101;

    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationBtn = findViewById(R.id.getLocation);
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String permission = android.Manifest.permission.ACCESS_FINE_LOCATION;
                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                        checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {permission}, PERMISSION_GPS_CODE);
                } else {
                    trackLocation();
//                    singleTrackLocation();
                }
            }
        });

        setWorkManagerStatus();

        getDataBtn = findViewById(R.id.getData);
        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView totalCnt = findViewById(R.id.cntView);
                int cn = new SessionHelper(MainActivity.this).getCnt();
                totalCnt.setText(""+cn);

                List<String> getLocationData = new SessionHelper(MainActivity.this).getLocationData();
                data.clear();
                data.addAll(getLocationData);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_GPS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                singleTrackLocation();
                trackLocation();
            } else {
                Toast.makeText(this, "location_permission_granted_msg not pranted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void singleTrackLocation() {
        Constraints myConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest locationWork = new OneTimeWorkRequest.Builder(LocationWorker.class)
                .build();
        WorkManager.getInstance().enqueue(locationWork);
    }


    private void trackLocation() {
        Constraints myConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest locationWork = new PeriodicWorkRequest.Builder(LocationWorker.class, 15, TimeUnit.MINUTES).addTag(LocationWorker.class.getSimpleName()
        ).build();
        WorkManager.getInstance().enqueue(locationWork);
    }

    private void stopTrackLocation() {
        WorkManager.getInstance().cancelAllWorkByTag(LocationWorker.class.getSimpleName());
    }

    private void setWorkManagerStatus() {
        TextView status = findViewById(R.id.status);
        WorkManager.getInstance().getWorkInfosByTag(LocationWorker.class.getSimpleName());
    }

}
