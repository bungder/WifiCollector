package com.gordon.exp.test.as.domain;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Location information, records coordination and wifi signal list
 * @author Gordon
 * @date 2015-8-2
 *
 */
public class LocationPointInfo {

	private Location location;
	private List<Signal> signals ;
	
	public LocationPointInfo(Location location){
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location){
		this.location = location;
	}
	
	public List<Signal> getSignals() {
		return signals;
	}
	public void setSignals(List<Signal> signals){
		this.signals = signals ;
	}
	public void addSignal(Signal signal){
		if(signals == null){
			signals = new LinkedList<Signal>() ;
		}
		signals.add(signal) ;
	}
	/**
	 * add signals in batch
	 * @param signals
	 */
	public void addSignals(Collection<Signal> signals){
		if(this.signals == null){
			this.signals = new LinkedList<Signal>() ;
		}
		this.signals.addAll(signals) ;
	}
	
	/**
	 * convert to csv strings without title line(header)
	 * @return
	 */
	public String[] toCSVLines(){
		StringBuffer buffer = new StringBuffer();
		String[] lines = new String[signals.size()];
		int i = 0;
		for(Signal signal : signals){
			buffer.append(location.getX());buffer.append(",");
			buffer.append(location.getY());buffer.append(",");
			buffer.append(signal.getSsid());buffer.append(",");
			buffer.append(signal.getBsid());buffer.append(",");
			buffer.append(signal.getLevel());buffer.append(",");
			buffer.append(signal.getTimestamp());
			lines[i++] = buffer.toString();
			buffer.delete(0, buffer.length());
		}
		return lines;
	}
	
	/**
	 * get title line of csv file
	 * @return
	 */
	public String getCSVHeader(){
		return "x,y,ssid,bsid,level,timestamp";
	}
	
	/**
	 * convert to csv strings with title line(header)
	 * @return
	 */
	public String toCSVStr(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(getCSVHeader());
		String[] lines = toCSVLines();
		for(String line : lines){
			buffer.append("\n");
			buffer.append(line);
		}
		return buffer.toString();
	}

	
	
}
