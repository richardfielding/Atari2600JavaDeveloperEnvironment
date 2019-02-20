// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.general.board;

import java.util.Arrays;

public final class RAM64k implements BUS16Bits {

  public RAM64k() {
    Arrays.fill(bytes, (byte) 0x00);
  }

  @Override
  public byte readByte(int address) {
    final byte value = bytes[address & 0xffff];
    System.out.println("readByte " + address + "=" + value);
    return value;
  }

  @Override
  public void writeByte(int address, byte b) {
    System.out.println("writeByte " + address + "=" + b);
    bytes[address & 0xffff] = b;
  }

  public void dump(int init, int quant) {
    System.out.printf("MEMORY DUMP FROM %04x:\n", init);
    for (int i = init; i < init + quant; i++)
      System.out.printf("%02x ", unsignedByte(i));
    System.out.println();
  }

  private int unsignedByte(int address) {
    return readByte(address) & 0xff;
  }

  private byte[] bytes = new byte[65536];

}
