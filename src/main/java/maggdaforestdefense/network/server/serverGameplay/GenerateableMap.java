package maggdaforestdefense.network.server.serverGameplay;

public class GenerateableMap {
    // Some static stuff
    public static final int CURRENT_VERSION = 1;

    // Vars

    /**
     * The version of the map file. Use only if previous versions are possibly incompatible to the new one.
     */
    private int version;

    // New

    public GenerateableMap() {
        version = CURRENT_VERSION;
    }

    // Getter and Setter

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
