package com.freelift;

public interface Constants {
        public static final String API_DOMAIN = "http://bookyourtaxi.vantageappspro.com/index.php?";
//    public static final String API_DOMAIN = "http://synopsis.yantradigital.com/bookyourtaxi/index.php?";
    public static final String METHOD_REGISTER = API_DOMAIN + "route=account/register";
    public static final String METHOD_LOGIN = API_DOMAIN + "route=account/login";
    public static final String METHOD_FORGOT =API_DOMAIN+"route=account/forgot";
    public static final String METHOD_LOGOUT = API_DOMAIN + "route=account/logout";
    public static final String METHOD_PROFILE_UPDATE = API_DOMAIN + "route=account/update";
    public static final String METHOD_PROFILE_DETAILS_GET = API_DOMAIN + "route=account/profile";
    public static final String METHOD_PROFILE_UPLOADPHOTO = API_DOMAIN + "route=account/updatepic";
    public static final String METHOD_BOOKING_CANCEL = API_DOMAIN + "route=book/cancel";
    // ===============Passenger Api===============================
    public static final String METHOD_PASSENGER_SEARCH_TAXI = API_DOMAIN + "route=search/taxi";
    public static final String METHOD_PASSENGER_BOOK_TAXI = API_DOMAIN + "route=book/taxi";
    public static final String METHOD_EMERGENCY_REPORT = API_DOMAIN + "route=account/emergency";
    public static final String METHOD_PASSENGER_RIDES = API_DOMAIN + "route=book/customerstats";
    public static final String METHOD_UPDATE_LOCATION = API_DOMAIN + "route=common/home/driverlocation";
    public static final String METHOD_REPORT = API_DOMAIN + "route=account/report";

    //========================Driver Api=============================

    public static final String METHOD_DRIVER_RIDES = API_DOMAIN + "route=book/confirm";
    public static final String METHOD_DRIVER_PENDING_LIST = API_DOMAIN + "route=book/driverstats";
    public static final String METHOD_DRIVER_BOOKING_ACCEPT = API_DOMAIN + "route=book/accept";
    public static final String METHOD_DRIVER_START_END = API_DOMAIN + "route=book/updatestatus";


    public static final int LIST_ITEMS = 20;

    public static final int FRIENDS_FIND = 1;
    public static final int QUESTION_ANSWER = 2;
    public static final int QUESTION_SELECT_ANSWER_IMG = 3;

    public static final Integer ERROR_SUCCESS = 0;

    public static final int ACCOUNT_STATE_ENABLED = 0;
    public static final int ACCOUNT_STATE_DISABLED = 1;
    public static final int ACCOUNT_STATE_BLOCKED = 2;
    public static final int ACCOUNT_STATE_DEACTIVATED = 3;

    public static final int GCM_NOTIFY_CONFIG = 0;
    public static final int GCM_NOTIFY_SYSTEM = 1;
    public static final int GCM_NOTIFY_CUSTOM = 2;
    public static final int GCM_NOTIFY_LIKE = 3;
    public static final int GCM_NOTIFY_ANSWER = 4;
    public static final int GCM_NOTIFY_QUESTION = 5;
    public static final int GCM_NOTIFY_COMMENT = 6;
    public static final int GCM_NOTIFY_FOLLOWER = 7;
    public static final int GCM_QUIZ_ANSWER = 8;

    public static final int NOTIFY_TYPE_LIKE = 0;
    public static final int NOTIFY_TYPE_ANSWER = 1;

    public static final int ERROR_UNKNOWN = 100;
    public static final int ERROR_ACCESS_TOKEN = 101;

    public static final int ERROR_LOGIN_TAKEN = 300;
    public static final int ERROR_EMAIL_TAKEN = 301;

    public static final int ERROR_ACCOUNT_ID = 400;

    public static final int QUESTION_TYPE_DEFAULT = 0;

    public static final int DISABLE_ANONYMOUS_QUESTIONS = 0;
    public static final int ENABLE_ANONYMOUS_QUESTIONS = 0;

    public static final String TAG = "TAG";

    public static final String HASHTAGS_COLOR = "#5BCFF2";
}