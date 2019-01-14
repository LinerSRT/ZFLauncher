package com.rmicro.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rmicro.launcher.utils.IntentAnim;
import com.rmicro.launcher.utils.IntentUtils;
import com.rmicro.launcher.adapter.AppAdapter;
import com.rmicro.launcher.adapter.MyVPAdapter;
import com.rmicro.launcher.custom.APPBean;
import com.rmicro.launcher.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class AllAppList extends AppCompatActivity {

    private final String TAG = "AllAppList";
    private Context mContext = AllAppList.this;
    private Activity mActivity = AllAppList.this;
    private ArrayList<APPBean> mData;
    private ViewPager mViewPager;
    private MyVPAdapter mAdapter;
    private LinearLayout appAppPoint;
    private ImageView point;
    private List<View> mDataView;
    private int pagerAppSize = 4;
    private int pagerNum;
    private int lastPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allapp);
        initView();
        initData();
        registApkFreshReceiver();
    }

    protected void initView() {
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        appAppPoint = (LinearLayout)findViewById(R.id.appAppPoint);
    }

    protected void initData() {
        setAllAPP();
        mAdapter = new MyVPAdapter(mDataView);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);//缓存4页
        setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                lastPoint = position;
                setCurrentItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setAllAPP() {
        mDataView = new ArrayList<>();
        mData = IntentUtils.getAllApp(mContext, false);
        int appNum = mData.size();
        pagerNum = appNum % pagerAppSize != 0 ? (appNum / pagerAppSize + 1)
                : appNum / pagerAppSize;
        for (int i = 0; i < pagerNum; i++) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.allappspager, null);
            GridView gridView = (GridView)view.findViewById(R.id.allAPPGridView);
            final List<APPBean> perPageApps = new ArrayList<>();
            if (i != (pagerNum - 1)) {
                for (int j = i * pagerAppSize; j < pagerAppSize * (i + 1); j++) {
                    perPageApps.add(mData.get(j));
                }
            } else {
                for (int j = i * pagerAppSize; j < mData.size(); j++) {
                    perPageApps.add(mData.get(j));
                }
            }
            AppAdapter adapter = new AppAdapter(this, perPageApps);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View paramView,
                                        int arg2, long arg3) {
                    Animation animation = AnimationUtils.loadAnimation(
                            mContext, R.anim.scale_view);
                    IntentAnim animlistener = new IntentAnim(mActivity,
                            perPageApps.get(arg2).getAppPackageName(), "");
                    animation.setAnimationListener(animlistener);
                    paramView.startAnimation(animation);
                }
            });
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0,
                                               View paramView, int arg2, long arg3) {
                    String pkgName = perPageApps.get(arg2).getAppPackageName();
                    String appName = perPageApps.get(arg2).getAppName();
                    Animation animation = AnimationUtils.loadAnimation(
                            mContext, R.anim.shake);
                    IntentAnim animlistener = new IntentAnim(mActivity,
                            pkgName, appName);
                    animation.setAnimationListener(animlistener);
                    paramView.startAnimation(animation);
                    return true;
                }
            });
            mDataView.add(view);
            newPoint();
            appAppPoint.addView(point);
        }
    }

    private void setCurrentItem(int pos) {
        for (int i = 0; i < appAppPoint.getChildCount(); i++) {
            if (i == pos) {
                appAppPoint.getChildAt(i).setSelected(true);
            } else {
                appAppPoint.getChildAt(i).setSelected(false);
            }
        }
    }

    private void newPoint() {
        point = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 3;
        params.leftMargin = 3;
        params.gravity = Gravity.CENTER_VERTICAL;
        point.setLayoutParams(params);
        point.setBackgroundResource(R.drawable.pointbg);
    }

    private void registApkFreshReceiver() {
        IntentFilter apkFreshFilter = new IntentFilter();
        apkFreshFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        apkFreshFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        apkFreshFilter.addDataScheme("package");
        mContext.registerReceiver(apkFreshReceiver, apkFreshFilter);
    }

    private BroadcastReceiver apkFreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_PACKAGE_ADDED) ||action.equals(Intent.ACTION_PACKAGE_REMOVED)){
                refresh();
            }
        }
    };

    private void refresh() {
        appAppPoint.removeAllViews();
        mData.clear();
        setAllAPP();
        lastPoint = lastPoint > pagerNum - 1 ? pagerNum - 1 : lastPoint;
        mAdapter = new MyVPAdapter(mDataView);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(lastPoint);
        setCurrentItem(lastPoint);
        mViewPager.invalidate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG,"onStart.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG,"onStop.");
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"onDestroy.");
        point = null;
        mData.clear();
        mDataView.clear();
        mContext.unregisterReceiver(apkFreshReceiver);
    }
}
