package com.gordon.exp.test.as.domain;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Xml;

import com.gordon.exp.test.as.util.AndroidFileUtils;

import com.gordon.exp.util.FileUtils;
import com.gordon.exp.util.Utils;

/**
 * Parameter Config
 * @author Gordon
 * @date 2015-8-2
 *
 */
public class Config {


	public static enum TransferProtocol{
		FTP,
		SFTP
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	/** directory to save data file */
	public static final String dir = Environment.getExternalStorageDirectory().getPath()+"/002wifi/";
	public static final String dir_train = dir + "train/";
	public static final String dir_test = dir + "test/";

	public static String datasetPrefix = sdf.format(new Date());
	private static String hostAddress = "0.0.0.0" ;

	private static int port = 220 ;
	/** collect times per second */
	private static int frequency = 1 ;
	/** collect times per point */
	private static int totalLength = 110;

	private static int totalTrainLength = 100;
	private static int totalTestLength = 10;
	/** server address */
	private static String ftpAddress;
	/** server port */
	private static int ftpPort;
	/** username to login to server */
	private static String username;
	/** user password to login to server */
	private static String passwd;
	private static TransferProtocol protocol = TransferProtocol.SFTP;
	
	public static final String filePath = Environment.getExternalStorageDirectory().getPath()+"/001Gordon/config.xml" ; 
	
	/*public static void init(Map<String, Object> map){
		hostAddress = (String)map.get("hostAddress") ;
		port = (Integer)map.get("port") ;
	}*/
	
	static{
		System.out.println("init Config");
		init() ;
	}
	
	public static void setHostAddress(String ip){
		hostAddress = ip ;
	}
	public static void setPort(int p){
		port = p ;
	}
	
	public static String getHostAddress(String defaultValue){
		System.out.println("host : " + hostAddress);
		if(Utils.isEmpty(hostAddress)){
			return defaultValue ;
		}else{
			return hostAddress ;
		}
	}
	
	public static int getPort(int defaultValue){
		System.out.println("port : " + port);
		if(port < 1)
			return defaultValue ;
		else 
			return port ;
	}
	
	/**
	 * collect times per second
	 * @return
	 */
	public static int getFrequency() {
		return frequency;
	}
	/**
	 * setup parameter that indicates collect times per second
	 * @param frequency collect times per second
	 */
	public static void setFrequency(int frequency) {
		Config.frequency = frequency;
	}

	
	/**
	 * collect times per point
	 * @return
	 */
	public static int getTotalLength() {
		return totalLength;
	}

	/**
	 * setup parameter that indicates collect times per point
	 * @param totalLength collect times per point
	 */
	public static void setTotalLength(int totalLength) {
		Config.totalLength = totalLength;
	}

	/**
	 * server address
	 * @param defaultString if config str is empty, returns defaultString
	 * @return
	 */
	public static String getFtpAddress(String defaultString) {
		if(Utils.isEmpty(ftpAddress)){
			return defaultString;
		}
		return ftpAddress;
	}

	/**
	 * setup parameter that indicates server address
	 * @param ftpAddress
	 */
	public static void setFtpAddress(String ftpAddress) {
		Config.ftpAddress = ftpAddress;
	}

	/**
	 * server port
	 * @param defaultPort if config integer equals to 0, return defaultPort
	 * @return
	 */
	public static int getFtpPort(int defaultPort) {
		if(ftpPort == 0){
			return defaultPort;
		}
		return ftpPort;
	}

	/**
	 * setup parameter that indicates server port
	 * @param ftpPort
	 */
	public static void setFtpPort(int ftpPort) {
		Config.ftpPort = ftpPort;
	}

	/** username to login to server */
	public static String getUsername() {
		return username;
	}

	/** setup parameter that indicates username to login to server */
	public static void setUsername(String username) {
		Config.username = username;
	}

	/** user password to login to server */
	public static String getPasswd() {
		return passwd;
	}

	/** setup parameter that indicates user password to login to server
	 * @param passwd user password to login to server */
	public static void setPasswd(String passwd) {
		Config.passwd = passwd;
	}
	
	public static TransferProtocol getProtocol() {
		return protocol;
	}
	public static void setProtocol(TransferProtocol protocol) {
		Config.protocol = protocol;
	}

	public static int getTotalTrainLength() {
		return totalTrainLength;
	}

	public static void setTotalTrainLength(int totalTrainLength) {
		Config.totalTrainLength = totalTrainLength;
	}

	public static int getTotalTestLength() {
		return totalTestLength;
	}

	public static void setTotalTestLength(int totalTestLength) {
		Config.totalTestLength = totalTestLength;
	}

	public static String getDatasetPrefix() {
		return datasetPrefix;
	}

	public static void setDatasetPrefix(String datasetPrefix) {
		Config.datasetPrefix = datasetPrefix;
	}

	/**
	 * save parameter configuration
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 */
	public static void save() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, IOException{
//		XmlPullParser parser = Xml.newPullParser() ;
		XmlSerializer serializer = Xml.newSerializer() ;
		File f = new File(filePath) ;
		if(!f.exists()){
			FileUtils.mkdir(f.getParentFile()) ;
			f.createNewFile() ;
		}
		OutputStream out = new FileOutputStream(f, false) ;
		serializer.setOutput(out, "UTF-8") ;
		serializer.startDocument("UTF-8", true) ;
		serializer.startTag(null, "config") ;
//		serializer.startTag(null, "ip") ;
		serializer.startTag(null, "ip") ;
		serializer.text(hostAddress) ;
		serializer.endTag(null, "ip") ;
		serializer.startTag(null, "port") ;
		serializer.text(""+port) ;
		serializer.endTag(null, "port") ;

		serializer.startTag(null, "datasetPrefix") ;
		serializer.text(datasetPrefix) ;
		serializer.endTag(null, "datasetPrefix") ;


		serializer.startTag(null, "ftpAddress") ;
		serializer.text(""+ftpAddress) ;
		serializer.endTag(null, "ftpAddress") ;
		serializer.startTag(null, "ftpPort") ;
		serializer.text(""+ftpPort) ;
		serializer.endTag(null, "ftpPort") ;

		serializer.startTag(null, "username") ;
		serializer.text(""+username) ;
		serializer.endTag(null, "username") ;
		serializer.startTag(null, "passwd") ;
		serializer.text(""+passwd) ;
		serializer.endTag(null, "passwd") ;
		serializer.startTag(null, "protocol") ;
		serializer.text(""+protocol.ordinal()) ;
		serializer.endTag(null, "protocol") ;
		
		serializer.startTag(null, "frequency") ;
		serializer.text(""+frequency) ;
		serializer.endTag(null, "frequency") ;
		serializer.startTag(null, "totalLength") ;
		serializer.text(""+totalLength) ;
		serializer.endTag(null, "totalLength") ;


		serializer.startTag(null, "totalTrainLength") ;
		serializer.text(""+totalTrainLength) ;
		serializer.endTag(null, "totalTrainLength") ;

		serializer.startTag(null, "totalTestLength") ;
		serializer.text(""+totalTestLength) ;
		serializer.endTag(null, "totalTestLength") ;
//		serializer.attribute(null, "ip", hostAddress) ;
//		serializer.attribute(null, "port", ""+port) ;
//		serializer.endTag(null, "ip") ;
		serializer.endTag(null, "config") ;
		serializer.endDocument() ;
		out.flush() ;
		out.close() ;
	}
	
	public static void doSth(){
		System.out.println("DoSomething");
	}
	
	/**
	 * initialize parameter, read data from file
	 */
	private static void init(){
		XmlPullParser parser = Xml.newPullParser() ;
		InputStream input = null ;
		try {
			System.out.println("1");
			input = new BufferedInputStream(new FileInputStream(filePath)) ;
			System.out.println("2");
			parser.setInput(input, "UTF-8") ;
			System.out.println("3");
			int event = parser.getEventType() ;
			System.out.println("4");
			while(event != XmlPullParser.END_DOCUMENT){
				System.out.println("==");
				switch(event){
				case XmlPullParser.START_DOCUMENT :
					break ;
				case XmlPullParser.START_TAG :
					if("ip".equalsIgnoreCase(parser.getName())){
						hostAddress = parser.nextText() ;
					}else if("port".equalsIgnoreCase(parser.getName())){
						port = Integer.parseInt(parser.nextText()) ;
					}else if("frequency".equalsIgnoreCase(parser.getName())){
						frequency = Integer.parseInt(parser.nextText()) ;
					}else if("totalLength".equalsIgnoreCase(parser.getName())){
						totalLength = Integer.parseInt(parser.nextText());
					}else if("totalTrainLength".equalsIgnoreCase(parser.getName())){
						totalTrainLength = Integer.parseInt(parser.nextText());
					}else if("totalTestLength".equalsIgnoreCase(parser.getName())){
						totalTestLength = Integer.parseInt(parser.nextText());
					}else if("ftpAddress".equalsIgnoreCase(parser.getName())){
						ftpAddress = parser.nextText();
					}else if("ftpPort".equalsIgnoreCase(parser.getName())){
						ftpPort = Integer.parseInt(parser.nextText());
					}else if("username".equalsIgnoreCase(parser.getName())){
						username = parser.nextText();
					}else if("passwd".equalsIgnoreCase(parser.getName())){
						passwd = parser.nextText();
					}else if("protocol".equalsIgnoreCase(parser.getName())){
						protocol = TransferProtocol.values()[Integer.parseInt(parser.nextText())];
					}else if("datasetPrefix".equalsIgnoreCase(parser.getName())){
						datasetPrefix = parser.nextText();
					}
					break ;
				case XmlPullParser.END_TAG :
					break ;
				}
				event = parser.next() ;
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(input != null)
					input.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
