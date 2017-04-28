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

public class MainActivity  extends Activity implements View.OnClickListener {

    private Chronometer chronometer;
    private long timeWhenStopped = 0;
    private  Animation mAnimation = new AlphaAnimation(1, 0);


    private  Button start_button,stop_button,select1,select2,select3,select4;
    private EditText mOutputView;
   // private EditText mInputView;
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

        //mInputView = (EditText) findViewById(R.id.input);
       // mStartButton = (Button) findViewById(R.id.startButton);
       // mStopButton = (Button) findViewById(R.id.stopButton);
       // mSendButton = (Button) findViewById(R.id.sendButton);

        mBluetoothServer = new BluetoothServer();
        mBluetoothServer.setListener(mBluetoothServerListener);
        onStartAuto();

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

                break;
            case R.id.btn_right:

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
                  //  mStopButton.setEnabled(true);
                  //  mStartButton.setEnabled(false);
                }

                @Override
                public void onConnected() {
                 //   writeMessage("*** Client has connected ***");
                   // mSendButton.setEnabled(true);
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
                   // mSendButton.setEnabled(false);
                   // mStopButton.setEnabled(false);
                    //mStartButton.setEnabled(true);
                }
            };

    public void onStartAuto(){
        try {
            mBluetoothServer.start();
        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }
   /* public void onStartClick(View view){
        try {
            mBluetoothServer.start();
        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }*/

   /* public void onStopClick(View view){
        mBluetoothServer.stop();
    }

   /* public void onSendClick(View view){
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
*/

    private void writeMessage(String message){

        if(message.charAt(0) == '1'){//sol sinyal aktifken
            select1=(Button)findViewById(R.id.btn_left);
            setAnimationButton(select1);
        }
        else if(message.charAt(0) == '0'){
            select1.clearAnimation();
        }

        if(message.charAt(1)=='1'){//sağ sinyal
            select2=(Button)findViewById(R.id.btn_right);
            setAnimationButton(select2);

        }
        else if(message.charAt(1) == '0'){
            select2.clearAnimation();
        }
        if(message.charAt(2)=='1'){//sağ sinyal
            select3=(Button)findViewById(R.id.btn_flash);
            setAnimationButton(select3);

        }
        else if(message.charAt(1) == '0'){
            select3.clearAnimation();
        }
        if(message.charAt(3)=='1'){//sağ sinyal
            select4=(Button)findViewById(R.id.btn_horn);
            setAnimationButton(select4);

        }
        else if(message.charAt(3) == '0'){
            select4.clearAnimation();
        }

        //  mInputView.setText("ahmet  " +message + "\r\n" + mInputView.getText().toString());

    }

    private void setAnimationButton(Button btn){
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(200);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);

        btn.startAnimation(mAnimation);
        btn.setClickable(false);
    }



    private void writeError(String message){
        writeMessage("ERROR: " + message);
    }
}
