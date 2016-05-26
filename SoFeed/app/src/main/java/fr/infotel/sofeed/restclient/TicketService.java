package fr.infotel.sofeed.restclient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import java.util.List;

import fr.infotel.sofeed.bean.Ticket;
import fr.infotel.sofeed.utils.RestClientUtils;

/**
 * Created by n_bl on 26/05/2016.
 */
public class TicketService {
    public static List<Ticket> getTickets(){
        Client client = RestClientUtils.getClient();
        WebResource r = client.resource("http://localhost:8080/infotel/employee/2/ticket/list");
        ClientResponse response = r.type("application/json").get(ClientResponse.class);
        List<Ticket> tickets = response.getEntity(new GenericType<List<Ticket>>(){});
        return tickets;
    }
}
