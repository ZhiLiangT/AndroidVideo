package com.tianzl.androidvideo.adapter;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;

/**
 * Created by tianzl on 2017/8/6.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String [] attrTitle;
    private List<Fragment> fragments;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context, String[] attrTitle, List<Fragment> fragments) {
        super(fm);
        this.context=context;
        this.attrTitle=attrTitle;
        this.fragments=fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return attrTitle.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return attrTitle[position%attrTitle.length];
    }
}
