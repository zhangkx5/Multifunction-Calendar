package com.example.kaixin.mycalendar.Utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kaixin on 2018/3/25.
 */

public abstract class DBCommand<T> {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public final void execute() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    postResult(doInBackground());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void postResult(final T result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(result);
            }
        });
    }

    protected abstract T doInBackground();
    protected void onPostExecute(T result) {}
}
