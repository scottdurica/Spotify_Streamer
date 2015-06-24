package emroxriprap.com.spotifystreamer.fragments;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import emroxriprap.com.spotifystreamer.R;
import emroxriprap.com.spotifystreamer.TopTenActivity;
import emroxriprap.com.spotifystreamer.models.TopTenTrack;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

//    private final String LOG_TAG ="PlayerFragment";
    private String url;
    public static MediaPlayer mMediaPlayer;
    private ArrayList<TopTenTrack>trackList;
    private TopTenTrack currentTrack;
    private ImageButton playPause;
    private static SeekBar seekBar;
    private static TextView seekBarStartTime,seekBarFinishTime;
    ImageButton previous, next;
    int chosenTrackPos;
    ImageView cover;
    TextView songName, artistName, albumName;
    public static Handler mHandler;
    int seekBarMaxVal;
    private final String CURRENT_TRACK = "current_track";
    private final String PLAYER_STATE = "player_state";
    private final String SEEKBAR_MAX_VALUE = "seekbar_max_value";

    enum PlayerState{
        NOT_PLAYING_NOT_PAUSED,
        PAUSED,
        PLAYING
    }
    private PlayerState playerState = PlayerState.NOT_PLAYING_NOT_PAUSED;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        trackList = args.getParcelableArrayList(TopTenActivity.DATA_LIST);
        chosenTrackPos = args.getInt(TopTenActivity.SELECTED_POSITION);
        if (savedInstanceState != null){
            currentTrack = savedInstanceState.getParcelable(CURRENT_TRACK);
            playerState = (PlayerState)savedInstanceState.getSerializable(PLAYER_STATE);
            seekBarMaxVal = (int)savedInstanceState.getSerializable(SEEKBAR_MAX_VALUE);
        }else {
            currentTrack = trackList.get(chosenTrackPos);
        }

        url = currentTrack.getPreviewUrl();

        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (playerState == PlayerState.NOT_PLAYING_NOT_PAUSED){
            //fragment just loaded.  start playing
            startPlayer(url);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonsForPlayerState();
        if (mMediaPlayer.isPlaying()){
            seekBar.setMax(mMediaPlayer.getDuration());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CURRENT_TRACK,currentTrack);
        outState.putSerializable(PLAYER_STATE,playerState);
        outState.putSerializable(SEEKBAR_MAX_VALUE,seekBarMaxVal);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        cover = (ImageView)rootView.findViewById(R.id.iv_player_album_cover);
        songName = (TextView)rootView.findViewById(R.id.tv_player_song_name);
        artistName = (TextView)rootView.findViewById(R.id.tv_player_artist_name);
        albumName = (TextView)rootView.findViewById(R.id.tv_player_album_name);

        songName.setText(currentTrack.getSongName());
        artistName.setText(currentTrack.getArtistName());
        albumName.setText(currentTrack.getAlbumName());
        Picasso.with(getActivity()).load(currentTrack.getLgImgUrl()).into(cover);

        playPause = (ImageButton)rootView.findViewById(R.id.ib_player_play_pause);
        previous = (ImageButton)rootView.findViewById(R.id.ib_player_previous);
        next = (ImageButton)rootView.findViewById(R.id.ib_player_next);
        playPause.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);

        setupSeekBar(rootView);
        seekBar.setMax(seekBarMaxVal);
        return rootView;


    }

    private void setupSeekBar(View rootView) {

            seekBar = (SeekBar) rootView.findViewById(R.id.sb_player_grabber_bar);
            seekBarStartTime = (TextView) rootView.findViewById(R.id.tv_player_0);
            seekBarFinishTime = (TextView) rootView.findViewById(R.id.tv_player_30);

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {

        TopTenTrack newTrack = null;
        switch (v.getId()){
            case R.id.ib_player_play_pause:

                if (playerState == PlayerState.PAUSED){
                    mMediaPlayer.start();
                    playerState = PlayerState.PLAYING;
                }else{
                    mMediaPlayer.pause();
                    playerState = PlayerState.PAUSED;
                }
                setButtonsForPlayerState();
                break;
            case R.id.ib_player_next:
                chosenTrackPos = chosenTrackPos + 1;
                if (chosenTrackPos == trackList.size()){
                    chosenTrackPos = 0;
                }
                newTrack = trackList.get(chosenTrackPos);
                mMediaPlayer.reset();
                url = newTrack.getPreviewUrl();
                startPlayer(url);
                songName.setText(newTrack.getSongName());
                artistName.setText(newTrack.getArtistName());
                albumName.setText(newTrack.getAlbumName());
                Picasso.with(getActivity()).load(newTrack.getLgImgUrl()).into(cover);
                break;
            case R.id.ib_player_previous:
                chosenTrackPos = chosenTrackPos - 1;
                if (chosenTrackPos <0){
                    chosenTrackPos = trackList.size()-1;
                }
                newTrack = trackList.get(chosenTrackPos);
                mMediaPlayer.reset();
                url = newTrack.getPreviewUrl();
                startPlayer(url);
                songName.setText(newTrack.getSongName());
                artistName.setText(newTrack.getArtistName());
                albumName.setText(newTrack.getAlbumName());
                Picasso.with(getActivity()).load(newTrack.getLgImgUrl()).into(cover);
                break;

        }

        if (newTrack != null){
            currentTrack = newTrack;

        }
    }
    private void startPlayer(String url){

        try {
             mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                playerState = PlayerState.PLAYING;
                setButtonsForPlayerState();
                setSeekBarValues();
                mHandler = new Handler();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mMediaPlayer != null ) {
                            int currentPos = mMediaPlayer.getCurrentPosition();
                            seekBar.setProgress(currentPos);
                            if (currentPos<10000){
                                seekBarStartTime.setText("0:0" + currentPos / 1000);
                            }else{
                                seekBarStartTime.setText("0:" + currentPos / 1000);
                            }
                        }
                            mHandler.postDelayed(this, 50);
                    }
                }

                    );

                }

        });
    }

    private void setSeekBarValues() {
        seekBarMaxVal = mMediaPlayer.getDuration();
        seekBar.setMax(seekBarMaxVal);
        seekBarFinishTime.setText("0:" + seekBar.getMax()/1000);
        seekBar.setProgress(0);
    }

    private void setButtonsForPlayerState(){

        if (playerState == PlayerState.PLAYING ){
            playPause.setImageResource(R.drawable.ic_pause_black_48dp);
        }else{
            playPause.setImageResource(R.drawable.ic_play_arrow_black_48dp);
        }
    }
}
