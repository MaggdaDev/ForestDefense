package maggdaforestdefense.auth;

public class Credentials {
    public boolean signedIn = false;
    public String userId = "";
    public String userName = "";
    public String authToken = "";
    MWUser mwUser;

    /**
     * Refreshes the user details.
     * @return Whether or not the refresh was successful.
     */
    public boolean refresh() {
        setAuthToken(AuthWindow.refreshToken(getAuthToken()));
        setMwUser(AuthWindow.getUserFromToken(getAuthToken()));
        setSignedIn(!(getMwUser()==null));
        return isSignedIn();
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public MWUser getMwUser() {
        return mwUser;
    }

    public void setMwUser(MWUser mwUser) {
        this.mwUser = mwUser;
    }
}
