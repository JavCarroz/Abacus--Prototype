package javcarroz.com.playtestabacus.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import javcarroz.com.playtestabacus.PlaytestAbacusApplication;
import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.data.ParseConstants;
import javcarroz.com.playtestabacus.ui.activities.PlaytestActivity;
import javcarroz.com.playtestabacus.ui.adapters.PlaytestAdapter;


public class InboxFragment extends ListFragment {

    public static final String TAG = InboxFragment.class.getSimpleName();

    protected List<ParseObject> mPlaytests;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "We're refreshing!", Toast.LENGTH_LONG).show();
                retrieveTests();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.swipe_refresh_01,
                R.color.swipe_refresh_02,
                R.color.swipe_refresh_03,
                R.color.swipe_refresh_04 );
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveTests();
    }

    private void retrieveTests() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_PLAYTESTS);
        query.whereNotEqualTo(ParseConstants.PLAYTESTS_KEY_TEST_STATUS, ParseConstants.VALUE_TEST_STATUS_DONE);
        query.whereEqualTo(ParseConstants.PLAYTESTS_KEY_BELONGS_TO, ParseUser.getCurrentUser());
        query.addAscendingOrder(ParseConstants.SHARED_KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> playtests, ParseException e) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (e == null) {
                    //query successful!
                    mPlaytests = playtests;

                    if (getListView().getAdapter() == null) {
                        PlaytestAdapter adapter = new PlaytestAdapter(getListView().getContext(), mPlaytests);
                        setListAdapter(adapter);
                    }
                    else {
                        ((PlaytestAdapter)getListView().getAdapter()).refill(mPlaytests);
                    }
                }
                else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(R.string.inbox_failed_load_playtests_error_text)
                            .setTitle(R.string.inbox_failed_load_playtests_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getListView().getContext(), PlaytestActivity.class);
        PlaytestAbacusApplication.mProjectRef = mPlaytests.get(position);
        startActivity(intent);
    }
}
