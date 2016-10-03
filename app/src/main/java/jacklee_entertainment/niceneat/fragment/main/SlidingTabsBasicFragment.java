/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jacklee_entertainment.niceneat.fragment.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jacklee_entertainment.niceneat.R;
import jacklee_entertainment.niceneat.TabOption;
import jacklee_entertainment.niceneat.fragment.main.MyProfileFragment;
import jacklee_entertainment.niceneat.utils.sliding.view.SlidingTabLayout;


public class SlidingTabsBasicFragment extends Fragment {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private TabOption option;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        option = this.getArguments().getParcelable("taboption");
        Log.d("pagecount",option.getPageCount()+"");

        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager(),getActivity()));
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
         mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

             mSlidingTabLayout.setOption(option);

         // END_INCLUDE (setup_slidingtablayout)

        //Customize tab color
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(option.getIndicatorColor());
            }


            @Override
            public int getDividerColor(int position) {
                return getResources().getColor(option.getDividerColor());
            }
        });
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tabs, 0);

        mSlidingTabLayout.setViewPager(mViewPager);

    }

    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context;


        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return option.getPageCount();
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new ChatFragment();}
                else if(position == 1){
                    return new EventFragment();}
            else  if(position == 2){
                        return new GroupFragment();}

            else  if(position == 3){
                return new MyProfileFragment();
            } else {return new MyProfileFragment();}

        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            if(option.isIconHeader()){
                return mSlidingTabLayout.getTintResources(position);
            }

            return option.getPageTitle()[position];
        }
    }

}

