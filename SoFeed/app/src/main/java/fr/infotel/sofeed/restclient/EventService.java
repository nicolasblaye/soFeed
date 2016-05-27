package fr.infotel.sofeed.restclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.infotel.sofeed.bean.Event;
import fr.infotel.sofeed.utils.Utils;

/**
 * Created by n_bl on 26/05/2016.
 */
public class EventService {

    public static List<Event> getEvents(){
        List<Event>events = null;
        try{
            URL url = new URL("http://192.168.1.77:8080/infotel/event/list");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setDoInput(true);
            InputStream is = conn.getInputStream();
            String json = Utils.convertStreamToString(is);
            is.close();
            ObjectMapper mapper = new ObjectMapper();
            events = mapper.readValue(json, new TypeReference<List<Event>>(){});
        }catch(IOException e){

        }
        return events;
    }
}
