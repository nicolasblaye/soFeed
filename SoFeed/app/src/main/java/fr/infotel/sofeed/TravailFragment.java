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
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import fr.infotel.sofeed.bean.Ticket;
import fr.infotel.sofeed.restclient.TicketService;

/**
 * Created by nicolas on 22/05/16.
 */
public class TravailFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.travail,container,false);
        TextView tv1 = (TextView) v.findViewById(R.id.projet_actuel);
        tv1.setText("Projet: SoFeed");
        TextView tv2 = (TextView) v.findViewById(R.id.description_projet_actuel);
        tv2.setText("Design d'une application android");
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
        List<Ticket> ticks = TicketService.getTickets();
        String[] tick = new String[ticks.size()];
        for(int i=0;i<ticks.size();i++){
            tick[i] = ticks.get(i).getDescription();
        }
        listViewTicket.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.travail_list_item, tick));
        return v;
    }
}
