package com.boyumi.yumitianqi;




public class Yumi {
	
	int grownValue;
	int waterValue;
	int healthyValue;
	int[] cronImage = new int[]{
			R.drawable.cron_bug,
			R.drawable.cron_waterless,
			R.drawable.cron,	
	};
	
	
	public Yumi(){
		grownValue = 10;
		waterValue = 40;
		healthyValue = 10;
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
	
}
