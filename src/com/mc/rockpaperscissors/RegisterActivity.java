package com.mc.rockpaperscissors;

import com.mc.rockpaperscissors.dataobject.PlayerDetailsBean;
import com.mc.rockpaperscissors.handlers.DBHandler;
import com.mc.rockpaperscissors.util.Constant;
import com.mc.rockpaperscissors.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;

public class RegisterActivity extends Activity implements Constant {

	EditText etusername;
	// EditText etpassword;
	Button btnregister;
	String sex;
	EditText age;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		etusername = (EditText) findViewById(R.id.et_register_username);
		// etpassword = (EditText) findViewById(R.id.et_register_password);
		btnregister = (Button) findViewById(R.id.btn_register_register);
		age = (EditText) findViewById(R.id.et_age);
	}

	public void onClickRegister(View view) {
		if (!etusername.getText().toString().trim().isEmpty()
				&& !age.getText().toString().trim().isEmpty()
				&& sex!=null) {
			PlayerDetailsBean player = new PlayerDetailsBean();
			player.setUsername(etusername.getText().toString());
			// player.setPassword(etpassword.getText().toString());
			player.setAge(age.getText().toString());
			player.setGender(sex);
			player.setWin("0");
			player.setLose("0");
			DBHandler db = new DBHandler(this);
			ToastUtil t = new ToastUtil();
			if (db.addPlayer(player)) {
				t.show(this, "Added");
				finish();
			} else {
				t.show(this, "Username already exit please use another name.");
			}
		}else{
			ToastUtil t = new ToastUtil();
			t.show(this, "Please fill all the above fields.");
		}
		
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.radio_female:
			if (checked)
				sex = FEMALE;
			break;
		case R.id.radio_male:
			if (checked)
				sex = MALE;
			break;
		}
	}

}
