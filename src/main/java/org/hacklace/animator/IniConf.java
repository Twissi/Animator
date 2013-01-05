package org.hacklace.animator;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

public class IniConf {

	private static String configPath = "/animatorconf.ini";
	private HierarchicalINIConfiguration conf;
	private static IniConf instance;
	
	public final static String separators = " ,;.:/_|";
	public final static String separatorsRegEx = "["+separators+"]";
	
	public static boolean isSeparator(char c) {
		return separators.indexOf(c) != -1;
	}

	private IniConf(String path) {
		configPath = path;
		reRead();
	}
	
	public void reRead() {
		URL url = this.getClass().getResource(configPath);
		try {
			conf = new HierarchicalINIConfiguration(url);
		} catch (ConfigurationException e) {
			System.err.println("Loading the configuration failed: " + url);
			System.exit(1);
		}
	}

	private IniConf() {
		reRead();
	}

	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static IniConf getInstance() {
		if (instance == null) {
			instance = new IniConf();
		}
		return instance;
	}

	/**
	 * Singleton for different ini file, eg. in unit tests
	 * 
	 * @param fileName
	 * @return
	 */
	public static IniConf getInstance(String fileName) {
		if (instance == null) {
			instance = new IniConf(fileName);
		}
		return instance;
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

	public int baud() {
		return conf.getInt("flash.baud");
	}

	// TODO: cache?
	public List<Integer> speedList() {
		List<Object> data = conf.getList("animation.speed");
		List<Integer> speedList = new LinkedList<Integer>();
		for (Object o : data) {
			speedList.add(Integer.parseInt(((String) o).trim()));
		}

		return Collections.unmodifiableList(speedList);
	}

	// TODO: cache?
	public List<Integer> delayList() {
		List<Object> data = conf.getList("animation.delay");
		List<Integer> delayList = new LinkedList<Integer>();
		for (Object o : data) {
			delayList.add(Integer.parseInt(((String) o).trim()));
		}

		return Collections.unmodifiableList(delayList);
	}

	public int maxBytes() {
		return conf.getInt("animation.maxbytes");
	}

	public int maxColumns() {
		return conf.getInt("animation.maxcolumns");
	}

	/**
	 * returns the total number of grids as max columns divided by columns
	 * 
	 * @return
	 */
	public int getNumGrids() {
		return maxColumns() / columns();
	}
	
	public double ser_clk_correction() {
		return conf.getDouble("animation.ser_clk_correction");
	}
}
