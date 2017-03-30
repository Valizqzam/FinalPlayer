package com.example.vai9105.finalplayer;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by vai9105 on 12/6/16.
 */
public class SongTitleFragment extends ListFragment {

    OnTitleSelectedListener selectedTitle;

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1
                : android.R.layout.simple_list_item_1;

        //create array adapter fot the list view
        setListAdapter((new ArrayAdapter<String>(getActivity(),
                layout, SongDB.songTitles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getResources().getColor(R.color.brightWhite));
                return textView;
            }
        }));


    }

    @Override
    public void onStart(){
        super.onStart();
        //When in two-pane layout, set the list view to highlight the selected
        //List item. onStart is the place where the listView is inflated and available
        if(getFragmentManager().findFragmentById(R.id.info_fragment) !=null){
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            selectedTitle = (OnTitleSelectedListener) context;
        } catch ( ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        selectedTitle.onTitleSelected(position);
        getListView().setItemChecked(position, true);
        InfoFragment fragment = (InfoFragment) getFragmentManager()
                .findFragmentById(R.id.info_fragment);
        //if(fragment != null)
        //    fragment.startMediaPlayer(position);
        if (fragment != null) {
            fragment.stopMediaPlayer();
            fragment.updateTitleView(position);
        }
        super.onListItemClick(l,v,position,id);

    }




}
