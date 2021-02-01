package maggdaforestdefense.auth;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FAuthCredentials implements Serializable, AuthCredentials {

    private String accessToken;

    public FAuthCredentials setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    @Override
    public AuthUser getAuthUser() throws AuthenticationException {
        URL userInfoUrl;
        try {
            userInfoUrl = new URL(IenokihpkgAuthHelper.USER_INFO_URL);
        } catch (MalformedURLException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            throw new AuthenticationException(AuthenticationException.Reason.HTTP_REQUEST_FAILED, sw.toString());
        }
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) userInfoUrl.openConnection();
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            BufferedReader in;
            if(con.getResponseCode()<299) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                throw new AuthenticationException(AuthenticationException.Reason.HTTP_REQUEST_FAILED, "Status code: " + con.getResponseCode() + "\nResult:\n" + content);
            }
            FAuthUser u;
            try {
                u = new Gson().fromJson(in, FAuthUser.class);
            } catch (JsonParseException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                throw new AuthenticationException(AuthenticationException.Reason.HTTP_REQUEST_FAILED, sw.toString());
            }
            in.close();
            con.disconnect();
            return u;
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            throw new AuthenticationException(AuthenticationException.Reason.HTTP_REQUEST_FAILED, sw.toString());
        }
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }
}
