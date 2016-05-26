package fr.infotel.sofeed.utils;

/**
 * Created by n_bl on 26/05/2016.
 */
public class Utils {
    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
