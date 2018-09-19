package com.innovasystem.appradio.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Fragments.ChatFragment;
import com.innovasystem.appradio.Fragments.FavoritosFragment;
import com.innovasystem.appradio.Fragments.HomeFragment;
import com.innovasystem.appradio.Fragments.NoticiasFragment;
import com.innovasystem.appradio.Fragments.SegmentosFragment;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, RadioStreamService.class));
        registerReceiver(receiverFromservice, new IntentFilter(RadioStreamService.SERVICE_TO_ACTIVITY));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_segmentos);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiverFromservice, new IntentFilter(RadioStreamService.SERVICE_TO_ACTIVITY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverFromservice);
    }


    /*=== Metodos utilitarios ===*/

    /**
     * Este metodo cambia el fragment asociado a la actividad por otro fragment
     *
     * @param fragment El fragmento nuevo, que va mostrarse y reemplazara al anterior.
     * @param viewResource El id del contenedor donde se va a mostrar el fragment
     * @return void
     */
    public void changeFragment(Fragment fragment, int viewResource) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(viewResource, fragment);
        ft.commit();
    }


    /*=== Listeners ===*/

    /**
     * Este BroadcastRecevier listener manejara los mensajes que se originen del servicio
     * RadioStreamService, y eventualmente mostrara en un mensaje los errores que se originen
     * en el servicio.
     */
    private BroadcastReceiver receiverFromservice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (RadioStreamService.SERVICE_TO_ACTIVITY.equalsIgnoreCase(action)) {


                Toast toast = null;
                String currentPlayerStatus = intent.getStringExtra(RadioStreamService.PLAYER_STATUS_KEY);
                Log.e("Player Mode", "" + currentPlayerStatus);

                switch(currentPlayerStatus){
                    case "erroronplaying":
                        toast= Toast.makeText(context, "Error al conectarse al servicio de streaming de radio, revise su conexion a internet", Toast.LENGTH_LONG);
                        break;
                    default:
                        //toast= Toast.makeText(context, "Current status of StreamService: " + currentPlayerStatus, Toast.LENGTH_LONG);
                        break;
                }
                
                if(toast != null)
                    toast.show();
            }
        }
    };


    /**
     * Este Listener manejara los eventos asociados al BottomNavigationView. Se encargara de cargar
     * los fragments segun la seleccion del usuario.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment contentFragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    contentFragment= new HomeFragment();
                    HomeActivity.this.changeFragment(contentFragment,R.id.frame_container_home);
                    return true;
                case R.id.navigation_segmentos:
                    contentFragment= new SegmentosFragment();
                    HomeActivity.this.changeFragment(contentFragment,R.id.frame_container_home);
                    return true;
                case R.id.navigation_favoritos:
                    contentFragment= new FavoritosFragment();
                    HomeActivity.this.changeFragment(contentFragment,R.id.frame_container_home);
                    return true;
                case R.id.navigation_chat:
                    contentFragment= new ChatFragment();
                    HomeActivity.this.changeFragment(contentFragment,R.id.frame_container_home);
                    return true;
                case R.id.navigation_noticias:
                    contentFragment= new NoticiasFragment();
                    HomeActivity.this.changeFragment(contentFragment,R.id.frame_container_home);
                    return true;
                /*case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_emisoras:
                    mTextMessage.setText(R.string.title_emisoras);
                    return true;
                case R.id.navigation_notificaciones:
                    mTextMessage.setText(R.string.title_notificaciones);
                    return true;
                case R.id.navigation_sugerencias:
                    mTextMessage.setText(R.string.title_sugerencias);
                    return true;
                case R.id.navigation_perfil:
                    mTextMessage.setText(R.string.title_perfil);
                    return true;
                    */
            }
            return false;
        }
    };



}
