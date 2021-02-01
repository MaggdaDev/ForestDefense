package maggdaforestdefense.auth;

public class AuthenticationException extends Exception {
    private Reason reason;
    private String details;

    public AuthenticationException(Reason reason) {
        this.reason = reason;
    }

    public AuthenticationException(Reason reason, String details) {
        this.reason = reason;
        this.details = details;
    }

    public String getReason() {
        switch (reason) {
            case HTTP_REQUEST_FAILED:
                if(details!=null) {
                    return "Couldn't request account information. Please check your internet connection.\nDetails:\n" + details;
                } else {
                    return "Couldn't request account information. Please check your internet connection.";
                }
            case IENOKIHPKG_MISSING:
                if(details!=null) {
                    return "The ienokihpkg cli could not be found. Make sure you have started ForestDefense through ienokihpkg. \nDetails:\n" + details;
                } else {
                    return "The ienokihpkg cli could not be found. Make sure you have started ForestDefense through ienokihpkg. ";
                }
            case IENOKIHPKG_EXECUTION_FAILED:
                if(details!=null) {
                    return "The ienokihpkg cli could not be started. Make sure you have started ForestDefense through ienokihpkg. \nDetails:\n" + details;
                } else {
                    return "The ienokihpkg cli could not be started. Make sure you have started ForestDefense through ienokihpkg. ";
                }
            default:
                return "unknown reason";
        }
    }

    public enum Reason {
        HTTP_REQUEST_FAILED,
        IENOKIHPKG_MISSING,
        IENOKIHPKG_EXECUTION_FAILED;
    }
}
