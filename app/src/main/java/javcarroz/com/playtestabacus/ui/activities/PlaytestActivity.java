package javcarroz.com.playtestabacus.ui.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import javcarroz.com.playtestabacus.PlaytestAbacusApplication;
import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.data.ParseConstants;
import javcarroz.com.playtestabacus.ui.adapters.ParticipantAdapter;

public class PlaytestActivity extends ListActivity {

    public final static String TAG = PlaytestActivity.class.getSimpleName();
    protected List<ParseObject> mParticipants;
    protected ParticipantAdapter mAdapter;
    private TextView mTestProgress;
    private Button mAddExtraParticipant;
    private Button mConcludeButton;

    private Handler mRefresher = new Handler();
    private long mRefreshInterval = 1000;// 1000 = 1 sec

    @Override
    protected void onResume() {
        super.onResume();

        Runnable refresh = new Runnable() {
            @Override
            public void run() {

                //if the activity is not finishing, refresh ui and re-schedule to do the same in 1 second
                if (!isFinishing()) {
                    mAdapter.notifyDataSetChanged();
                    mRefresher.postDelayed(this, mRefreshInterval);
                }
            }
        };

        //call the runnable to refresh the participants list with the timer data.
        mRefresher.post(refresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playtest);

        mParticipants = new ArrayList<>();
        mTestProgress = (TextView) findViewById(R.id.testCountdownLabel);
        mConcludeButton = (Button) findViewById(R.id.concludeTestButton);
        final View.OnClickListener concludeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concludePlaytest(mParticipants, PlaytestAbacusApplication.mProjectRef);
            }
        };
        mAddExtraParticipant = (Button) findViewById(R.id.addExtraButton);
        final View.OnClickListener addParticipantListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlaytestActivity.this, "Not yet implemented", Toast.LENGTH_LONG).show();
            }
        };


        mAdapter = new ParticipantAdapter(PlaytestActivity.this, mParticipants);
        setListAdapter(mAdapter);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.LOCAL_SUBCLASS_PARTICIPANT);
        query.whereEqualTo(ParseConstants.SHARED_KEY_PARENT, PlaytestAbacusApplication.mProjectRef);
        query.addAscendingOrder(ParseConstants.PARTICIPANTS_KEY_SUFFIX);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> participants, ParseException e) {
                if (e == null) {
                    //query successful!
                    mParticipants.addAll(participants);
                    mAdapter.notifyDataSetChanged();
                    mConcludeButton.setVisibility(View.VISIBLE);
                    mAddExtraParticipant.setVisibility(View.VISIBLE);
                    mConcludeButton.setOnClickListener(concludeButtonListener);
                    mAddExtraParticipant.setOnClickListener(addParticipantListener);

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

    }

    private void concludePlaytest(List<ParseObject> participants, ParseObject project) {

        for(ParseObject participant: participants){
            if (participant.getInt(ParseConstants.PARTICIPANTS_KEY_PARTICIPANT_STATUS) != 1) {
                participant.put(ParseConstants.PARTICIPANTS_KEY_PARTICIPANT_STATUS, 1);
                participant.saveInBackground();
            }
        }

        project.put(ParseConstants.PLAYTESTS_KEY_TEST_STATUS, 2);
        project.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlaytestActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
    Might be used later on
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String message = "This is participant number " + position;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    */

}