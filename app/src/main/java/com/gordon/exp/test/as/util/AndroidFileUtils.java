package com.gordon.exp.test.as.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

import com.gordon.exp.test.as.domain.Config;
import com.gordon.exp.util.FileUtils;

/**
 * @author Gordon
 */
@SuppressLint("DefaultLocale")
public class AndroidFileUtils {
	
	public static void saveToSDCard(String filename, String content) throws Exception{
		File file = new File(new File(Config.dir), filename) ;
		FileUtils.mkdir(file.getParentFile()) ;
		if(!file.exists())
			file.createNewFile() ;
		FileOutputStream out = new FileOutputStream(file) ;
		out.write(content.getBytes()) ;
		out.close() ;
	}

}
