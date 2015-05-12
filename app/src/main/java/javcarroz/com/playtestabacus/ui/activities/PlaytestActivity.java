package javcarroz.com.playtestabacus.ui.activities;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.model.AppConstants;

public class PlaytestActivity extends ListActivity {

    public final static String TAG = PlaytestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playtest);


        String data01 = getIntent().getStringExtra(AppConstants.CONST_PROJECT_NAME);
        String data02 = getIntent().getStringExtra(AppConstants.CONST_CLIENT_NAME);
        String data03 = getIntent().getStringExtra(AppConstants.CONST_PRODUCT_NAME);
        String data04 = getIntent().getStringExtra(AppConstants.CONST_CODING);
        int data05 = getIntent().getIntExtra(AppConstants.CONST_NUM_OF_PARTICIPANTS, 64);
        String data06 = getIntent().getStringExtra(AppConstants.CONST_TEST_TIMER);

        Log.i(TAG, data01 + " " + data02 + " " + data03 + " " + data04 + " " + data05 + " " + data06);

        String[] daysOfTheWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Manzana", "Jorge", "Penguin",
            "Chicken", "Bottle", "TV", "Sofa", "Cat", "Backpack", "Laptop", "Piano"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysOfTheWeek);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String message = "This is list item number " + position;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
