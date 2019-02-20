package com.richardfieldinggames.atari2600.developer;

abstract public class Instruction extends Assembler {
  final public String name;

  public String toString() {
    return name;
  }

  public Instruction(final Assembler parent,
                     final String label,
                     final int length,
                     final String name) {
    super(parent, label, length);
    this.name = name;
  }
}
