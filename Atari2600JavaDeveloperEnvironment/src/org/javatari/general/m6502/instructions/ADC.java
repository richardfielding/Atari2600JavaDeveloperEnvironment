// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.general.m6502.instructions;

import org.javatari.general.m6502.Instruction;
import org.javatari.general.m6502.M6502;
import org.javatari.general.m6502.OperandType;

public final class ADC extends Instruction {

	public ADC(M6502 cpu, int type) {
		super(cpu);
		this.type = type;
	}

	@Override
	public int fetch() {
		if (type == OperandType.IMM) 		{ ea = cpu.fetchImmediateAddress(); return 2; }
		if (type == OperandType.Z_PAGE) 	{ ea = cpu.fetchZeroPageAddress(); return 3; }
		if (type == OperandType.Z_PAGE_X) 	{ ea = cpu.fetchZeroPageXAddress(); return 4; }
		if (type == OperandType.ABS) 		{ ea = cpu.fetchAbsoluteAddress(); return 4; }
		if (type == OperandType.ABS_X) 		{ ea = cpu.fetchAbsoluteXAddress(); return 4 + (cpu.pageCrossed?1:0); }
		if (type == OperandType.ABS_Y) 		{ ea = cpu.fetchAbsoluteYAddress(); return 4 + (cpu.pageCrossed?1:0); }
		if (type == OperandType.IND_X) 		{ ea = cpu.fetchIndirectXAddress(); return 6; }
		if (type == OperandType.IND_Y)		{ ea = cpu.fetchIndirectYAddress(); return 5 + (cpu.pageCrossed?1:0); }
		throw new IllegalStateException("ADC Invalid Operand Type: " + type);
	}
	
	@Override
	public void execute() {
		int b = cpu.bus.readByte(ea);
		int uB = M6502.toUnsignedByte(b);
		int oldA = cpu.A;
		int uOldA = M6502.toUnsignedByte(oldA);

		int aux = oldA + b + (cpu.CARRY?1:0); 
		int uAux = uOldA + uB + (cpu.CARRY?1:0); 
		
		// ZERO flag is affected always as in Binary mode
		byte newA = (byte) M6502.toUnsignedByte(uAux);		// Could be aux 
		cpu.ZERO = newA == 0;

		// But the others flags and the ACC are computed differently in Decimal Mode
		if (!cpu.DECIMAL_MODE) {
			cpu.NEGATIVE = newA < 0;
			cpu.OVERFLOW = aux > 127 || aux < -128; 
			cpu.CARRY = uAux > 0xff;
			cpu.A = newA; 
			return;
		}

		// Decimal Mode computations
		uAux = (uOldA & 0x0f) + (uB & 0x0f) + (cpu.CARRY?1:0);
		if (uAux >= 0x0A) uAux = ((uAux + 0x06) & 0x0f) + 0x10;
			aux = (byte)(uOldA & 0xf0) + (byte)(uB & 0xf0) + (byte)uAux;     // Holy shit, that was the *unsigned* operation
			cpu.NEGATIVE = (aux & 0x80) > 0;
			cpu.OVERFLOW = (aux > 127) | (aux < -128);
		uAux = (uOldA & 0xf0) + (uB & 0xf0) + uAux;
		if (uAux >= 0xA0) uAux += 0x60;
		cpu.CARRY = uAux > 0xff;							
		cpu.A = (byte) M6502.toUnsignedByte(uAux);
	}
		
	private final int type;
	
	private int ea;
	

	public static final long serialVersionUID = 1L;

}
