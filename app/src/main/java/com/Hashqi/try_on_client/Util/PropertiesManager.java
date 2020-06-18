package com.Hashqi.try_on_client.Util;

import android.content.Context;

import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    private static Properties props = null;

    static {
        if (props == null) {
            props = new Properties();
        }
    }

    public static String getProps(Context ctx, String filename, String key){
        try {
            props.load(ctx.getApplicationContext().getAssets().open(filename));
            return props.getProperty(key);
        } catch (IOException e) {
            System.out.println("-----"+e);
            return "";
        }

    }

}
