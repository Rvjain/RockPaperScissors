package com.mc.rockpaperscissors;

import com.mc.rockpaperscissors.dataobject.PlayerDetailsBean;
import com.mc.rockpaperscissors.handlers.DBHandler;
import com.mc.rockpaperscissors.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	EditText etusername;
	EditText etpassword;
	Button btnsubmit;
	Button btnregister;
	ToastUtil t = new ToastUtil();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		etusername = (EditText) findViewById(R.id.et_username);
		//etpassword = (EditText) findViewById(R.id.et_password);
		btnsubmit = (Button) findViewById(R.id.btn_login);
		btnregister = (Button) findViewById(R.id.btn_register);
	}

	public void onClickLogin(View view) {
		if(!etusername.getText().toString().trim().isEmpty()){
			DBHandler db = new DBHandler(this);
			 PlayerDetailsBean player = db.login(etusername.getText().toString());
			 if(player!=null){
				 Intent intent = new Intent(this, MenuActivity.class);
				 intent.putExtra("id", player.getId());
				 intent.putExtra("username", player.getUsername());
				 intent.putExtra("win", player.getWin());
				 intent.putExtra("lose", player.getLose());
				 intent.putExtra("age", player.getAge());
				 intent.putExtra("sex", player.getGender());
				 Log.d("id", player.getId()+"");
				 Log.d("win", player.getWin()+"");
				 Log.d("lose", player.getLose()+"");
				 Log.d("username", player.getUsername()+"");
				 Log.d("age", player.getAge()+"");
				 Log.d("sex", player.getGender()+"");
				 startActivity(intent);
				 finish();
			 }else{
				 t.show(this, "Error in getting data please register first");
			 }
		}else{
			t.show(this, "Please fill the username.");
		}
		

		//int count = db.getPlayerCount();
		//Log.d("LoginActivity", count + "");
	}

	public void onClickRegister(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		
	}
	public void onClickOption(View view) {
		Intent intent = new Intent(this, OptionActivity.class);
		startActivity(intent);
		
	}

}
