package javcarroz.com.playtestabacus.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

public class ParticipantAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mParticipants;

    public ParticipantAdapter (Context context, String[] participants) {
        mContext = context;
        mParticipants = participants;
    }

    @Override
    public int getCount() {
        return mParticipants.length;
    }

    @Override
    public Object getItem(int position) {
        return mParticipants[position];
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
            //Update the line below once you have a list item XML layout
            //convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
              holder = new ViewHolder();
//            holder.participantCode = (TextView) convertView.findViewById(R.id.iconImageView);
//            holder.participantStatus = (TextView) convertView.findViewById(R.id.iconImageView);
//            holder.startTime = (TextView) convertView.findViewById(R.id.iconImageView);
//            holder.endTime = (TextView) convertView.findViewById(R.id.iconImageView);
//            holder.timer = (TextClock) convertView.findViewById(R.id.iconImageView);
//            holder.pauseButton = (Button) convertView.findViewById(R.id.iconImageView);
//            holder.voidButton = (Button) convertView.findViewById(R.id.iconImageView);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*Day day = mDays[position];
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() +"");
        if (position == 0){
            holder.dayLabel.setText("Today");
        }
        else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }*/

        return convertView;
    }


    private static class ViewHolder {
        TextView participantCode;
        TextView participantStatus;
        TextView startTime;
        TextView endTime;
        TextClock timer;
        Button pauseButton;
        Button voidButton;
    }
}
