package com.mc.rockpaperscissors;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.mc.rockpaperscissors.dataobject.PlayerDetailsBean;
import com.mc.rockpaperscissors.handlers.DBHandler;
import com.mc.rockpaperscissors.util.Constant;
import com.mc.rockpaperscissors.util.ToastUtil;

public class MultiplayerActivity extends Activity implements Constant {

	private String TAG = "MultiPlayActivity";
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT      = 2;
	BluetoothAdapter btAdp = BluetoothAdapter.getDefaultAdapter();
	// Message types sent from the BluetoothService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	private BluetoothGameService mGameService = null;
	private String mConnectedDeviceName = null;
	public Button btn_connect;
	public Button btn_speak;
	protected static final int RESULT_SPEECH = 1000;
	ToastUtil t = new ToastUtil();
	int user = 0;
	int opponent =0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_play);
		btn_connect = (Button) findViewById(R.id.btn_connect);
		btn_speak = (Button) findViewById(R.id.btn_speak);
	}
	
	public void onClickConnect(View view) {
		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	}
	
	public void onSpeak(View view) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		try {
			startActivityForResult(intent, RESULT_SPEECH);
		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Ops! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}
		
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!btAdp.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
    		if (mGameService == null) setupGame();
        }
	}

	@Override
	public synchronized void onResume() {
		super.onResume();

		if (mGameService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mGameService.getState() == BluetoothGameService.STATE_NONE) {
				// Start the Bluetooth chat services
				mGameService.start();
			}
		}
	}

	private void setupGame() {
		// TODO Auto-generated method stub
		mGameService = new BluetoothGameService(this, mHandler);
	}
	
	// The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothGameService.STATE_CONNECTED:
                    break;
                case BluetoothGameService.STATE_CONNECTING:
                    break;
                case BluetoothGameService.STATE_LISTEN:
                	break;
                case BluetoothGameService.STATE_NONE:
                    break;
                }
                break;
            case MESSAGE_WRITE:
            	byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
//                Toast.makeText(getApplicationContext(), writeMessage,
//                        Toast.LENGTH_SHORT).show();
            	break;
            case MESSAGE_READ:
            	byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                opponent = Integer.parseInt(readMessage);
                if(user!=0){
                	compute();
                }else{
                	Toast.makeText(getApplicationContext(), "Please provide an option.",
                            Toast.LENGTH_SHORT).show();
                }
            	break;
            case MESSAGE_DEVICE_NAME:
            	// save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
            	break;
            case MESSAGE_TOAST:
            	Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                        Toast.LENGTH_SHORT).show();
            	break;
            }
        }
    };
    
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mGameService.getState() != BluetoothGameService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            Toast.makeText(getApplicationContext(), send.toString(), Toast.LENGTH_SHORT).show();
            mGameService.write(send);
        }
    }
    
    private void compute() {
    	if(user==0){
    		t.show(getApplicationContext(), "Please give your input.");
    	}else if(opponent ==0){
    		//sendMessage("Please send an input.");
    	}else{
    		if ((user == ROCK && opponent == SCISSORS) || (user == PAPER && opponent == ROCK)
    				|| (user == SCISSORS && opponent == PAPER)) {
    			t.show(getApplicationContext(), "You win");
    			
    		} else if ((opponent == ROCK && user == SCISSORS)
    				|| (opponent == PAPER && user == ROCK)
    				|| (opponent == SCISSORS && user == PAPER)) {
    			t.show(getApplicationContext(), "You Lose.");
    		} else {
    			t.show(getApplicationContext(), "Its a draw.");
    		}
    		user=0;
			opponent=0;
    	}
		
	}
    
    @Override 
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		switch(requestCode){
			case REQUEST_CONNECT_DEVICE:
				if (resultCode == Activity.RESULT_OK) {
					// When DeviceListActivity returns with a device to connect
					String address = data.getExtras().getString("deviceaddr");
					// Get the BLuetoothDevice object
	                BluetoothDevice device = btAdp.getRemoteDevice(address);

	        		if (mGameService == null) setupGame();
	        		
	                mGameService.connect(device);
				}
	        break;
			case REQUEST_ENABLE_BT:
	           if(btAdp.isEnabled()) {
	               Toast.makeText(MultiplayerActivity.this,"Status: Enabled",Toast.LENGTH_LONG).show();

	               Intent btActStart = new Intent(MultiplayerActivity.this,BtMainActivity.class);
	               startActivityForResult(btActStart, REQUEST_CONNECT_DEVICE);
	           } else {
	        	   Toast.makeText(MultiplayerActivity.this,"Status: Disabled",Toast.LENGTH_LONG).show();
	           }
	        break;
			case RESULT_SPEECH:
				if (resultCode == RESULT_OK && null != data) {
					boolean flag = false;
					ArrayList<String> text = data
							.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					for (int i = 0; i < text.size(); i++) {
						if (text.get(i).equalsIgnoreCase("rock")) {
							user = ROCK;
							sendMessage(Integer.toString(ROCK));
							flag = true;
							break;
						} else if (text.get(i).equalsIgnoreCase("paper")) {
							user = PAPER;
							sendMessage(Integer.toString(PAPER));
							flag = true;
							break;
						} else if (text.get(i).equalsIgnoreCase("scissors")) {
							user = SCISSORS;
							sendMessage(Integer.toString(SCISSORS));
							flag = true;
							break;
						}
						Log.d(TAG, text.get(i));
					}
					if (!flag) {
						t.show(this, "Wrong option, please speak again.");
					}else{
						compute();
					}
				}
				break;
		}
	}
}
