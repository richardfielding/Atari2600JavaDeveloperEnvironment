// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.general.m6502.instructions;

import org.javatari.general.m6502.Instruction;
import org.javatari.general.m6502.M6502;

public final class uKIL extends Instruction {

	public uKIL(M6502 cpu) {
		super(cpu);
	}

	@Override
	public int fetch() {
		// Actually no cycles should be taken, as the CPU would Halt. But we will simulate a NOP with a Debug hook
		return 2;
	}

	@Override
	public void execute() {
		cpu.debug(">>> Undocumented opcode KIL (HLT)");
	}

	
	public static final long serialVersionUID = 1L;

}
