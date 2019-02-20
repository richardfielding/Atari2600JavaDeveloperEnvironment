// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.general.m6502;

import java.io.Serializable;

public abstract class Instruction implements Serializable, Cloneable {

	public Instruction(M6502 cpu) {
		this.cpu = cpu;
	}

	public abstract int fetch();	// Should return the number of cycles needed to complete execution

	public abstract void execute();

	@Override
	protected Instruction clone() {
		try { 
			return (Instruction)super.clone(); 
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	
	protected transient M6502 cpu;

	
	public static final long serialVersionUID = 1L;

}
