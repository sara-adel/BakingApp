package com.sara.bakingapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class StepDetailsActivity extends AppCompatActivity {

    ImageView back;
    TextView title;
    TextView stepVideo, stepDescription;
    ImageView stepThumbnail;
    SimpleExoPlayerView videoPlayer;

    private SimpleExoPlayer player;
    private String description, video, thumbnail;
    boolean isPlayWhenReady;
    long position;
    private BandwidthMeter bandwidthMeter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        initViews();

        position = C.TIME_UNSET;
        if (savedInstanceState != null) {
            description = savedInstanceState.getString(Constants.DESCRIPTION);
            video = savedInstanceState.getString(Constants.VIDEO);
            position = savedInstanceState.getLong(Constants.VIDEO_STATE);
            thumbnail = savedInstanceState.getString(Constants.THUMBNAIL);
            isPlayWhenReady = savedInstanceState.getBoolean("playState");
        } else {
            Intent intent = this.getIntent();
            if (intent != null) {
                description = intent.getStringExtra(Constants.DESCRIPTION);
                video = intent.getStringExtra(Constants.VIDEO);
                thumbnail = intent.getStringExtra(Constants.THUMBNAIL);
            }
        }

        setupViews();
    }

    public void initViews() {
        title = findViewById(R.id.main_toolbar_title);
        title.setText("Step Detail");
        back = findViewById(R.id.backPress);

        stepVideo = findViewById(R.id.activity_step_video);
        stepDescription = findViewById(R.id.activity_step_description);
        stepThumbnail = findViewById(R.id.thumbnail_imageView);
        videoPlayer = findViewById(R.id.activity_step_video_player);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        handler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
    }

    public void setupViews() {

        if (!(thumbnail == null || TextUtils.isEmpty(thumbnail))) {
            imageLoader(thumbnail);
        }
        if (description != null) {
            stepDescription.setText(description);
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
        }
    }

    private void setupMediaPlayer(Uri uri) {
        if (player == null) {
            TrackSelection.Factory selector = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(handler, selector);
            LoadControl control = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, control);
            videoPlayer.setPlayer(player);

            String user = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(this, user),
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
            Log.e("stop", "" + position);
            player.stop();
            player.release();
            player = null;
        }
    }

    private void imageLoader(String url) {
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(stepThumbnail);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setupMediaPlayer(Uri.parse(video));
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (!(video == null || TextUtils.isEmpty(video))) {
            setupMediaPlayer(Uri.parse(video));
        }
        if ((Util.SDK_INT <= 23 || player == null)) {
            setupMediaPlayer(Uri.parse(video));

        } else {
            player.seekTo(position);

            player.setPlayWhenReady(isPlayWhenReady);
        }

    }

    @Override
    protected void onPause() {
        position = player.getCurrentPosition();
        isPlayWhenReady = player.getPlayWhenReady();
        super.onPause();
        if (Util.SDK_INT <= 23) {
            stopPlayer();
        }
    }

    @Override
    protected void onStop() {
        position = player.getCurrentPosition();
        isPlayWhenReady = player.getPlayWhenReady();
        super.onStop();
        if (Util.SDK_INT > 23) {
            stopPlayer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.DESCRIPTION, description);
        outState.putString(Constants.VIDEO, video);
        outState.putString(Constants.THUMBNAIL, thumbnail);
        outState.putLong(Constants.VIDEO_STATE, position);
        outState.putBoolean("playState", isPlayWhenReady);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        description = savedInstanceState.getString(Constants.DESCRIPTION);
        video = savedInstanceState.getString(Constants.VIDEO);
        position = savedInstanceState.getLong(Constants.VIDEO_STATE);
        thumbnail = savedInstanceState.getString(Constants.THUMBNAIL);
        isPlayWhenReady = savedInstanceState.getBoolean("playState");
    }


}