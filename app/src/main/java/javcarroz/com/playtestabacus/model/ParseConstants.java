package javcarroz.com.playtestabacus.model;


public final class ParseConstants {

    //This java file is for saving parameters that are constantly used to interact with the Parse server.

    //Class names in Parse Backend DB
    public static final String CLASS_PLAYTESTS = "Playtests";
    public static final String CLASS_PARTICIPANTS = "Participants";

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

    //Static values used for queries related to the Parse Object 'Participants'
    public static final int VALUE_PARTICIPANT_NON_VOID_STATUS = 0; // This acts as a bool (Parse only handles int) to flag that the participants is not void (e.g. playtest data is valid)
    public static final int VALUE_PARTICIPANT_NOT_PAUSED_STATUS = 0; // This acts as a bool to flag if the participant'stimer is paused = 1, non-paused = 0.
    public static final int VALUE_PARTICIPANT_STATUS = 0; // this acts as a bool to flag if participant's session has been initiated = 1, not initiated = 0
}