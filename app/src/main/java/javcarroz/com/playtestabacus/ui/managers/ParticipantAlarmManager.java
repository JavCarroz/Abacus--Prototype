package javcarroz.com.playtestabacus.ui.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import java.util.HashMap;

import javcarroz.com.playtestabacus.model.AppConstants;
import javcarroz.com.playtestabacus.model.Participant;
import javcarroz.com.playtestabacus.ui.services.ParticipantAlarmIntentService;

public class ParticipantAlarmManager {

    /**
     * Class scheduling and cancelling participant-specific alarms via AlarmManager
     * It will trigger an IntentService-extended class in order to create a Notification
     *   with the purpose of bringing the user into the playtest activity related to the project
     * It will be the activity's responsibility to make the necessary checks to define the user(s)
     *   that should have finished the test.
     * In order to keep several alarms scheduled, the PendingIntent passed (which is also used to
     *   cancel them) has to have a unique integer value. ParseObject objects do not have integer id
     *   Thus, we have to generate and keep track of the integer values ourselves to cancel/schedule
     *   based on the participant's ID.
     */

    private static final String PREFERENCES = "preferences.participantsalarmmanager";

    private static ParticipantAlarmManager mInstance;

    private Context mContext;
    private AlarmManager mAlarmManager;
    private HashMap<String, Integer> mParticipantIntentIds;
    private int mLastIntentId = 10;

    public static ParticipantAlarmManager getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new ParticipantAlarmManager(context);
        }

        return mInstance;
    }

    private ParticipantAlarmManager(Context context) {
        mContext = context.getApplicationContext();
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    public void schedule(String projectId, Participant participant, long inMillis) {
        schedule(projectId, participant.getObjectId(), inMillis);
    }

    public void schedule(String projectId, String participantId, long inMillis) {
        long now = SystemClock.elapsedRealtime();
        long when = now + inMillis;

        PendingIntent operation = buildOperation(projectId, participantId);

        //type is ELAPSED_REALTIME_WAKEUP to match millis given by SystemClock.elapsedRealtime()
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, when, operation);
    }

    public void cancel(String projectId, Participant participant) {
        cancel(projectId, participant.getObjectId());
    }

    public void cancel(String projectId, String participantId) {
        mAlarmManager.cancel(buildOperation(projectId, participantId));
    }

    private PendingIntent buildOperation(String projectId, String participantId) {
        int uniqueId = getParticipantIntentId(participantId);

        Intent service = new Intent(mContext, ParticipantAlarmIntentService.class);
        service.putExtra(ParticipantAlarmIntentService.EXTRA_PROJECT_ID, projectId);
        PendingIntent operation = PendingIntent.getService(mContext, uniqueId, service, PendingIntent.FLAG_UPDATE_CURRENT);

        return operation;
    }

    private int getParticipantIntentId(String participantId) {
        if (mParticipantIntentIds == null) {
            loadParticipantIntentIds();
        }

        if (!mParticipantIntentIds.containsKey(participantId)) {
            mParticipantIntentIds.put(participantId, mLastIntentId++);
            saveParticipantIntentIds();
        }

        return mParticipantIntentIds.get(participantId);
    }

    private void loadParticipantIntentIds() {

        //TODO: it has to be saved/loaded to/from disk (local persistent storage for re-instanciated apps)

        mParticipantIntentIds = new HashMap<>();
    }

    private void saveParticipantIntentIds() {

        //TODO: it has to be saved/loaded to/from disk (local persistent storage for re-instanciated apps)
    }
}
