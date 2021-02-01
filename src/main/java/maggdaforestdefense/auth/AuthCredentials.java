package maggdaforestdefense.auth;

public interface AuthCredentials {
    AuthUser getAuthUser() throws AuthenticationException;
    boolean isAnonymous();
}
