package org.hacklace.animator.exporter;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;

import org.hacklace.animator.HacklaceConfigManager;
import org.hacklace.animator.IniConf;
import org.hacklace.animator.gui.AnimatorGui;

/**
 * http://dev.root1.de/projects/rxtx-rebundled/wiki
 */
public class FlashExporter {

	private IniConf conf;
	private String deviceName;

	public FlashExporter() {
		conf = AnimatorGui.getIniConf();
		conf.reRead();
		deviceName = conf.device();
	}

	public CommPortIdentifier getPortIdentifier(String deviceName)
			throws PortUnreachableException {
		Enumeration<CommPortIdentifier> portList;
		CommPortIdentifier portId;

		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL
					&& portId.getName().equals(deviceName)) {
				return portId;
			}
		}
		throw new PortUnreachableException(deviceName);
	}

	private SerialPort initSerialPort(CommPortIdentifier portId)
			throws UnsupportedCommOperationException, PortInUseException {
		SerialPort sp;
		sp = (SerialPort) portId.open("Hacklace animator", 2000);
		sp.setSerialPortParams(conf.baud(), SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		return sp;
	}

	/**
	 * Write contents of file inputFile to serial port
	 * 
	 * @param inputFile
	 * @throws IOException
	 * @throws UnsupportedCommOperationException
	 * @throws PortInUseException
	 */
	public void write(File inputFile) throws IOException,
			UnsupportedCommOperationException, PortInUseException {
		FileInputStream fis = new FileInputStream(inputFile);
		write(fis);
		fis.close();
	}

	/**
	 * Write contents of stream inputStream to serial port
	 * 
	 * @param stream
	 * @throws IOException
	 * @throws UnsupportedCommOperationException
	 * @throws PortInUseException
	 */
	public void write(InputStream inputStream) throws IOException,
			UnsupportedCommOperationException, PortInUseException {
		SerialPort serialPort = initSerialPort(getPortIdentifier(deviceName));
		OutputStream serialOut = serialPort.getOutputStream();
		writeTo(inputStream, serialOut);
		serialOut.close();
		serialPort.close();
	}

	public void write(String rawString)
			throws UnsupportedCommOperationException, PortInUseException,
			IOException {
		SerialPort serialPort = initSerialPort(getPortIdentifier(deviceName));
		OutputStream serialOut = serialPort.getOutputStream();
		writeTo(rawString, serialOut);
		serialOut.close();
		serialPort.close();
	}

	/**
	 * Write contents of stream inputStream to a file, used for debugging / unit
	 * tests
	 * 
	 * @param in
	 * @param outFile
	 * @throws IOException
	 * @throws UnsupportedCommOperationException
	 * @throws PortInUseException
	 */
	public void writeTo(InputStream in, File outFile) throws IOException {
		FileOutputStream fos = new FileOutputStream(outFile);
		writeTo(in, fos);
		fos.close();
	}

	/**
	 * The actual writer with the data generation code, reads from inputStream,
	 * writes to outputStream
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 * @throws UnsupportedCommOperationException
	 * @throws PortInUseException
	 */
	public void writeTo(InputStream in, OutputStream out) throws IOException {
		out.write((byte) 27);

		out.write((byte) 'H');
		out.write((byte) 'L');

		int i;
		while ((i = in.read()) != -1) {
			out.write(i);
		}

		out.write((byte) 27);
	}

	public void writeTo(String rawString, OutputStream out) throws IOException {
		InputStream stream = new ByteArrayInputStream(
				rawString.getBytes(HacklaceConfigManager.HACKLACE_CHARSET));
		writeTo(stream, out);
	}

	public void writeTo(String rawString, File outFile) throws IOException {
		InputStream stream = new ByteArrayInputStream(
				rawString.getBytes(HacklaceConfigManager.HACKLACE_CHARSET));
		writeTo(stream, outFile);
	}

	public LinkedList<CommPortIdentifier> listPorts() {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier
				.getPortIdentifiers();
		LinkedList<CommPortIdentifier> ports = new LinkedList<CommPortIdentifier>();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			ports.add(portIdentifier);
		}
		return ports;
	}

	public ArrayList<String> listDeviceNames() {
		ArrayList<String> result = new ArrayList<String>();
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier
				.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			result.add(portIdentifier.getName());
		}
		return result;
	}

	public String getPortTypeName(int portType) {
		switch (portType) {
		case CommPortIdentifier.PORT_I2C:
			return "I2C";
		case CommPortIdentifier.PORT_PARALLEL:
			return "Parallel";
		case CommPortIdentifier.PORT_RAW:
			return "Raw";
		case CommPortIdentifier.PORT_RS485:
			return "RS485";
		case CommPortIdentifier.PORT_SERIAL:
			return "Serial";
		default:
			return "unknown type";
		}
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
