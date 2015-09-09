package com.gordon.exp.test.as.domain;

/**
 * wifi signal
 * @author Gordon
 * @date 2015-8-2
 *
 */
public class Signal {

	private String ssid ;
	private String bsid ;
	private int level ;
	private long timestamp ;
	/**remark that denotes in which loop these records are recorded. <br/>
	 * It makes it easier to depart train set and test set.*/
	private int count;

	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getBsid() {
		return bsid;
	}
	public void setBsid(String bsid) {
		this.bsid = bsid;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 *
	 * @return remark that denotes in which loop these records are recorded. <br/>
	 * It makes it easier to depart train set and test set.
	 */
	public int getCount(){
		return count;
	}

	/**
	 *
	 * @param count remark that denotes in which loop these records are recorded. <br/>
	 * It makes it easier to depart train set and test set.
	 */
	public void setCount(int count){
		this.count = count;
	}
	@Override
	public String toString() {
		return "Signal [ssid=" + ssid + ", bsid=" + bsid + ", level=" + level
				+ ", timestamp=" + timestamp + ", count=" + count + "]";
	}
}
