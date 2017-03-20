package com.zmm.datetimeselect;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmm.wheelview.OnWheelChangedListener;
import com.zmm.wheelview.OnWheelScrollListener;
import com.zmm.wheelview.WheelView;
import com.zmm.wheelview.adapter.NumericWheelAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/3/20
 * Time:下午4:37
 */

public class YearActivity extends AppCompatActivity {

    private PopupWindow popupWindow;

    private WheelView wl_start_year;

    private RelativeLayout rl_main;
    private int mScreenWidth;
    private String selectDate;//选择时间
    private NumericWheelAdapter mWheelAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);

        initView();

    }

    private void initView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        rl_main = (RelativeLayout) findViewById(R.id.activity_main);
        Button yearSelect = (Button) findViewById(R.id.btn_year);
        yearSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
                makeWindowDark();
            }
        });
    }

    private void showPop() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View popupWindowView = inflater.inflate(R.layout.pop_double_time_select, null);
        Button btn_cancel = (Button) popupWindowView.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) popupWindowView.findViewById(R.id.btn_ok);
        popupWindow = new PopupWindow(popupWindowView, 4 * mScreenWidth / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        initWheelView(popupWindowView);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                makeWindowLight();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate = wl_start_year.getCurrentItem() + 2000 + "";//年
                Log.d("DATE YEAR","select year = "+selectDate);
                popupWindow.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(rl_main, Gravity.CENTER, 0, 0);
    }

    private void initWheelView(View view) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        wl_start_year = (WheelView) view.findViewById(R.id.wl_start_year);

        mWheelAdapter = new NumericWheelAdapter(this, 2000, 2020);
        mWheelAdapter.setLabel(" ");
        wl_start_year.setViewAdapter(mWheelAdapter);
        mWheelAdapter.setTextColor(R.color.black);
        mWheelAdapter.setTextSize(20);
        wl_start_year.setCyclic(true);//是否可循环滑动
        wl_start_year.setVisibleItems(5);
        wl_start_year.addScrollingListener(startScrollListener);
        wl_start_year.setCurrentItem(curYear - 2000);
        wl_start_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mWheelAdapter.getItemText(wl_start_year.getCurrentItem());
                setTextViewSize(currentText, mWheelAdapter);
            }
        });
        mWheelAdapter.setPosition(wl_start_year.getCurrentItem());
    }

    OnWheelScrollListener startScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            selectDate = wl_start_year.getCurrentItem() + 2000 + "";
            Log.d("YearActivity","当前选中年份:"+selectDate);
        }
    };
    private void setTextViewSize(String curriteItemText, NumericWheelAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString().trim();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(20);
                textvew.setTextColor(Color.BLACK);
            } else {
                textvew.setTextSize(18);
                textvew.setTextColor(Color.parseColor("#FF585858"));
            }
        }
    }
    /**
     * 让屏幕变暗
     */
    private void makeWindowDark() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    /**
     * 让屏幕变亮
     */
    private void makeWindowLight() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
    }
}
