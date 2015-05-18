package javcarroz.com.playtestabacus.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import javcarroz.com.playtestabacus.PlaytestAbacusApplication;
import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.data.AppConstants;
import javcarroz.com.playtestabacus.data.ParseConstants;
import javcarroz.com.playtestabacus.data.Participant;

public class EditTestSettingsActivity extends AppCompatActivity {

    private EditText mProjectName;
    private EditText mProductName;
    private EditText mClientName;
    private EditText mCoding;
    private EditText mTimer;
    private EditText mNumParticipants;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test_settings);

        mProjectName = (EditText) findViewById(R.id.projectNameField);
        mClientName = (EditText) findViewById(R.id.clientNameField);
        mProductName = (EditText) findViewById(R.id.productNameField);
        mCoding = (EditText) findViewById(R.id.codingField);
        mTimer = (EditText) findViewById(R.id.testTimerInput);
        mNumParticipants = (EditText) findViewById(R.id.numPartInput );
        mStartButton = (Button) findViewById(R.id.startTestButton);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectName = mProjectName.getText().toString();
                String clientName = mClientName.getText().toString();
                String productName = mProductName.getText().toString();
                String coding = mCoding.getText().toString();
                String numParticipants = mNumParticipants.getText().toString();
                String timer = mTimer.getText().toString();

                if (projectName.isEmpty() || numParticipants.isEmpty() || timer.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditTestSettingsActivity.this);
                    builder.setMessage(R.string.first_setup_settings_text)
                            .setTitle(R.string.first_setup_settings_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    int formattedNumParticipants = Integer.parseInt(numParticipants);

                    if (clientName.isEmpty()) {
                        clientName = AppConstants.DEFAULT_CLIENT_NAME;
                    }
                    if (productName.isEmpty()) {
                        productName = AppConstants.DEFAULT_PRODUCT_NAME;
                    }
                    if (coding.isEmpty()) {
                        coding = AppConstants.DEFAULT_CODING;
                    }

                    initPlaytestToParse(projectName, clientName, productName, coding,formattedNumParticipants, timer);
                }

            }
        });


    }

    private void initPlaytestToParse(String projectName, String clientName, String productName, final String coding, final int numParticipants, String timer) {
        final ParseObject playtest = new ParseObject(ParseConstants.CLASS_PLAYTESTS);

        playtest.put(ParseConstants.PLAYTESTS_KEY_PROJECT_NAME, projectName);
        playtest.put(ParseConstants.PLAYTESTS_KEY_CLIENT_NAME, clientName);
        playtest.put(ParseConstants.PLAYTESTS_KEY_PRODUCT_NAME, productName);
        playtest.put(ParseConstants.PLAYTESTS_KEY_PART_CODE, coding + "_");
        playtest.put(ParseConstants.PLAYTESTS_KEY_NUM_OF_PART, numParticipants);
        //Save timer as a milliseconds string
        playtest.put(ParseConstants.PLAYTESTS_KEY_TEST_TIMER, timer);
        playtest.put(ParseConstants.PLAYTESTS_KEY_TEST_STATUS, ParseConstants.VALUE_TEST_STATUS_ONGOING);
        playtest.put(ParseConstants.PLAYTESTS_KEY_COMPLETED_AT, ParseConstants.VALUE_TEST_EMPTY_COMPLETED_AT);
        playtest.put(ParseConstants.PLAYTESTS_KEY_BELONGS_TO, ParseUser.getCurrentUser());

        ParseObject dataSet = new ParseObject(ParseConstants.CLASS_PARTICIPANTS);

        List<Participant> participantsList = new ArrayList<Participant>();

        for (int i = 1; i <= numParticipants; i++) {
            Participant participant = new Participant();

            participant.setSuffix(i);
            //init these 3 time values as empty strings.
            // Once a participant timer is init for the 1st time we will update to save the unixtime as string (Parse can't handle unixtime).
            participant.setStartTime("");
            participant.setEndTime("");
            participant.setRemainderTime("");
            participant.setPreviousUnpauseTime("");
            participant.setVoid(ParseConstants.VALUE_PARTICIPANT_NON_VOID_STATUS);
            participant.setPaused(ParseConstants.VALUE_PARTICIPANT_NOT_PAUSED_STATUS);
            participant.setParticipantStatus(ParseConstants.VALUE_PARTICIPANT_STATUS);
            participant.put(ParseConstants.SHARED_KEY_PARENT, playtest);

            participantsList.add(participant);
        }

        dataSet.saveAllInBackground(participantsList, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //success!
                    Toast.makeText(EditTestSettingsActivity.this, R.string.playtest_to_parse_success, Toast.LENGTH_LONG).show();
                    PlaytestAbacusApplication.mProjectRef = playtest;
                    Intent intent = new Intent(EditTestSettingsActivity.this, PlaytestActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditTestSettingsActivity.this, R.string.error_saving_playtest_to_parse, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
