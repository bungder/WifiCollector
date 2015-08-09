package com.gordon.exp.test.as.service;

import com.gordon.exp.test.as.domain.Config;
import com.gordon.exp.util.FileUtils;

import java.io.File;


/**
 * Local file manager
 * @author Gordon
 * @date 2015-8-5
 *
 */
public class LocalFileMgr {

	/**
	 * delete local csv files
	 */
	public void delete(){
		String dir = Config.dir;
		File[] files = new File(dir).listFiles();
		for(File f : files){
			if(f.isDirectory()){
				continue;
			}
			if("csv".equalsIgnoreCase(FileUtils.getSuffix(f.getName()))){
				f.delete();
			}
		}
	}
}
