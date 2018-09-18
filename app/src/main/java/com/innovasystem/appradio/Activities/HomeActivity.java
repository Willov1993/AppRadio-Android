package com.innovasystem.appradio.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.innovasystem.appradio.Fragments.ChatFragment;
import com.innovasystem.appradio.Fragments.FavoritosFragment;
import com.innovasystem.appradio.Fragments.HomeFragment;
import com.innovasystem.appradio.Fragments.NoticiasFragment;
import com.innovasystem.appradio.Fragments.SegmentosFragment;
import com.innovasystem.appradio.R;

public class HomeActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }

    public void changeFragment(Fragment fragment, int viewResource) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(viewResource, fragment);
        ft.commit();
    }

}
