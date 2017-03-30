package com.example.vai9105.finalplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;


public class MainActivity extends FragmentActivity
implements SongTitleFragment.OnTitleSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        if(findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }


            //Create an instance of SongTitleFragment and throw that in the container
            SongTitleFragment titleFragment = new SongTitleFragment();

            titleFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, titleFragment).commit();
        }
    }

    @Override
    public void onTitleSelected(int position){
        InfoFragment songInfo =  (InfoFragment) getSupportFragmentManager().findFragmentById(R.id.info_fragment);

        if(songInfo != null){
            //Call a method in the song Info Fragment to update its content
            songInfo.updateTitleView(position);

        }else{

            //if fragment is not in the one-page layout we have to swap

            //the fragments when title gets selected

            InfoFragment fragment = new InfoFragment();
            Bundle args= new Bundle();
            args.putInt(InfoFragment.ARG_POSITION, position);
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }

        public boolean onCreateOptionsMenu(Menu menu){
        // Called when menu creation happens
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }




}


