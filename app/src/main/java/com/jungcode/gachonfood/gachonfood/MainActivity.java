package com.jungcode.gachonfood.gachonfood;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
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
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import es.dmoral.toasty.Toasty;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


public class MainActivity extends AppCompatActivity {

    String parse,parse2,parse3,parse4;
    String[] str = {"월","화","수","목","금","토","일"};
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    final Handler mHandler = new Handler();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumSquareR.otf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumSquareB.otf"));

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fabb);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.appinfo){
                    Intent intent = new Intent(MainActivity.this, Start.class);
                    startActivity(intent);

                }
                if(id == R.id.github){
                    new MaterialDialog.Builder(MainActivity.this)
                            .title("오픈소스 라이브러리")
                            .content("com.baoyz.pullrefreshlayout:library:1.2.0\n" +
                                    "io.github.yavski:fab-speed-dial:1.0.6\n" +
                                    "com.afollestad.material-dialogs:commons:0.9.1.0\n" +
                                    "com.tsengvn:Typekit:1.0.0\n" +
                                    "com.github.GrenderG:Toasty:1.2.5")
                            .positiveText("확인")
                            .show();
                }
                if(id == R.id.message){
                    new MaterialDialog.Builder(MainActivity.this)
                            .autoDismiss(false)
                            .title("개발자에게 메시지 보내기")
                            .content("기능 개선사항이나 버그가 있을 시 요청해주세요.")
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input("내용", "", new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(final MaterialDialog dialog, CharSequence input) {
                                    final String temp = "https://api.telegram.org/bot439468143:AAE5Kk8kJfTPD3p4EQTjmG8FojGg5oMS7EQ/sendMessage?chat_id=128419855&text=(Gachon_Food)%20Ver_" + BuildConfig.VERSION_NAME + "_" + input.toString();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            posthttp2(temp);

                                        }
                                    }).start();


                                    dialog.dismiss();
                                }
                            }).show();
                }
                return false;
            }
        });

        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getApplicationContext(), getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        try{
            Calendar cal = Calendar.getInstance();
            String strWeek = null;
            int nWeek = cal.get(Calendar.DAY_OF_WEEK);
            if(nWeek == 1)
                nWeek = 8;
            mViewPager.setCurrentItem(nWeek - 2);
        }catch(Exception e){

        }


        if (isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    posthttp3("http://www.gachon.ac.kr/etc/food.jsp?gubun=A");
                    posthttp4("http://www.gachon.ac.kr/etc/food.jsp?gubun=B");
                    posthttp5("http://www.gachon.ac.kr/etc/food.jsp?gubun=C");
                    posthttp6("http://ace.gachon.ac.kr/dormitory/reference/menu");
                    SharedPreferences mPref = getSharedPreferences("mPref", 0);
                    SharedPreferences.Editor mPrefEdit = mPref.edit();
                    mPrefEdit.putString("parse1", parse);
                    mPrefEdit.putString("parse2", parse2);
                    mPrefEdit.putString("parse3", parse3);
                    mPrefEdit.putString("parse4", parse4);
                    mPrefEdit.commit();

                    String[] str_temp = parse4.split("<th class=\"");
                    for(int i = 0;i<7;i++){
                        str[i] = str_temp[i + 1].split("</th>")[0].split("\">")[1];
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
            /*
            SharedPreferences mPref = getSharedPreferences("mPref", 0);
            SharedPreferences.Editor mPrefEdit = mPref.edit();
            mPrefEdit.putString("parse1", "");
            mPrefEdit.putString("parse2", "");
            mPrefEdit.putString("parse3", "");
            mPrefEdit.putString("parse4", "");
            mPrefEdit.commit();*/

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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context mContext;

        public SectionsPagerAdapter(Context mContext, FragmentManager fm) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            switch (position) {
                case 0:
                    return new activity_tabs1(mContext,"월");
                case 1:
                    return new activity_tabs1(mContext,"화");
                case 2:
                    return new activity_tabs1(mContext,"수");
                case 3:
                    return new activity_tabs1(mContext,"목");
                case 4:
                    return new activity_tabs1(mContext,"금");
                case 5:
                    return new activity_tabs1(mContext,"토");
                case 6:
                    return new activity_tabs1(mContext,"일");
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
    public void posthttp2(String sstr1) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet post = new HttpGet();
            post.setURI(new URI(sstr1));
            HttpResponse resp = client.execute(post);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"euc-kr"));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();
            //parse = sb.toString();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toasty.success(getApplicationContext(), "전송 성공!", Toast.LENGTH_SHORT, true).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void posthttp3(String sstr1) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet post = new HttpGet();
            post.setURI(new URI(sstr1));
            HttpResponse resp = client.execute(post);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"utf8"));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();
            parse = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void posthttp4(String sstr1) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet post = new HttpGet();
            post.setURI(new URI(sstr1));
            HttpResponse resp = client.execute(post);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"utf8"));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();
            parse2 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void posthttp5(String sstr1) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet post = new HttpGet();
            post.setURI(new URI(sstr1));
            HttpResponse resp = client.execute(post);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"utf8"));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();
            parse3 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void posthttp6(String sstr1) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet post = new HttpGet();
            post.setURI(new URI(sstr1));
            HttpResponse resp = client.execute(post);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"utf8"));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();
            parse4 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
