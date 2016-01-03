package com.soundframeworkinterview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UITestActivity extends AppCompatActivity {
    static final int SONG_LENGTH_SECONDS = 10;
    static final int UPDATE_RATE = 4; // update progress every quarter of a second
    static final int MILLIS_IN_SECOND = 1000;
    ProgressBar mSongProgress;
    int mProgressMax;
    Handler mHandler;
    boolean mIsSongPlaying = false;
    Thread mSongPlayingThread;

    View.OnClickListener mButtonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = null;
            switch (v.getId()) {
                case R.id.action_menu:
                    text = getString(R.string.action_menu);
                    break;
                case R.id.action_favorite:
                    text = getString(R.string.action_favorite);
                    break;
                case R.id.action_headphones:
                    text = getString(R.string.action_headphones);
                    break;
                case R.id.action_refresh:
                    text = getString(R.string.action_refresh);
                    break;
                case R.id.action_location:
                    text = getString(R.string.action_location);
                    break;
                default:
                    break;
            }
            if (text != null) {
                Toast.makeText(UITestActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uitest);
        FloatingActionButton targetButton = (FloatingActionButton) findViewById(R.id.target_button);
        targetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UITestActivity.this, R.string.target_button, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        findViewById(R.id.action_menu).setOnClickListener(mButtonsListener);
        findViewById(R.id.action_favorite).setOnClickListener(mButtonsListener);
        findViewById(R.id.action_headphones).setOnClickListener(mButtonsListener);
        findViewById(R.id.action_refresh).setOnClickListener(mButtonsListener);
        findViewById(R.id.action_location).setOnClickListener(mButtonsListener);

        mSongProgress = (ProgressBar) findViewById(R.id.song_progress);
        mProgressMax = SONG_LENGTH_SECONDS * UPDATE_RATE;
        mSongProgress.setMax(mProgressMax);
        mHandler = new Handler();

        final ImageView playPauseButton = (ImageView) findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsSongPlaying = !mIsSongPlaying;
                Drawable playbackDrawable = getResources().getDrawable(
                        mIsSongPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
                playPauseButton.setImageDrawable(playbackDrawable);
                if (mIsSongPlaying) {
                    playSong();
                } else {
                    pauseSong();
                }
            }
        });
    }

    private void playSong() {
        mSongPlayingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int quarterSeconds = mSongProgress.getProgress();
                while (true) {
                    try {
                        if (quarterSeconds >= mProgressMax) {
                            quarterSeconds = 0;
                        }
                        final int finalQuarterSeconds = quarterSeconds;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mSongProgress.setProgress(finalQuarterSeconds);
                            }
                        });
                        Thread.sleep(MILLIS_IN_SECOND / UPDATE_RATE);
                        quarterSeconds++;
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        mSongPlayingThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseSong();
    }

    private void pauseSong() {
        if (mSongPlayingThread != null && !mSongPlayingThread.isInterrupted()) {
            mSongPlayingThread.interrupt();
        }
    }
}
