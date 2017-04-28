package com.aozyilmaz35.safebike;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    /******************************************************/

    android.support.v7.widget.Toolbar toolbar;
    LocationService myService;
    static boolean status;
    LocationManager locationManager;
    static TextView dist,time,speed;
    Button start,pause,stop;
    static long startTime,endTime;
    ImageView image,help;
    static ProgressDialog locate;
    static int p=0;


    private ServiceConnection sc=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder=(LocationService.LocalBinder)service;
            myService=binder.getService();
            status=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status=false;
        }
    };

    void bindService()
    {
        if(status==true)
            return;
        Intent i=new Intent(getApplicationContext(),LocationService.class);
        bindService(i, sc, BIND_AUTO_CREATE);
        status=true;
        startTime=System.currentTimeMillis();
    }

    void unbindService()
    {
        if(status==false)
            return;
        Intent i=new Intent(getApplicationContext(),LocationService.class);
        unbindService(sc);
        status=false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(status==true)
            unbindService();
    }

    @Override
    public void onBackPressed() {
        if(status==false)
            super.onBackPressed();
        else
            moveTaskToBack(true);
    }

    /****************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //((Button) findViewById(R.id.btn_Start)).setOnClickListener(this);
       // ((Button) findViewById(R.id.btn_end)).setOnClickListener(this);
       // ((Button) findViewById(R.id.btn_restart)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_left)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_right)).setOnClickListener(this);

        //mInputView = (EditText) findViewById(R.id.input);
       // mStartButton = (Button) findViewById(R.id.startButton);
       // mStopButton = (Button) findViewById(R.id.stopButton);
       // mSendButton = (Button) findViewById(R.id.sendButton);

        mBluetoothServer = new BluetoothServer();
        mBluetoothServer.setListener(mBluetoothServerListener);
        onStartAuto();


        dist=(TextView)findViewById(R.id.mDistText);
        speed=(TextView)findViewById(R.id.mSpeedText);

        start=(Button)findViewById(R.id.start);
        pause=(Button)findViewById(R.id.pause);
        stop=(Button)findViewById(R.id.stop);

        image=(ImageView)findViewById(R.id.image);

        start.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.aozyilmaz35.safebike.Chronometer mChronometer = (com.aozyilmaz35.safebike.Chronometer) findViewById(R.id.chronometer);

                checkGps();
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(status==false)
                    bindService();
                locate=new ProgressDialog(MainActivity.this);
                locate.setIndeterminate(true);
                locate.setCancelable(false);
                locate.setMessage("Getting Location...");
                locate.show();
                start.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                pause.setText("Pause");
                stop.setVisibility(View.VISIBLE);

                mChronometer.start();

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.aozyilmaz35.safebike.Chronometer mChronometer = (com.aozyilmaz35.safebike.Chronometer) findViewById(R.id.chronometer);

                if(mChronometer.ismRunning()== true)
                    mChronometer.pause();
                else{
                    mChronometer.start();

                    //mChronometer.updateText(  mChronometer.getBase() + timeWhenStopped );
                }


                if (pause.getText().toString().equalsIgnoreCase("pause")) {
                    pause.setText("Resume");
                    p = 1;
                    //  mChronometer.start();
                    //  mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);

                    mChronometer.updateText(  SystemClock.elapsedRealtime());
                }
                else if(pause.getText().toString().equalsIgnoreCase("Resume"))
                {
                    checkGps();
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    pause.setText("Pause");
                    p=0;

                }
                //mChronometer.stop();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            com.aozyilmaz35.safebike.Chronometer mChronometer = (com.aozyilmaz35.safebike.Chronometer) findViewById(R.id.chronometer);

            @Override
            public void onClick(View v) {
                if(status==true)
                    unbindService();
                start.setVisibility(View.VISIBLE);
                pause.setText("Pause");
                pause.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                p=0;

                mChronometer.stop();

            }


        });

        /****************************************************/

    }
    /*****************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         return super.onOptionsItemSelected(item);
    }


    void checkGps()
    {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    /*****************************************************************/
        @Override
    public void onClick(View v) {


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
