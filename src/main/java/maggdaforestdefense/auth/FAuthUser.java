package maggdaforestdefense.auth;

import java.io.Serializable;

public class FAuthUser implements Serializable, AuthUser {

    private String applicationId;
    private String birthdate;
    private String email;
    private boolean email_verified;
    private String family_name;
    private String given_name;
    private String name;
    private String middle_name;
    private String phone_number;
    private String picture;
    private String[] roles;
    private String preferred_username; // Username
    private String sub; // ID

    @Override
    public String getUsername() {
        return preferred_username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean getEmailVerified() {
        return email_verified;
    }

    @Override
    public String getPictureUrl() {
        return picture;
    }

    @Override
    public String getId() {
        return sub;
    }

    @Override
    public boolean isSignedIn() {
        return true;
    }
}
