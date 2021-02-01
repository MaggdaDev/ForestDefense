package maggdaforestdefense.auth;

public interface AuthUser {
    String getUsername();
    String getEmail();
    boolean getEmailVerified();
    String getPictureUrl();
    String getId();
    boolean isSignedIn();
}
