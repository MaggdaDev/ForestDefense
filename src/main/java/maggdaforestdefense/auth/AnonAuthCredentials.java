package maggdaforestdefense.auth;

import java.util.Random;

public class AnonAuthCredentials implements AuthCredentials {
    private String userNumber;

    @Override
    public AuthUser getAuthUser() throws AuthenticationException {
        if(userNumber==null) {
            userNumber = Integer.toString(new Random().nextInt(1000)+1);
        }
        return new AnonAuthUser().setNumber(userNumber);
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }
}
