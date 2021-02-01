package maggdaforestdefense.config;

public class Configuration {
    private boolean anon = true;

    public boolean isAnon() {
        return anon;
    }

    public Configuration setAnon(boolean anon) {
        this.anon = anon;
        return this;
    }
}
