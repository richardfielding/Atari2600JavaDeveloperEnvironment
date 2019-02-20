// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.pc.cartridge;

import java.awt.Dimension;
import java.io.File;
import java.security.AccessControlException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.javatari.atari.cartridge.Cartridge;
import org.javatari.parameters.Parameters;


public final class FileROMChooser {

	public static Cartridge chooseFile() {
		if (lastFileChosen == null) lastFileChosen = new File(Parameters.LAST_ROM_FILE_CHOSEN);
		try {
			if (chooser == null) {
				chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter(ROMLoader.VALID_FILES_DESC, ROMLoader.VALID_FILE_EXTENSIONS));
				chooser.setPreferredSize(new Dimension(580, 400));
			}
			chooser.setSelectedFile(lastFileChosen);
			int res = chooser.showOpenDialog(null);
			if (res != 0) return null;
		} catch (AccessControlException ex) {
			// Automatically tries FileServiceChooser if access is denied
			return FileServiceROMChooser.chooseFile();
		}
		lastFileChosen = chooser.getSelectedFile();
		Parameters.LAST_ROM_FILE_CHOSEN = lastFileChosen.toString();
		Parameters.savePreferences();
		return ROMLoader.load(lastFileChosen);
	}
	
	private static JFileChooser chooser;
	private static File lastFileChosen;

}
