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
            holder.clock.setText(PlaytestAbacusApplication.mProjectRef.getString(ParseConstants.PLAYTESTS_KEY_TEST_TIMER));
            holder.invalidButton.setOnClickListener(this);

            if (!isInitialized(participant)) {
                //Starts for the first time
                holder.startPauseButton.setText(R.string.start_participant_text);
            }
            else if ( (isInitialized(participant) == true) && (participant.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 1) ) {
                //resuming the countdown
                holder.startPauseButton.setText(R.string.resume_participant_text);
            }
            else if ( (isInitialized(participant) == true) && (participant.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 0) ) {
                //countdown is running
                holder.startPauseButton.setText(R.string.pause_participant_text);
                long now = System.currentTimeMillis();
                long formattedInitDate = Long.parseLong(participant.getString(ParseConstants.PARTICIPANTS_KEY_START_TIME));
                long diff = (now - formattedInitDate);
                long seconds = diff /1000;
                holder.clock.setText(seconds +" secs (since started)");
                //Need to format this output. Currently shows the amount of seconds that have passed since being init.
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
            Participant p = (Participant) getItem(position);

            if (!isInitialized(p)) {
                //Participant has not been initialized yet!

                final String initDate = System.currentTimeMillis()+ "";
                Log.i("insideClick", initDate);
                p.put(ParseConstants.PARTICIPANTS_KEY_START_TIME, initDate);
                p.saveInBackground();
            }
            else if ((isInitialized(p)) && (p.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 0)) {
                Toast.makeText(v.getContext(), "Pause is in development", Toast.LENGTH_LONG).show();
            }

            /*
            else if ((isInitialized(p)) && (p.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 1)) {
                //Participant has been initialized but is currently paused!
                p.getString(ParseConstants.PARTICIPANTS_KEY_REMAINDER_TIME);
                //Restarts the timer with this time
                p.put(ParseConstants.PARTICIPANTS_KEY_PAUSED, 0);
                p.saveInBackground();
            }

            else if ((isInitialized(p)) && (p.getInt(ParseConstants.PARTICIPANTS_KEY_PAUSED) == 0)) {
                //Participant is currently running
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy, H:m"); //May 13, 2015, 23:26
                final String convertedDate = formatter.format(currentTime);

                            String remainderTime = part.getString(ParseConstants.PARTICIPANTS_KEY_REMAINDER_TIME);
                            if (remainderTime.isEmpty()) {
                                String startDate = part.getString(ParseConstants.PARTICIPANTS_KEY_START_TIME);
                                DateFormat format = new SimpleDateFormat("MMM d, yyyy, H:m", Locale.ENGLISH);
                                try {
                                    Date initDate= format.parse(startDate);
                                } catch (java.text.ParseException e1) {
                                    e1.printStackTrace();
                                }

                            }
                            part.put(ParseConstants.PARTICIPANTS_KEY_START_TIME, convertedDate);
                            part.saveInBackground();
                        }
                    }
                });
            }*/

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