package javcarroz.com.playtestabacus.ui.adapters;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javcarroz.com.playtestabacus.PlaytestAbacusApplication;
import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.data.ParseConstants;
import javcarroz.com.playtestabacus.data.Participant;
import javcarroz.com.playtestabacus.ui.activities.PlaytestActivity;

public class ParticipantAdapter extends BaseAdapter implements View.OnClickListener {

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
            holder.clock = (TextView) convertView.findViewById(R.id.clock);
            holder.iconStatus = (ImageView) convertView.findViewById(R.id.iconStatusView);
            holder.startPauseButton = (Button) convertView.findViewById(R.id.startPauseButton);
            holder.startPauseButton.setTag(position);
            holder.invalidButton = (Button)convertView.findViewById(R.id.unvalidButton);
            holder.position = position;

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject participant = mParticipants.get(position);
        String formattedSuffix = PlaytestAbacusApplication.mProjectRef.getString(ParseConstants.PLAYTESTS_KEY_PART_CODE) +
                "" + String.valueOf(participant.getInt(ParseConstants.PARTICIPANTS_KEY_SUFFIX));
        holder.participantCodeLabel.setText(formattedSuffix);

        if (participant.getInt(ParseConstants.PARTICIPANTS_KEY_VOID) == 1){
            holder.iconStatus.setImageResource(R.mipmap.ic_action_warning);
            holder.clock.setText("Void!");
        }

        else if (participant.getInt(ParseConstants.PARTICIPANTS_KEY_PARTICIPANT_STATUS) == 1 &&
                participant.getInt(ParseConstants.PARTICIPANTS_KEY_VOID) == 0) {
            holder.iconStatus.setImageResource(R.mipmap.ic_action_done);
            holder.clock.setText("Done!");
            holder.invalidButton.setOnClickListener(this);
        }

        else {
            holder.iconStatus.setImageResource(R.mipmap.ic_action_edit);
            holder.invalidButton.setOnClickListener(this);

            if (!isInitialized(participant)) {
                //Starts for the first time
                long initTestTimer = Long.parseLong(PlaytestAbacusApplication.mProjectRef.getString(ParseConstants.PLAYTESTS_KEY_TEST_TIMER));
                holder.clock.setText(initTestTimer+"");
                holder.startPauseButton.setText(R.string.start_participant_text);
            }
            else if ( (isInitialized(participant) == true) && (participant.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 0) ) {
                //countdown is running
                holder.startPauseButton.setText(R.string.pause_participant_text);
                long now = System.currentTimeMillis();
                long longInitDate = Long.parseLong(participant.getString(ParseConstants.PARTICIPANTS_KEY_START_TIME));
                long diff = (now - longInitDate);
                long seconds = diff /1000;
                holder.clock.setText(seconds+"");
                //Need to format this output. Currently shows the amount of seconds that have passed since being init.
            }
            else if ( (isInitialized(participant) == true) && (participant.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 1) ) {
                //resuming the countdown
                holder.startPauseButton.setText(R.string.resume_participant_text);
                long seconds = Long.parseLong(participant.getString(ParseConstants.PARTICIPANTS_KEY_REMAINDER_TIME));
                holder.clock.setText(seconds+"");
            }

            holder.startPauseButton.setOnClickListener(this);
        }


        return convertView;
    }

    @Override
    public void onClick(View v) {
        //Start of: handling the events relating to the startPauseButton
        if (v.getId() == R.id.startPauseButton) {
            int position = ((Integer) v.getTag());
            long testTimer = Long.parseLong(PlaytestAbacusApplication.mProjectRef.getString(ParseConstants.PLAYTESTS_KEY_TEST_TIMER));
            Participant p = (Participant) getItem(position);


            if (!isInitialized(p)) {
                //Participant has not been initialized yet!

                final String initTime = System.currentTimeMillis()+ "";
                Log.i("initClick", initTime + " "+position);
                p.put(ParseConstants.PARTICIPANTS_KEY_START_TIME, initTime);
                p.saveInBackground();
            }

            else if ((isInitialized(p)) && (p.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 0)) {
                //Participant has been initialized and its time is currently running.
                String remainder = p.getString(ParseConstants.PARTICIPANTS_KEY_REMAINDER_TIME);
                long elapsedTime = System.currentTimeMillis();
                long timeToEnd;

                if (remainder.isEmpty()) {
                    //it has not been paused before
                    long initTime = Long.parseLong(p.getString(ParseConstants.PARTICIPANTS_KEY_START_TIME));
                    timeToEnd = (testTimer - (elapsedTime - initTime));
                }
                else {
                    //it had a previous pause
                    long lastUnpauseTime = Long.parseLong(p.getString(ParseConstants.PARTICIPANTS_KEY_PREV_UNPAUSE_TIME));
                    elapsedTime = (elapsedTime - lastUnpauseTime);
                    timeToEnd = Long.parseLong(remainder);
                    timeToEnd -= elapsedTime;
                }
                Log.i("timeToEnd", timeToEnd+"");
                p.put(ParseConstants.PARTICIPANTS_KEY_REMAINDER_TIME, timeToEnd+ "");
                p.put(ParseConstants.PARTICIPANTS_KEY_PAUSED, 1);
                p.saveInBackground();
            }

            else if ((isInitialized(p)) && (p.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 1)){
                long unpauseTime = System.currentTimeMillis();
                p.put(ParseConstants.PARTICIPANTS_KEY_PREV_UNPAUSE_TIME, unpauseTime+"");
                p.put(ParseConstants.PARTICIPANTS_KEY_PAUSED, 0);
                p.saveInBackground();
                Log.i("unpause:", " engage!");
            }

        }
        //End of: handling the events relating to the startPauseButton

        //Start of: handling the events relating to the invalidButton.
        else {

        }
        //End of: handling the events relating to the invalidButton.
    }

    private boolean isInitialized(ParseObject p) {
        return ! p.getString(ParseConstants.PARTICIPANTS_KEY_START_TIME).isEmpty();
    }

    private static class ViewHolder {
        TextView participantCodeLabel;
        TextView clock;
        ImageView iconStatus;
        Button startPauseButton;
        Button invalidButton;
        int position;
    }
}