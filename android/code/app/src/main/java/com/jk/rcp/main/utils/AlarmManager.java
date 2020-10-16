package com.jk.rcp.main.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

public class AlarmManager implements MediaPlayer.OnPreparedListener {
    //    private static AlarmManager singleInstance = null;
    private final MediaPlayer mPlayer;
    private int mVolumeLevel = -1;

    public AlarmManager(MediaPlayer.OnCompletionListener onCompletionListener) {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(onCompletionListener);
    }

//    public static AlarmManager getInstance() {
//        if (singleInstance == null)
//            singleInstance = new AlarmManager();
//
//        return singleInstance;
//    }

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
                mVolumeLevel = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, 3, 0);
            }

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.setLooping(repeat);
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
