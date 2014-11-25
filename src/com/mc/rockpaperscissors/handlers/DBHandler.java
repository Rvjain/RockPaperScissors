package com.mc.rockpaperscissors.handlers;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.mc.rockpaperscissors.dataobject.PlayerDetailsBean;

public class DBHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 8;
	private static final String DATABASE_NAME = "rockPaperScissors";
	private static final String TABLE_PLAYER = "playerDetails";

	private static final String KEY_ID = "p_id";
	private static final String KEY_USERNAME = "username";
	// private static final String KEY_PASSWORD = "password";
	private static final String KEY_WIN = "win";
	private static final String KEY_LOSE = "lose";
	private static final String KEY_AGE = "age";
	private static final String KEY_SEX = "sex";
	private static String TAG = "DBHandler";

	public DBHandler(Context context) {
		 //super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Created");
		String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ KEY_USERNAME + " TEXT UNIQUE," + KEY_WIN + " TEXT,"
				+ KEY_LOSE + " TEXT," + KEY_AGE + " TEXT," + KEY_SEX
				+ " TEXT )";
		db.execSQL(CREATE_PLAYER_TABLE);
		Log.d(TAG, "Created###");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Updates");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
		onCreate(db);
		Log.d(TAG, "Updated###");
	}

	public boolean addPlayer(PlayerDetailsBean player) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, player.getUsername());
		// values.put(KEY_PASSWORD, player.getPassword());
		values.put(KEY_WIN, player.getWin());
		values.put(KEY_LOSE, player.getLose());
		values.put(KEY_AGE, player.getAge());
		values.put(KEY_SEX, player.getGender());
		Log.d(TAG, "Added");
		// Inserting Row
		long rowid = db.insert(TABLE_PLAYER, null, values);
		db.close();
		if (rowid != -1) {
			Log.d(TAG, "Added");
			return true;
		} else {
			return false;
		}

		// Closing database connection
	}

	public PlayerDetailsBean getPlayer(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PLAYER, new String[] { KEY_ID,
				KEY_USERNAME, KEY_WIN, KEY_LOSE, KEY_AGE, KEY_SEX }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		PlayerDetailsBean player = new PlayerDetailsBean();
		player.setId(Integer.parseInt(cursor.getString(0)));
		player.setUsername(cursor.getString(1));
		player.setWin(cursor.getString(2));
		player.setLose(cursor.getString(3));
		player.setAge(cursor.getString(4));
		player.setGender(cursor.getString(5));
		cursor.close();
		return player;
	}

	public PlayerDetailsBean login(String username) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PLAYER, new String[] { KEY_ID,
				KEY_USERNAME, KEY_WIN, KEY_LOSE, KEY_AGE, KEY_SEX },
				KEY_USERNAME + "=?", new String[] { String.valueOf(username) },
				null, null, null, null);
		// Log.d(TAG, "$$$$$"+cursor.getString(0));
		if (cursor != null && cursor.moveToFirst()) {
			PlayerDetailsBean player = new PlayerDetailsBean();
			player.setId(Integer.parseInt(cursor.getString(0)));
			player.setUsername(cursor.getString(1));
			player.setWin(cursor.getString(2));
			player.setLose(cursor.getString(3));
			player.setAge(cursor.getString(4));
			player.setGender(cursor.getString(5));
			cursor.close();
			return player;
		} else {
			return null;
		}
	}

	public int getPlayerCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PLAYER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	public int updateContact(PlayerDetailsBean player) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WIN, player.getWin());
		values.put(KEY_LOSE, player.getLose());

		// updating row
		return db.update(TABLE_PLAYER, values, KEY_ID + " = ?",
				new String[] { String.valueOf(player.getId()) });

	}

}
