package com.aozyilmaz35.safebike;


/*
 * The Android chronometer widget revised so as to count milliseconds
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;

import java.text.DecimalFormat;

public class Chronometer extends android.support.v7.widget.AppCompatTextView {
    @SuppressWarnings("unused")
	private static final String TAG = "Chronometer";

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    private  int minutes = 0 ;
    private  int hours = 0 ;
    private  int seconds = 0 ;



    public interface OnChronometerTickListener {

        void onChronometerTick(Chronometer chronometer);
    }

    private long mBase;
    private boolean mVisible;
    private boolean mStarted;

    public boolean ismRunning() {
        return mRunning;
    }

    public void setmRunning(boolean mRunning) {
        this.mRunning = mRunning;
    }

    private boolean mRunning;
    private OnChronometerTickListener mOnChronometerTickListener;

    private static final int TICK_WHAT = 2;

    private long timeElapsed;
    
    public Chronometer(Context context) {
        this (context, null, 0);
    }

    public Chronometer(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);

        init();
    }

     void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
        //MainActivity.cal.setText(mKal);

     }

    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public long getBase() {
        return mBase;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    public void start() {

        mStarted = true;
        updateRunning();
    }

    public void stop() {
        mStarted = false;
        mRunning = false;
        setBase(SystemClock.elapsedRealtime());
    }
    public void pause() {
        mStarted = true;
        mRunning = false;

    }

    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super .onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super .onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    synchronized void updateText(long now) {
        timeElapsed = now - mBase;
        
        DecimalFormat df = new DecimalFormat("00");
        
        hours = (int)(timeElapsed / (3600 * 1000));
        int remaining = (int)(timeElapsed % (3600 * 1000));
        
          minutes = (int)(remaining / (60 * 1000));
        remaining = (int)(remaining % (60 * 1000));
        
        seconds = (int)(remaining / 1000);
        remaining = (int)(remaining % (1000));
        
        int milliseconds = (int)(((int)timeElapsed % 1000) / 100);
        
        String text = "";
        
        if (hours > 0) {
        	text += df.format(hours) + ":";
        }
        
       	text += df.format(minutes) + ":";
       	text += df.format(seconds) + ":";
       	text += Integer.toString(milliseconds);
        
        setText(text);


    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 100);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;

        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this , TICK_WHAT),
                        100);
            }
        }
    };

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

	public long getTimeElapsed() {
		return timeElapsed;
	}
    
}
