package com.mobilemd.datepicker;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.bridge.MemoryPressure;
import com.facebook.react.bridge.ReadableMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePicker{

    /* package */ static final String TITLE_TEXT = "titleText";
    /* package */ static final String TITLE_TEXT_COLOR = "titleTextColor";
    /* package */ static final String DONE_TEXT = "doneText";
    /* package */ static final String DONE_TEXT_COLOR = "doneTextColor";
    /* package */ static final String CANCEL_TEXT = "cancelText";
    /* package */ static final String CANCEL_TEXT_COLOR = "cancelTextColor";
    /* package */ static final String TOP_BAR_COLOR = "topBarColor";
    int startYear = 0;
    int yearRange = 115;
    int dayRange = 31;
    WheelView picker1 = null;
    WheelView picker2 = null;
    WheelView picker3 = null;
    WheelView picker4 = null;
    WheelView picker5 = null;

    int maxYear = 0;
    int maxMonth = 12;
    int maxDay = 31;
    int maxHour = 24;
    int maxMinute = 60;

    int minYear = 0;
    int minMonth = 1;
    int minDay = 1;
    int minHour = 0;
    int minMinute = 0;

    int currentYear = 0;
    int currentMonth = 0;
    int currentDay = 0;
    int currentHour = -1;
    int currentMinute = -1;

    String mStrMax = null;
    String mStrMin = null;
    String mStrFormat = "yyyy-MM-dd";
    String mTitle = "";
    WPopupWindow popupWindow;
    OnDidSelectListener mListener = null;
    ReadableMap mOptions;
    public DatePicker(ReadableMap options)
    {
        mOptions = options;
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        startYear = c.get(Calendar.YEAR)-100; // 获取当前年份

        if (options.hasKey("title"))
        {
            mTitle = options.getString("title");
        }

        if (options.hasKey("formatter"))
        {
            mStrFormat = options.getString("formatter");
        }
        else
        {
            if (options.hasKey("mode"))
            {
                String mode = options.getString("mode");
                if (mode.equals("date"))
                {
                    mStrFormat = "yyyy-MM-dd";
                }
                else if(mode.equals("datetime"))
                {
                    mStrFormat = "yyyy-MM-dd HH:mm";
                }
                else if(mode.equals("time"))
                {
                    mStrFormat = "HH:mm";
                }
            }
        }


        if (options.hasKey("defaultValue"))
        {
            String strDate = options.getString("defaultValue");
            SimpleDateFormat sdf= new SimpleDateFormat(mStrFormat);
            Date date = null;
            try {
                date = sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            c.setTime(date);
        }

        if (options.hasKey("maxDate"))
        {
            mStrMax = options.getString("maxDate");
        }

        if (options.hasKey("minDate"))
        {
            mStrMin = options.getString("minDate");
        }


        maxYear = startYear + yearRange;
        minYear = startYear;

        dateRange();

        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        currentDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        currentHour = c.get(Calendar.HOUR_OF_DAY);//时
        currentMinute = c.get(Calendar.MINUTE);//分
    }

    void dateRange()
    {

        if (mStrMax != null)
        {
            SimpleDateFormat sdf= new SimpleDateFormat(mStrFormat);
            Date date = null;
            try {
                date = sdf.parse(mStrMax);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendarMax = Calendar.getInstance();
            calendarMax.setTime(date);
            maxYear = calendarMax.get(Calendar.YEAR);
            maxMonth = calendarMax.get(Calendar.MONTH) + 1;
            maxDay = calendarMax.get(Calendar.DAY_OF_MONTH);
            maxHour = calendarMax.get(Calendar.HOUR_OF_DAY);
            maxMinute = calendarMax.get(Calendar.MINUTE);
        }


        if (mStrMin != null)
        {
            SimpleDateFormat sdf= new SimpleDateFormat(mStrFormat);
            Date date = null;
            try {
                date = sdf.parse(mStrMin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendarMin = Calendar.getInstance();
            calendarMin.setTime(date);
            minYear = calendarMin.get(Calendar.YEAR);
            minMonth = calendarMin.get(Calendar.MONTH) + 1;
            minDay = calendarMin.get(Calendar.DAY_OF_MONTH);
            minHour = calendarMin.get(Calendar.HOUR_OF_DAY);
            minMinute = calendarMin.get(Calendar.MINUTE);
        }
    }


    int isAllDay(int year,int month)
    {
        int day=31;
        switch(month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day=31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day=30;
                break;
            case 2:
            {
                int yearTemp = year + startYear - 1;
                if(((yearTemp%4==0)&&(yearTemp%100!=0))||(yearTemp%400==0))
                {
                    day=29;
                    break;
                }
                else
                {
                    day=28;
                    break;
                }
            }
            default:
                break;
        }
        return day;
    }




    void reloadPicker1()
    {
        picker1.clearData();
        picker1.addData("未知");
        for (int i=0;i<=maxYear-minYear;i++)
        {
            String value = (minYear+i)+"";
            picker1.addData(value);
        }

        picker1.notifyDataSetChanged();
    }

    void reloadPicker2()
    {
        picker2.clearData();
        picker2.addData("未知");

        if (currentYear == minYear)
        {
            for (int i=minMonth-1;i<12;i++)
            {
                String value = (i+1)+"";
                picker2.addData(value);
            }
        }
        else if(currentYear == maxYear)
        {
            for (int i=0;i<maxMonth;i++)
            {
                String value = (i+1)+"";
                picker2.addData(value);
            }
        }
        else
        {
            for (int i=0;i<12;i++)
            {
                String value = (i+1)+"";
                picker2.addData(value);
            }
        }


        picker2.notifyDataSetChanged();
    }

    void reloadPicker3()
    {

        dayRange = isAllDay(currentYear,currentMonth);
        picker3.clearData();
        picker3.addData("未知");

        if (currentYear == minYear && currentMonth == minMonth)
        {
            for (int i=minDay-1;i<dayRange;i++)
            {
                String value = (i+1)+"";
                picker3.addData(value);
            }

            if (currentDay >= dayRange)
            {
                currentDay = dayRange;
            }
            else if (currentDay <= minDay)
            {
                currentDay = minDay;
            }
        }
        else if(currentYear == maxYear && currentMonth == maxMonth)
        {
            for (int i=0;i<maxDay;i++)
            {
                String value = (i+1)+"";
                picker3.addData(value);
            }
            if (currentDay >= maxDay)
            {
                currentDay = maxDay;

            }
        }
        else
        {
            for (int i=0;i<dayRange;i++)
            {
                String value = (i+1)+"";
                picker3.addData(value);
            }

            if (currentDay >= dayRange)
            {
                currentDay = dayRange;
            }
        }

        picker3.setCenterItem(currentDay+"");

        picker3.notifyDataSetChanged();
    }

    void reloadPicker4()
    {
        picker4.clearData();
        picker4.addData("未知");

        if (currentYear == minYear && currentMonth == minMonth && currentDay == minDay)
        {
            for (int i=minHour;i<24;i++)
            {
                String value = i+"";
                picker4.addData(value);
            }
            if (currentHour >= 23)
            {
                currentHour = 23;
            }
            else if (currentHour <= minHour)
            {
                currentHour = minHour;
            }

        }
        else if(currentYear == maxYear && currentMonth == maxMonth && currentDay == maxDay)
        {
            for (int i=0;i<=maxHour;i++)
            {
                String value = i+"";
                picker4.addData(value);
            }

            if (currentHour >= maxHour)
            {
                currentHour = maxHour;
            }
        }
        else
        {
            for (int i=0;i<24;i++)
            {
                String value = i+"";
                picker4.addData(value);
            }

            if (currentHour >= 23)
            {
                currentHour = 23;
            }
        }

        picker4.setCenterItem(currentHour+"");


        picker4.notifyDataSetChanged();
    }


    void reloadPicker5()
    {
        picker5.clearData();
        picker5.addData("未知");

        if (currentYear == minYear && currentMonth == minMonth && currentDay == minDay && currentHour == minHour)
        {
            for (int i=minMinute;i<60;i++)
            {
                String value = i+"";
                picker5.addData(value);
            }
            if (currentMinute >= 59)
            {
                currentMinute = 59;
            }
            else if (currentMinute <= minMinute)
            {
                currentMinute = minMinute;
            }

        }
        else if(currentYear == maxYear && currentMonth == maxMonth && currentDay == maxDay && currentHour == maxHour)
        {
            for (int i=0;i<=maxMinute;i++)
            {
                String value = i+"";
                picker5.addData(value);
            }

            if (currentMinute >= maxMinute)
            {
                currentMinute = maxMinute;
            }
        }
        else
        {
            for (int i=0;i<60;i++)
            {
                String value = i+"";
                picker5.addData(value);
            }

            if (currentMinute >= 59)
            {
                currentMinute = 59;
            }
        }

        picker5.setCenterItem(currentMinute+"");


        picker5.notifyDataSetChanged();
    }



    void addPicker1(Activity activity,LinearLayout ml)
    {
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        picker1 = new WheelView(activity);
        ml.addView(picker1,params);
        picker1.unit = "年";
        picker1.setOnDidSelectListener(new WheelView.OnDidSelectListener() {
            @Override
            public void onDidSelect(Object o) {
                if (o.equals("未知"))
                {
                    currentYear = 0;
                }
                else
                {
                    currentYear = Integer.parseInt((String)o);
                }

                if (picker2 != null)
                {
                    reloadPicker2();
                }

                if (picker3 != null)
                {
                    reloadPicker3();
                }
                if (picker4 != null)
                {
                    reloadPicker4();
                }
                if (picker5 != null)
                {
                    reloadPicker5();
                }
            }
        });

        reloadPicker1();
        picker1.setCenterItem(currentYear+"");
    }


    void addPicker2(Activity activity,LinearLayout ml)
    {
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        picker2 = new WheelView(activity);
        ml.addView(picker2,params);

        picker2.unit = "月";
        picker2.setOnDidSelectListener(new WheelView.OnDidSelectListener() {
            @Override
            public void onDidSelect(Object o) {
                if (o.equals("未知"))
                {
                    currentMonth = 0;
                }
                else
                {
                    currentMonth = Integer.parseInt((String)o);
                }
                if (picker3 != null)
                {
                    reloadPicker3();
                }
                if (picker4 != null)
                {
                    reloadPicker4();
                }
                if (picker5 != null)
                {
                    reloadPicker5();
                }
            }
        });

        reloadPicker2();
        picker2.setCenterItem(currentMonth + "");
    }


    void addPicker3(Activity activity,LinearLayout ml)
    {
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        picker3 = new WheelView(activity);
        ml.addView(picker3,params);

        picker3.unit = "日";
        picker3.setOnDidSelectListener(new WheelView.OnDidSelectListener() {
            @Override
            public void onDidSelect(Object o) {
                if (o.equals("未知"))
                {
                    currentDay = 0;
                }
                else
                {
                    currentDay = Integer.parseInt((String)o);
                }
                if (picker4 != null)
                {
                    reloadPicker4();
                }

                if (picker5 != null)
                {
                    reloadPicker5();
                }

            }
        });

        reloadPicker3();
        picker3.setCenterItem(currentDay +"");
    }

    void addPicker4(Activity activity,LinearLayout ml)
    {
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        picker4 = new WheelView(activity);
        ml.addView(picker4,params);
        picker4.unit = "时";
        picker4.setOnDidSelectListener(new WheelView.OnDidSelectListener() {
            @Override
            public void onDidSelect(Object o) {
                if (o.equals("未知"))
                {
                    currentHour = -1;
                }
                else
                {
                    currentHour = Integer.parseInt((String)o);
                }

                reloadPicker5();

            }
        });

        reloadPicker4();
        picker4.setCenterItem(currentHour +"");
    }

    void addPicker5(Activity activity,LinearLayout ml)
    {
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        picker5 = new WheelView(activity);
        ml.addView(picker5,params);
        picker5.unit = "分";
        picker5.setOnDidSelectListener(new WheelView.OnDidSelectListener() {
            @Override
            public void onDidSelect(Object o) {
                if (o.equals("未知"))
                {
                    currentMinute = -1;
                }
                else
                {
                    currentMinute = Integer.parseInt((String)o);
                }

            }
        });

        reloadPicker5();
        picker5.setCenterItem(currentMinute +"");
    }

    void showTime(Activity activity)
    {
        View wh= LayoutInflater.from(activity).inflate(R.layout.layout_time,null);
        LinearLayout ml = (LinearLayout)wh.findViewById(R.id.linear_layout);
        if (mStrFormat.indexOf("yyyy") >= 0 || mStrFormat.indexOf("YYYY") >= 0)
        {
            addPicker1(activity,ml);
        }

        if (mStrFormat.indexOf("MM") >= 0)
        {
            addPicker2(activity,ml);
        }

        if (mStrFormat.indexOf("dd") >= 0)
        {
            addPicker3(activity,ml);
        }

        if (mStrFormat.indexOf("HH") >= 0 || mStrFormat.indexOf("hh") >= 0)
        {
            addPicker4(activity,ml);
        }

        if (mStrFormat.indexOf("mm") >= 0)
        {
            addPicker5(activity,ml);
        }


        popupWindow=new WPopupWindow(wh);

        RelativeLayout relativeLayout = (RelativeLayout)wh.findViewById(R.id.top);
        if (mOptions.hasKey(TOP_BAR_COLOR))
        relativeLayout.setBackgroundColor(mOptions.getInt(TOP_BAR_COLOR));

        TextView text = (TextView)wh.findViewById(R.id.center);

        if (mOptions.hasKey(TITLE_TEXT))
        {
            text.setText(mOptions.getString(TITLE_TEXT));
        }

        if (mOptions.hasKey(TITLE_TEXT_COLOR))
        {
            text.setTextColor(mOptions.getInt(TITLE_TEXT_COLOR));
        }


        TextView left = (TextView)wh.findViewById(R.id.left);
        if (mOptions.hasKey(CANCEL_TEXT))
        {
            left.setText(mOptions.getString(CANCEL_TEXT));
        }

        if (mOptions.hasKey(CANCEL_TEXT_COLOR))
        {
            left.setTextColor(mOptions.getInt(CANCEL_TEXT_COLOR));
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        TextView right = (TextView)wh.findViewById(R.id.right);
        if (mOptions.hasKey(DONE_TEXT))
        {
            right.setText(mOptions.getString(DONE_TEXT));
        }

        if (mOptions.hasKey(DONE_TEXT_COLOR))
        {
            right.setTextColor(mOptions.getInt(DONE_TEXT_COLOR));
        }

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (mListener != null)
                {
                    String strDate = new String(mStrFormat);
                    if (picker1 != null)
                    {
                        strDate = strDate.replace("yyyy",(String)picker1.getCenterItem());
                    }

                    if (picker2 != null)
                    {
                        strDate = strDate.replace("MM",getValue(picker2));
                    }

                    if (picker3 != null)
                    {
                        strDate = strDate.replace("dd",getValue(picker3));
                    }

                    if (picker4 != null)
                    {
                        strDate = strDate.replace("HH",getValue(picker4));
                    }

                    if (picker5 != null)
                    {

                        strDate = strDate.replace("mm",getValue(picker5));
                    }
                    mListener.onDatePickerSure(strDate);
                }
            }
        });

    }

    String getValue(WheelView picker)
    {
        String strValue = (String)picker.getCenterItem();
        if (strValue.length() == 1)
        {
            strValue = "0"+strValue;
        }
        if (strValue.equals("未知"))
        {
            if (picker == picker1)
            {
                strValue = "unkw";
            }
            else
            {
                strValue = "uk";
            }
        }
        return strValue;
    }


    void show(Activity activity)
    {
        if (popupWindow != null)
        {
            popupWindow.showAtLocation(getContentView(activity), Gravity.BOTTOM, 0, 0);
        }

    }


    public static View getContentView(Activity ac){
        ViewGroup view = (ViewGroup)ac.getWindow().getDecorView();
        FrameLayout content = (FrameLayout)view.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }

    public void setOnDidSelectListener(OnDidSelectListener l)
    {
        mListener = l;
    }

    public interface OnDidSelectListener
    {
        void onDatePickerSure(String date);
    }
}