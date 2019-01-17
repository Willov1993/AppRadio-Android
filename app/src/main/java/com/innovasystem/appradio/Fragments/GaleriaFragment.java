package com.innovasystem.appradio.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovasystem.appradio.Classes.Models.Segmento;
import com.innovasystem.appradio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GaleriaFragment extends Fragment {

    TextView tv_segmento;
    FragmentsAdapter adapter;

    Segmento segmento;

    public GaleriaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_galeria, container, false);

        tv_segmento= root.findViewById(R.id.tv_galeria_segmento);
        ViewPager vpager= root.findViewById(R.id.vpager_galeria);
        TabLayout tabLayout= root.findViewById(R.id.tablayout_galeria);

        adapter= new FragmentsAdapter(getChildFragmentManager());
        setupViewPager(vpager);
        tabLayout.setupWithViewPager(vpager);

        segmento= getArguments().getParcelable("segmento");

        tv_segmento.setText(segmento.getNombre());


        return root;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragmentsAdapter(getChildFragmentManager());
        adapter.addFragment(new ImagenesFragment(),"Imágenes");
        adapter.addFragment(new VideosFragment(),"Videos");
        adapter.addFragment(new EnVivoFragment(),"En Vivo");
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
