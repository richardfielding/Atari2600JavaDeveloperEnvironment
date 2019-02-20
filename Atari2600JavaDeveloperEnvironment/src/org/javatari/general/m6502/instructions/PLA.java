// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.general.m6502.instructions;

import org.javatari.general.m6502.Instruction;
import org.javatari.general.m6502.M6502;

public final class PLA extends Instruction {

	public PLA(M6502 cpu) {
		super(cpu);
	}

	@Override
	public int fetch() {
		return 4;
	}

	@Override
	public void execute() {
		// Does not perform the dummy PC + 1 read
		// Does not perform the dummy stack read
		byte val = cpu.pullByte();
		cpu.A = val;
		cpu.NEGATIVE = val < 0;
		cpu.ZERO = val == 0;
	}
	

	public static final long serialVersionUID = 1L;

}
