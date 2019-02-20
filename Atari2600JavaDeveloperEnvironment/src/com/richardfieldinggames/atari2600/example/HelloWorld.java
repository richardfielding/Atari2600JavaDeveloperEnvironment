package com.richardfieldinggames.atari2600.example;

import com.richardfieldinggames.atari2600.developer.Atari2600Assembler;

/**
 * This program demonstrates how to create a simple Atari 2600 program that fills the frame with colour
 *
 * Simply run this. It will compile the program into assembler, load it into a cartridge and then
 * run that in the javatari emulator.
 */
final public class HelloWorld extends Atari2600Assembler {
  {
    cleanStart();

    final String startOfFrame = "startOfFrame";
    label(startOfFrame);

    startOfVerticalBlankCode();
    waitForEndOfVerticalBlankCode("HelloWorld");

    for (int i = 0; i < 192; i++) {
      WSYNC();
      LDA_immediate(i);
      STA_zeroPage(COLUBK);
    }

    overscanCode("HelloWorld");

    JMP_absolute(startOfFrame);
  }

  static public void main(final String... args) throws Exception {
    new HelloWorld().compileInto4kCartridgeAndRunInEmulator("~/HelloWorld.A26");
  }
}
