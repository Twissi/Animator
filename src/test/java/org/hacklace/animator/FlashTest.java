package org.hacklace.animator;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;

import junit.framework.TestCase;
import junitx.framework.FileAssert;

import org.hacklace.animator.exporter.FlashExporter;
import org.junit.Test;

public class FlashTest extends TestCase {

	private File exampleConf;
	private File exampleFlash;
	private File output;
	private HacklaceConfigManager manager;
	private FlashExporter flashExporter;
	
	@Override
	protected void setUp() throws IOException {
		manager = new HacklaceConfigManager();
		output = File.createTempFile("test", ".hack");
		URL url = this.getClass().getResource("/configs/example.hack");
		exampleConf = new File(url.getFile());
		url = this.getClass().getResource("/configs/example.flash");
		exampleFlash = new File(url.getFile());
		flashExporter = new FlashExporter();
	}
	
	@Override
	protected void tearDown() {
		output.delete();
		exampleConf = null;
		exampleFlash = null;
	}
	
	@Test
	public void testReadEqualsWrite()
			throws IOException {
		manager.readFile(exampleConf, new ErrorContainer());
		String rawString = manager.getRawString();
		flashExporter.writeTo(rawString, output);
		FileAssert.assertEquals(exampleFlash, output);
	}
	
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testEnumeratePorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    h.add(com);
                    System.out.println("Port "  + com.getName() + " is free.");
                } catch (PortInUseException e) {
                    System.out.println("Port "  + com.getName() + " is in use.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " +  com.getName());
                    e.printStackTrace();
                }
            }
        }
        // return h;
	}

}
