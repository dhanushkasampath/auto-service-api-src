package com.auto.care.autoserviceapisrc.util;

public class Constants {
    private Constants(){
        //This class facilitate constants
    }

    //////////////////Customer JWT Properties/////////////////////
    public static final String USER_ID_KEY = "use_id";
    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_FIRST_NAME_KEY = "first_name";
    public static final String USER_LAST_NAME_KEY = "last_name";
    public static final String USER_EMAIL_KEY = "email";
    public static final String USER_DOB_KEY = "dob";
    public static final String USER_CONTACT_NO_KEY = "contact_no";
    public static final String USER_DISTRICT_KEY = "district";
    public static final String USER_PROVINCE_KEY = "province";
    public static final String USER_TOWN_KEY = "town";
    public static final String USER_ONE_TIME_AUTH_KEY = "one_time_auth_key";
    public static final String USER_TYPE_KEY = "user_type";
    public static final String AUTHORIZATION_HEADER = "Authorization";



    /////////////// UniqueConstraintExceptions //////////////////
    public static final String DUPLICATE_USER_NAME = "duplicateUserName";
    public static final String DUPLICATE_EMAIL = "duplicateEmail";
}
