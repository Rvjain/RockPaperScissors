package com.mc.rockpaperscissors;

import com.mc.rockpaperscissors.util.ToastUtil;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity {
	Button singlePlayer;
	Button multiPlayer;
	Button optionMenu;
	ToastUtil toast = new ToastUtil();
	Intent intent;
	Intent intent1;
	int id;
	String username;
	String win;
	String lose;
	String age;
	String sex;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT      = 2;
	BluetoothAdapter btAdp = BluetoothAdapter.getDefaultAdapter();
	static BluetoothDevice deviceShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		singlePlayer = (Button) findViewById(R.id.btn_single);
		multiPlayer = (Button) findViewById(R.id.btn_multi);
		optionMenu = (Button) findViewById(R.id.btn_otpion);
		intent1 = getIntent();
		Log.d("%%%%", intent1 + "");
		id = intent1.getIntExtra("id", -1);
		username = intent1.getStringExtra("username");
		win = intent1.getStringExtra("win");
		lose = intent1.getStringExtra("lose");
		age = intent1.getStringExtra("age");
		sex = intent1.getStringExtra("sex");
	}

	public void onClickSinglePlay(View view) {
		// toast.show(this, "Single Play");
		intent = new Intent(this, SinglePlayActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("username", username);
		intent.putExtra("win", win);
		intent.putExtra("lose", lose);
		intent.putExtra("age", age);
		intent.putExtra("sex", sex);
		startActivity(intent);
	}

	public void onClickMultiPlay(View view) {
		//toast.show(this, "Multi Play");
		intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
	}

	public void onClickOption(View view) {
		intent = new Intent(this, OptionActivity.class);
		startActivity(intent);

	}
	
	


}
