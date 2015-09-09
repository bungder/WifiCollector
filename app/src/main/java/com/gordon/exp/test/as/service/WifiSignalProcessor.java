package com.gordon.exp.test.as.service;

import java.util.LinkedList;
import java.util.List;

import android.annotation.TargetApi;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.gordon.exp.test.as.domain.Signal;


/**
 * Prime logic about WIFI signal processing<br><br>
 * 
 * @author Gordon
 * @date 2015-8-2
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class WifiSignalProcessor {

	private WifiManager wifiManager ;
	private List<ScanResult> scanResults;
	
	
	public WifiSignalProcessor(WifiManager wifiManager){
		this.wifiManager = wifiManager ;
	}
	
	/**
	 * check for wifi switch, if WLAN is off, then turn it on
	 */
	public void openWifi() {
		if(!wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(true);
		}
	}
	
	/**
	 * if WLAN is on
	 * @return
	 */
	public boolean isWifiOn(){
		return wifiManager.isWifiEnabled();
	}
	
	/**
	 * turn off WLAN
	 */
	public void closeWifi(){
		if(wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(false) ;
	}
	
	/**
	 * start scanning WiFi signal
	 */
	public void startScan() {
		openWifi();
		wifiManager.startScan();
		scanResults = wifiManager.getScanResults();
	}
	
	/**
	 * get current wifi signal information list
	 * @param count remark that denotes in which loop these records are recorded. <br/>
	 *              It makes it easier to depart train set and test set.
	 * @return
	 */
	public List<Signal> getSignals(int count){
		List<Signal> signals = new LinkedList<Signal>() ;
		for(ScanResult sr : scanResults){
			Signal signal = new Signal() ;
			signal.setSsid(sr.SSID) ;
			signal.setBsid(sr.BSSID) ;
			signal.setLevel(sr.level) ;
			signal.setTimestamp(sr.timestamp) ;
			signal.setCount(count);
			signals.add(signal) ;
		}
		return signals ;
	}
	
}
