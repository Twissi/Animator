package org.hacklace.animator.exporter;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.util.Enumeration;

import org.hacklace.animator.IniConf;

/**
 * http://dev.root1.de/projects/rxtx-rebundled/wiki
 */
public class FlashExporter {
	
	private IniConf conf;
	private SerialPort  serialPort;
		
	public FlashExporter() {
		conf = new IniConf();
	}
	
	@SuppressWarnings("unchecked")
	private CommPortIdentifier getPortIdentifier() throws PortUnreachableException {
		Enumeration<CommPortIdentifier> portList;
		CommPortIdentifier portId;

		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().equals(conf.device())) {
				return portId;
			}
		}
		throw new PortUnreachableException( conf.device());
	}
	
	private SerialPort initSerialPort(CommPortIdentifier portId) throws UnsupportedCommOperationException, PortInUseException {
		SerialPort sp;
		sp = (SerialPort) portId.open("Hacklace animator", 2000);
		sp.setSerialPortParams(conf.baud(), SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		return sp;
	}
	
	private void close() {
		
	}
	
	public void write(File f) throws IOException, UnsupportedCommOperationException, PortInUseException {
		serialPort = initSerialPort( getPortIdentifier());
		OutputStream serialOut = serialPort.getOutputStream();
		
		FileInputStream fis = new FileInputStream(f);
				
		serialOut.write( (byte) 27);

		serialOut.write( (byte) 'H');
		serialOut.write( (byte) 'L');
		
		
		int i;
		while( (  i = fis.read()) != -1 )  	 {
			serialOut.write(i);
		}
		
		serialOut.write( (byte) 27);
		
		fis.close();
		serialOut.close();
		serialPort.close();		
		close();		
	}

	public static void main(String[] args) throws UnsupportedCommOperationException, PortInUseException, IOException {
		FlashExporter e = new FlashExporter();
		File f = new File(e.getClass().getResource("/example.cfg").getFile());
		
		e.write(f );
	}
}
