package com.example.vai9105.finalplayer;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.R.attr.start;

/**
 * Created by vai9105 on 12/6/16.
 */
public class InfoFragment extends Fragment {


    public static String DEBUG_TAG = "Info Fragment";
    public final static String ARG_POSITION = "position";

    private int currentPosition = -1;
    private MediaPlayer mp = null;
    boolean isPaused = false;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private ScrollView mScrollView;
    private Button startButton;
    private Button stopButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.info_layout, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();

        if (args != null) {
            updateTitleView(args.getInt(ARG_POSITION));
            //startMediaPlayer(args.getInt(ARG_POSITION));
        } else if (currentPosition != -1) {
            updateTitleView(currentPosition);
            //startMediaPlayer(currentPosition);
        }

    }


    public void updateTitleView(int position) {

        mScrollView = (ScrollView) getActivity().findViewById(R.id.scroll);
        startButton = (Button) getActivity().findViewById(R.id.button_start);
        stopButton = (Button) getActivity().findViewById(R.id.button_stop);

        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        } else {
            timerHandler = new Handler();
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    mScrollView.smoothScrollBy(0, 1);         // 5 is how many pixels you want it to scroll vertically by
                    timerHandler.postDelayed(this, 50);     // 10 is how many milliseconds you want this thread to run
                }
            };


            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timerHandler.postDelayed(timerRunnable, 0);

                    if (mp != null && mp.isPlaying()) {
                        isPaused = true;
                        stopMediaPlayer();
                        timerHandler.removeCallbacks(timerRunnable);
                        startButton.setText(R.string.play);

                    } else {
                        if (mp == null || !isPaused)
                            startMediaPlayer(currentPosition);

                        else mp.start();
                        isPaused = false;
                        startButton.setText(R.string.pause);
                        stopButton.setText(R.string.stop);
                        //start.setDrawa
                    }

                }
            });

            stopButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    stopMediaPlayer();
                    isPaused = false;
                    timerHandler.removeCallbacks(timerRunnable);
                    stopButton.setText(R.string.stopped);
                    startButton.setText(R.string.play);
                    mScrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            });
        }
        startButton.setText(R.string.play);
        stopButton.setText(R.string.stop);
        mScrollView.fullScroll(ScrollView.FOCUS_UP);

        ImageView album = (ImageView)getActivity().findViewById(R.id.album);
        album.setImageResource(SongDB.songAlbums[position]);

        TextView notes = (TextView)getActivity().findViewById(R.id.info);
        notes.setText(SongDB.songInfo[position]);
        currentPosition = position;

    }


    public void startMediaPlayer(int position) {


        try {

            if (mp == null) {
                mp = new MediaPlayer();
            }


            mp.reset();

            AssetFileDescriptor afd = getResources().
                    openRawResourceFd(SongDB.resourceID[position]);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();


        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error on player!");
        }


    }

    public void stopMediaPlayer(){
        if (mp != null && mp.isPlaying()){
            mp.pause();
        }
    }


    @Override

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_POSITION, currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause(){

        stopMediaPlayer();
        super.onPause();

    }


    @Override
    public void onResume(){
        super.onResume();
    }
}
