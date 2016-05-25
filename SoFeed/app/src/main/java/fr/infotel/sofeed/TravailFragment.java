package fr.infotel.sofeed;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by nicolas on 22/05/16.
 */
public class TravailFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.travail,container,false);
        ListView listView = (ListView) v.findViewById(R.id.members);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.drawer_list_item, getResources().getStringArray(R.array.members)));
        return v;
    }
}
