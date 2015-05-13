package javcarroz.com.playtestabacus.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.model.ParseConstants;

public class PlaytestAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mPlaytests;

    public PlaytestAdapter (Context context, List<ParseObject> playtests) {
        super(context, R.layout.playtest_item, playtests);

        mContext = context;
        mPlaytests = playtests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.playtest_item, null);
            holder = new ViewHolder();

            holder.iconStatusView = (ImageView) convertView.findViewById(R.id.IconStatusView);
            holder.projectNameLabel = (TextView) convertView.findViewById(R.id.projectNameLabel);
            holder.createdAtLabel = (TextView) convertView.findViewById(R.id.timeLabel);
            holder.participantsProgress = (TextView) convertView.findViewById(R.id.participantsProgressLabel);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject playtest = mPlaytests.get(position);
        Date createdAt = playtest.getCreatedAt();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
        String convertedDate = formatter.format(createdAt);

        if (playtest.getInt(ParseConstants.PLAYTESTS_KEY_TEST_STATUS) == (ParseConstants.VALUE_TEST_STATUS_ON_HOLD)) {
            holder.iconStatusView.setImageResource(R.mipmap.ic_action_pause);
        }
        else {
            holder.iconStatusView.setImageResource(R.mipmap.ic_action_upload);
        }

        int numOfParts = playtest.getInt(ParseConstants.PLAYTESTS_KEY_NUM_OF_PART);
        //Need to pull the current number of completed participants from a given playtest!
        holder.participantsProgress.setText("XX completed out of " + numOfParts + " participants");
        holder.createdAtLabel.setText(convertedDate);
        holder.projectNameLabel.setText(playtest.getString(ParseConstants.PLAYTESTS_KEY_PROJECT_NAME));

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconStatusView;
        TextView projectNameLabel;
        TextView createdAtLabel;
        TextView participantsProgress;
    }

    public void refill (List<ParseObject> messages) {
        mPlaytests.clear();
        mPlaytests.addAll(messages);
        notifyDataSetChanged();
    }
}
