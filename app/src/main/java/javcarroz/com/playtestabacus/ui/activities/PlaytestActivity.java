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

public class PlaytestActivity extends ListActivity {

    public final static String TAG = PlaytestActivity.class.getSimpleName();
    protected List<ParseObject> mParticipants;
    protected ParticipantAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playtest);
        mParticipants = new ArrayList<>();
        mAdapter = new ParticipantAdapter(PlaytestActivity.this, mParticipants);
        setListAdapter(mAdapter);


        String currentProjectUniqueId = getIntent().getStringExtra("projectId");
        Log.i(TAG, "Project id = " + currentProjectUniqueId);

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
}