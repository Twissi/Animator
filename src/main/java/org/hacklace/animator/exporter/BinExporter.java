package org.hacklace.animator.exporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.hacklace.animator.ConversionUtil;
import org.hacklace.animator.HacklaceConfigManager;

public class BinExporter {

	public void write(InputStream in, File out) throws IOException {
		FileOutputStream fos = new FileOutputStream(out);
		write(in, fos);
		fos.close();
	}

	public void write(InputStream in, OutputStream out) throws IOException {
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(in,
				HacklaceConfigManager.HACKLACE_CHARSET));
		String cfgLine;
		while ((cfgLine = br.readLine()) != null) {
			// write rest of line
			for (byte c: ConversionUtil.convertFlashStringToBytes(cfgLine)) {
				out.write(c);
			}
			out.write(0x00);
		}
		out.write(0x00);
	}
}
