package com.gordon.exp.test.as.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gordon.exp.test.as.R;
import com.gordon.exp.test.as.activity.MainActivity;
import com.gordon.exp.test.as.domain.Config;
import com.gordon.exp.test.as.domain.Location;
import com.gordon.exp.test.as.domain.LocationPointInfo;

import java.io.IOException;


/**
 * Coordinate wifi signal info recorder
 * @author Gordon
 * @date 2015-8-2
 *
 */
public class LocationRecorder extends Thread{

	Activity activity;
	WifiSignalProcessor wifi;
	private Location location;
	private MainActivity.CollectHandler handler;
	/** if it is collecting data */
	private volatile boolean collecting = false;
	
	/**
	 * 
	 * @param wifi wifi signal manager
	 * @param location coordinate to be recorded
	 * @param handler
	 */
	public LocationRecorder(Activity activity, WifiSignalProcessor wifi, Location location, MainActivity.CollectHandler handler){
		this.activity = activity;
		this.wifi = wifi;
		this.location = location;
		this.handler = handler;
	}
	
	/**
	 * start collecting data asynchronously
	 */
	public void collect(){
		this.start();
	}
	
	@SuppressLint("ShowToast")
	public void run(){
		synchronized(this){
			if(collecting){
				Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.info_already_collecting), Toast.LENGTH_LONG).show();
				return;
			}
			collecting = true;
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					if(handler != null){
						handler.setWIFIToggleEnable(false);
						handler.disableViewComponents();
					}
				}

			});
			wifi.openWifi();
			wifi.startScan();
			int frequency = Config.getFrequency();
			int totalLength = Config.getTotalLength();
			int totalTrainLength = Config.getTotalTrainLength();
			int totalTestLength = Config.getTotalTestLength();
			final long duration = (long) ((((double)totalLength)/frequency)*1000);
			LocationPointInfo trainSet =  new LocationPointInfo(location);
			LocationPointInfo testSet =  new LocationPointInfo(location);
			int gap = 1000/frequency;
			final long t1 = System.currentTimeMillis();
			int index = 1;
			while(t1+duration > System.currentTimeMillis()){
				if(index <= totalTrainLength) {
					trainSet.addSignals(wifi.getSignals(index++));
				}else{
					testSet.addSignals(wifi.getSignals(index++ - totalTrainLength));
				}
				wifi.startScan();
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						if(handler != null){
							String info = String.format(activity.getString(R.string.btn_collecting), ((t1+duration-System.currentTimeMillis()+999)/1000));
							handler.setCollectButtonText(info);
						}
					}
				});
				try {
					Thread.sleep(gap);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Data collected, write to file
			String trainContent = trainSet.toCSVStr();
			String testContent = testSet.toCSVStr();
			try {
				CSVSaver.saveTrainSet(trainSet.getLocation(), trainContent);
				CSVSaver.saveTestSet(testSet.getLocation(), testContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Finish collecting data, hint user
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					// display voice
					AudioManager audioService = (AudioManager) activity
							.getSystemService(Context.AUDIO_SERVICE);
					boolean shouldPlayBeep = true;
					if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
						shouldPlayBeep = false;
					}
					if(shouldPlayBeep) {
						Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Ringtone r = RingtoneManager.getRingtone(activity.getApplicationContext(), notification);
						r.play();
					}


					// make text
					Toast.makeText(activity, activity.getString(R.string.info_collected),
							Toast.LENGTH_LONG).show();
					if(handler != null){
						handler.setWIFIToggleEnable(true);
						handler.resetViewComponents();
					}
				}

			});
			
			collecting = false;
		}
	}

}
