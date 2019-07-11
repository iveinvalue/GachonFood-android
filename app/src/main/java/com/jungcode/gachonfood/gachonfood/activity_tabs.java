package com.jungcode.gachonfood.gachonfood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import androidx.annotation.BoolRes;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.baoyz.widget.PullRefreshLayout;

@SuppressLint("ValidFragment")
public class activity_tabs extends Fragment {
    Context mContext;
    String parse1, parse2, parse3, parse4;
    String[] first, first2, total, total2;
    TextView food1, food3, food5, food1_2, food1_3, food3_2, food3_3, food5_2, food5_3, food1q, food1_2q, food1_3q;
    SharedPreferences mPref;
    String _1, _2, _3;
    String when_ = "";
    Boolean[] check = {true, true, true, true};
    int is_first = 1;

    public activity_tabs(Context context, String when) {
        mContext = context;
        when_ = when;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (is_first == 0) {
                for (int i = 0; i < 4; i++) {
                    try {
                        if (mPref.getString("check" + String.valueOf(i), "").contains("0"))
                            check[i] = false;
                        else if (mPref.getString("check" + String.valueOf(i), "").contains("1"))
                            check[i] = true;
                        else
                            check[i] = true;
                    } catch (Exception e) {
                        check[i] = true;
                        //Toast.makeText(getActivity(),"sdsd", Toast.LENGTH_LONG).show();
                    }

                    if (check[i] == true) {
                        yeah(i, 0, getView());
                    } else if (check[i] == false) {
                        yeah(i, 1, getView());
                    }
                }
            }
            is_first = 0;
            //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        } else {
            //preload 될때(전페이지에 있을때)
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_tabs, null);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mPref = this.getActivity().getSharedPreferences("mPref", 0);

        food1 = view.findViewById(R.id.content);
        food1_2 = view.findViewById(R.id.content2);
        food1_3 = view.findViewById(R.id.content3);
        food1q = view.findViewById(R.id.contentq);
        food1_2q = view.findViewById(R.id.content2q);
        food1_3q = view.findViewById(R.id.content3q);
        food3 = view.findViewById(R.id.contenta);
        food3_2 = view.findViewById(R.id.contenta2);
        food3_3 = view.findViewById(R.id.contenta3);
        food5 = view.findViewById(R.id.contentab);
        food5_2 = view.findViewById(R.id.contentab2);
        food5_3 = view.findViewById(R.id.contentab3);

        for (int i = 0; i < 4; i++) {
            try {
                if (mPref.getString("check" + String.valueOf(i), "").contains("0"))
                    check[i] = false;
                else if (mPref.getString("check" + String.valueOf(i), "").contains("1"))
                    check[i] = true;
                else
                    check[i] = true;
            } catch (Exception e) {
                check[i] = true;
                //Toast.makeText(getActivity(),"sdsd", Toast.LENGTH_LONG).show();
            }

            if (check[i] == true) {
                yeah(i, 0, view);
            } else if (check[i] == false) {
                yeah(i, 1, view);
            }

        }

        CardView button1 = (CardView) view.findViewById(R.id.openworld);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (check[0] == false) {
                    yeah(0, 0, view);
                } else if (check[0] == true) {
                    yeah(0, 1, view);
                }

            }
        });

        CardView button2 = (CardView) view.findViewById(R.id.openworld2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (check[1] == false) {
                    yeah(1, 0, view);
                } else if (check[1] == true) {
                    yeah(1, 1, view);
                }

            }
        });

        CardView button3 = (CardView) view.findViewById(R.id.openworlda);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (check[2] == false) {
                    yeah(2, 0, view);
                } else if (check[2] == true) {
                    yeah(2, 1, view);
                }


            }
        });

        CardView button4 = (CardView) view.findViewById(R.id.openworldab);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (check[3] == false) {
                    yeah(3, 0, view);
                } else if (check[3] == true) {
                    yeah(3, 1, view);
                }


            }
        });

        final PullRefreshLayout layout = (PullRefreshLayout) view.findViewById(R.id.refresh);

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                food1.setText("로딩중...");
                food1_2.setText("로딩중...");
                food1_3.setText("로딩중...");
                food1q.setText("로딩중...");
                food1_2q.setText("로딩중...");
                food1_3q.setText("로딩중...");
                food3.setText("로딩중...");
                food3_2.setText("로딩중...");
                food3_3.setText("로딩중...");
                food5.setText("로딩중...");
                food5_2.setText("로딩중...");
                food5_3.setText("로딩중...");
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override
                    public void run() {
                        refresh();
                        layout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        layout.setColor(Color.parseColor("#cc6666"));

        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                refresh();
            }
        }, 500);
        return view;
    }

    void yeah(int checkk, int checkk2, View view_temp) {
        SharedPreferences.Editor mPrefEdit = mPref.edit();
        Boolean tr_fa;
        String _01, str_temp;
        int visiable;

        if (checkk2 == 0) {
            tr_fa = true;
            _01 = "1";
            visiable = View.VISIBLE;
            str_temp = "눌러서 작게보기";
        } else {
            tr_fa = false;
            _01 = "0";
            visiable = View.GONE;
            str_temp = "눌러서 크게보기";
        }

        check[checkk] = tr_fa;
        mPrefEdit.putString("check" + String.valueOf(checkk), _01);
        mPrefEdit.commit();

        if (checkk == 3) {
            TextView _text = (TextView) view_temp.findViewById(R.id._4text);
            _text.setText(str_temp);
            view_temp.findViewById(R.id.contentab).setVisibility(visiable);
            view_temp.findViewById(R.id.contentab2).setVisibility(visiable);
            view_temp.findViewById(R.id.contentab3).setVisibility(visiable);
            view_temp.findViewById(R.id.textab).setVisibility(visiable);
            view_temp.findViewById(R.id.textab2).setVisibility(visiable);
            view_temp.findViewById(R.id.textab3).setVisibility(visiable);
        }
        if (checkk == 2) {
            TextView _text = (TextView) view_temp.findViewById(R.id._3text);
            _text.setText(str_temp);
            view_temp.findViewById(R.id.contenta).setVisibility(visiable);
            view_temp.findViewById(R.id.contenta2).setVisibility(visiable);
            view_temp.findViewById(R.id.contenta3).setVisibility(visiable);
            view_temp.findViewById(R.id.texta).setVisibility(visiable);
            view_temp.findViewById(R.id.texta2).setVisibility(visiable);
            view_temp.findViewById(R.id.texta3).setVisibility(visiable);
        }
        if (checkk == 1) {
            TextView _text = (TextView) view_temp.findViewById(R.id._2text);
            _text.setText(str_temp);
            view_temp.findViewById(R.id.contentq).setVisibility(visiable);
            view_temp.findViewById(R.id.content2q).setVisibility(visiable);
            view_temp.findViewById(R.id.content3q).setVisibility(visiable);
            view_temp.findViewById(R.id.textq).setVisibility(visiable);
            view_temp.findViewById(R.id.text2q).setVisibility(visiable);
            view_temp.findViewById(R.id.text3q).setVisibility(visiable);
        }
        if (checkk == 0) {
            TextView _text = (TextView) view_temp.findViewById(R.id._1text);
            _text.setText(str_temp);
            view_temp.findViewById(R.id.content).setVisibility(visiable);
            view_temp.findViewById(R.id.content2).setVisibility(visiable);
            view_temp.findViewById(R.id.content3).setVisibility(visiable);
            view_temp.findViewById(R.id.text).setVisibility(visiable);
            view_temp.findViewById(R.id.text2).setVisibility(visiable);
            view_temp.findViewById(R.id.text3).setVisibility(visiable);
        }

        //MainActivity.mViewPager.getAdapter().notifyDataSetChanged();
    }

    void refresh() {
        parse1 = mPref.getString("parse1", "");
        parse2 = mPref.getString("parse2", "");
        parse3 = mPref.getString("parse3", "");
        parse4 = mPref.getString("parse4", "");
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //parse1 = parse1.replace("\n\n", "");
                        parse1 = parse1.replace("\n", "");
                        parse1 = parse1.replace("<br />", "\n");

                        while (parse1.contains("\n\n")) {
                            parse1 = parse1.replace("\n\n", "\n");
                        }

                        total = parse1.split(">" + when_);
                        total2 = total[1].split("</tr>");

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[1].split("</td>");
                        _1 = first2[0];

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[2].split("</td>");
                        _2 = first2[0];

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[3].split("</td>");
                        _3 = first2[0];

                        _1 = _1.trim();
                        _2 = _2.trim();
                        _3 = _3.trim();
                        food5.setText(_1);
                        food5_2.setText(_2);
                        food5_3.setText(_3);

                    } catch (Exception e) {
                        food5.setText("운영없음");
                        food5_2.setText("운영없음");
                        food5_3.setText("운영없음");
                    }

                    try {
                        //parse2 = parse2.replace("\n\n", "");
                        parse2 = parse2.replace("\n", "");
                        parse2 = parse2.replace("<br />", "\n");

                        while (parse2.contains("\n\n")) {
                            parse2 = parse2.replace("\n\n", "\n");
                        }

                        total = parse2.split(">" + when_);
                        total2 = total[1].split("</tr>");

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[1].split("</td>");
                        _1 = first2[0];

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[2].split("</td>");
                        _2 = first2[0];

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[3].split("</td>");
                        _3 = first2[0];

                        _1 = _1.trim();
                        _2 = _2.trim();
                        _3 = _3.trim();
                        food3.setText(_2);
                        food3_2.setText(_1);
                        food3_3.setText(_3);
                    } catch (Exception e) {
                        food3.setText("운영없음");
                        food3_2.setText("운영없음");
                        food3_3.setText("운영없음");
                    }

                    try {
                        //parse3 = parse3.replace("\n\n", "");
                        parse3 = parse3.replace("-. ", "-");
                        parse3 = parse3.replace("\n", "");
                        parse3 = parse3.replace("<br />", "\n");

                        while (parse3.contains("\n\n")) {
                            parse3 = parse3.replace("\n\n", "\n");
                        }

                        parse3 = parse3.replace("-시간 :\n", "-시간 :");

                        total = parse3.split(">" + when_);
                        total2 = total[1].split("</tr>");

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[1].split("</td>");
                        _1 = first2[0];

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[2].split("</td>");
                        _2 = first2[0];

                        first = total2[0].split("<td class=\"tl\">");
                        first2 = first[3].split("</td>");
                        _3 = first2[0];

                        _1 = _1.trim();
                        _2 = _2.trim();
                        _3 = _3.trim();
                        food1.setText(_1);
                        food1_2.setText(_2);
                        food1_3.setText(_3);
                    } catch (Exception e) {
                        food1.setText("운영없음");
                        food1_2.setText("운영없음");
                        food1_3.setText("운영없음");
                    }

                    try {
                        parse4 = parse4.replace('(', '@');
                        parse4 = parse4.replace(')', '#');
                        total = parse4.split("일@" + when_ + "#</th>");

                        total[1] = total[1].replace("@", "(");
                        total[1] = total[1].replace("#", ")");

                        total[1] = total[1].replace("\t", "");
                        total[1] = total[1].replace("\n", "");
                        total[1] = total[1].replace("</div>", "");
                        total[1] = total[1].replace("<div>", "");
                        total[1] = total[1].replace(" ", "");
                        total[1] = total[1].replace("<td>", "");
                        total[1] = total[1].replace("B:", "\nB:");
                        _1 = total[1].split("</td>")[0];
                        _2 = total[1].split("</td>")[1];
                        _3 = total[1].split("</td>")[2];


                        food1q.setText(_1);
                        food1_2q.setText(_2);
                        food1_3q.setText(_3);
                    } catch (Exception e) {
                        food1q.setText("운영없음2");
                        food1_2q.setText("운영없음2");
                        food1_3q.setText("운영없음2");
                    }
                }
            });
        } catch (Exception e) {

        }
    }
}