package javcarroz.com.playtestabacus.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Participant")
public class Participant extends ParseObject {

    protected String mSuffix = ParseConstants.PARTICIPANTS_KEY_SUFFIX;
    protected String mStartTime = ParseConstants.PARTICIPANTS_KEY_START_TIME;
    protected String mEndTime = ParseConstants.PARTICIPANTS_KEY_END_TIME;
    protected String mVoid = ParseConstants.PARTICIPANTS_KEY_VOID;
    protected String mPaused = ParseConstants.PARTICIPANTS_KEY_PAUSED;
    protected String mParticipantStatus = ParseConstants.PARTICIPANTS_KEY_PARTICIPANT_STATUS;
    protected String mRemainderTime = ParseConstants.PARTICIPANTS_KEY_REMAINDER_TIME;

    public String getSuffix() {
        return getString(mSuffix);
    }

    public void setSuffix(int suffix) {
        put(mSuffix, suffix);
    }

    public String getStartTime() {
        return getString(mStartTime);
    }

    public void setStartTime(String startTime) {
        put(mStartTime, startTime);
    }

    public String getEndTime() {
        return getString(mEndTime);
    }

    public void setEndTime(String endTime) {
        put(mEndTime, endTime);
    }

    public String getVoid() {
        return getString(mVoid);
    }

    public void setVoid(int aVoid) {
        put(mVoid, aVoid);
    }

    public String getPaused() {
        return getString(mPaused);
    }

    public void setPaused(int paused) {
        put(mPaused, paused);
    }

    public String getParticipantStatus() {
        return getString(mParticipantStatus);
    }

    public void setParticipantStatus(int participantStatus) {
        put(mParticipantStatus, participantStatus);
    }

    public String getRemainderTime() {
        return getString(mRemainderTime);
    }

    public void setRemainderTime(String remainderTime) {
        put(mRemainderTime, remainderTime);
    }

}
