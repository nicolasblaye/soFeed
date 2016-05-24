package fr.infotel.sofeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by nicolas on 22/05/16.
 */
public class LoisirFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.loisir,container,false);
        Button piscine = (Button)v.findViewById(R.id.button);
        FloatingActionButton fab = (FloatingActionButton)v.findViewById(R.id.button7);
        fab.setOnClickListener(this);
        piscine.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button7:
                //create new activity type
                break;
            case R.id.button:
                Intent piscine = new Intent(getActivity(),PiscineActivity.class);
                getActivity().startActivity(piscine);
                break;
        }
    }
}
