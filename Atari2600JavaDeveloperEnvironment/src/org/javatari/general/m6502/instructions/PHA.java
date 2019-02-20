// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.general.m6502.instructions;

import org.javatari.general.m6502.Instruction;
import org.javatari.general.m6502.M6502;

public final class PHA extends Instruction {

	public PHA(M6502 cpu) {
		super(cpu);
	}

	@Override
	public int fetch() {
		return 3;
	}

	@Override
	public void execute() {
		// Does not perform the dummy PC + 1 read
		cpu.pushByte(cpu.A);
	}
	

	public static final long serialVersionUID = 1L;
	
}
