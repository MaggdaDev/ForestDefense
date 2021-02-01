package maggdaforestdefense.auth;

public class WireCredentials {
    private CredentialType type;
    private FAuthCredentials fAuthCredentials;
    private AnonAuthCredentials anonAuthCredentials;

    public CredentialType getType() {
        return type;
    }

    public WireCredentials(CredentialType type, FAuthCredentials fAuthCredentials, AnonAuthCredentials anonAuthCredentials) {
        this.fAuthCredentials = fAuthCredentials;
        this.anonAuthCredentials = anonAuthCredentials;
        this.type = type;
    }

    public FAuthCredentials getfAuthCredentials() {
        return fAuthCredentials;
    }

    public AnonAuthCredentials getAnonAuthCredentials() {
        return anonAuthCredentials;
    }

    public enum CredentialType {
        AnonAuthCredentials,
        FAuthCredentials;
    }
}
