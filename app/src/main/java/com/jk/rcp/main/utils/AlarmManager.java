package com.jk.rcp.main.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

public class AlarmManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static AlarmManager singleInstance = null;
    private final MediaPlayer mPlayer;
    private int mVolumeLevel = -1;

    public AlarmManager() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
    }

    public static AlarmManager getInstance() {
        if (singleInstance == null)
            singleInstance = new AlarmManager();

        return singleInstance;
    }

    public void stopSound() {
        mPlayer.stop();
        mPlayer.reset();
    }

    public void startSound(Context context, String soundName, Boolean repeat, Boolean interruptPreviousSound) {
        AssetFileDescriptor afd = null;
        if (mPlayer.isPlaying() && !interruptPreviousSound) {
            return;
        }

        try {
            afd = context.getAssets().openFd(soundName);

            AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            if (am != null) {
                mVolumeLevel = am.getStreamVolume(AudioManager.STREAM_ALARM);
                am.setStreamVolume(AudioManager.STREAM_ALARM, am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
            }

            mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.setLooping(repeat);
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
