package fr.infotel.sofeed.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Created by n_bl on 26/05/2016.
 */
public class RestClientUtils {
    private static Client client;

    public static Client getClient(){
        if (client == null){
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            client = Client.create(clientConfig);
        }

        return client;
    }
}
