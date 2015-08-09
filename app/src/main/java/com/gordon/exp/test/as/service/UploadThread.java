package com.gordon.exp.test.as.service;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gordon.exp.net.ftp.FTPClientUtils;
import com.gordon.exp.net.ftp.FTPOpResult;
import com.gordon.exp.net.ssh.SFTPClientUtils;
import com.gordon.exp.net.ssh.SFTPOpResult;
import com.gordon.exp.test.as.R;
import com.gordon.exp.test.as.domain.Config;
import com.gordon.exp.util.FileUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Thread to upload data
 * 
 * @author Gordon
 * @date 2015-8-5
 * 
 */
public class UploadThread extends Thread {

	public static final int UPLOAD_DONE = 999800;
	public static final int UPLOAD_SUCCESS = 999801;
	public static final int UPLOAD_FAIL = 999802;
	public static final int LOGIN_FAIL = 999803;
	public static final int CONNECT_FAIL = 999804;
	private Activity activity;

	public UploadThread(Activity activity) {
		this.activity = activity;
	}

	public void run() {
		String dir = Config.dir;
		String remoteDir = "wifi/";
		Map<String, String> pathMaps = new LinkedHashMap<String, String>();
		File[] files = new File(dir).listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				continue;
			}
			if ("csv".equalsIgnoreCase(FileUtils.getSuffix(f.getName()))) {
				pathMaps.put(f.getAbsolutePath(), remoteDir + f.getName());
			}
		}
		System.out.println(String.format(Config.getProtocol() + " %s:%s@%s:%d", Config.getUsername(), Config.getPasswd(), Config.getFtpAddress("127.0.0.1"), 
				Config.getFtpPort(21)));
		switch (Config.getProtocol()) {
		case FTP:
			ftpUpload(pathMaps);
			break;
		case SFTP:
			sftpUpload(pathMaps);
			break;
		}
	}

	/**
	 * Use ftp to upload
	 * 
	 * @param pathMaps
	 */
	private void ftpUpload(Map<String, String> pathMaps) {
		FTPOpResult[] results = FTPClientUtils.upload(
				Config.getFtpAddress("127.0.0.1"), Config.getFtpPort(21),
				Config.getUsername(), Config.getPasswd(), pathMaps);
		final String message;
		if (results == null || results.length == 0) {
			message = activity.getString(R.string.info_no_local_data);
//			message = "本地还没有数据，请收集数据之后再上传";
		} else {
			switch (results[0]) {
			case CONNECT_FAIL:
				message = activity.getString(R.string.info_ftp_connect_fail);
				break;
			case LOGIN_FAIL:
				message = activity.getString(R.string.info_ftp_login_fail);
				break;
			case UPLOAD_FAIL:
				message = activity.getString(R.string.info_ftp_upload_fail);
				break;
			case UPLOAD_SUCCESS:
				message = activity.getString(R.string.info_ftp_upload_success);
				break;
			default:
				message = activity.getString(R.string.info_ftp_unknown_result) + results[0];
			}
		}
		Log.d("UPLOAD", "Upload Successfully！！");
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
			}

		});
		System.out.println("Upload done!");
	}

	/**
	 * use sftp to upload
	 * 
	 * @param pathMaps
	 */
	private void sftpUpload(Map<String, String> pathMaps) {
		SFTPOpResult[] results = SFTPClientUtils.upload(
				Config.getFtpAddress("127.0.0.1"), Config.getFtpPort(21),
				Config.getUsername(), Config.getPasswd(), pathMaps);
		final String message;
		if (results == null || results.length == 0) {
			message = activity.getString(R.string.info_no_local_data);
		} else {
			switch (results[0]) {
				case CONNECT_FAIL:
					message = activity.getString(R.string.info_sftp_connect_fail);
					break;
				case LOGIN_FAIL:
					message = activity.getString(R.string.info_sftp_login_fail);
					break;
				case UPLOAD_FAIL:
					message = activity.getString(R.string.info_sftp_upload_fail);
					break;
				case UPLOAD_SUCCESS:
					message = activity.getString(R.string.info_sftp_upload_success);
					break;
				default:
					message = activity.getString(R.string.info_sftp_unknown_result) + results[0];
			}
		}
		Log.d("UPLOAD", "Upload successfully!!");
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
			}

		});
		System.out.println("Upload done!");
	}
}
