package fr.infotel.sofeed.utils;


import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by nicolas on 25/05/16.
 */
public class RabbitMqUtils {
    private static ConnectionFactory connectionFactory;

    public static ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null){
            connectionFactory= new ConnectionFactory();
            connectionFactory.setHost("192.168.1.22");
            connectionFactory.setAutomaticRecoveryEnabled(false);

        }
        return connectionFactory;

    }
}
