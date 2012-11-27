package org.hacklace.animator;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;


public class IniConf {
	
	private final String configPath = "/animatorconf.ini";
	private HierarchicalINIConfiguration conf;
	
	public IniConf() {		
		URL url = this.getClass().getResource(configPath);
		try {			
			conf = new HierarchicalINIConfiguration(url);
		} catch (ConfigurationException e) {
			System.err.println("Loading the configuration failed: " + url);
			System.exit(1);
		}
	}
	
	public int rows() {
		return conf.getInt("grid.rows");
	}
	
	public int columns() {
		return conf.getInt("grid.columns");
	}
	
	public int displayWidth() {
		return conf.getInt("display.width");
	}
	
	public int displayHeight() {
		return conf.getInt("display.height");
	}
	
	public String device() {
		return conf.getString("flash.device");
	}
	
	// TODO: cache?
	public List<Integer> speedList() {		
		List<Object> data = conf.getList("animation.speed");
		List<Integer> speedList = new LinkedList<Integer>();
		for(Object o : data) {
			speedList.add( Integer.parseInt( ((String) o).trim()));
		}
		
		return Collections.unmodifiableList(speedList);
	}
	
	// TODO: cache?
	public List<Integer> delayList() {		
		List<Object> data = conf.getList("animation.delay");
		List<Integer> delayList = new LinkedList<Integer>();
		for(Object o : data) {
			delayList.add( Integer.parseInt( ((String) o).trim()));
		}
		
		return Collections.unmodifiableList(delayList);
	}
	
	public static void main(String[] args) {
		IniConf conf = new IniConf();
		System.out.println( conf.rows());
		System.out.println( conf.columns());
		System.out.println( conf.speedList());
	}
	
	
}
