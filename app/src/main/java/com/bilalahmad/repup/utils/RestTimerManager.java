package com.bilalahmad.repup.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.core.app.NotificationCompat;

import com.bilalahmad.repup.R;

public class RestTimerManager {
    private static final String CHANNEL_ID = "repup_rest_timer_channel";
    private static final int NOTIFICATION_ID = 1001;

    private final Context context;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private final TimerCallback callback;

    public interface TimerCallback{
        void onTick(long millisUntilFinished);
        void onFinish();
    }

    //Constructor
    public RestTimerManager(Context context, TimerCallback callback) {
        this.context = context.getApplicationContext();
        this.callback = callback;
        createNotificationChannel();
    }

    public void startTimer(long seconds) {
        if (isTimerRunning && countDownTimer != null) {
            countDownTimer.cancel();
        }
        long totalMillis = seconds * 1000;

        countDownTimer = new CountDownTimer(totalMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;
                if(callback != null) {
                    callback.onTick(millisUntilFinished);
                }
            }
            @Override
            public void onFinish() {
                isTimerRunning = false;
                if(callback != null) {
                    callback.onFinish();
                }
                sendTimerFinishedNotification();
            }
        }.start();

    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    public boolean isTimerRunning() {
        return isTimerRunning;

    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Rest Timer Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Alerts user when set rest period is complete.");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void sendTimerFinishedNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Uses default launcher icon
                .setContentTitle("Rest Time Complete!")
                .setContentText("Time to smash your next set!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
