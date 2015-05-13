package javcarroz.com.playtestabacus.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.model.ParseConstants;
import javcarroz.com.playtestabacus.model.Participant;

public class ParticipantAdapter extends BaseAdapter {

    private Context mContext;
    private List<ParseObject> mParticipants;

    public ParticipantAdapter (Context context, List<ParseObject> participants) {
        mContext = context;
        mParticipants = participants;
    }

    @Override
    public int getCount() {
        return mParticipants.size();
    }

    @Override
    public Object getItem(int position) {
        return mParticipants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //the variable convertView is the one that determines if we are reusing a list element to fill further items on our scrolling list.
        if (convertView == null){
            //brand new information is displayed
            convertView = LayoutInflater.from(mContext).inflate(R.layout.participant_item, null);
            holder = new ViewHolder();
            holder.participantCodeLabel = (TextView) convertView.findViewById(R.id.participantCode);
            holder.clock= (TextView) convertView.findViewById(R.id.clock);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject participant = mParticipants.get(position);

        holder.participantCodeLabel.setText(String.valueOf(participant.getInt(ParseConstants.PARTICIPANTS_KEY_SUFFIX)));
        holder.clock.setText(participant.getObjectId());

        return convertView;
    }

    private static class ViewHolder {
        TextView participantCodeLabel;
        TextView clock;
    }
}
