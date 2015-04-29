package com.boyumi.yumitianqi;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Cron {
	
	private int grownValue;
	private int waterValue;
	private int healthyValue;
	private int[] cronImage = new int[]{
			R.drawable.cron_bug,
			R.drawable.cron_waterless,
			R.drawable.cron,	
	};
	private int weather ;
	private View view;
	
	
	public Cron(YahooWeather w,View passview){
		grownValue = 10;
		waterValue = 40;
		healthyValue = 10;
		weather = w.getWiNum();
		view = passview;
	}
	
	public void stauteChange(int g, int w, int h){
		healthyValue += h;	
		if(healthyValue <= 1){}
		else{
			grownValue += g;
			}
		waterValue += w;
		grownValue = juge(200,grownValue);
		waterValue = juge(50,waterValue);
		healthyValue = juge(10,healthyValue);
	}
	
	public int juge(int max, int tempCompare){
		if (tempCompare > max)return max;
		if (tempCompare < 0)return 0;
		return tempCompare;
	}
	
	public int getCronIconNum(){
		if (healthyValue == 0)return 0;//bug
		if (waterValue < 30)return 1;//less water
		return 2;//normal
	}
	
	public void weatherChange(int statue){
		switch(statue){
		case 0:this.stauteChange(3,-1,1);
		case 1:this.stauteChange(2,-1,0);
		case 2:this.stauteChange(0,0,0);
		case 3:this.stauteChange(2,0,1);
		case 4:this.stauteChange(1,0,0);
		case 5:this.stauteChange(-3,-1,-3);
		}
	}
	
	public void showcron() {
		ImageView c = (ImageView) view.findViewById(R.id.cron);
		// c.setImageResource(a.cronImage[a.getCronIconNum()]);
		c.setImageBitmap(HandleImage(view.getContext()));
		ProgressBar gv = (ProgressBar) view.findViewById(R.id.progressBar_gv);
		gv.setProgress(this.grownValue);
		ProgressBar wv = (ProgressBar) view.findViewById(R.id.progressBar_wv);
		wv.setProgress(this.waterValue);
		ProgressBar hv = (ProgressBar) view.findViewById(R.id.progressBar_hv);
		hv.setProgress(this.healthyValue);
	}

	public void bugsComing(View view) {
		int p = new Random().nextInt(11);//10% of get a bug on cron
		if (p < 1)
			this.stauteChange(0, -3, -10);
		weatherChange(weather);
		showcron();
	}

	public Bitmap HandleImage(Context context) {
		Resources res = context.getResources();
		Bitmap bitmap = BitmapFactory.decodeResource(res,
				this.cronImage[this.getCronIconNum()]);
		Matrix matrix = new Matrix();
		float scale = (float) (40 + this.grownValue * 0.3) / 100;
		matrix.setScale(scale, scale);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}
	
	
}
