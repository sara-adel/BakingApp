package com.sara.bakingapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sara.bakingapp.Constants;
import com.sara.bakingapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by sara on 4/14/2018.
 */

public class StepDetailsFragment extends Fragment {

    View view;

    SimpleExoPlayerView videoPlayer;
    TextView stepVideo, stepDescription;
    ImageView stepThumbnail;

    Long position;
    private SimpleExoPlayer player;
    private String description, video, thumbnail;
    boolean isPlayWhenReady;
    private BandwidthMeter bandwidthMeter;
    private Handler handler;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_steps_detail, container, false);
        initViews();

        position = C.TIME_UNSET;
        if (savedInstanceState != null) {
            description = savedInstanceState.getString(Constants.DESCRIPTION);
            video = savedInstanceState.getString(Constants.VIDEO);
            position = savedInstanceState.getLong(Constants.VIDEO_STATE);
            thumbnail = savedInstanceState.getString(Constants.THUMBNAIL);
            isPlayWhenReady = savedInstanceState.getBoolean("playState");
        }

        setupViews();
        return view;
    }

    public void initViews() {
        videoPlayer = view.findViewById(R.id.playerView);
        stepVideo = view.findViewById(R.id.step_video);
        stepDescription = view.findViewById(R.id.step_description);
        stepThumbnail = view.findViewById(R.id.step_thumbnail);

        handler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
    }

    public void setupViews() {
        if (description != null) {
            stepDescription.setText(description);
        } else {
            stepDescription.setText("No Description Added");
        }

        if (video != null) {
            if (TextUtils.isEmpty(video) || video.isEmpty()) {
                stepVideo.setText("No Video");
                stepVideo.setVisibility(View.VISIBLE);
                videoPlayer.setVisibility(View.GONE);
            } else {
                videoPlayer.setVisibility(View.VISIBLE);
                stepVideo.setVisibility(View.GONE);
                setupMediaPlayer(Uri.parse(video));
            }
        } else {
            stepVideo.setText("No Video");
            stepVideo.setVisibility(View.VISIBLE);
            videoPlayer.setVisibility(View.GONE);
        }

        if (!(thumbnail == null || TextUtils.isEmpty(thumbnail))) {
            imageLoader(thumbnail);
        }
    }

    private void setupMediaPlayer(Uri uri) {
        if (player == null) {
            TrackSelection.Factory selector = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(handler, selector);
            LoadControl control = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, control);
            videoPlayer.setPlayer(player);

            String user = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getActivity(), user),
                    new DefaultExtractorsFactory(), null, null);
            if (position != C.TIME_UNSET) {
                player.seekTo(position);
            }

            player.prepare(mediaSource);
            isPlayWhenReady = player.getPlayWhenReady();
            player.setPlayWhenReady(isPlayWhenReady);
        }
    }

    private void stopPlayer() {
        if (player != null) {
            position = player.getCurrentPosition();
            player.stop();
            player.release();
        }
    }

    private void imageLoader(String url) {
        Picasso.with(getActivity())
                .load(url)
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(stepThumbnail);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.VIDEO, video);
        outState.putString(Constants.DESCRIPTION, description);
        outState.putString(Constants.THUMBNAIL, thumbnail);
        outState.putLong(Constants.VIDEO_STATE, position);
        outState.putBoolean("playState", isPlayWhenReady);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            savedInstanceState.getLong(Constants.VIDEO_STATE);
            savedInstanceState.getString(Constants.VIDEO);
            savedInstanceState.getString(Constants.THUMBNAIL);
            savedInstanceState.getString(Constants.DESCRIPTION);
            savedInstanceState.getBoolean("playState");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}