package javcarroz.com.playtestabacus.ui.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
    private Button mConcludeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playtest);
        mParticipants = new ArrayList<>();
        mTestProgress = (TextView) findViewById(R.id.testCountdownLabel);
        mConcludeButton = (Button) findViewById(R.id.concludeTestButton);
        mAdapter = new ParticipantAdapter(PlaytestActivity.this, mParticipants);
        setListAdapter(mAdapter);

        final View.OnClickListener concludeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concludePlaytest(mParticipants,PlaytestAbacusApplication.mProjectRef );
            }
        };

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Participant");
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
                    mConcludeButton.setOnClickListener(concludeButtonListener);

//                    for (ParseObject participant: mParticipants) {
//                        if (participant.getInt(ParseConstants.PARTICIPANTS_KEY_PARTICIPANT_STATUS) == 1){
//
//                        }
//                    }
//
//                    mTestProgress.setText();


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
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Participant");
            query.getInBackground(participant.getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject part, ParseException e) {
                    if (e == null) {
                        part.put(ParseConstants.PARTICIPANTS_KEY_PARTICIPANT_STATUS, 1);
                        part.saveInBackground();
                    }
                }
            });
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_PLAYTESTS);
        query.getInBackground(project.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject proj, ParseException e) {
                if (e == null) {
                    proj.put(ParseConstants.PLAYTESTS_KEY_TEST_STATUS, 2);
                    proj.saveInBackground();
                }
            }
        });
        finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String message = "This is participant number " + position;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlaytestActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}