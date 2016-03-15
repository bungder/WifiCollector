package com.gordon.exp.test.as.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.gordon.exp.test.as.domain.Config;
import com.gordon.exp.test.as.domain.Location;
import com.gordon.exp.test.as.util.AndroidFileUtils;
import com.gordon.exp.util.FileUtils;


/**
 * csv file save tools
 * @author Gordon
 * @date 2015-8-2
 *
 */
public class CSVSaver {

	
	static{
		FileUtils.mkdir(new File(Config.dir));
	}
	
	/**
	 * save data to csv format file, file locates at directory CSVSaver.<B><i>dir</i></B>
	 * @param location location that data belongs to
	 * @param content
	 * @throws Exception 
	 */
	@SuppressLint("SimpleDateFormat")
	private static void save(String dir, Location location, String content) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String path = dir+location.getX()+"-"+location.getY()+"_" + sdf.format(new Date()) + ".csv";
		path = FileUtils.loopFileName(FileUtils.getFullFileName(path));
		AndroidFileUtils.saveToSDCard(path, content);
	}

	/**
	 * save training data to csv format file.
	 * @param location
	 * @param content
	 * @throws Exception
	 */
	public static void saveTrainSet(Location location, String content) throws Exception{
		String dir = Config.dir+"train/";
		save(dir, location, content);
	}

	/**
	 * save test data to csv format file.
	 * @param location
	 * @param content
	 * @throws Exception
	 */
	public static void saveTestSet(Location location, String content) throws Exception{
		String dir = Config.dir+"test/";
		save(dir, location, content);
	}
}
