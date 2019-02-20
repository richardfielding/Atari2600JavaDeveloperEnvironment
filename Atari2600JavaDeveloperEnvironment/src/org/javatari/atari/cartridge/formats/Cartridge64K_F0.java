// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.atari.cartridge.formats;

import org.javatari.atari.cartridge.Cartridge;
import org.javatari.atari.cartridge.CartridgeFormat;
import org.javatari.atari.cartridge.CartridgeFormatOption;
import org.javatari.atari.cartridge.ROM;

/**
 * Implements the 64K "F0" Dynacom Megaboy format
 */
public final class Cartridge64K_F0 extends CartridgeBanked {

	private Cartridge64K_F0(ROM rom) {
		super(rom, FORMAT);
	}

	@Override
	protected void performBankSwitchOnMaskedAddress() {
		if (maskedAddress == BANKSW_ADDRESS) {	// Bank selection. Increments bank
			bankAddressOffset += BANK_SIZE;
			if (bankAddressOffset >= SIZE) bankAddressOffset = 0;
		}
	}

	
	private static final int SIZE = 65536;
	private static final int BANK_SIZE = 4096;
	private static final int BANKSW_ADDRESS = 0x0ff0;

	public static final CartridgeFormat FORMAT = new CartridgeFormat("F0", "64K Dynacom Megaboy") {
		@Override
		public Cartridge createCartridge(ROM rom) {
			return new Cartridge64K_F0(rom);
		}
		@Override
		public CartridgeFormatOption getOption(ROM rom) {
			if (rom.content.length != SIZE) return null;
			return new CartridgeFormatOption(101, this, rom);
		}
		private static final long serialVersionUID = 1L;
	};

	public static final long serialVersionUID = 1L;

}

