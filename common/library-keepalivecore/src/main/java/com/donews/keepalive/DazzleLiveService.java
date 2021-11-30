package com.donews.keepalive;

import android.app.job.JobParameters;
import android.app.job.JobService;


public class DazzleLiveService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        DazzleActivity.showOnePixelActivity(this);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
