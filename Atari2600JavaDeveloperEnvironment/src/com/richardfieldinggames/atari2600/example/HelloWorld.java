package com.richardfieldinggames.atari2600.example;

import com.richardfieldinggames.atari2600.developer.Atari2600Assembler;

import static com.richardfieldinggames.atari2600.developer.Binary.binary;

/**
 * This program demonstrates how to create a simple Atari 2600 program that fills the frame with colour
 * <p>
 * Simply run this. It will compile the program into assembler, load it into a cartridge and then
 * run that in the javatari emulator.
 */
final public class HelloWorld extends Atari2600Assembler {
  {
    cleanStart();

    final int colourIndex = 128;
    final int frameColourIndex = 129;

    final String clearZeroPageLoop = "clearZeroPageLoop";
    LDA_immediate(0);
    LDX_immediate(0);
    label(clearZeroPageLoop);
    STA_zeroPageX(0);
    DEX();
    BNE(clearZeroPageLoop);

    LDA_immediate(0);
    STA_zeroPage(colourIndex);

    LDA_immediate(1);
    STA_zeroPage(audio0.volume);
    STA_zeroPage(audio1.volume);

    final int scanLines = 192;

    final String colourData = "colourData";

    final String startOfFrame = "startOfFrame";
    label(startOfFrame);

    startOfVerticalBlankCode();

    // Can do some code here
    INC_zeroPage(colourIndex);
    LDA_zeroPage(colourIndex);
    LSR_accumulator();
    LSR_accumulator();
    AND_immediate(binary("00000111"));
    STA_zeroPage(frameColourIndex);

    STA_zeroPage(audio0.frequency);
    STA_zeroPage(audio1.frequency);

    STA_zeroPage(audio0.control);
    STA_zeroPage(audio1.control);

    waitForEndOfVerticalBlankCode("HelloWorld");

    for (int i = 0; i < scanLines / 8; i++) {
      LDX_zeroPage(7);
      final String loop = "loop" + i;
      label(loop);

      LDA_zeroPage(frameColourIndex);
      CLC();
      ADC_immediate(1);
      AND_immediate(binary("00000111"));
      STA_zeroPage(frameColourIndex);

      TAY();
      LDA_absoluteY(colourData);

      WSYNC();
      STA_zeroPage(COLUBK);
      DEX();
      BPL(loop);
    }

    overscanCode("HelloWorld");

    // Can get colours from here:
    // http://www.randomterrain.com/atari-2600-memories-tia-color-charts.html
    data(colourData, new int[]{0x74, 0x78, 0x78, 0x78, 0x7C, 0x78, 0x78, 0x78});

    JMP_absolute(startOfFrame);
  }

  static public void main(final String... args) throws Exception {
    new HelloWorld().compileInto4kCartridgeAndRunInEmulator("~/HelloWorld.A26");
  }
}
