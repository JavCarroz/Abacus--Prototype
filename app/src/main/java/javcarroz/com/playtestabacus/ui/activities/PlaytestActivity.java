package javcarroz.com.playtestabacus.ui.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import javcarroz.com.playtestabacus.PlaytestAbacusApplication;
import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.model.AppConstants;
import javcarroz.com.playtestabacus.model.ParseConstants;
import javcarroz.com.playtestabacus.ui.adapters.ParticipantAdapter;
import javcarroz.com.playtestabacus.ui.adapters.PlaytestAdapter;
import com.parse.ParseObject;

import java.sql.ParameterMetaData;

import javcarroz.com.playtestabacus.model.AppConstants;
import javcarroz.com.playtestabacus.model.Participant;
import javcarroz.com.playtestabacus.ui.managers.ParticipantAlarmManager;

public class PlaytestActivity extends ListActivity {

    public final static String TAG = PlaytestActivity.class.getSimpleName();

    private String mProjectId;
    private List<ParseObject> mParticipants;
    private ParticipantAdapter mAdapter;
    private ParticipantAlarmManager mParticipantAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playtest);
        mParticipants = new ArrayList<>();
        mAdapter = new ParticipantAdapter(PlaytestActivity.this, mParticipants);
        setListAdapter(mAdapter);


        mProjectId = getIntent().getStringExtra("projectId");
        Log.i(TAG, "Project id = " + mProjectId);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Participant");
        query.whereEqualTo(ParseConstants.SHARED_KEY_PARENT, PlaytestAbacusApplication.mProjectRef);
        query.addAscendingOrder(ParseConstants.PARTICIPANTS_KEY_SUFFIX);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> participants, ParseException e) {
                if (e == null) {
                    //query successful!
                    Log.i(TAG, String.valueOf(participants.size()));
                    mParticipants.addAll(participants);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(R.string.failed_load_participants_list_text)
                            .setTitle(R.string.failed_load_participants_list_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

//        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
//        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class );

        mParticipantAlarmManager = ParticipantAlarmManager.getInstance(this);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String message = "This is participant number " + position;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        Participant participant = (Participant) mAdapter.getItem(position);

        //boolean isPaused = participant.getPaused(); //TODO: RETURN BOOLEAN
        // assuming at this point that if there's any value in the remainder key, it is paused
        boolean isPaused = participant.getRemainderTime() != null && participant.getRemainderTime().trim().length() > 0;

        Log.i(AppConstants.TAG, "isPaused=" + isPaused);

        if (isPaused) {
            //long reminder = Long.valueOf(participant.getRemainderTime()); //if in seconds, convert to milli with "X * 1000"
            participant.setRemainderTime("");
            long inMillis = 4 * 1000; //TODO: Get Playtest duration in milliseconds minus remainder in milliseconds
            startParticipant(mProjectId, participant, inMillis);
        } else {
            participant.setRemainderTime("1234");
            pauseParticipant(mProjectId, participant);
        }

        //notify of changes either way (paused or resumed)
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlaytestActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playtest, menu);
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

    private void startParticipant(String projectId, Participant participant, long inMillis) {
        mParticipantAlarmManager.schedule(projectId, participant, inMillis);
    }

    private void pauseParticipant(String projectId, Participant participant) {
        mParticipantAlarmManager.cancel(projectId, participant);
    }
}
