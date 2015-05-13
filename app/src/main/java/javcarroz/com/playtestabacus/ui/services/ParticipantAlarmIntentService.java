package javcarroz.com.playtestabacus.ui.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.ui.activities.MainActivity;
import javcarroz.com.playtestabacus.ui.activities.PlaytestActivity;

public class ParticipantAlarmIntentService extends IntentService {

    public static final String EXTRA_PROJECT_ID = "extras.intentservice.project.id";

    private static final int NOTIFICATION_ID = 10;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ParticipantAlarmIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String projectId = intent.getStringExtra(EXTRA_PROJECT_ID);

        if (projectId == null) {
            return;
        }

        //set stack of activities [main, details]
        Intent main = new Intent(this, MainActivity.class);

        Intent details = new Intent(this, PlaytestActivity.class);
        details.putExtra("projectId", projectId); //TODO: Make use of public static constant of this extra

        Intent[] stack = new Intent[] {main, details};

        //intent triggered when notification is tapped
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 1, stack, 0);

        //notify
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_participanttimer_title))
                .setContentText(getString(R.string.notification_participanttimer_message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
