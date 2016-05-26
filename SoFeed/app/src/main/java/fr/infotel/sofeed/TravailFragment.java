package fr.infotel.sofeed;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.Calendar;

import fr.infotel.sofeed.bean.Ticket;

/**
 * Created by nicolas on 22/05/16.
 */
public class TravailFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.travail,container,false);
        ListView listView = (ListView) v.findViewById(R.id.members);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.travail_list_item, getResources().getStringArray(R.array.members)));
        ListView listViewTicket = (ListView) v.findViewById(R.id.tickets);
        // Il faut prendre les tickets sur Rest, stub ici
        Ticket ticket = new Ticket();
        ticket.setName("Développer le Service REST");
        Ticket ticket2 = new Ticket();
        ticket2.setName("Set up le projet GitHub");
        String[]tickets = new String[2];
        tickets[0] = ticket.getName();
        tickets[1] = ticket2.getName();
        listViewTicket.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.travail_list_item, tickets));
        return v;
    }
}
