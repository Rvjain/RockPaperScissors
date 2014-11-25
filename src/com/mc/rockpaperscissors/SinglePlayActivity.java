package com.mc.rockpaperscissors;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.rockpaperscissors.dataobject.PlayerDetailsBean;
import com.mc.rockpaperscissors.handlers.DBHandler;
import com.mc.rockpaperscissors.util.Constant;
import com.mc.rockpaperscissors.util.ToastUtil;

public class SinglePlayActivity extends Activity implements Constant {

	ImageView img_cpu;
	ImageView img_user;
	TextView tv_result;
	TextView tvresultDisplay;
	Button btn_speak;
	private String TAG = "SinglePlayActivity";
	protected static final int RESULT_SPEECH = 1;
	Random rnd = new Random();
	ToastUtil t = new ToastUtil();
	int user, cpu;
	DBHandler db;
	Intent intent = getIntent();
	int id;
	String username;
	String win;
	String lose;
	String age;
	String sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_single_play);
		img_cpu = (ImageView) findViewById(R.id.iv_cpu);
		img_user = (ImageView) findViewById(R.id.iv_user);
		tv_result = (TextView) findViewById(R.id.tv_result);
		tvresultDisplay = (TextView) findViewById(R.id.tv_resultDisplay);
		btn_speak = (Button) findViewById(R.id.btn_speak);
		intent = getIntent();
		id = intent.getIntExtra("id", -1);
		username = intent.getStringExtra("username");
		win = intent.getStringExtra("win");
		lose = intent.getStringExtra("lose");
		age = intent.getStringExtra("age");
		sex = intent.getStringExtra("sex");
	}

	public void onClickSpeak(View view) {
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				boolean flag = false;
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				for (int i = 0; i < text.size(); i++) {
					if (text.get(i).equalsIgnoreCase("rock")) {
						img_user.setImageResource(R.drawable.rock);
						user = ROCK;
						randomCPU();
						flag = true;
						break;
					} else if (text.get(i).equalsIgnoreCase("paper")) {
						img_user.setImageResource(R.drawable.paper);
						user = PAPER;
						randomCPU();
						flag = true;
						break;
					} else if (text.get(i).equalsIgnoreCase("scissors")) {
						img_user.setImageResource(R.drawable.scissors);
						user = SCISSORS;
						randomCPU();
						flag = true;
						break;
					}
					Log.d(TAG, text.get(i));
				}
				if (!flag) {
					t.show(this, "Wrong option, please speak again.");
				}
			}
			break;
		}

		}
	}

	public void randomCPU() {
		int random = rnd.nextInt((3 - 1) + 1) + 1;
		Log.d(TAG, random+"");
		switch (random) {
		case ROCK:
			img_cpu.setImageResource(R.drawable.rock);
			cpu = ROCK;
			break;
		case PAPER:
			cpu = PAPER;
			img_cpu.setImageResource(R.drawable.paper);
			break;
		case SCISSORS:
			cpu = SCISSORS;
			img_cpu.setImageResource(R.drawable.scissors);
			break;
		default:
			break;
		}
		compute();
		Log.d(TAG, random + "");
	}

	private void compute() {
		if ((user == ROCK && cpu == SCISSORS) || (user == PAPER && cpu == ROCK)
				|| (user == SCISSORS && cpu == PAPER)) {
			tv_result.setText("You Win!!!");
			db = new DBHandler(this);
			//(this, win+"");
			
			PlayerDetailsBean player = db.getPlayer(id);
			
			String tmp = String.valueOf((Integer.parseInt(player.getWin()) + 1));
			player.setWin(tmp);
			player.setLose(player.getLose());
			player.setId(id);
			if(db.updateContact(player)>0){
				displayResult();
			}
			else{
				t.show(this,"error in update");
			}

		} else if ((cpu == ROCK && user == SCISSORS)
				|| (cpu == PAPER && user == ROCK)
				|| (cpu == SCISSORS && user == PAPER)) {
			tv_result.setText("You Lose");
			db = new DBHandler(this);
			PlayerDetailsBean player = db.getPlayer(id);
			player.setWin(player.getWin());
			String tmp = String.valueOf((Integer.parseInt(player.getLose()) + 1));
			player.setLose(tmp);
			player.setId(id);
			if(db.updateContact(player)>0){
				displayResult();
			}
			else{
				t.show(this,"error in update");
			}
		} else {
			tv_result.setText("Its a Draw");
			displayResult();
		}
	}

	public void displayResult() {
		db = new DBHandler(this);
		PlayerDetailsBean player = db.getPlayer(id);
		int win = Integer.parseInt(player.getWin());
		int lose = Integer.parseInt(player.getLose());
		//int total = (Integer.parseInt(player.getLose()) +Integer.parseInt( player.getWin()));
		tvresultDisplay.setText("Total win is " + win + " and "
				+ "Total lose is " + lose );
	}
}
