package com.richardfieldinggames.atari2600.developer;

import org.javatari.main.Standalone;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.richardfieldinggames.atari2600.developer.Binary.binary;
import static com.richardfieldinggames.atari2600.developer.Invariant.assertEquals;
import static com.richardfieldinggames.atari2600.developer.Invariant.assertInInclusiveRange;

/**
 * vcs.h: https://github.com/johnidm/asm-atari-2600/blob/master/vcs.h
 * macro.h: https://alienbill.com/2600/101/bin/macro.h
 */
public class Atari2600Assembler extends MOS6502Assembler {
  //////////////////////////////////////////
  // TIA - Television Interface Adaptor
  //////////////////////////////////////////

  // All these addresses are in the zero page, so we can use zero-page addressing to set them

  // This address controls vertical sync time by writing D1 into the VSYNC latch
  // 00000000 = stop vertical sync
  // 00000010 = start vertical sync
  // vertical sync set-clear
  final public int VSYNC = 0x00;

  // vertical blank set-clear
  final public int VBLANK = 0x01;
  // wait for leading edge of horizontal blank
  final public int WSYNC = 0x02;
  // reset horizontal sync counter
  final public int RSYNC = 0x03;

  // number-size player 0/missile 0
  final public int NUSIZ0 = 0x04;
  // number-size player 0/missile 1
  final public int NUSIZ1 = 0x05;

  // Color Luminance Registers
  // There are four registers that contain color-lum codes.
  // Four bits of color code and three its of luminance code may be written into each of these registers (COLUP0, COLUP1, COLUPF, COLUBK)
  // by the microprocessor at any time.
  // These codes (representing 16 color values and 8 luminance values) are given in the Detailed Address List
  //
  // Bits 1-3 are used for the 8 luminance values and bits 4-7 for the 16 colours
  //
  // See here for a nice, interactive colour pallette:
  // http://www.randomterrain.com/atari-2600-memories-tia-color-charts.html

  // color-lum player 0/missile 0
  final public int COLUP0 = 0x06;
  // color-lum player 1/missile 1
  final public int COLUP1 = 0x07;
  // color-lum playfield + ball
  final public int COLUPF = 0x08;
  // color-lum background
  final public int COLUBK = 0x09;

  // This address is used to write into the playfield control register (a logic 1 causes action as described below)
  // D0 = reflect
  // control playfield ball size & collisions
  final public int CTRLPF = 0x0A;

  // reflect player 0
  final public int REFP0 = 0x0B;
  // reflect player 1
  final public int REFP1 = 0x0C;

  // playfield register byte 0
  final public int PF0 = 0x0D;
  // playfield register byte 1
  final public int PF1 = 0x0E;
  // playfield register byte 2
  final public int PF2 = 0x0F;

  // reset player 0
  final public int RESP0 = 0x10;
  // reset player 1
  final public int RESP1 = 0x11;
  // reset missile 0
  final public int RESM0 = 0x12;
  // reset missile 1
  final public int RESM1 = 0x13;
  // reset ball
  final public int RESBL = 0x14;

  // audio control 0
  final public int AUDC0 = 0x15;
  // audio control 1
  final public int AUDC1 = 0x16;

  // audio frequency 0
  final public int AUDF0 = 0x17;
  // audio frequency 1
  final public int AUDF1 = 0x18;

  // audio volume 0
  final public int AUDV0 = 0x19;
  // audio volume 1
  final public int AUDV1 = 0x1A;

  // graphics player 0
  final public int GRP0 = 0x1B;
  // graphics player 1
  final public int GRP1 = 0x1C;

  // Enable missile 0
  // D1=0 = disable
  // D1=1 = enable
  final public int ENAM0 = 0x1D;
  // Enable missile 1
  // 0 = disable
  // 1 = enable
  final public int ENAM1 = 0x1E;
  // Enable ball
  // D1=0 = disable
  // D1=1 = enable
  final public int ENABL = 0x1F;

  // horizontal motion player 0
  final public int HMP0 = 0x20;
  // horizontal motion player 1
  final public int HMP1 = 0x21;

  // Horizontal motion
  // These addresses write data (horizontal motion values) into
  // the horizontal motion registers.  These registers will
  // cause horizontal motion only when commanded to do so by the
  // horiz. move command HMOVE.
  // The motion values are coded as shown below :
  //
  //  D7 D6  D5 D4
  //  0  1   1  1   +7
  //  0  1   1  0   +6
  //  0  1   0  1   +5   Move left
  //  0  1   0  0   +4   indicated number
  //  0  0   1  1   +3   of clocks
  //  0  0   1  0   +2
  //  0  0   0  1   +1
  //  0  0   0  0   0    No Motion
  //  1  1   1  1   -1
  //  1  1   1  0   -2
  //  1  1   0  1   -3
  //  1  1   0  0   -4   move right
  //  1  0   1  1   -5   indicated number
  //  1  0   1  0   -6   of clocks
  //  1  0   0  1   -7
  //  1  0   0  0   -8
  //
  // WARNING : These motion registers should not be modified
  // during the 24 computer cycles immediately following an
  // HMOVE command.  Unpredictable motion values may result.

  // horizontal motion missile 0
  final public int HMM0 = 0x22;
  // horizontal motion missile 1
  final public int HMM1 = 0x23;
  // horizontal motion ball
  final public int HMBL = 0x24;

  // vertical delay player 0
  final public int VDELP0 = 0x25;
  // vertical delay player 1
  final public int VDELP1 = 0x26;
  // vertical delay ball
  final public int VDELBL = 0x27;

  // reset missile 0 to player 0
  final public int RESMP0 = 0x28;
  // reset missile 1 to player 1
  final public int RESMP1 = 0x29;

  // This address causes the horizontal motion register values to be acted upon during the horizontal blank time in which it occurs.
  // It must occur at the beginning of horiz. blank in order to allow time for generation of extra clock pulses into the
  // horizontal position counters if motion is desired this command must immediately follow a WSYNC command in the program
  // No data bits are used so we can write anything
  // apply horizontal motion
  final public int HMOVE = 0x2A;

  // clear horizontal motion registers
  final public int HMCLR = 0x2B;
  // clear collision latches
  final public int CXCLR = 0x2C;


  ////

  final class BaseColour {
    final int value;
    final Colour[] colours;

    BaseColour(final int value) {
      assertInInclusiveRange(value, 0, 15);
      this.value = value;
      this.colours = new Colour[8];
      for (int i = 0; i < colours.length; i++) {
        colours[i] = new Colour(this, i);
      }
    }
  }

  final class Colour {
    final BaseColour baseColour;
    final int luminance;
    final int value;

    Colour(final BaseColour baseColour,
           final int luminance) {
      this.baseColour = baseColour;
      this.luminance = luminance;
      assertInInclusiveRange(luminance, 0, 7);
      value = (baseColour.value << 4) | (luminance << 1);
    }
  }

  final class Colours {
    final BaseColour[] baseColours = new BaseColour[16];

    {
      for (int i = 0; i < baseColours.length; i++) {
        baseColours[i] = new BaseColour(i);
      }
    }

    final Colour black = baseColours[0].colours[0];
    final Colour grey1 = baseColours[0].colours[1];
    final Colour grey2 = baseColours[0].colours[2];
    final Colour grey3 = baseColours[0].colours[3];
    final Colour grey4 = baseColours[0].colours[4];
    final Colour grey5 = baseColours[0].colours[5];
    final Colour grey6 = baseColours[0].colours[6];
    final Colour grey7 = baseColours[0].colours[7];

    {
      assertEquals(black.value, binary("00000000"));
      assertEquals(grey1.value, binary("00000010"));
      assertEquals(grey2.value, binary("00000100"));
      assertEquals(grey6.value, binary("00001100"));
      assertEquals(grey7.value, binary("00001110"));
    }
  }

  final public Colours colours = new Colours();

  final public void WSYNC() {
    STA_zeroPage(WSYNC);
  }

  final public void HMOVE() {
    STA_zeroPage(HMOVE);
  }

  final class Ball {
    final int reset;
    final int enable;
    final int horizontalMotion;
    final int verticalDelay;

    Ball() {
      reset = RESBL;
      enable = ENABL;
      horizontalMotion = HMBL;
      verticalDelay = VDELBL;
    }
  }

  final public class Player {
    final public int reflect;
    final public int reset;
    final public int graphics;
    final public int horizontalMotion;
    final public int verticalDelay;

    Player(final int index) {
      assertInInclusiveRange(index, 0, 1);
      reflect = REFP0 + index;
      reset = RESP0 + index;
      graphics = GRP0 + index;
      horizontalMotion = HMP0 + index;
      verticalDelay = VDELP0 + index;
    }
  }

  final public class Missile {
    final public int reset;
    final public int enable;
    final public int horizontalMotion;
    final public int resetToPlayer;

    Missile(final int index) {
      assertInInclusiveRange(index, 0, 1);
      reset = RESM0 + index;
      enable = ENAM0 + index;
      horizontalMotion = HMM0 + index;
      resetToPlayer = RESMP0 + index;
    }
  }

  final public Ball ball = new Ball();
  final public Player player0 = new Player(0);
  final public Player player1 = new Player(1);

  final public Missile missile0 = new Missile(0);
  final public Missile missile1 = new Missile(1);

  final public class Audio {
    final public int control;
    final public int frequency;
    final public int volume;

    public Audio(final int index) {
      this.control = AUDC0 + index;
      this.frequency = AUDF0 + index;
      this.volume = AUDV0 + index;
    }
  }

  final public Audio audio0 = new Audio(0);
  final public Audio audio1 = new Audio(1);

  public class AudioControl {
    final public int value;

    public AudioControl(final int value) {
      this.value = value;
    }
  }

  final public class AudioControl1 extends AudioControl {
    final public int silent = 0; // Special for rest
    final public int C6 = 1;
    final public int F5 = 2;
    final public int C5 = 3;
    final public int Gsharp4 = 4;
    final public int F4 = 5;
    final public int D4 = 6;
    final public int C4 = 7;
    final public int Asharp3 = 8;
    final public int Gsharp3 = 9;
    final public int Fsharp3 = 10;
    final public int F3 = 11;
    final public int E3 = 12;
    final public int D3 = 13;
    final public int Csharp3 = 14;
    final public int C3 = 15;
    final public int B2 = 16;
    final public int Asharp2 = 17;
    final public int A2 = 18;
    final public int Gsharp2 = 19;
    //final public int G2 = 20;
    final public int G2 = 21;
    final public int Fsharp2 = 22;
    final public int F2 = 23;
    //final public int E2 = 24;
    final public int E2 = 25;
    final public int Dsharp2 = 26;
    //final public int D2 = 27;
    final public int D2 = 28;
    //final public int Csharp2 = 29;
    final public int Csharp2 = 30;
    final public int C2 = 31;

    AudioControl1(final int value) {
      super(value);
    }
  }

  final public class AudioControl4_5 extends AudioControl {
    final public int silent = 0;
    final public int B8 = 1;
    final public int E8 = 2;
    final public int B7 = 3;
    final public int G7 = 4;
    final public int E7 = 5;
    final public int Csharp7 = 6;
    final public int B6 = 7;
    final public int A6 = 8;
    final public int G6 = 9;
    final public int F6 = 10;
    final public int E6 = 11;
    final public int D6 = 12;
    final public int Csharp6 = 13;
    final public int C6 = 14;
    final public int B5 = 15;
    final public int Asharp5 = 16;
    final public int A5 = 17;
    final public int Gsharp5 = 18;
    final public int G5 = 19;
    final public int Fsharp5 = 20;
    final public int F5_2 = 21;
    final public int F5 = 22;
    final public int E5 = 23;
    final public int Dsharp5 = 24;
    final public int D5_2 = 25;
    final public int D5 = 26;
    final public int Csharp5_2 = 27;
    final public int Csharp5 = 28;
    final public int C5 = 29;
    final public int B4_2 = 30;
    final public int B4 = 31;

    AudioControl4_5(final int value) {
      super(value);
    }
  }

  final public AudioControl1 audioControl1 = new AudioControl1(1);
  final public AudioControl4_5 audioControl4 = new AudioControl4_5(4);
  final public AudioControl4_5 audioControl5 = new AudioControl4_5(5);

  final public class Note {
    final public int frequency;
    final public int length;

    public Note(final int frequency,
                final int length) {
      this.frequency = frequency;
      this.length = length;
    }
  }

  final public Note note(final int frequency, final int length) {
    return new Note(frequency, length);
  }

  //////////////////////////////////////////
  // PIA - Peripheral Interface Adapter
  //////////////////////////////////////////

  // Joystick
  // Two joysticks can be read by configuring the entire port as input and reading the data at SWCHA according to the following table:
  // player 0: bits 4-7
  // player 1: bits 0-3
  //
  // 7/3: right
  // 6:2: left
  // 5:1: down
  // 4:0  up
  //
  // A "0" in a data bit indicates the joystick has been moved to close that switch. All "1's" in a player's nibble indicates that joystick is not moving.
  final public int SWCHA = 0x280;

  // D1 = game select (0 = switch pressed)
  // D0 = game reset (0 = switch pressed)
  final public int SWCHB = 0x282;

  // Timing
  final public int INTIM = 0x284;
  // This timer counts down on each 1x clock cycle
  final public int TIM1T = 0x294;
  // This timer counts down on each 8x clock cycle
  final public int TIM8T = 0x295;
  // This timer counts down on each 64x clock cycle
  final public int TIM64T = 0x296;
  // This timer counts down on each 1024x clock cycle
  final public int T1024T = 0x297;

  final protected void startOfVerticalBlankCode() {
    // Each frame consists of:
    // Vertical sync (3 scan lines)
    // Vertical blank (37 scan lines)
    // Visible tv picture (+ a horizontal blank for each scan line) (192 scan lines)
    // Overscan (30 scan lines)

    LDA_immediate(binary("00000010"));
    STA_zeroPage(VSYNC);
    WSYNC(); // 1
    WSYNC(); // 2

    WSYNC(); // 3
    LDA_immediate(43);
    STA_absolute(TIM64T);
    LDA_immediate(binary("00000000"));
    STA_zeroPage(VSYNC);
  }

  final protected void waitForEndOfVerticalBlankCode(final String label) {
    // Wait until we've reached the end of the vertical blank
    final String WaitForVblankEnd = "WaitForVblankEnd-" + label;
    label(WaitForVblankEnd);
    LDA_absolute(INTIM);
    BNE(WaitForVblankEnd);

    // End the vertical blank section - note that this gets started at the end of the frame
    WSYNC();
    STA_zeroPage(VBLANK);
  }

  final protected void overscanCode(final String label) {
    // Start the vertical blank section - note that this gets stopped at the top of the frame
    LDA_immediate(2);
    WSYNC();
    STA_zeroPage(VBLANK);

    // Now wait for the overscan
    LDX_immediate(30);
    final String overScanWaitLoop = "overScanWaitLoop-" + label;
    label(overScanWaitLoop);
    WSYNC();
    DEX();
    BNE(overScanWaitLoop);
  }

  final public void cleanStart() {
    // This code will do a clean-start of the stack pointer + registers
    SEI();
    CLD();
    LDX_immediate(0xFF);
    TXS();
  }

  ////

  final public void compileInto4KCartridge(final String filename) throws Exception {
    final int cartridgeSize = 1024 * 4;

    final int[] intArray = new int[cartridgeSize];
    final byte[] byteArray = new byte[cartridgeSize];

    final int startAddress = 65536 - byteArray.length;
    assertEquals(0, startAddress % 256);

    final Map<String, Integer> labelToAddress = new LinkedHashMap<>();
    compilePassOne(labelToAddress, startAddress);

    final Map<Integer, Integer> addressToData = new LinkedHashMap<>();
    compilePassTwo(labelToAddress, addressToData, startAddress);

    for (Integer address : addressToData.keySet()) {
      final int index = address - startAddress;
      final int data = addressToData.get(address);
      intArray[index] = data;
    }

    System.out.println(dump(labelToAddress, addressToData, startAddress));

    intArray[intArray.length - 4] = 0;
    intArray[intArray.length - 3] = startAddress / 256;
    intArray[intArray.length - 2] = 0;
    intArray[intArray.length - 1] = startAddress / 256;

    for (int i = 0; i < intArray.length; i++) {
      byteArray[i] = (byte) intArray[i];
    }

    final Path romDir = FileSystems.getDefault().getPath(filename);
    romDir.toFile().getParentFile().mkdirs();
    Files.write(romDir, byteArray);
  }

  final public void runInEmulator(final String filename) {
    final String arg = "file:" + filename;
    System.out.println(arg);
    Standalone.main(new String[]{arg});
  }

  final public void compileInto4kCartridgeAndRunInEmulator(final String filename) throws Exception {
    compileInto4KCartridge(filename);
    runInEmulator(filename);
  }

}
