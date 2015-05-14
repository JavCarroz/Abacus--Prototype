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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javcarroz.com.playtestabacus.PlaytestAbacusApplication;
import javcarroz.com.playtestabacus.R;
import javcarroz.com.playtestabacus.data.ParseConstants;

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
            holder.clock = (TextView) convertView.findViewById(R.id.clock);
            holder.iconStatus = (ImageView) convertView.findViewById(R.id.iconStatusView);
            holder.startPauseButton = (Button) convertView.findViewById(R.id.startPauseButton);
            holder.unvalidButton = (Button)convertView.findViewById(R.id.unvalidButton);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

//        class CounterClass extends CountDownTimer {
//            /**
//             * @param millisInFuture    The number of millis in the future from the call
//             *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
//             *                          is called.
//             * @param countDownInterval The interval along the way to receive
//             *                          {@link #onTick(long)} callbacks.
//             */
//            public CounterClass(long millisInFuture, long countDownInterval) {
//                super(millisInFuture, countDownInterval);
//            }
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                long millis = millisUntilFinished;
//                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
//                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
//                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//                //holder.clock.setText(hms);
//            }
//
//            @Override
//            public void onFinish() {
//                //conclude the participant in parse
//            }
//        }

        ParseObject participant = mParticipants.get(position);
        final String objectId = participant.getObjectId();
        View.OnClickListener onVoidParticipantListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Participant");
                query.getInBackground(objectId, new GetCallback<ParseObject>() {
                    public void done(ParseObject part, ParseException e) {
                        if (e == null) {
                            Log.i("voidButton", "It is been pressed");
                            part.put(ParseConstants.PARTICIPANTS_KEY_VOID, 1);
                            part.saveInBackground();
                            //Need to refresh the list item after this.
                        }
                    }
                });
                v.setClickable(false);
            }
        };
//        final CounterClass timer = new CounterClass(5000,1000);
//        View.OnClickListener timerPlayPauseListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                timer.start();
//                Log.i("startButton", "It is been pressed");
//
//                //timer.cancel();
//                //Need to refresh the list item after this.
//            }
//        };

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
            holder.unvalidButton.setOnClickListener(onVoidParticipantListener);
        }

        else {
            holder.iconStatus.setImageResource(R.mipmap.ic_action_edit);
            holder.clock.setText(PlaytestAbacusApplication.mProjectRef.getString(ParseConstants.PLAYTESTS_KEY_TEST_TIMER));
            holder.unvalidButton.setOnClickListener(onVoidParticipantListener);
            //mHolder.startPauseButton.setOnClickListener(timerPlayPauseListener);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView participantCodeLabel;
        TextView clock;
        ImageView iconStatus;
        Button startPauseButton;
        Button unvalidButton;
    }
}
