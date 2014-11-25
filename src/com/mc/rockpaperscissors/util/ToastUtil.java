package com.mc.rockpaperscissors.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public void show(Context context,String text){
		
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		
	}
	
}
