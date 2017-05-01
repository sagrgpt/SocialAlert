package com.example.socialalert;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

import com.example.socialalert.Adapter.ViewPagerAdapter;
import com.example.socialalert.Fragment.*;

public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Setting the tab bar for the tab layout **/
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPage(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public void setUpViewPage(ViewPager mViewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new CurrentFragment(),getString(R.string.current_feed_title));
        viewPagerAdapter.addFragment(new FeedFragment(),getString(R.string.feed_title));
        mViewPager.setAdapter(viewPagerAdapter);
    }


}
