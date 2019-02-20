package com.richardfieldinggames.atari2600.developer;

import java.util.Map;

import static com.richardfieldinggames.atari2600.developer.Invariant.assertEquals;
import static com.richardfieldinggames.atari2600.developer.Invariant.assertGreaterThan;

// http://obelisk.me.uk/6502/reference.html
public class MOS6502Assembler extends Assembler {
  public MOS6502Assembler() {
    super(null, null, 0);
  }

  final public void ADC_immediate(final Integer value) {
    immediateInstruction("ADC", 0x69, value);
  }

  final public void ADC_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("ADC", 0x65, zeroPageAddress);
  }

  final public void ADC_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("ADC", 0x75, zeroPageAddress);
  }

  final public void ADC_absolute(final Object address) {
    absoluteInstruction("ADC", 0x6D, address);
  }

  final public void ADC_absoluteX(final Object address) {
    absoluteXInstruction("ADC", 0x7D, address);
  }

  final public void ADC_absoluteY(final Object address) {
    absoluteYInstruction("ADC", 0x79, address);
  }

  final public void AND_immediate(final Integer value) {
    immediateInstruction("AND", 0x29, value);
  }

  final public void AND_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("AND", 0x25, zeroPageAddress);
  }

  final public void AND_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("AND", 0x35, zeroPageAddress);
  }

  final public void AND_absolute(final Object address) {
    absoluteInstruction("AND", 0x2D, address);
  }

  final public void AND_absoluteX(final Object address) {
    absoluteXInstruction("AND", 0x3D, address);
  }

  final public void AND_absoluteY(final Object address) {
    absoluteYInstruction("AND", 0x39, address);
  }

  final public void ASL_accumulator() {
    accumulatorInstruction("ASL", 0x0A);
  }

  final public void ASL_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("ASL", 0x06, zeroPageAddress);
  }

  final public void ASL_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("ASL", 0x16, zeroPageAddress);
  }

  final public void ASL_absolute(final Object address) {
    absoluteInstruction("ASL", 0x0E, address);
  }

  final public void ASL_absoluteX(final Object address) {
    absoluteXInstruction("ASL", 0x1E, address);
  }

  final public void BCC(final Object address) {
    relativeInstruction("BCC", 0x90, address);
  }

  final public void BCS(final Object address) {
    relativeInstruction("BCS", 0xB0, address);
  }

  final public void BEQ(final Object address) {
    relativeInstruction("BEQ", 0xF0, address);
  }

  final public void BIT_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("BIT", 0x24, zeroPageAddress);
  }

  final public void BIT_absolute(final Object address) {
    absoluteInstruction("BIT", 0x2C, address);
  }

  final public void BMI(final Object address) {
    relativeInstruction("BMI", 0x30, address);
  }

  final public void BNE(final Object address) {
    relativeInstruction("BNE", 0xD0, address);
  }

  final public void BPL(final Object address) {
    relativeInstruction("BPL", 0x10, address);
  }

  final public void BVC(final Object address) {
    relativeInstruction("BVC", 0x50, address);
  }

  final public void BVS(final Object address) {
    relativeInstruction("BVS", 0x70, address);
  }

  final public void CLC() {
    impliedInstruction("CLC", 0x18);
  }

  final public void CLD() {
    impliedInstruction("CLD", 0xD8);
  }

  final public void CMP_immediate(final Integer value) {
    immediateInstruction("CMP", 0xC9, value);
  }

  final public void CMP_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("CMP", 0xC5, zeroPageAddress);
  }

  final public void CMP_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("CMP", 0xD5, zeroPageAddress);
  }

  final public void CMP_absolute(final Object address) {
    absoluteInstruction("CMP", 0xCD, address);
  }

  final public void CMP_absoluteX(final Object address) {
    absoluteXInstruction("CMP", 0xDD, address);
  }

  final public void CMP_absoluteY(final Object address) {
    absoluteYInstruction("CMP", 0xD9, address);
  }

  final public void CPX_immediate(final Integer value) {
    immediateInstruction("CPX", 0xE0, value);
  }

  final public void CPX_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("CPX", 0xE4, zeroPageAddress);
  }

  final public void CPX_absolute(final Object address) {
    absoluteInstruction("CPX", 0xEC, address);
  }

  final public void CPY_immediate(final Integer value) {
    immediateInstruction("CPY", 0xC0, value);
  }

  final public void CPY_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("CPY", 0xC4, zeroPageAddress);
  }

  final public void CPY_absolute(final Object address) {
    absoluteInstruction("CPY", 0xCC, address);
  }

  // Clear Interrupt Disable
  // Sets the decimal mode flag to zero
  final public void CLI() {
    impliedInstruction("CLI", 0x58);
  }

  final public void DEC_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("DEC", 0xC6, zeroPageAddress);
  }

  final public void DEC_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("DEC", 0xD6, zeroPageAddress);
  }

  final public void DEC_absolute(final Object zeroPageAddress) {
    absoluteInstruction("DEC", 0xCE, zeroPageAddress);
  }

  final public void DEC_absoluteX(final Object zeroPageAddress) {
    absoluteXInstruction("DEC", 0xDE, zeroPageAddress);
  }

  final public void DEX() {
    impliedInstruction("DEX", 0xCA);
  }

  final public void DEY() {
    impliedInstruction("DEY", 0x88);
  }

  final public void EOR_immediate(final Integer value) {
    immediateInstruction("EOR", 0x49, value);
  }

  final public void EOR_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("EOR", 0x55, zeroPageAddress);
  }

  final public void EOR_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("EOR", 0x55, zeroPageAddress);
  }

  final public void EOR_absolute(final Object address) {
    absoluteInstruction("EOR", 0x4D, address);
  }

  final public void EOR_absoluteX(final Object address) {
    absoluteXInstruction("EOR", 0x5D, address);
  }

  final public void EOR_absoluteY(final Object address) {
    absoluteYInstruction("EOR", 0x59, address);
  }

  final public void INC_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("INC", 0xE6, zeroPageAddress);
  }

  final public void INC_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("INC", 0xF6, zeroPageAddress);
  }

  final public void INC_absolute(final Object zeroPageAddress) {
    absoluteInstruction("INC", 0xEE, zeroPageAddress);
  }

  final public void INC_absoluteX(final Object zeroPageAddress) {
    absoluteXInstruction("INC", 0xFE, zeroPageAddress);
  }

  final public void INX() {
    impliedInstruction("INX", 0xE8);
  }

  final public void INY() {
    impliedInstruction("INY", 0xC8);
  }

  final public void JMP_absolute(final Object address) {
    absoluteInstruction("JMP", 0x4C, address);
  }

  final public void LDA_immediate(final Integer value) {
    immediateInstruction("LDA", 0xA9, value);
  }

  final public void LDA_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("LDA", 0xA5, zeroPageAddress);
  }

  final public void LDA_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("LDA", 0xB5, zeroPageAddress);
  }

  final public void LDA_absolute(final Object address) {
    absoluteInstruction("LDA", 0xAD, address);
  }

  final public void LDA_absoluteX(final Object address) {
    absoluteXInstruction("LDA", 0xBD, address);
  }

  final public void LDA_absoluteY(final Object address) {
    absoluteYInstruction("LDA", 0xB9, address);
  }

  final public void LDX_immediate(final Integer value) {
    immediateInstruction("LDX", 0xA2, value);
  }

  final public void LDX_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("LDX", 0xA6, zeroPageAddress);
  }

  final public void LDX_zeroPageY(final Object zeroPageAddress) {
    zeroPageYInstruction("LDX", 0xB6, zeroPageAddress);
  }

  final public void LDX_absolute(final Object address) {
    absoluteInstruction("LDX", 0xAE, address);
  }

  final public void LDX_absoluteY(final Object address) {
    absoluteYInstruction("LDX", 0xBE, address);
  }

  final public void LDY_immediate(final Integer value) {
    immediateInstruction("LDY", 0xA0, value);
  }

  final public void LDY_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("LDY", 0xA4, zeroPageAddress);
  }

  final public void LDY_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("LDY", 0xB4, zeroPageAddress);
  }

  final public void LDY_absolute(final Object address) {
    absoluteInstruction("LDY", 0xAC, address);
  }

  final public void LDY_absoluteX(final Object address) {
    absoluteXInstruction("LDY", 0xBC, address);
  }

  final public void LSR_accumulator() {
    accumulatorInstruction("LSR", 0x4A);
  }

  final public void LSR_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("LSR", 0x46, zeroPageAddress);
  }

  final public void LSR_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("LSR", 0x56, zeroPageAddress);
  }

  final public void LSR_absolute(final Object address) {
    absoluteInstruction("LSR", 0x4E, address);
  }

  final public void LSR_absoluteX(final Object address) {
    absoluteXInstruction("LSR", 0x5E, address);
  }

  final public void NOP() {
    impliedInstruction("NOP", 0xEA);
  }

  final public void ORA_immediate(final Integer value) {
    immediateInstruction("ORA", 0x09, value);
  }

  final public void ORA_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("ORA", 0x05, zeroPageAddress);
  }

  final public void ORA_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("ORA", 0x15, zeroPageAddress);
  }

  final public void ORA_absolute(final Object address) {
    absoluteInstruction("ORA", 0x0D, address);
  }

  final public void ORA_absoluteX(final Object address) {
    absoluteXInstruction("ORA", 0x1D, address);
  }

  final public void ORA_absoluteY(final Object address) {
    absoluteYInstruction("ORA", 0x19, address);
  }

  final public void ROL_accumulator() {
    accumulatorInstruction("ROL", 0x2A);
  }

  final public void ROL_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("ROL", 0x26, zeroPageAddress);
  }

  final public void ROL_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("ROL", 0x36, zeroPageAddress);
  }

  final public void ROL_absolute(final Object address) {
    absoluteInstruction("ROL", 0x2E, address);
  }

  final public void ROL_absoluteX(final Object address) {
    absoluteXInstruction("ROL", 0x3E, address);
  }

  final public void ROR_accumulator() {
    accumulatorInstruction("ROR", 0x6A);
  }

  final public void ROR_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("ROR", 0x66, zeroPageAddress);
  }

  final public void ROR_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("ROR", 0x76, zeroPageAddress);
  }

  final public void ROR_absolute(final Object address) {
    absoluteInstruction("ROR", 0x6E, address);
  }

  final public void ROR_absoluteX(final Object address) {
    absoluteXInstruction("ROR", 0x7E, address);
  }

  final public void SBC_immediate(final Integer value) {
    immediateInstruction("SBC", 0xE9, value);
  }

  final public void SBC_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("SBC", 0xE5, zeroPageAddress);
  }

  final public void SBC_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("SBC", 0xF5, zeroPageAddress);
  }

  final public void SBC_absolute(final Object address) {
    absoluteInstruction("SBC", 0xED, address);
  }

  final public void SBC_absoluteX(final Object address) {
    absoluteXInstruction("SBC", 0xFD, address);
  }

  final public void SBC_absoluteY(final Object address) {
    absoluteYInstruction("SBC", 0xF9, address);
  }

  final public void SEC() {
    impliedInstruction("SEC", 0x38);
  }

  final public void SED() {
    impliedInstruction("SED", 0xF8);
  }

  // Set Interrupt Disable
  // Set the interrupt disable flag to one.
  final public void SEI() {
    impliedInstruction("SEI", 0x78);
  }

  final public void STA_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("STA", 0x85, zeroPageAddress);
  }

  final public void STA_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("STA", 0x95, zeroPageAddress);
  }

  final public void STA_absolute(final Object address) {
    absoluteInstruction("STA", 0x8D, address);
  }

  final public void STA_absoluteX(final Object address) {
    absoluteXInstruction("STA", 0x9D, address);
  }

  final public void STA_absoluteY(final Object address) {
    absoluteYInstruction("STA", 0x99, address);
  }

  final public void STX_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("STX", 0x86, zeroPageAddress);
  }

  final public void STX_zeroPageY(final Object zeroPageAddress) {
    zeroPageYInstruction("STX", 0x96, zeroPageAddress);
  }

  final public void STX_absolute(final Object address) {
    absoluteInstruction("STX", 0x8E, address);
  }

  final public void STY_zeroPage(final Object zeroPageAddress) {
    zeroPageInstruction("STY", 0x84, zeroPageAddress);
  }

  final public void STY_zeroPageX(final Object zeroPageAddress) {
    zeroPageXInstruction("STY", 0x94, zeroPageAddress);
  }

  final public void STY_absolute(final Object address) {
    absoluteInstruction("STY", 0x8C, address);
  }

  final public void TAX() {
    impliedInstruction("TAX", 0xAA);
  }

  final public void TAY() {
    impliedInstruction("TAY", 0xA8);
  }

  final public void TSX() {
    impliedInstruction("TSX", 0xBA);
  }

  final public void TXA() {
    impliedInstruction("TXA", 0x8A);
  }

  final public void TXS() {
    impliedInstruction("TXS", 0x9A);
  }

  final public void TYA() {
    impliedInstruction("TYA", 0x98);
  }

  ////


  final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
    for (Assembler child : children) {
      child.dump(labelToAddress, addressToData, address, stringBuilder);
      stringBuilder.append("\n");
      address += child.recursiveLength();
    }
  }

  // This routine sleeps for n cycles in the most memory efficient manner, using
  // instructions that take up the most number of cycles
  final public void sleep(int cycles) {
    assertGreaterThan(cycles, 1);
    while (cycles >= 7) {
      ASL_absoluteX(4096); // 7 cycles
      cycles -= 7;
    }
    if (cycles == 6) {
      ASL_absolute(4096); // 6 cycles
      cycles -= 6;
    } else if (cycles == 5) {
      ASL_zeroPage(128); // 5 cycles
      cycles -= 5;
    } else if (cycles == 4) {
      BIT_absolute(4096); // 4 cycles
      cycles -= 4;
    } else if (cycles == 3) {
      BIT_zeroPage(128); // 3 cycles
      cycles -= 3;
    } else if (cycles == 2) {
      NOP();
      cycles -= 2;
    } else if (cycles == 1) {
      Invariant.assertFail();
    }
    assertEquals(0, cycles);
  }
}
