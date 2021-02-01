package maggdaforestdefense.auth;

import maggdaforestdefense.storage.Logger;

import java.io.*;

public class IenokihpkgAuthHelper {
    public static String USER_INFO_URL = "https://sso.minortom.net/oauth2/userinfo";

    public static AuthCredentials signIn() throws AuthenticationException {
        Logger.logClient("Signing in");
        if(System.getenv("IENOKIGPKG_CLI")!=null) {
            Process process;
            try {
                process = Runtime.getRuntime().exec(System.getenv("IENOKIGPKG_CLI") + " --get-api-key");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Logger.logClient("ienokihpkgcli: " + line);
                    if(line.startsWith("APITOKEN ")) {
                        return new FAuthCredentials().setAccessToken(line.split("APITOKEN ")[0]);
                    }
                }
                int exitVal = process.waitFor();
                Logger.logClient("ienokihpkg exited: " + exitVal);
                throw new AuthenticationException(AuthenticationException.Reason.IENOKIHPKG_EXECUTION_FAILED, "Exited unexpectedly");
            } catch (IOException | InterruptedException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                throw new AuthenticationException(AuthenticationException.Reason.IENOKIHPKG_EXECUTION_FAILED, "Exception:\n" + sw);
            }
        } else {
            //return new FAuthCredentials().setAccessToken("PLACEHOLDER");
            throw new AuthenticationException(AuthenticationException.Reason.IENOKIHPKG_MISSING);
        }
    }

    public static void signOut() throws AuthenticationException {
        Logger.logClient("Signing out");
        if(System.getenv("IENOKIGPKG_CLI")!=null) {
            Process process;
            try {
                process = Runtime.getRuntime().exec(System.getenv("IENOKIGPKG_CLI") + " --signout");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Logger.logClient("ienokihpkgcli: " + line);
                    if(line.startsWith("SUCCESS")) {
                        return;
                    }
                }
                int exitVal = process.waitFor();
                Logger.logClient("ienokihpkg exited: " + exitVal);
                throw new AuthenticationException(AuthenticationException.Reason.IENOKIHPKG_EXECUTION_FAILED, "Exited unexpectedly");
            } catch (IOException | InterruptedException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                throw new AuthenticationException(AuthenticationException.Reason.IENOKIHPKG_EXECUTION_FAILED, "Exception:\n" + sw);
            }
        } else {
            throw new AuthenticationException(AuthenticationException.Reason.IENOKIHPKG_MISSING);
        }
    }
}
