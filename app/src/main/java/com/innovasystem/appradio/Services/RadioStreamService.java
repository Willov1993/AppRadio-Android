package com.innovasystem.appradio.Services;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Utils.PlaybackStatus;
import com.innovasystem.appradio.Utils.Utils;

import java.io.IOException;


/* Servicio que implementa la reproduccion de Streaming de Radio por internet */
public class RadioStreamService extends Service implements AudioManager.OnAudioFocusChangeListener {

    /* CONSTANTES DE PROTOCOLO PARA ENVIO Y RECEPCION DE MENSAJES */
    public static final String BROADCAST_TO_SERVICE = "com.mediaplayer.playerfunction";
    public static final String SERVICE_TO_ACTIVITY = "com.mediaplayer.currentPlayerStatus";
    public static final String PLAYER_FUNCTION_TYPE = "playerfunction";
    public static final String PLAYER_TRACK_URL = "trackURL";
    public static final int PLAY_MEDIA_PLAYER = 1;
    public static final int PAUSE_MEDIA_PLAYER = 2;
    public static final int RESUME_MEDIA_PLAYER = 3;
    public static final int STOP_MEDIA_PLAYER = 4;
    public static final int CHANGE_PLAYER_TRACK = 5;
    public static final int MUTE_MEDIA_PLAYER= 6;
    public static final String PLAYER_STATUS_KEY = "PlayerCurrentStatus";


    /* Variables de la clase */
    private MediaPlayer mPlayer;          //ESTA VARIABLE ES LA QUE MANEJA LA REPRODUCCION DEL STREAMING
    public static String radioURL="";           //esta variable almacena el URL que se recibe como mensaje
    private AudioManager audioManager;  //esta variable representa un manejador de peticiones de sonido

    /* Variables para manejar los eventos de llamadas por telefono*/
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    WifiManager.WifiLock wifiLock;

    public static final String ACTION_PLAY = "com.valdioveliu.valdio.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.valdioveliu.valdio.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.valdioveliu.valdio.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.valdioveliu.valdio.audioplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.valdioveliu.valdio.audioplayer.ACTION_STOP";

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callStateListener();
    }

    /**
     * Este metodo incializa el servicio para poder recibir mensajes, el servicio se inicia
     * desde otro componente utilizando el metodo startService().
     *
     * @param intent
     * @param flags
     * @param startId
     * @return Un entero indicando el comportamiento del servicio en cuanto a la recepcion de
     *         solicitudes.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("STARTING SERVICE!!!");
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }
        IntentFilter intentFilter = new IntentFilter(BROADCAST_TO_SERVICE);
        registerReceiver(playerReceiver, intentFilter);
        if (mPlayer != null && mPlayer.isPlaying()) {
            sendPlayerStatus("playing");
        }

        handleIncomingActions(intent);

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


    /*=====Metodos para manejar los estados del mediaPlayer ======*/

    /**
     * Este metodo pausa el mediaPlayer y envia un mensaje de pausa  con
     * el metodo {@link #sendPlayerStatus(String)}
     */
    private void pausePlayer() {

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            sendPlayerStatus("pause");
        }
    }

    /**
     * Este metodo reanuda la reproduccion del mediaPlayer solamente si no
     * esta en estado playing.
     * Envia un mensaje de playing con el metodo {@link #sendPlayerStatus(String)}
     */
    private void resumePlayer() {

        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            sendPlayerStatus("playing");
        }
    }

    /**
     * Este metodo se encarga de cambiar la reproduccion actual
     * por otra URL de streaming
     */
    private void changeTrack(String url) {

        stopPlayer();
        startMediaPlayer(url);

    }

    /**
     * Este metodo detiene el mediaPlayer y envia un mensaje de stopped  con
     * el metodo {@link #sendPlayerStatus(String)}
     */
    private void stopPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            wifiLock.release();
            sendPlayerStatus("stopped");
            removeNotification();

        }
    }

    /**
     * Este metodo inicializa el mediaPlayer con la URL del servicio de streaming. Aqui se
     * registraran los listeners necesarios para el mediaPlayer y en caso de ocurrir errores
     * estos se enviaran como mensajes con el metodo {@link #sendPlayerStatus(String)}
     *
     * @param url La url del servicio de Streaming.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startMediaPlayer(String url) {

        if (TextUtils.isEmpty(url))
            return;
        if (mPlayer == null)
            mPlayer = new MediaPlayer();
            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
            wifiLock.acquire();



        try {

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(url);
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("MediaPlayerError",String.format("what: %d extra: %d",what,extra));

                    if (extra == MediaPlayer.MEDIA_ERROR_SERVER_DIED || extra == MediaPlayer.MEDIA_ERROR_MALFORMED) {
                        sendPlayerStatus("erroronserver");
                    } else if (extra == MediaPlayer.MEDIA_ERROR_IO) {
                        sendPlayerStatus("erroronconnection");
                        return false;
                    }else{
                        sendPlayerStatus("erroronplaying");
                    }

                    removeNotification();

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
                    Utils.ocultarSnackBar();
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

            if (mediaSessionManager == null) {
                try {
                    initMediaSession();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    //stopSelf();
                }
                buildNotification(PlaybackStatus.PLAYING);
            }
            else{
                buildNotification(PlaybackStatus.PLAYING);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("ILLEGAL ARGUMENT ERROR");
            //e.printStackTrace();
        } catch (IllegalStateException e) {
            System.out.println("ILLEGAL STATE ERROR");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ILLEGAL IO ERROR");
            //e.printStackTrace();
        }
    }

    /**
     * Este metodo envia un mensaje broadCast a todos los componentes que hayan registrado
     * un listener para recibir los mensajes de este servicio.
     * @param status El mensaje o status del servicio actual.
     */
    private void sendPlayerStatus(String status) {
        Intent intent = new Intent();
        intent.setAction(SERVICE_TO_ACTIVITY);
        intent.putExtra(PLAYER_STATUS_KEY, status);
        sendBroadcast(intent);
    }


    /**
     * Este listener maneja los mensajes que el servicio recibe de otros componentes, dependiendo
     * de la accion que se recibe como mensaje, el servicio realizara acciones sobre la variable
     * del servicio {@link #mPlayer}
     *
     */
    private BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BROADCAST_TO_SERVICE.equalsIgnoreCase(action)) {
                System.out.println("LINE BEFORE TRACK URL");
                String trackURL = intent.hasExtra(PLAYER_TRACK_URL) ? intent.getStringExtra(PLAYER_TRACK_URL) : "";
                radioURL= (!trackURL.equals("")) ? trackURL : radioURL;
                Log.e("URL",trackURL == null ? "" : trackURL);
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

    /*-----METHODS FOR AUDIOFOCUS---*/
    @Override
    public void onAudioFocusChange(int focusState) {
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mPlayer == null)
                    startMediaPlayer(radioURL);
                else if (!mPlayer.isPlaying())
                    mPlayer.start();

                if(mPlayer != null)
                    mPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if(mPlayer == null)
                    return;
                else {
                    if (mPlayer.isPlaying())
                        mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if(mPlayer == null)
                    return;
                if (mPlayer.isPlaying())
                    mPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if(mPlayer == null)
                    return;
                if (mPlayer.isPlaying())
                    mPlayer.setVolume(0.1f, 0.1f);
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


    /*---METHODS FOR HANDLING CALLS---*/
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
                        if (mPlayer != null && ongoingCall) {
                                ongoingCall = false;
                                resumePlayer();
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


    /*==== METHODS FOR HANDLING NOTIFICATIONS ==== */

    /**
     * Este metodo inicializa el manejador de notificaciones con todos los controles y listeners
     * para cada evento
     * @throws RemoteException
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                resumePlayer();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();
                pausePlayer();
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                //Stop the service
                stopSelf();
            }

        });
    }


    /**
     * Este metodo actualiza los datos de la notificacion
     */
    private void updateMetaData() {
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
                        BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_dialog_email))
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "RADIO")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Segmento")
                .build());
    }

    /**
     * Este metodo cambia la emisora desde la notificacion
     */
    private void skipToNext() {
        /*
        if (audioIndex == audioList.size() - 1) {
            //if last in playlist
            audioIndex = 0;
            activeAudio = audioList.get(audioIndex);
        } else {
            //get next in playlist
            activeAudio = audioList.get(++audioIndex);
        }

        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);

        stopMedia();
        //reset mediaPlayer
        mediaPlayer.reset();
        initMediaPlayer();
        */
    }

    /**
     * Este metodo cambia a la emisora anterior desde la notificacion
     */
    private void skipToPrevious() {
        /*
        if (audioIndex == 0) {
            //if first in playlist
            //set index to the last of audioList
            audioIndex = audioList.size() - 1;
            activeAudio = audioList.get(audioIndex);
        } else {
            //get previous in playlist
            activeAudio = audioList.get(--audioIndex);
        }

        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);

        stopMedia();
        //reset mediaPlayer
        mediaPlayer.reset();
        initMediaPlayer();
        */
    }


    /**
     * Este metodo construye la notificacion en base al estado en que se encuentra el reproductor
     *
     * @param playbackStatus
     */

    private void buildNotification(PlaybackStatus playbackStatus) {

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.vecina); //replace with your own image

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(getResources().getColor(R.color.appColorTheme))
                // Set the large and small icons
                .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                // Set Notification content information
                .setContentText("Canela TV")
                .setContentTitle("El Show de la Vecina")
                // Add playback actions
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    /**
     * Este metodo elimina la notificacion de la barra de notificaciones
     */
    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }


    /**
     * Este metodo maneja los eventos que se envian desde los botones de la notificacion,
     * de manera que invoca a los listeners registrados en el notificationsessionManager
     * @param actionNumber
     * @return un PendingIntent indicando la accion a realizar
     */
    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, RadioStreamService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }


    /**
     * Este metodo maneja las acciones enlaza las acciones de la notificacion con los listeners
     * asignados al sessionManager
     *
     * @param playbackAction
     */
    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }


}
