package com.innovasystem.appradio.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.innovasystem.appradio.Classes.SessionConfig;
import com.innovasystem.appradio.Fragments.EmisorasFragment;
import com.innovasystem.appradio.Fragments.EncuestasFragment;
import com.innovasystem.appradio.Fragments.FavoritosFragment;
import com.innovasystem.appradio.Fragments.HomeFragment;
import com.innovasystem.appradio.Fragments.ConcursosFragment;
import com.innovasystem.appradio.R;
import com.innovasystem.appradio.Services.RadioStreamService;
import com.innovasystem.appradio.Utils.Utils;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    BottomNavigationView navigation;
    DrawerLayout nav_drawer;
    NavigationView nav_view;

    boolean menuOpened=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, RadioStreamService.class));
        registerReceiver(receiverFromservice, new IntentFilter(RadioStreamService.SERVICE_TO_ACTIVITY));

        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        nav_drawer= (DrawerLayout) findViewById(R.id.home_drawer);
        nav_view= (NavigationView) findViewById(R.id.nav_lateral);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setLabelVisibilityMode(BottomNavigationView.VISIBLE);


        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment contentFragmet;
                switch (item.getItemId()){
                    case R.id.nav_item_favoritos:
                        contentFragmet= new FavoritosFragment();
                        HomeActivity.this.changeFragment(contentFragmet,R.id.frame_container,false);
                        nav_drawer.closeDrawer(Gravity.END);
                        break;
                    case R.id.nav_item_notificaciones:
                        nav_drawer.closeDrawer(Gravity.END);
                        break;
                    case R.id.nav_item_sugerencias:
                        nav_drawer.closeDrawer(Gravity.END);
                        break;
                }

                nav_drawer.closeDrawer(Gravity.END);
                return true;
            }
        });

        //Carga de Provincia Guardada
        SharedPreferences preferences = getSharedPreferences("session", MODE_PRIVATE);
        SessionConfig.getSessionConfig(getApplication()).provincia= preferences.getString("provincia","");

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){


            if(getSupportFragmentManager().findFragmentById(R.id.frame_container)instanceof HomeFragment){
                finish();
            }
            //Si no queda ningun elemento en el Stack del Manager, entonces volvemos al home
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                navigation.setSelectedItemId(R.id.navigation_home);
            }
            else{
                getSupportFragmentManager().popBackStackImmediate();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*=== Metodos utilitarios ===*/

    /**
     * Este metodo cambia el fragment asociado a la actividad por otro fragment
     *
     * @param fragment El fragmento nuevo, que va mostrarse y reemplazara al anterior.
     * @param viewResource El id del contenedor donde se va a mostrar el fragment
     * @param addToStack Un valor booleano para indicar si se debe agregar al stack del
     *                   FragmentManager (util para cuando se desea regresar de un fragment
     *                   al anterior)
     * @return void
     */
    public void changeFragment(Fragment fragment, int viewResource,boolean addToStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(viewResource, fragment);
        if(addToStack)
            ft.addToBackStack(null); //Esto hace que el fragmentManager mantenga en su stack este fragment
        ft.commit();
    }


    /**
     * Este Listener manejara los eventos asociados al BottomNavigationView. Se encargara de cargar
     * los fragments segun la seleccion del usuario.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment contentFragmet;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    contentFragmet= new HomeFragment();
                    HomeActivity.this.changeFragment(contentFragmet,R.id.frame_container,false);
                    return true;

                case R.id.navigation_concursos:
                    contentFragmet= new ConcursosFragment();
                    HomeActivity.this.changeFragment(contentFragmet,R.id.frame_container,false);
                    return true;

                case R.id.navigation_emisoras:
                    contentFragmet= new EmisorasFragment();
                    HomeActivity.this.changeFragment(contentFragmet,R.id.frame_container,false);
                    return true;

                case R.id.navigation_encuestas:
                    contentFragmet= new EncuestasFragment();
                    HomeActivity.this.changeFragment(contentFragmet,R.id.frame_container,false);
                    return true;

                case R.id.navigation_menu:
                    if(!menuOpened) {
                        nav_drawer.openDrawer(Gravity.END);
                        menuOpened=true;
                    }
                    else{
                        nav_drawer.closeDrawer(Gravity.END);
                        menuOpened=false;
                    }
                    return true;
            }
            return false;
        }
    };

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
                        Utils.mostrarMensajeSnackBar(getWindow().getDecorView().getRootView(), "Error al reproducir el servicio de streaming de radio, revise su conexion a internet");
                        break;
                    case "erroronserver":
                        Utils.mostrarMensajeSnackBar(getWindow().getDecorView().getRootView(), "Ha ocurrido un error en el servidor de streaming, intente mas luego");
                        break;
                    case "erroronconnection":
                        Utils.mostrarMensajeSnackBar(getWindow().getDecorView().getRootView(), "Ha ocurrido un erro al conectarse al servicio de streaming, intente mas luego");
                        break;
                    default:

                        break;
                }

                if(toast != null)
                    toast.show();
            }
        }
    };
}
