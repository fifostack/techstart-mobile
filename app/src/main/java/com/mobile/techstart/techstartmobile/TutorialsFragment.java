package com.mobile.techstart.techstartmobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Lucas on 2/2/2018.
 */

public class TutorialsFragment extends Fragment implements YouTubePlayer.OnInitializedListener {

    View myView;
    private static String video_id = "dIe9Zty-sw8";
    private YouTubePlayer ytPlayer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.tutorials_layout, container, false);

        YouTubePlayerFragment youtubePlayerFragment = new YouTubePlayerFragment();
        youtubePlayerFragment.initialize(Config.getKey(), this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.youtube_fragment, youtubePlayerFragment);
        fragmentTransaction.commit();

        return myView;
    }


    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.loadVideo(video_id);
        youTubePlayer.play();

    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        String errorMessage = youTubeInitializationResult.toString();
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        Log.d("errorMessage:", errorMessage);

    }
}
