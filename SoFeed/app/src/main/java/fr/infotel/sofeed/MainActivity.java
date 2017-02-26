package fr.infotel.sofeed;

import android.app.ListFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import fr.infotel.sofeed.utils.HashMapUtils;
import fr.infotel.sofeed.utils.RabbitMqUtils;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private String[] mChat;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private String username;
    private ConnectionFactory factory;
    private Thread notificationThread;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //username
        username = this.getIntent().getStringExtra("USERNAME");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create tabMenu
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Work"));
        tabLayout.addTab(tabLayout.newTab().setText("Fun"));
        tabLayout.addTab(tabLayout.newTab().setText("Learn"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        String[] Titles = {"Accueil", "Travail", "Loisir", "Veille"};
        int Numboftabs = 4;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mChat = getResources().getStringArray(R.array.nav_drawer_labels);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mChat));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_navigation);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        factory = RabbitMqUtils.getConnectionFactory();
        // instantiate queue
        HashMapUtils.getMap();
        notification();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Start Chat activity
     */
    private void selectItem(int position) {
        Bundle args = new Bundle();
        if (!mChat[position].equals("Employés") && !mChat[position].equals("Projet") && !mChat[position].equals("")) {
            String chatRoom = mChat[position];
            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("CHATROOM", chatRoom);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message m) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.AppTheme_PopupOverlay);
            // set title
            alertDialogBuilder.setTitle("Nouvelle Evénement");
            // set dialog message
            alertDialogBuilder
                    .setMessage((String) m.obj)
                    .setCancelable(false)
                    .setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    };

    public void notification() {
        if (notificationThread == null) {
            notificationThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    while (true) {
                        try {
                            Connection connection = factory.newConnection();
                            com.rabbitmq.client.Channel channel = connection.createChannel();
                            channel.basicQos(1);
                            AMQP.Queue.DeclareOk q = channel.queueDeclare();
                            channel.queueBind(q.getQueue(), "amq.direct", "notification");
                            QueueingConsumer consumer = new QueueingConsumer(channel);
                            channel.basicConsume(q.getQueue(), true, consumer);

                            while (true) {
                                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                                String message = new String(delivery.getBody());
                                Message m = Message.obtain();
                                m.obj = message;
                                mHandler.sendMessage(m);
                            }
                        } catch (InterruptedException e) {
                            break;
                        } catch (Exception e1) {
                            e1.printStackTrace();
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
            notificationThread.start();
        }
    }
}

