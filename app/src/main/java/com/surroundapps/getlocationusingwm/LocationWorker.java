package com.surroundapps.getlocationusingwm;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class LocationWorker extends Worker {
    private Context context;
    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            LocationManager.getLocation(context);
            new SessionHelper(context).setCnt();
            return Result.success();
        }catch (Exception e) {
            return Result.failure();
        }
    }
}
