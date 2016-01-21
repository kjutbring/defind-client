package com.defind.kittyj.defind_client.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.defind.kittyj.defind_client.MainActivity;

import java.util.LinkedList;


/**
 * Created by kittyj on 1/20/16.
 */
public class MyjobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("START JOB: ", String.valueOf(params.getJobId()));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("STOP JOB: ", String.valueOf(params.getJobId()));
        return false;
    }

}
