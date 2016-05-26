package fr.infotel.sofeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.nio.channels.Channel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import fr.infotel.sofeed.utils.RabbitMqUtils;

/**
 * Created by n_bl on 25/05/2016.
 */
public class ChatActivity extends AppCompatActivity{
    private String username;
    private ConnectionFactory factory;
    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
    private Thread subscribeThread;
    private Thread publishThread;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String chatRoom = getIntent().getStringExtra("CHATROOM");
        setTitle("ChatRoom: " + chatRoom);
        username = getIntent().getStringExtra("USERNAME");

        //start chat services
        factory = RabbitMqUtils.getConnectionFactory();
        factory = RabbitMqUtils.getConnectionFactory();
        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                TextView tv = (TextView) findViewById(R.id.textView);
                Date now = new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
                tv.append(ft.format(now) + ' ' + message + '\n');
            }
        };
        subscribe(incomingMessageHandler, username, chatRoom);
        publishToAMQP(username,chatRoom);
        setupPubButton();

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
            publishThread.interrupt();
            subscribeThread.interrupt();
            startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    void publishMessage(String message, String username) {
        try {
            queue.putLast(username+": "+message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publishToAMQP(final String username, final String chatRoom)
    {
        publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Connection connection = factory.newConnection();
                        com.rabbitmq.client.Channel ch = connection.createChannel();
                        ch.confirmSelect();

                        while (true) {
                            String message = queue.takeFirst();
                            try{

                                ch.basicPublish("amq.fanout", getChatName(username,chatRoom),
                                        null, message.getBytes());
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
    public void subscribe(final Handler handler, final String username, final String chatRoom){
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Connection connection = factory.newConnection();
                        com.rabbitmq.client.Channel channel = connection.createChannel();
                        channel.basicQos(1);
                        AMQP.Queue.DeclareOk q = channel.queueDeclare();

                        channel.queueBind(q.getQueue(), "amq.fanout", getChatName(username, chatRoom));
                        QueueingConsumer consumer = new QueueingConsumer(channel);
                        channel.basicConsume(q.getQueue(), true, consumer);

                        while (true) {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                            String message = new String(delivery.getBody());
                            Log.d("","[r] " + message);
                            Message msg = handler.obtainMessage();
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
                            Thread.sleep(5000); //sleep and then try again
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            }
        });
        subscribeThread.start();
    }

    private String getChatName(String username, String chatRoom) {
        username = username.toLowerCase();
        chatRoom = chatRoom.toLowerCase();
        String chat;
        if (chatRoom.equals("SoFeed")){
            return chatRoom;
        }
        else if (username.compareTo(chatRoom)>=1){
            chat = username+"-"+chatRoom;
        }
        else{
            chat = chatRoom+"-"+username;
        }
        return chat;
    }

    void setupPubButton() {
        Button button = (Button) findViewById(R.id.publish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText et = (EditText) findViewById(R.id.text);
                publishMessage(et.getText().toString(), username);
                et.setText("");
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        publishThread.interrupt();
        subscribeThread.interrupt();
    }
}
