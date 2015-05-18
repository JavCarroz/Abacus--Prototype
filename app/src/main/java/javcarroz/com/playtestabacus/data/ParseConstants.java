package javcarroz.com.playtestabacus.data;


public final class ParseConstants {

    //This java file is for saving parameters that are constantly used to interact with the Parse server.

    //Class names in Parse Backend DB
    public static final String CLASS_PLAYTESTS = "Playtests";
    public static final String CLASS_PARTICIPANTS = "Participants";
    public static final String LOCAL_SUBCLASS_PARTICIPANT = "Participant";

    //These field name keysare shared by all Parse Objects
    public static final String SHARED_KEY_CREATED_AT = "createdAt";
    public static final String SHARED_KEY_PARENT = "parent";


    //Field names in Parse Object 'Playtests'
    public static final String PLAYTESTS_KEY_PROJECT_NAME = "projectName";
    public static final String PLAYTESTS_KEY_CLIENT_NAME = "clientName";
    public static final String PLAYTESTS_KEY_PRODUCT_NAME = "productName";
    public static final String PLAYTESTS_KEY_NUM_OF_PART = "numOfParticipants";
    public static final String PLAYTESTS_KEY_PART_CODE = "participantsCode";
    public static final String PLAYTESTS_KEY_TEST_TIMER = "testTimer";
    public static final String PLAYTESTS_KEY_TEST_STATUS = "testStatus";
    public static final String PLAYTESTS_KEY_COMPLETED_AT = "completedAt";
    public static final String PLAYTESTS_KEY_BELONGS_TO = "belongsTo";

    //Static values used for queries related to the Parse Object 'Playtests'
    public static final int VALUE_TEST_STATUS_ONGOING = 0;
    public static final int VALUE_TEST_STATUS_ON_HOLD = 1;
    public static final int VALUE_TEST_STATUS_DONE = 2;
    public static final String VALUE_TEST_EMPTY_COMPLETED_AT = "";



    //Field names in Parse Object 'Participants'
    public static final String PARTICIPANTS_KEY_SUFFIX = "suffix";
    public static final String PARTICIPANTS_KEY_START_TIME = "startTime";
    public static final String PARTICIPANTS_KEY_END_TIME = "endTime";
    public static final String PARTICIPANTS_KEY_VOID = "void";
    public static final String PARTICIPANTS_KEY_PAUSED = "paused";
    public static final String PARTICIPANTS_KEY_PARTICIPANT_STATUS = "participantStatus";
    public static final String PARTICIPANTS_KEY_REMAINDER_TIME = "remainderTime";
    public static final String PARTICIPANTS_KEY_OBJECT_ID = "objectId";
    public static final String PARTICIPANTS_KEY_PREV_UNPAUSE_TIME = "prevUnpauseTimer";

    //Static values used for queries related to the Parse Object 'Participants'
    //Although Parse handles booleans, we will work with int as certain status flags have more than two (2) values.
    public static final int VALUE_PARTICIPANT_NON_VOID_STATUS = 0;
    public static final int VALUE_PARTICIPANT_NOT_PAUSED_STATUS = 0;
    public static final int VALUE_PARTICIPANT_STATUS = 0;
}