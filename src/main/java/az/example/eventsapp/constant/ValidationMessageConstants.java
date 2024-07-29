package az.example.eventsapp.constant;

public class ValidationMessageConstants {

    public  static final String TELEPHONE_REGEX = "^\\+?[0-9. ()-]{7,25}$";
    public static final String TELEPHONE_REGEX_MESSAGE = "Telephone number is required. Please include a phone number of a least 7 digits. ";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String EMAIL_REGEX_MESSAGE = "Invalid email address. Please enter an email address in the following format: example@example.com";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]+$";
    public static final String USERNAME_REGEX_MESSAGE = "Numbers and the first letter starting with a lowercase letter not allowed";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    public static final String PASSWORD_REGEX_MESSAGE = "The password must be at least 8 characters , contain at least 1 digit, least 1 lowercase letter, least 1 uppercase letter and must not contain spaces.";
}
