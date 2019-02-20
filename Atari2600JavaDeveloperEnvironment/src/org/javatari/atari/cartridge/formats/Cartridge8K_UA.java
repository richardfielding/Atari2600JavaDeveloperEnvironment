// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.atari.cartridge.formats;

import org.javatari.atari.cartridge.Cartridge;
import org.javatari.atari.cartridge.CartridgeFormat;
import org.javatari.atari.cartridge.CartridgeFormatOption;
import org.javatari.atari.cartridge.ROM;

/**
 * Implements the 8K "UA" UA Limited format
 */
public class Cartridge8K_UA extends CartridgeBankedByBusMonitoring {

	protected Cartridge8K_UA(ROM rom) {
		super(rom, FORMAT);
	}

	@Override
	protected void performBankSwitchOnMonitoredAccess(int address) {
		if (address == 0x0220) {
			if (bankAddressOffset != 0) bankAddressOffset = 0;
		} else if (address == 0x0240) {
			if (bankAddressOffset != BANK_SIZE) bankAddressOffset = BANK_SIZE;
		}
	}
	

	protected static final int BANK_SIZE = 4096;
	protected static final int SIZE = 2 * BANK_SIZE;

	public static final CartridgeFormat FORMAT = new CartridgeFormat("UA", "8K UA Limited") {
		@Override
		public Cartridge createCartridge(ROM rom) {
			return new Cartridge8K_UA(rom);
		}
		@Override
		public CartridgeFormatOption getOption(ROM rom) {
			if (rom.content.length != SIZE) return null;
			return new CartridgeFormatOption(115, this, rom);
		}
		private static final long serialVersionUID = 1L;
	};

	public static final long serialVersionUID = 1L;

}