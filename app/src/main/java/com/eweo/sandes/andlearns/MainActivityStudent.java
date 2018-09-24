package com.eweo.sandes.andlearns;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivityStudent extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

            private TabLayout tabLayout;
            private ViewPager viewPager;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_student);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
                tabLayout.addTab(tabLayout.newTab().setText("SCHEDULE"));
                tabLayout.addTab(tabLayout.newTab().setText("PROGRESS"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                viewPager = (ViewPager) findViewById(R.id.pager);

                TabPagerAdapterStudent adapter = new TabPagerAdapterStudent(getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);
                tabLayout.setOnTabSelectedListener(this);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }