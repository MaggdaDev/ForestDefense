/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

import com.google.gson.Gson;

/**
 *
 * @author DavidPrivat
 */
public class GsonConverter {
    
    public static String toGsonString(Object object) {
        return new Gson().toJson(object);
    }

    
    public static Object fromString(String s, Class c) {
        return new Gson().fromJson(s, c);
    }
}
