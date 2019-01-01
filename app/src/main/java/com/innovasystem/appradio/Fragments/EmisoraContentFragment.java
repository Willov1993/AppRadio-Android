package com.innovasystem.appradio.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Emisora;
import com.innovasystem.appradio.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EmisoraContentFragment extends Fragment {

    TextView tv_nombre_emisora,tv_ciudad_frecuencia;

    public static Emisora emisora;
    FragmentsAdapter adapter;

    public EmisoraContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_emisora_content, container, false);

        tv_nombre_emisora= root.findViewById(R.id.tv_econtent_nombre);
        tv_ciudad_frecuencia= root.findViewById(R.id.tv_econtent_frecuencia);
        ViewPager vpager= root.findViewById(R.id.vpager);
        TabLayout tabLayout= root.findViewById(R.id.tablayout_info_emisora);

        emisora= getArguments().getParcelable("emisora");

        tv_nombre_emisora.setText(emisora.getNombre());
        tv_ciudad_frecuencia.setText(emisora.getCiudad() + " • " + emisora.getFrecuencia_dial());

        adapter= new FragmentsAdapter(getChildFragmentManager());
        setupViewPager(vpager);
        tabLayout.setupWithViewPager(vpager);

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragmentsAdapter(getChildFragmentManager());
        adapter.addFragment(new EmisoraInfoFragment(),"Info");
        adapter.addFragment(new NoticiasFragment(),"Noticias");
        adapter.addFragment(new SegmentosFragment(),"Programación");
        viewPager.setAdapter(adapter);
    }

    static class FragmentsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public FragmentsAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment,String name) {
            mFragments.add(fragment);
            mFragmentTitles.add(name);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
