package com.mobilemd.datepicker;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

public class DatePickerViewModule extends ReactContextBaseJavaModule {
    private static final String REACT_CLASS = "RNDatePicker";
    final ReactApplicationContext reactContext;
    DatePicker picker;
    ReadableMap mOptions = null;
    Callback sureCallback;
    public DatePickerViewModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        getReactApplicationContext()
                .getJSModule(RCTNativeAppEventEmitter.class)
                .emit(eventName, params);
    }

    @ReactMethod
    public void _init(final ReadableMap options)
    {
        mOptions = options;
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                picker = new DatePicker(mOptions);
                picker.setOnDidSelectListener(new DatePicker.OnDidSelectListener() {
                    @Override
                    public void onDatePickerSure(String date) {
                        WritableMap dict = Arguments.createMap();
                        dict.putString("selectedValue",date);
                        dict.putString("type","confirm");
                        sendEvent("pickerEvent",dict);
                    }
                });
                picker.showTime(getCurrentActivity());
            }
        });

    }

    @ReactMethod
    public void show(final ReadableMap options, final Callback callback )
    {
        mOptions = options;
        sureCallback = callback;
//        UiThreadUtil.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                picker = new DatePicker(mOptions);
//                picker.setOnDidSelectListener(new DatePicker.OnDidSelectListener() {
//                    @Override
//                    public void onDatePickerSure(String date) {
//                        WritableMap dict = Arguments.createMap();
//                        dict.putString("selectedValue",date);
//                        dict.putString("type","confirm");
//                        sendEvent("pickerEvent",dict);
//                    }
//                });
//                picker.showTime(getCurrentActivity());
//            }
//        });

        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                picker = new DatePicker(mOptions);
                picker.setOnDidSelectListener(new DatePicker.OnDidSelectListener() {
                    @Override
                    public void onDatePickerSure(String date) {
//                        WritableMap dict = Arguments.createMap();
//                        dict.putString("selectedValue",date);
//                        dict.putString("type","confirm");
                        sureCallback.invoke(date);
//                        sendEvent("pickerEvent",dict);
                    }
                });
                picker.showTime(getCurrentActivity());
                picker.show(getCurrentActivity());
            }
        });
    }

    @ReactMethod
    public void hide()
    {

    }
}