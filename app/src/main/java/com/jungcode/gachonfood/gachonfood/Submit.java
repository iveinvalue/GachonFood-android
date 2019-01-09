package com.jungcode.gachonfood.gachonfood;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Submit extends AppCompatActivity {

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    String parse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        String date, location;

        Intent i = getIntent();
        Bundle b = i.getExtras();
        date = b.getString("date");
        location = b.getString("location");

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(location );
        ab.setSubtitle(date);

        EditText editText = (EditText) findViewById(R.id.editText);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//키보드숨김
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);//키보드숨김

        String get = "http://blog.naver.com/CommentList.nhn?blogId=ffasha&logNo=220659423364&currentPage=&isMemolog=false&focusingCommentNo=&showLastPage=true&shortestContentAreaWidth=false";
        posthttp(get);

        parse = parse.replace("<span class=\\\"t_point\\\">", "");//태그제거
        parse = parse.replace("<\\/span>", "");//태그제거
        parse = parse.replace("</span>", "");//태그제거

        Thread thread = new Thread() {
            @Override
            public void run() {
                int aa = 1;
                while (aa < 50) {
                    try {
                        String[] data = parse.split("value=\"/2016-03-19/");
                        String[] data2 = data[aa].split("/end/\"");
                        mAdapter.addItem(data2[0]);
                    } catch (Exception e) {
                    }
                    aa++;
                }
            }
        };
        thread.start();

    }

    public void posthttp(String sstr1) {
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
            parse = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//포스트1

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_card, null);

                holder.mcheck = (TextView) convertView.findViewById(R.id.mcheck);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);


            holder.mcheck.setText(mData.mcheck);




            return convertView;
        }

        public void addItem(String mcheck){
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mcheck = mcheck;

            mListData.add(addInfo);
        }
        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }
        public void removeall(){
            mListData.clear();
            dataChange();
        }
        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public TextView mDate;
        public TextView mText2;
        public TextView mcheck;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
