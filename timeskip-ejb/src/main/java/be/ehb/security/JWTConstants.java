package be.ehb.security;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public interface JWTConstants {
    //JWT header
    String HEADER_TYPE = "typ";
    String HEADER_TYPE_VALUE = "JWT";
    String HEADER_X5U = "x5u";
    String AUDIENCE_CLAIM = "aud";
    String EXPIRATION_CLAIM = "exp";
    //JWT keys
    String NAME = "name";
    String EMAIL = "email";
    String GIVEN_NAME = "given_name";
    String SURNAME = "family_name";
}
