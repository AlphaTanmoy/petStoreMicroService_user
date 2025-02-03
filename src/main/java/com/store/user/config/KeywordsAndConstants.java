package com.store.user.config;


public class KeywordsAndConstants {

    public static final String ADMIN_MICROSERVICE_BASE_URL_LOC = "http://localhost:8081";
    public static final String AUTH_MICROSERVICE_BASE_URL_LOC = "http://localhost:8082";
    public static final String CHAT_MICROSERVICE_BASE_URL_LOC = "http://localhost:8083";
    public static final String CORE_MICROSERVICE_BASE_URL_LOC = "http://localhost:8084";
    public static final String PAYMENT_MICROSERVICE_BASE_URL_LOC = "http://localhost:8085";
    public static final String SELLER_MICROSERVICE_BASE_URL_LOC = "http://localhost:8086";
    public static final String USER_MICROSERVICE_BASE_URL_LOC = "http://localhost:8087";

    public static final String HEADER_AUTH_TOKEN="Alpha";
    public static final String API_KEY_PREFIX="Alpha-Key-";
    public static final String ALGORITHM = "AES";
    public static final String UTF_8 = "UTF-8";
    public static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";
    public static final String SECRET_KEY_FOR_PASSWORD_ENCRYPTION = "ALPHA1JGSkZWa2RTTUdoS1UydDBUVlJWTlZCVlJrWlRWVEZTVmxac1pGbFhWbkJvV1cxT2ExcFhXbTVoUjJ4eFlU";
    public static final Integer GET_PASSWORD_MIN_LENGTH = 8;
    public static final Boolean IS_PASSWORD_MUST_HAVE_CAPITAL_LETTER= false;
    public static final Boolean IS_PASSWORD_MUST_HAVE_SMALL_LETTER=false;
    public static final Boolean IS_PASSWORD_MUST_HAVE_NUMBER=false;
    public static final Boolean IS_PASSWORD_MUST_HAVE_SPECIAL_CHARACTER=false;
    public static final String RABBIT_MQ_USER_NAME="alphaTanmoy";
    public static final String RABBIT_MQ_PASSWORD="password";
    public static final String RABBIT_MQ_HOST="localhost";
    public static final Integer RABBIT_MQ_PORT=5672;

    public static final String RABBIT_MQ_EXCHANGE_USER="RABBIT_MQ_EXCHANGE_USER";
    public static final String RABBIT_MQ_QUEUE_FOR_EVENTS_USER = "RABBIT_MQ_QUEUE_FOR_EVENTS_USER";
    public static final String RABBIT_MQ_ROUTE_KEY_FOR_EVENTS_USER = "RABBIT_MQ_ROUTE_KEY_FOR_EVENTS_USER";
    public static final String RABBIT_MQ_QUEUE_FOR_REQUEST_SANITATION_USER = "RABBIT_MQ_QUEUE_FOR_REQUEST_SANITATION_USER";
    public static final String RABBIT_MQ_ROUTE_KEY_FOR_REQUEST_SANITATION_USER = "RABBIT_MQ_ROUTE_KEY_FOR_REQUEST_SANITATION_USER";
    public static final String RABBIT_MQ_QUEUE_FOR_LOGIN_OR_SIGNUP_OTP_USER = "RABBIT_MQ_QUEUE_FOR_LOGIN_OR_SIGNUP_OTP_USER";
    public static final String RABBIT_MQ_ROUTE_KEY_FOR_LOGIN_OR_SIGNUP_OTP_USER = "RABBIT_MQ_ROUTE_KEY_FOR_LOGIN_OR_SIGNUP_OTP_USER";
    public static final String RABBIT_MQ_ROUTE_KEY_FOR_FOREX_DATA_USER = "RABBIT_MQ_ROUTE_KEY_FOR_FOREX_DATA_USER";
    public static final String RABBIT_MQ_QUEUE_FOR_FOREX_DATA_USER = "RABBIT_MQ_QUEUE_FOR_FOREX_DATA_USER";


    public static final Integer MAXIMUM_DEVICE_CAN_CONNECT = 3;
    public static final Integer OTP_EXPIRED_IN_MINUTES = 2;
    public static final String SECRET_KEY="wpembytrwcvnryxksdbqwjebrTANMOYuyGHyudqgwveytrtrCSnwifoesarjbwe";
    public static final String JWT_HEADER="Authorization";
    public static final String SIGNING_PREFIX="signing_";
    public static final String OTP_TEXT_FOR_LOGIN="your login otp is - ";
    public static final String OTP_SUBJECT_FOR_LOGIN="petStore Login/Signup Otp";

}
