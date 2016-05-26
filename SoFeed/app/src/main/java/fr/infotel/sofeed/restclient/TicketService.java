package fr.infotel.sofeed.restclient;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.infotel.sofeed.bean.Ticket;
import fr.infotel.sofeed.utils.Utils;

/**
 * Created by n_bl on 26/05/2016.
 */
public class TicketService {
    public static List<Ticket> getTickets(){
        List<Ticket>tickets = null;
        try{
            URL url = new URL("http://192.168.1.12:8080/infotel/employee/2/ticket/list");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setDoInput(true);
            InputStream is = conn.getInputStream();
            String json = Utils.convertStreamToString(is);
            is.close();
            ObjectMapper mapper = new ObjectMapper();
            tickets = mapper.readValue(json, new TypeReference<List<Ticket>>(){});
        }catch(IOException e){

        }

        return tickets;
    }

}
