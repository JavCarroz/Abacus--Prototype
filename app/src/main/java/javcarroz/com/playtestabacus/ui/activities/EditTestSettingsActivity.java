package javcarroz.com.playtestabacus.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.model.AppConstants;

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
                    Intent intent = new Intent(EditTestSettingsActivity.this, PlaytestActivity.class);
                    int formattedNumParticipants = Integer.parseInt(numParticipants);

                    intent.putExtra(AppConstants.CONST_PROJECT_NAME, projectName);
                    intent.putExtra(AppConstants.CONST_NUM_OF_PARTICIPANTS, formattedNumParticipants);
                    intent.putExtra(AppConstants.CONST_TEST_TIMER, timer);

                    if (clientName.isEmpty()) {
                        clientName = AppConstants.DEFAULT_CLIENT_NAME;
                    }
                    // check why it is not entering here
                    else if (productName.isEmpty()) {
                        productName = AppConstants.DEFAULT_PRODUCT_NAME;
                    }
                    // check why it is not entering here
                    else if (coding.isEmpty()) {
                        coding = AppConstants.DEFAULT_CODING;
                    }
                    intent.putExtra(AppConstants.CONST_CLIENT_NAME, clientName);
                    intent.putExtra(AppConstants.CONST_PRODUCT_NAME, productName);
                    intent.putExtra(AppConstants.CONST_CODING, coding);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });


    }

}
