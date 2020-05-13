package maggdaforestdefense.auth;

import java.io.Serializable;

public class MWUser implements Serializable {
    private String username = "";
    private int editcount = 0;
    private boolean confirmed_email = false;
    private boolean blocked = false;
    private String registered = "";
    private String[] groups = new String[]{};
    private String[] rights = new String[]{};
    private String[] grants = new String[]{};
    private String realname = "";
    private String email = "";

    public String getUsername() {
        return username;
    }

    public int getEditcount() {
        return editcount;
    }

    public boolean isConfirmed_email() {
        return confirmed_email;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getRegistered() {
        return registered;
    }

    public String[] getGroups() {
        return groups;
    }

    public String[] getRights() {
        return rights;
    }

    public String[] getGrants() {
        return grants;
    }

    public String getRealname() {
        return realname;
    }

    public String getEmail() {
        return email;
    }


    public static MWUser anonymous() {
        MWUser u = new MWUser();
        u.username = "Anonymous";
        return u;
    }
}
