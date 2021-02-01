package maggdaforestdefense.util;

import com.google.gson.*;
import maggdaforestdefense.auth.AuthCredentials;

import java.lang.reflect.Type;

public class Json {
    private static Gson gson;

    public static Gson getGson() {
        if(gson==null) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }
}
