/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

/**
 *
 * @author David
 */
public class CommandArgument {

    private String name, value;

    public CommandArgument(String n, String val) {
        name = n;
        value = val;
    }
    
    public CommandArgument(String n, NetworkCommand innerCommand) {
        name = n;
        value = innerCommand.toString();
    }
    
    public NetworkCommand getInnerCommand() {
        try {
        return NetworkCommand.fromString(value);
        } catch(JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public CommandArgument(String n, int val) {
        this(n, String.valueOf(val));
    }
    
    public CommandArgument(String n, double val) {
        this(n, String.valueOf(val));
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
}
