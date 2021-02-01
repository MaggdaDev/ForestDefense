package maggdaforestdefense.auth;

public class AnonAuthUser implements AuthUser {

    private String number;

    public AnonAuthUser setNumber(String number) {
        this.number = number;
        return this;
    }

    @Override
    public String getUsername() {
        return "Anonymous #" + number;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public boolean getEmailVerified() {
        return false;
    }

    @Override
    public String getPictureUrl() {
        return null;
    }

    @Override
    public String getId() {
        return "Anonymous";
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
