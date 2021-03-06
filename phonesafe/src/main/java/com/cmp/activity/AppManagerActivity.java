package com.cmp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cmp.adapter.AppListViewAdapter1;
import com.cmp.adapter.AppListViewAdapter2;
import com.cmp.adapter.AppPageAdapter;
import com.cmp.data.AppInfo;
import com.cmp.phonesafe.R;
import com.cmp.util.AppInfoParser;
import com.cmp.util.FileSizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：cmp on 2016/10/12 11:11
 * <p>
 * 软件管家
 */
public class AppManagerActivity extends Activity implements View.OnClickListener {
    private TabLayout mTabLayout;//选项卡标题布局
    private ViewPager mViewPager;//选项卡内容布局
    private ArrayList<View> mArrayList;//viewpager布局
    private String[] mTitle;//选项卡标题
    private List<AppInfo> appInfos;//总集合
    private List<AppInfo> userAppInfos;//个人软件
    private List<AppInfo> systemAppInfos;//系统软件
    private ListView mListView1;//个人软件
    private AppListViewAdapter1 mLVAdapter1;
    private ListView mListView2;//系统软件
    private AppListViewAdapter2 mLVAdapter2;
    private ListView mListView3;//安装包
    private UninstallReceiver receiver;
    private TextView mToolbarTitleTv;//标题
    //异步线程处理
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (mLVAdapter1 == null) {
                        mLVAdapter1 = new AppListViewAdapter1
                                (getApplicationContext(), userAppInfos, R.layout.activity_app_item);
                    }
                    if (mLVAdapter2 == null) {
                        mLVAdapter2 = new AppListViewAdapter2
                                (getApplicationContext(), systemAppInfos, R.layout.activity_app_item);
                    }
                    mListView1.setAdapter(mLVAdapter1);
                    mLVAdapter2.notifyDataSetChanged();
                    mListView2.setAdapter(mLVAdapter2);
                    mLVAdapter2.notifyDataSetChanged();
                case 15:
                    mLVAdapter1.notifyDataSetChanged();
                    mLVAdapter2.notifyDataSetChanged();
            }
        }
    };
    private ImageButton mToolbarBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        initView();
        initData();
        initAdapter();
    }


    //初始化组件
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.app_tab);
        //viewpager
        mViewPager = (ViewPager) findViewById(R.id.app_vp);
        LayoutInflater li = LayoutInflater.from(this);
        View view1 = li.inflate(R.layout.activity_app_page1, null);
        View view2 = li.inflate(R.layout.activity_app_page2, null);
        View view3 = li.inflate(R.layout.activity_app_page3, null);
        mArrayList = new ArrayList<>();
        mArrayList.add(view1);
        mArrayList.add(view2);
        mArrayList.add(view3);
        //手机内存
        TextView mPhoneMemory = (TextView) findViewById(R.id.app_phone_memory);
        mPhoneMemory.setText(
                mPhoneMemory.getText()
                        + FileSizeUtil.formatFileSize
                        (FileSizeUtil.getAvailableInternalMemorySize()));
        //SD卡内存
        TextView mSDMemory = (TextView) findViewById(R.id.app_card_memory);
        mSDMemory.setText(
                mSDMemory.getText() +
                        FileSizeUtil.formatFileSize
                                (FileSizeUtil.getAvailableExternalMemorySize()));
        //ListView
        mListView1 = (ListView) view1.findViewById(R.id.app_page1);
        mListView2 = (ListView) view2.findViewById(R.id.app_page2);
        mListView3 = (ListView) view3.findViewById(R.id.app_page3);
        mToolbarTitleTv = (TextView) findViewById(R.id.toolbar_title_tv);
        mToolbarBackBtn = (ImageButton) findViewById(R.id.toolbar_back_btn);
        mToolbarBackBtn.setOnClickListener(this);
    }

    //适配器
    private void initAdapter() {
        AppPageAdapter mAdapter = new AppPageAdapter(mArrayList, mTitle, AppManagerActivity.this);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //初始化数据
    public void initData() {
        mTitle = super.getResources().getStringArray(R.array.app_tab);
        mToolbarTitleTv.setText(getResources().getStringArray(R.array.home_name)[2]);
        appInfos = new ArrayList<>();
        userAppInfos = new ArrayList<>();
        systemAppInfos = new ArrayList<>();
        new Thread() {
            public void run() {
                appInfos.clear();
                userAppInfos.clear();
                systemAppInfos.clear();
                appInfos.addAll(AppInfoParser.getappInfos(AppManagerActivity.this));
                //判断是个人软件还是系统软件
                for (AppInfo appInfo : appInfos) {
                    if (appInfo.isUserApp()) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(10);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_btn:
                finish();
                break;
        }
    }

    class UninstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }

    protected void onDestory() {
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }
}
