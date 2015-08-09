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
	 * @return
	 */
	public List<Signal> getSignals(){
		List<Signal> signals = new LinkedList<Signal>() ;
//		StringBuilder sb = new StringBuilder();
		for(ScanResult sr : scanResults){
			Signal signal = new Signal() ;
			signal.setSsid(sr.SSID) ;
			signal.setBsid(sr.BSSID) ;
			signal.setLevel(sr.level) ;
			signal.setTimestamp(sr.timestamp) ;
			signals.add(signal) ;
			/*sb.append("ssid:").append(sr.SSID).append("\n");
			sb.append("bssid:").append(sr.BSSID).append("\n");
			sb.append("level:").append(sr.level).append("\n");
			sb.append("timestamp:").append(sr.timestamp).append("\n");
			sb.append("===========\n");*/
		}
		return signals ;
	}
	
}
