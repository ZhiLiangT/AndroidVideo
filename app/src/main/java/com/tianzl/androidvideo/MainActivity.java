package com.tianzl.androidvideo;



import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.support.design.widget.TabLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tianzl.androidvideo.adapter.ViewPagerAdapter;
import com.tianzl.androidvideo.fragment.FileFragment;
import com.tianzl.androidvideo.fragment.NetFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btVideo;
    private Button btSurface;
    private Button btTexture;
    private TextView tvTitle;
    private ViewPager viewPager;
    private TabLayout tableLayout;
    private String[] attr=new String[]{"本地视频","网络视频"};
    private List<Fragment> fragments;
    private FileFragment fileFragment;
    private NetFragment netFragment;
    private ViewPagerAdapter adapter;

    public static int flag=0;
    public static final int VIDEOVIEW_FLAG=0;
    public static final int SURFACEVIEW_FLAG=1;
    public static final int TEXTUREVIEW_FLAG=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        fragments=new ArrayList<>();
        fileFragment=new FileFragment();
        netFragment=new NetFragment();
        fragments.add(fileFragment);
        fragments.add(netFragment);
        tableLayout.addTab(tableLayout.newTab().setText(attr[0]));
        tableLayout.addTab(tableLayout.newTab().setText(attr[1]));
        adapter=new ViewPagerAdapter(getSupportFragmentManager(),this,attr,fragments);
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

    }

    private void initView() {
        btVideo= (Button) findViewById(R.id.main_video);
        btVideo.setOnClickListener(this);
        btSurface= (Button) findViewById(R.id.main_surface);
        btSurface.setOnClickListener(this);
        btTexture= (Button) findViewById(R.id.main_texture);
        btTexture.setOnClickListener(this);
        tvTitle= (TextView) findViewById(R.id.main_title);
        viewPager= (ViewPager) findViewById(R.id.main_viewpager);
        tableLayout= (TabLayout) findViewById(R.id.main_tab);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_video:
                Toast.makeText(this,"切换到VideoVIew",Toast.LENGTH_SHORT).show();
                flag=VIDEOVIEW_FLAG;
                break;
            case R.id.main_surface:
                Toast.makeText(this,"切换到SurfaceView",Toast.LENGTH_SHORT).show();
                flag=SURFACEVIEW_FLAG;
                break;
            case R.id.main_texture:
                Toast.makeText(this,"切换到TextureView",Toast.LENGTH_SHORT).show();
                flag=TEXTUREVIEW_FLAG;
                break;
        }
    }
}
