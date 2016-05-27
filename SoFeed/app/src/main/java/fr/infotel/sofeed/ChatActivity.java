package fr.infotel.sofeed;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import fr.infotel.sofeed.utils.HashMapUtils;
import fr.infotel.sofeed.utils.RabbitMqUtils;

import fr.infotel.sofeed.bean.Message;

/**
 * Created by n_bl on 25/05/2016.
 */
public class ChatActivity extends AppCompatActivity{
    private String username;
    private ConnectionFactory factory;
    private HashMap<String,String> messages = HashMapUtils.getMap();
    private BlockingDeque<String> queue= new LinkedBlockingDeque<String>();
    private String currentChatRoom="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentChatRoom = getIntent().getStringExtra("CHATROOM");
        setTitle("ChatRoom: " + currentChatRoom);
        username = getIntent().getStringExtra("USERNAME");

        factory = RabbitMqUtils.getConnectionFactory();
        publishToAMQP();
        setupPubButton();
        if (messages.containsKey(currentChatRoom)) {
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(messages.get(currentChatRoom));
        }

        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
                Date now = new Date();
                // Map the response
                String message;
                ObjectMapper mapper = new ObjectMapper();
                Message messageO = null;
                try {
                    messageO = mapper.readValue(msg.getData().getString("msg"), Message.class);
                    message = messageO.getMessage();
                } catch (IOException e) {
                    message = msg.getData().getString("msg");
                    messageO.setSender("null");
                    messageO.setChatRoom("null");
                }
                if (isMessageForUsername(messageO,username)) {
                    message = ft.format(now) + ' ' + messageO.getSender() + ": "+ message + '\n';
                    String sender = messageO.getSender();
                    String chatRoom = messageO.getChatRoom();
                    if (messages.containsKey(chatRoom)) {
                        messages.put(chatRoom, messages.get(chatRoom) + message);
                    } else {
                        messages.put(chatRoom, message);
                    }
                    TextView tv = (TextView) findViewById(R.id.textView);
                    tv.setText(messages.get(currentChatRoom));
                }
            }
        };
        subscribe(incomingMessageHandler);
        setupPubButton();

    }

    private boolean isMessageForUsername(Message messageO, String username) {
        if (messageO.getChatRoom().equals(username) || messageO.getSender().equals(username)
                || messageO.getChatRoom().equals("SoFeed")){
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home){
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.putExtra("USERNAME",username);
            startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    void setupPubButton() {
        Button button = (Button) findViewById(R.id.publish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText et = (EditText) findViewById(R.id.text);
                publishMessage(et.getText().toString());
                et.setText("");
            }
        });
    }


    Thread subscribeThread;
    Thread publishThread;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        publishThread.interrupt();
        subscribeThread.interrupt();
    }


    void publishMessage(String message) {
        //Adds a message to internal blocking queue
        Message messageO = new Message();
        messageO.setSender(username);
        messageO.setChatRoom(currentChatRoom);
        messageO.setMessage(message);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(messageO);
        } catch (JsonProcessingException e) {
            json = message;
        }
        try {
            Log.d("","[q] " + json);
            queue.putLast(json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void subscribe(final Handler handler)
    {
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.basicQos(1);
                        AMQP.Queue.DeclareOk q = channel.queueDeclare();
                        channel.queueBind(q.getQueue(), "amq.fanout", "chat");
                        QueueingConsumer consumer = new QueueingConsumer(channel);
                        channel.basicConsume(q.getQueue(), true, consumer);

                        // Process deliveries
                        while (true) {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                            String message = new String(delivery.getBody());
                            Log.d("","[r] " + message);

                            android.os.Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();

                            bundle.putString("msg", message);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e1) {
                        Log.d("", "Connection broken: " + e1.getClass().getName());
                        try {
                            Thread.sleep(4000); //sleep and then try again
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            }
        });
        subscribeThread.start();
    }

    public void publishToAMQP()
    {
        publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel ch = connection.createChannel();
                        ch.confirmSelect();

                        while (true) {
                            String message = queue.takeFirst();
                            try{
                                ch.basicPublish("amq.fanout", "chat", null, message.getBytes());
                                Log.d("", "[s] " + message);
                                ch.waitForConfirmsOrDie();
                            } catch (Exception e){
                                Log.d("","[f] " + message);
                                queue.putFirst(message);
                                throw e;
                            }
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        Log.d("", "Connection broken: " + e.getClass().getName());
                        try {
                            Thread.sleep(5000); //sleep and then try again
                        } catch (InterruptedException e1) {
                            break;
                        }
                    }
                }
            }
        });
        publishThread.start();
    }
}
