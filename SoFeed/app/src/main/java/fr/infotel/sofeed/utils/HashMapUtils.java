package fr.infotel.sofeed.utils;

import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by nicolas on 26/05/16.
 */
public class HashMapUtils {
    private static HashMap<String,String> map = null;

    public static HashMap<String,String> getMap(){
        if (map == null) {
            map = new HashMap<String,String>();
        }
        return map;
    }
}
