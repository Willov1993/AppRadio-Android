package com.innovasystem.appradio.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

public class RadioStreamService extends Service implements AudioManager.OnAudioFocusChangeListener {
    public static final String BROADCAST_TO_SERVICE = "com.mediaplayer.playerfunction";
    public static final String SERVICE_TO_ACTIVITY = "com.mediaplayer.currentPlayerStatus";
    public static final String PLAYER_FUNCTION_TYPE = "playerfunction";
    public static final String PLAYER_TRACK_URL = "trackURL";
    public static final int PLAY_MEDIA_PLAYER = 1;
    public static final int PAUSE_MEDIA_PLAYER = 2;
    public static final int RESUME_MEDIA_PLAYER = 3;
    public static final int STOP_MEDIA_PLAYER = 4;
    public static final int CHANGE_PLAYER_TRACK = 5;
    public static final String PLAYER_STATUS_KEY = "PlayerCurrentStatus";

    public String radioURL="";

    private AudioManager audioManager;

    //Global Variables for handling calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }
        IntentFilter intentFilter = new IntentFilter(BROADCAST_TO_SERVICE);
        registerReceiver(playerReceiver, intentFilter);
        if (mPlayer != null && mPlayer.isPlaying()) {
            sendPlayerStatus("playing");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            stopPlayer();
            mPlayer.release();
        }
        removeAudioFocus();
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

    }


    private BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BROADCAST_TO_SERVICE.equalsIgnoreCase(action)) {
                String trackURL = intent.hasExtra(PLAYER_TRACK_URL) ? intent.getStringExtra(PLAYER_TRACK_URL) : "";
                radioURL= trackURL;
                Log.e("URL",trackURL);
                int function = intent.getIntExtra(PLAYER_FUNCTION_TYPE, 0);
                switch (function) {
                    case CHANGE_PLAYER_TRACK:
                        changeTrack(trackURL);
                        break;
                    case STOP_MEDIA_PLAYER:
                        stopPlayer();
                        break;
                    case PLAY_MEDIA_PLAYER:
                        startMediaPlayer(trackURL);
                        break;
                    case PAUSE_MEDIA_PLAYER:
                        pausePlayer();
                        break;
                    case RESUME_MEDIA_PLAYER:
                        resumePlayer();
                        break;
                }

            }
        }
    };

    private MediaPlayer mPlayer;

    private void pausePlayer() {

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            sendPlayerStatus("pause");
        }
    }

    private void resumePlayer() {

        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            sendPlayerStatus("playing");
        }
    }

    private void changeTrack(String url) {

        stopPlayer();
        startMediaPlayer(url);

    }

    private void stopPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            sendPlayerStatus("stopped");

        }
    }

    public void startMediaPlayer(String url) {

        if (TextUtils.isEmpty(url))
            return;
        if (mPlayer == null)
            mPlayer = new MediaPlayer();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(url);
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (extra == MediaPlayer.MEDIA_ERROR_SERVER_DIED
                            || extra == MediaPlayer.MEDIA_ERROR_MALFORMED) {
                        sendPlayerStatus("erroronplaying");
                    } else if (extra == MediaPlayer.MEDIA_ERROR_IO) {
                        sendPlayerStatus("erroronplaying");
                        return false;
                    }
                    return false;
                }
            });

            mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.e("onBufferingUpdate", "" + percent);

                }
            });

            mPlayer.prepareAsync();

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    System.out.println("PLAYING RADIO!!!");
                    mPlayer.start();
                    sendPlayerStatus("playing");
                }
            });

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("onCompletion", "Yes");
                    sendPlayerStatus("completed");
                }
            });

            mPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });



        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPlayerStatus(String status) {
        Intent intent = new Intent();
        intent.setAction(SERVICE_TO_ACTIVITY);
        intent.putExtra(PLAYER_STATUS_KEY, status);
        sendBroadcast(intent);
    }

    //METHODS FOR AUDIOFOCUS
    @Override
    public void onAudioFocusChange(int focusState) {
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mPlayer == null)
                    startMediaPlayer(radioURL);
                else if (!mPlayer.isPlaying())
                    mPlayer.start();
                mPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mPlayer.isPlaying()) mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mPlayer.isPlaying()) mPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mPlayer.isPlaying()) mPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    //METHODS FOR HANDLING CALLS
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mPlayer != null) {
                            pausePlayer();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumePlayer();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

}
