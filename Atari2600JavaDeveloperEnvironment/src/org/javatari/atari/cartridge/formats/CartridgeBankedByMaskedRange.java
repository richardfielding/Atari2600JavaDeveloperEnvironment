// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.atari.cartridge.formats;

import java.util.Arrays;

import org.javatari.atari.cartridge.CartridgeFormat;
import org.javatari.atari.cartridge.ROM;


/**
 * Implements the simple bank switching method by masked address range access (within Cart area)
 * Supports SuperChip extra RAM (ON/OFF/AUTO). 
 * Used by several n * 4K bank formats with varying extra RAM sizes
 */
public abstract class CartridgeBankedByMaskedRange extends CartridgeBanked {

	protected CartridgeBankedByMaskedRange(ROM rom, CartridgeFormat format, 
			int baseBankSwitchAddress, Boolean superChip, int extraRAMSize) {
		super(rom, format);
		int numBanks = bytes.length / BANK_SIZE;
		this.baseBankSwitchAddress = baseBankSwitchAddress;
		this.topBankSwitchAddress = baseBankSwitchAddress + numBanks - 1;
		this.extraRAMSize = extraRAMSize;
		// SuperChip mode. null = automatic mode
		if (superChip == null) { 
			superChipMode = false;
			superChipAutoDetect = true;
		} else {
			superChipMode = superChip;
			superChipAutoDetect = false;
		}
		extraRAM = (superChip == null || superChip) ? Arrays.copyOf(bytes, extraRAMSize) : null;
	}

	@Override
	public byte readByte(int address) {		
		maskAddress(address);
		// Check for SuperChip Extra RAM reads
		if (superChipMode && (maskedAddress >= extraRAMSize) && (maskedAddress < extraRAMSize * 2))
			return extraRAM[maskedAddress - extraRAMSize];
		else
			// Always add the correct offset to access bank selected
			return bytes[bankAddressOffset + maskedAddress];	
	}

	@Override
	public void writeByte(int address, byte b) {	
		maskAddress(address);
		// Check for Extra RAM writes and then turn superChip mode on
		if (maskedAddress < extraRAMSize && (superChipMode || superChipAutoDetect)) {
			if (!superChipMode) superChipMode = true;
			extraRAM[maskedAddress] = b;
		}
	}

	@Override
	protected void performBankSwitchOnMaskedAddress() {
		// Check and perform bank-switch as necessary
		if (maskedAddress >= baseBankSwitchAddress && maskedAddress <= topBankSwitchAddress)
			bankAddressOffset = romStartAddress + BANK_SIZE * (maskedAddress - baseBankSwitchAddress);
	}

	@Override
	public CartridgeBankedByMaskedRange clone() {
		CartridgeBankedByMaskedRange clone = (CartridgeBankedByMaskedRange)super.clone();
		if (extraRAM != null) clone.extraRAM = extraRAM.clone();
		return clone;
	}


	private final int baseBankSwitchAddress;
	protected int     topBankSwitchAddress;
	
	protected int romStartAddress = 0;
	
	private boolean superChipMode = false;
	private final boolean superChipAutoDetect;
	private final int extraRAMSize;
	protected byte[] extraRAM;
	

	protected static final int BANK_SIZE = 4096;

	public static final long serialVersionUID = 1L;

}