package com.aozyilmaz35.safebike;

import android.app.Activity;
import android.os.Bundle;

import android.os.SystemClock;

import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity  extends Activity implements View.OnClickListener {

    private Chronometer chronometer;
    private long timeWhenStopped = 0;
    private  Animation mAnimation = new AlphaAnimation(1, 0);


    private  Button start_button,stop_button,select;
    private EditText mOutputView;
    private EditText mInputView;
    private Button mStartButton;
    private Button mStopButton;
    private Button mSendButton;

    private BluetoothServer mBluetoothServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btn_Start)).setOnClickListener(this);
        ((Button) findViewById(R.id.btn_end)).setOnClickListener(this);
        ((Button) findViewById(R.id.btn_restart)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_left)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_right)).setOnClickListener(this);

        mInputView = (EditText) findViewById(R.id.input);
        mStartButton = (Button) findViewById(R.id.startButton);
        mStopButton = (Button) findViewById(R.id.stopButton);
        mSendButton = (Button) findViewById(R.id.sendButton);

        mBluetoothServer = new BluetoothServer();
        mBluetoothServer.setListener(mBluetoothServerListener);

    }

        @Override
    public void onClick(View v) {
        com.aozyilmaz35.safebike.Chronometer mChronometer = (com.aozyilmaz35.safebike.Chronometer) findViewById(R.id.chronometer);

        switch (v.getId()) {
            case R.id.btn_Start:
                mChronometer.start();
                break;
            case R.id.btn_end:
                    //Define here what happens when the Chronometer reaches the time above.
                mChronometer.stop();
                mChronometer.updateText(  SystemClock.elapsedRealtime());

                break;
            case R.id.btn_restart:

                mChronometer.start();
                mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);

                 break;
            case R.id.btn_left:
                select=(Button)findViewById(R.id.btn_left);
                mAnimation.setDuration(200);
                mAnimation.setInterpolator(new LinearInterpolator());
                mAnimation.setRepeatCount(Animation.INFINITE);
                mAnimation.setRepeatMode(Animation.REVERSE);
                select.startAnimation(mAnimation);
                select.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        v.clearAnimation();
                    }
                });
                break;
            case R.id.btn_right:
                select=(Button)findViewById(R.id.btn_right);
                mAnimation = new AlphaAnimation(1, 0);
                mAnimation.setDuration(200);
                mAnimation.setInterpolator(new LinearInterpolator());
                mAnimation.setRepeatCount(Animation.INFINITE);
                mAnimation.setRepeatMode(Animation.REVERSE);
                select.startAnimation(mAnimation);
                select.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        v.clearAnimation();
                    }
                });
                break;



        }

    }

    /**
     * Bluetooth server events listener.
     */
    private BluetoothServer.IBluetoothServerListener mBluetoothServerListener =
            new BluetoothServer.IBluetoothServerListener() {
                @Override
                public void onStarted() {
                    //writeMessage("*** Server has started, waiting for client connection ***");
                    mStopButton.setEnabled(true);
                    mStartButton.setEnabled(false);
                }

                @Override
                public void onConnected() {
                 //   writeMessage("*** Client has connected ***");
                    mSendButton.setEnabled(true);
                }

                @Override
                public void onData(byte[] data) {
                    writeMessage(new String(data));
                }

                @Override
                public void onError(String message) {
                    writeError(message);
                }

                @Override
                public void onStopped() {
                  //  writeMessage("*** Server has stopped ***");
                    mSendButton.setEnabled(false);
                    mStopButton.setEnabled(false);
                    mStartButton.setEnabled(true);
                }
            };

    public void onStartClick(View view){
        try {
            mBluetoothServer.start();
        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }

    public void onStopClick(View view){
        mBluetoothServer.stop();
    }

    public void onSendClick(View view){
        try {

            mBluetoothServer.send(mOutputView.getText().toString().getBytes());
            mOutputView.setText("");
        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }


    private void writeMessage(String message){
        mInputView.setText("ahmet  " +message + "\r\n" + mInputView.getText().toString());

    }

    private void writeError(String message){
        writeMessage("ERROR: " + message);
    }
}
