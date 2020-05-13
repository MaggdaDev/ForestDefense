package maggdaforestdefense.config;

import maggdaforestdefense.auth.Credentials;

public class Configuration {
    public Credentials auth = new Credentials();

    public Credentials getAuth() {
        return auth;
    }

    public void setAuth(Credentials auth) {
        this.auth = auth;
    }
}
