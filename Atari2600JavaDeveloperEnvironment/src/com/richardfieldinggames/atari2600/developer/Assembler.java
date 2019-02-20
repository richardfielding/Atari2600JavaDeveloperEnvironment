package com.richardfieldinggames.atari2600.developer;

import java.util.ArrayList;
import java.util.Map;

abstract public class Assembler {
  final public Assembler parent;
  final public String label;
  final public ArrayList<Assembler> children = new ArrayList<>();
  final public int length;

  Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
    return null;
  }

  public Assembler(final Assembler parent,
                   final String label,
                   final int length) {
    this.parent = parent;
    this.label = label;
    this.length = length;

    if (parent != null) {
      parent.children.add(this);
    }
  }

  final int recursiveLength() {
    int result = length;
    for (Assembler child : children) {
      result += child.recursiveLength();
    }
    return result;
  }

  final public void compilePassOne(final Map<String, Integer> labelToAddress, int address) {
    if (label != null) {
      labelToAddress.put(label, address);
    }
    address += length;

    for (final Assembler child : children) {
      child.compilePassOne(labelToAddress, address);
      address += child.recursiveLength();
    }
  }

  final public void compilePassTwo(final Map<String, Integer> labelToAddress,
                                   final Map<Integer, Integer> addressToData,
                                   int address) {
    final Integer data = resolve(labelToAddress, address);

    if (data != null) {
      addressToData.put(address, data);
      address += length;
    }

    for (final Assembler child : children) {
      child.compilePassTwo(labelToAddress, addressToData, address);
      address += child.recursiveLength();
    }
  }

  final public String dump(final Map<String, Integer> labelToAddress,
                           final Map<Integer, Integer> addressToData,
                           int address) {
    final StringBuilder stringBuilder = new StringBuilder();
    dump(labelToAddress, addressToData, address, stringBuilder);
    return stringBuilder.toString();
  }

  abstract void dump(final Map<String, Integer> labelToAddress,
                     final Map<Integer, Integer> addressToData,
                     final int address,
                     final StringBuilder stringBuilder);

  final public void data(final String label, final int[] values) {
    label(label);

    for (int value : values) {
      new Assembler(this, null, 1) {
        Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
          return value;
        }

        final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
          formatAddress(stringBuilder, address);
          stringBuilder.append(value);
        }
      };
    }
  }

  final public void label(final String label) {
    new Assembler(this, label, 0) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        return labelToAddress.get(this);
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        stringBuilder.append("\n");
        formatAddress(stringBuilder, address);
        stringBuilder.append(label + ":");
        stringBuilder.append("\n");
      }
    };
  }

  final void accumulatorInstruction(final String name, final Integer opcode) {
    impliedInstruction(name, opcode);
  }

  final void impliedInstruction(final String name, final Integer opcode) {
    new Instruction(this, null, 1, name) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        return opcode;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        final int column1Index = stringBuilder.length();
        formatAddress(stringBuilder, address);
        stringBuilder.append(name);

        final int column2Index = pad1(stringBuilder, column1Index);
        stringBuilder.append(addressToData.get(address));

        final int column3Index = pad2(stringBuilder, column2Index);
        stringBuilder.append(toHexString(addressToData.get(address)));
      }
    };
  }

  final void immediateInstruction(final String prefix, final Integer opcode, final Integer value) {
    Invariant.assertInInclusiveRange(opcode, 0, 255);
    Invariant.assertInInclusiveRange(value, 0, 255);
    final String name = prefix + "_immediate";
    final Instruction instruction = new Instruction(this, null, 1, name) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        return opcode;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        final int column1Index = stringBuilder.length();
        formatAddress(stringBuilder, address);
        stringBuilder.append(name);
        stringBuilder.append(" ");
        stringBuilder.append(value);

        final int column2Index = pad1(stringBuilder, column1Index);
        stringBuilder.append(addressToData.get(address));
        stringBuilder.append(" ");
        stringBuilder.append(addressToData.get(address + 1));

        final int column3Index = pad2(stringBuilder, column2Index);
        stringBuilder.append(toHexString(addressToData.get(address)));
        stringBuilder.append(" ");
        stringBuilder.append(toHexString(addressToData.get(address + 1)));
      }
    };

    new Assembler(instruction, null, 1) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        return value;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        stringBuilder.append("ARSE1");
      }
    };
  }

  final void relativeInstruction(final String name, final Integer opcode, final Object relativeAddress) {
    final Instruction instruction = new Instruction(this, null, 1, name) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        return opcode;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        final int column1Index = stringBuilder.length();
        formatAddress(stringBuilder, address);
        stringBuilder.append(name);
        stringBuilder.append(" ");
        stringBuilder.append(relativeAddress);

        final int column2Index = pad1(stringBuilder, column1Index);
        stringBuilder.append(addressToData.get(address));
        stringBuilder.append(" ");
        stringBuilder.append(addressToData.get(address + 1));
        stringBuilder.append(" [");
        stringBuilder.append(((byte) (int) addressToData.get(address + 1)));
        stringBuilder.append("]");

        final int column3Index = pad2(stringBuilder, column2Index);
        stringBuilder.append(toHexString(addressToData.get(address)));
        stringBuilder.append(" ");
        stringBuilder.append(toHexString(addressToData.get(address + 1)));
      }
    };

    new Assembler(instruction, null, 1) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        // https://www.c64-wiki.com/wiki/Relative_addressing

// Relative addressing is the addressing mode used by all conditional-branch instructions in the 65xx instruction set:
// Beyond the instruction for the instruction itself, all these instructions take up a single, signed-integer byte that specifies,
// in relative terms, how far "up" or "down" to jump if the required conditions are met: This signed 8-bit figure is called the offset.
// If a branching instruction is situated at address a, and needs to branch to a given address b, the single-byte offset argument
// that should follow the instruction is calculated as b − a − 2.
// Conversely, if a branching instruction at address a has an offset argument of value F, the destination address for the branch (if taken), is a + F + 2
// The signed offset needs to fit in a single byte, so this scheme imposes a limit to how far a branching instruction can actually "branch", or jump;
// no further than 126 bytes "backwards", or 129 bytes "forwards" relative to the location of the branching instruction.
// There are eight branching instructions, all of which support only this relative addressing mode: They are BCC, BCS, BEQ, BMI, BNE, BPL, BVC, and BVS.

        Integer actualRelativeAddress = labelToAddress.get(relativeAddress);
        if (actualRelativeAddress == null) {
          actualRelativeAddress = (Integer) relativeAddress;
        }

        if (actualRelativeAddress <= address) {
          Invariant.assertLessThanEqual(address - actualRelativeAddress, 126);
        } else {
          Invariant.assertLessThanEqual(actualRelativeAddress - address, 129);
        }

        final int diff = actualRelativeAddress - address - 1;

        // http://stackoverflow.com/questions/7401550/how-to-convert-int-to-unsigned-byte-and-back
        final int result = ((byte) diff) & 0xFF;

        return result;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        formatAddress(stringBuilder, address);
        stringBuilder.append(name);
      }
    };
  }

  final public void absoluteInstruction(final String name, final Integer opcode, final Object absoluteAddress) {
    absoluteInstruction(name, "", opcode, absoluteAddress);
  }

  final public void absoluteXInstruction(final String name, final Integer opcode, final Object absoluteAddress) {
    absoluteInstruction(name, "X", opcode, absoluteAddress);
  }

  final public void absoluteYInstruction(final String name, final Integer opcode, final Object absoluteAddress) {
    absoluteInstruction(name, "X", opcode, absoluteAddress);
  }

  private void absoluteInstruction(final String prefix, final String suffix, final Integer opcode, final Object absoluteAddress) {
    final String name = prefix + "_absolute" + suffix;

    final Instruction instruction = new Instruction(this, null, 1, name) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        return opcode;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        final int column1Index = stringBuilder.length();
        formatAddress(stringBuilder, address);
        stringBuilder.append(name);
        stringBuilder.append(" ");
        stringBuilder.append(absoluteAddress);

        final int column2Index = pad1(stringBuilder, column1Index);
        stringBuilder.append(addressToData.get(address));
        stringBuilder.append(" ");
        stringBuilder.append(addressToData.get(address + 1));
        stringBuilder.append(" ");
        stringBuilder.append(addressToData.get(address + 2));

        final int column3Index = pad2(stringBuilder, column2Index);
        stringBuilder.append(toHexString(addressToData.get(address)));
        stringBuilder.append(" ");
        stringBuilder.append(toHexString(addressToData.get(address + 1)));
        stringBuilder.append(" ");
        stringBuilder.append(toHexString(addressToData.get(address + 2)));
      }
    };

    instruction.addressAssemblerLsb(absoluteAddress);
    instruction.addressAssemblerMsb(absoluteAddress);
  }

  final Assembler addressAssemblerLsb(final Object value) {
    return new Assembler(this, null, 1) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        Integer result = labelToAddress.get(value);
        if (result == null) {
          result = (Integer) value;
        }
        Invariant.assertGreaterThan(result, 255);
        result = result & 0xFF;
        Invariant.assertInInclusiveRange(result, 0, 255);
        return result;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        stringBuilder.append("ARSE4");
      }
    };
  }

  final Assembler addressAssemblerMsb(final Object value) {
    return new Assembler(this, null, 1) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        Integer result = labelToAddress.get(value);
        if (result == null) {
          result = (Integer) value;
        }
        Invariant.assertGreaterThan(result, 255);
        result = result >> 8;
        Invariant.assertInInclusiveRange(result, 0, 255);
        return result;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        stringBuilder.append("ARSE5");
      }
    };
  }

  final void zeroPageInstruction(final String prefix, final Integer opcode, final Object zeroPageAddress) {
    zeroPageInstruction(prefix, "", opcode, zeroPageAddress);
  }

  final void zeroPageXInstruction(final String prefix, final Integer opcode, final Object zeroPageAddress) {
    zeroPageInstruction(prefix, "X", opcode, zeroPageAddress);
  }

  final void zeroPageYInstruction(final String prefix, final Integer opcode, final Object zeroPageAddress) {
    zeroPageInstruction(prefix, "Y", opcode, zeroPageAddress);
  }

  private void zeroPageInstruction(final String prefix, final String suffix, final Integer opcode, final Object zeroPageAddress) {
    final String name = prefix + "_zeroPage" + suffix;
    final Instruction instruction = new Instruction(this, null, 1, name) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        return opcode;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        final int column1Index = stringBuilder.length();
        formatAddress(stringBuilder, address);
        stringBuilder.append(name);
        stringBuilder.append(" ");
        stringBuilder.append(zeroPageAddress);

        final int column2Index = pad1(stringBuilder, column1Index);
        stringBuilder.append(addressToData.get(address));
        stringBuilder.append(" ");
        stringBuilder.append(addressToData.get(address + 1));

        final int column3Index = pad2(stringBuilder, column2Index);
        stringBuilder.append(toHexString(addressToData.get(address)));
        stringBuilder.append(" ");
        stringBuilder.append(toHexString(addressToData.get(address + 1)));
      }
    };

    instruction.zeroPageAddressAssembler(zeroPageAddress);
  }

  final Assembler zeroPageAddressAssembler(final Object value) {
    return new Assembler(this, null, 1) {
      Integer resolve(final Map<String, Integer> labelToAddress, final int address) {
        Integer result = labelToAddress.get(value);
        if (result == null) {
          result = (Integer) value;
        }
        Invariant.assertInInclusiveRange(result, 0, 255);
        return result;
      }

      final void dump(final Map<String, Integer> labelToAddress, final Map<Integer, Integer> addressToData, int address, final StringBuilder stringBuilder) {
        stringBuilder.append("ARSE6");
      }
    };
  }

  final void formatAddress(final StringBuilder stringBuilder, final int address) {
    stringBuilder.append(toHexString(address));
    stringBuilder.append(" ");
    stringBuilder.append(address);
    stringBuilder.append(" : ");
  }

  final int pad1(final StringBuilder stringBuilder, final int startIndex) {
    return pad(stringBuilder, startIndex, 40);
  }

  final int pad2(final StringBuilder stringBuilder, final int startIndex) {
    return pad(stringBuilder, startIndex, 15);
  }

  final private int pad(final StringBuilder stringBuilder, final int startIndex, final int padSize) {
    final int currentIndex = stringBuilder.length();
    for (int i = currentIndex - startIndex; i < padSize; i++) {
      stringBuilder.append(" ");
    }
    return stringBuilder.length();
  }

  final String toHexString(final int value) {
    String result = Integer.toHexString(value).toUpperCase();
    if (result.length() == 1) {
      result = "0" + result;
    }
    return result;
  }
}

