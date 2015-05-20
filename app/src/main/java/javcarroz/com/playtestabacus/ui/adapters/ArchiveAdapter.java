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
import javcarroz.com.playtestabacus.data.ParseConstants;

public class ArchiveAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mPlaytests;

    public ArchiveAdapter(Context context, List<ParseObject> playtests) {
        super(context, R.layout.archive_item, playtests);

        mContext = context;
        mPlaytests = playtests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.archive_item, null);
            holder = new ViewHolder();

            holder.iconStatusView = (ImageView) convertView.findViewById(R.id.iconStatusView);
            holder.projectNameLabel = (TextView) convertView.findViewById(R.id.projectNameLabel);
            holder.initTimeLabel = (TextView) convertView.findViewById(R.id.initTimeLabel);
            holder.endTimeLabel = (TextView) convertView.findViewById(R.id.endTimeLabel);
            holder.participantsTotalProgress = (TextView) convertView.findViewById(R.id.participantsTotalLabel);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject playtest = mPlaytests.get(position);

        Date createdAt = playtest.getCreatedAt();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
        String convertedInitDate = formatter.format(createdAt);
        String endDate = playtest.getString(ParseConstants.PLAYTESTS_KEY_COMPLETED_AT);

        holder.iconStatusView.setImageResource(R.mipmap.ic_action_done);

        int numOfParts = playtest.getInt(ParseConstants.PLAYTESTS_KEY_NUM_OF_PART);
        holder.participantsTotalProgress.setText(numOfParts + " participants tested!");
        holder.initTimeLabel.setText(convertedInitDate);
        holder.endTimeLabel.setText(endDate);
        holder.projectNameLabel.setText(playtest.getString(ParseConstants.PLAYTESTS_KEY_PROJECT_NAME));


        return convertView;
    }

    private static class ViewHolder {
        ImageView iconStatusView;
        TextView projectNameLabel;
        TextView initTimeLabel;
        TextView endTimeLabel;
        TextView participantsTotalProgress;
    }

    public void refill (List<ParseObject> messages) {
        mPlaytests.clear();
        mPlaytests.addAll(messages);
        notifyDataSetChanged();
    }
}
