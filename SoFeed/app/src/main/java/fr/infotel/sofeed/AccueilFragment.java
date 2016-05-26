package fr.infotel.sofeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.infotel.sofeed.R;
import fr.infotel.sofeed.bean.Event;
import fr.infotel.sofeed.restclient.EventService;

/**
 * Created by hp1 on 21-01-2015.
 */
public class AccueilFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 1;
    private static final int DATASET_COUNT = 60;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER
    }

    protected LayoutManagerType layoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
    protected RecyclerView mRecyclerViewToday;
    protected RecyclerView mRecyclerViewWeek;
    protected RecyclerView mRecyclerViewMonth;
    protected EventAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<String> mDatasetToday;
    protected List<String> mDatasetWeek;
    protected List<String> mDatasetMonth;
    protected String[] mToday;
    protected String[] mWeek;
    protected String[] mMonth;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.accueil,container,false);
        v.setTag(TAG);
        try {
            initDataset();
        } catch (ParseException e) {
            Log.d("Exception", "parse error exception");
        }
        mRecyclerViewToday = (RecyclerView)  v.findViewById(R.id.eventToday_recycler_view);
        mRecyclerViewWeek = (RecyclerView)  v.findViewById(R.id.eventWeek_recycler_view);
        mRecyclerViewMonth = (RecyclerView)  v.findViewById(R.id.eventMonth_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        setRecyclerViewLayoutManager(mRecyclerViewToday,layoutManagerType);
        setRecyclerViewLayoutManager(mRecyclerViewWeek,layoutManagerType);
        setRecyclerViewLayoutManager(mRecyclerViewMonth,layoutManagerType);


        // Set CustomAdapter as the adapter for RecyclerView.
        mAdapter = new EventAdapter(mToday);
        mRecyclerViewToday.setAdapter(mAdapter);
        mAdapter = new EventAdapter(mWeek);
        mRecyclerViewWeek.setAdapter(mAdapter);
        mAdapter = new EventAdapter(mMonth);
        mRecyclerViewMonth.setAdapter(mAdapter);
        return v;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(RecyclerView mRecyclerView, LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                layoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                layoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() throws ParseException{
        // Stub dataset, will need to connect to rest
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        List<Event> events = new ArrayList<Event>();
        Event event1 = new Event();
        event1.setName("Désigner une application smartphone");
        event1.setStartDate(formatter.parse("27-mai-2016"));
        Event event2 = new Event();
        event2.setName("Soirée d'agence Mougins/Monaco");
        event2.setStartDate(formatter.parse("09-juin-2016"));
        Event event3 = new Event();
        event3.setName("Covoiturage Antibes-Mougins");
        event3.setStartDate(formatter.parse("26-mai-2016"));
        Event event = new Event();
        event.setName("Réunion Projet SoFeed à 18h");
        event.setStartDate(formatter.parse("26-mai-2016"));
        events.add(event);
        events.add(event1);
        events.add(event2);
        events.add(event3);
        // Handle the list of event and put them in three tabs
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        Calendar calEvent = Calendar.getInstance();
        events = EventService.getEvents();
        mDatasetToday = new ArrayList<String>();
        mDatasetWeek = new ArrayList<String>();
        mDatasetMonth = new ArrayList<String>();
        for (Event e : events){
          calEvent.setTime(e.getStartDate());
            if (cal.get(Calendar.DAY_OF_YEAR)==calEvent.get(Calendar.DAY_OF_YEAR)){
                mDatasetToday.add(formatter.format(e.getStartDate())+": "+e.getName());
            }
            else if(cal.get(Calendar.WEEK_OF_YEAR)==calEvent.get(Calendar.WEEK_OF_YEAR)){
                mDatasetWeek.add(formatter.format(e.getStartDate())+": "+e.getName());
            }
            else if(cal.get(Calendar.MONTH)+1==calEvent.get(Calendar.MONTH)){
                mDatasetMonth.add(formatter.format(e.getStartDate())+": "+e.getName());
            }
        }
        mToday = new String[mDatasetToday.size()];
        mWeek = new String[mDatasetWeek.size()];
        mMonth = new String[mDatasetMonth.size()];
        for (int i=0;i<mDatasetToday.size();i++){
            mToday[i] = mDatasetToday.get(i);
        }
        for (int i=0;i<mDatasetWeek.size();i++){
            mWeek[i] = mDatasetWeek.get(i);
        }
        for (int i=0;i<mDatasetMonth.size();i++){
            mMonth[i] = mDatasetMonth.get(i);
        }
    }
}