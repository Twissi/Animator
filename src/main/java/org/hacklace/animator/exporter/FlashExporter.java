package org.hacklace.animator.exporter;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.util.Enumeration;
import java.util.LinkedList;

import org.hacklace.animator.IniConf;

/**
 * http://dev.root1.de/projects/rxtx-rebundled/wiki
 */
public class FlashExporter {
	
	private IniConf conf;
	private SerialPort  serialPort;
		
	public FlashExporter() {
		conf = IniConf.getInstance();
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
		FileInputStream fis = new FileInputStream(f);
		write(fis);
		fis.close();
	}
	
	public void write(InputStream stream) throws IOException, UnsupportedCommOperationException, PortInUseException {
		serialPort = initSerialPort( getPortIdentifier());
		OutputStream serialOut = serialPort.getOutputStream();
		
		serialOut.write( (byte) 27);

		serialOut.write( (byte) 'H');
		serialOut.write( (byte) 'L');
		
		
		int i;
		while( (  i = stream.read()) != -1 )  	 {
			serialOut.write(i);
		}
		
		serialOut.write( (byte) 27);
		
		serialOut.close();
		serialPort.close();		
		close();		
	}
	
    @SuppressWarnings("unchecked")
    // TODO Add help that user has to be part of the corresponding group
	public LinkedList<CommPortIdentifier> listPorts()
    {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        LinkedList<CommPortIdentifier> ports = new LinkedList<CommPortIdentifier>();
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            ports.add(portIdentifier);
        }        
        return ports;
    }
    
    public String getPortTypeName ( int portType )
    {
        switch ( portType )
        {
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

	public static void main(String[] args) throws UnsupportedCommOperationException, PortInUseException, IOException {
		FlashExporter e = new FlashExporter();
		e.listPorts();
		//File f = new File(e.getClass().getResource("/example.cfg").getFile());
		//e.write(f );
	}
}
