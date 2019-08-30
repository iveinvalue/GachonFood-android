package com.jungcode.gachonfood.gachonfood;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


import es.dmoral.toasty.Toasty;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    static Boolean dark = false;
    String parse, parse2, parse3, parse4;
    String[] str = {"월", "화", "수", "목", "금", "토", "일"};

    SectionsPagerAdapter mSectionsPagerAdapter;
    final Handler mHandler = new Handler();

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Switch actionBarSwitch = new Switch(this);

        SharedPreferences mPref = getSharedPreferences("mPref", 0);
        dark = mPref.getBoolean("dark", false);
        if(dark){
            actionBarSwitch.setChecked(true);
        }else{
            actionBarSwitch.setChecked(false);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(actionBarSwitch, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL | Gravity.END));

        actionBarSwitch.setOnCheckedChangeListener(this);

        FabSpeedDial fabSpeedDial = findViewById(R.id.fabb);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.appinfo) {
                    new MaterialDialog.Builder(MainActivity.this)
                            .title("버전")
                            .content(BuildConfig.VERSION_NAME)
                            .positiveText("확인")
                            .show();
                }
                if (id == R.id.github) {
                    new MaterialDialog.Builder(MainActivity.this)
                            .title("오픈소스 라이브러리")
                            .content("com.baoyz.pullrefreshlayout:library:1.2.0\n" +
                                    "io.github.yavski:fab-speed-dial:1.0.6\n" +
                                    "com.afollestad.material-dialogs:commons:0.9.1.0\n" +
                                    "com.github.GrenderG:Toasty:1.2.5")
                            .positiveText("확인")
                            .show();
                }
                if (id == R.id.message) {
                    new MaterialDialog.Builder(MainActivity.this)
                            .autoDismiss(false)
                            .title("개발자에게 메시지 보내기")
                            .content("기능 개선사항이나 버그가 있을 시 요청해주세요.")
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input("내용", "", new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(final MaterialDialog dialog, CharSequence input) {
                                    if (!input.toString().equals("")) {
                                        final String msg = "Ver_" + BuildConfig.VERSION_NAME + "_" + input.toString();
                                        final String temp = "http://wiffy.io/gachon/food/report.php?content=" + msg;
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                posthttp(temp);
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toasty.success(getApplicationContext(), "전송 성공!", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                });
                                            }
                                        }).start();
                                    }
                                    dialog.dismiss();
                                }
                            }).show();
                }
                return false;
            }
        });


        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getApplicationContext(), getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        try {
            Calendar cal = Calendar.getInstance();
            String strWeek = null;
            int nWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (nWeek == 1)
                nWeek = 8;
            mViewPager.setCurrentItem(nWeek - 2);
        } catch (Exception e) {

        }

        if (isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    parse = posthttp("http://www.gachon.ac.kr/etc/food.jsp?gubun=A");
                    parse2 = posthttp("http://www.gachon.ac.kr/etc/food.jsp?gubun=B");
                    parse3 = posthttp("http://www.gachon.ac.kr/etc/food.jsp?gubun=C");
                    parse4 = posthttp("http://ace.gachon.ac.kr/dormitory/reference/menu");
                    SharedPreferences mPref = getSharedPreferences("mPref", 0);
                    SharedPreferences.Editor mPrefEdit = mPref.edit();
                    mPrefEdit.putString("parse1", parse);
                    mPrefEdit.putString("parse2", parse2);
                    mPrefEdit.putString("parse3", parse3);
                    mPrefEdit.putString("parse4", parse4);
                    mPrefEdit.commit();

                    Log.d("asdf", "asdf" + parse4);
                    try {
                        String[] str_temp = parse4.split("<th class=\"");
                        for (int i = 0; i < 7; i++) {
                            str[i] = str_temp[i + 1].split("</th>")[0].split("\">")[1];
                        }
                    } catch (Exception e) {

                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.getAdapter().notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        } else {
            Toasty.warning(getApplicationContext(), "인터넷 연결에 실패했습니다. 저장된 학식을 불러옵니다.", Toast.LENGTH_LONG, true).show();
            mViewPager.getAdapter().notifyDataSetChanged();
        }

    }

    public boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences mPref = getSharedPreferences("mPref", 0);
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        if(isChecked){
            dark = true;
            mPrefEdit.putBoolean("dark", true);
        }else{
            dark = false;
            mPrefEdit.putBoolean("dark", false);
        }
        mPrefEdit.commit();
        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                restart();
            }
        }, 300);
    }

    public void restart(){
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context mContext;

        public SectionsPagerAdapter(Context mContext, FragmentManager fm) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new activity_tabs(mContext, "월");
                case 1:
                    return new activity_tabs(mContext, "화");
                case 2:
                    return new activity_tabs(mContext, "수");
                case 3:
                    return new activity_tabs(mContext, "목");
                case 4:
                    return new activity_tabs(mContext, "금");
                case 5:
                    return new activity_tabs(mContext, "토");
                case 6:
                    return new activity_tabs(mContext, "일");
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return str[0];
                case 1:
                    return str[1];
                case 2:
                    return str[2];
                case 3:
                    return str[3];
                case 4:
                    return str[4];
                case 5:
                    return str[5];
                case 6:
                    return str[6];
            }
            return null;
        }
    }

    public String posthttp(String str) {
        try {
            URL httpURL = new URL(str);
            HttpURLConnection conn = (HttpURLConnection) httpURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream(); //input스트림 개방
            StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //문자열 셋 세팅
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            String result = builder.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
