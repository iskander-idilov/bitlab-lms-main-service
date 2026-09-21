package kz.bitlab.springboot.mainservice.util;

public final class Constants {
    private Constants() {}

    public static final class Keycloak {
        private Keycloak() {}

        public static final String GRANT_TYPE = "grant_type";
        public static final String GRANT_TYPE_PASSWORD = "password";
        public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
        public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

        public static final String CLIENT_ID = "client_id";
        public static final String CLIENT_SECRET = "client_secret";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String ACCESS_TOKEN = "access_token";

        public static final String FIELD_USERNAME = "username";
        public static final String FIELD_EMAIL = "email";
        public static final String FIELD_FIRST_NAME = "firstName";
        public static final String FIELD_LAST_NAME = "lastName";
        public static final String FIELD_ENABLED = "enabled";
        public static final String FIELD_EMAIL_VERIFIED = "emailVerified";

        public static final String CREDENTIAL_TYPE = "type";
        public static final String CREDENTIAL_TYPE_PASSWORD = "password";
        public static final String CREDENTIAL_VALUE = "value";
        public static final String CREDENTIAL_TEMPORARY = "temporary";
    }
}