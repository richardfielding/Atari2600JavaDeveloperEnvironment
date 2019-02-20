package com.richardfieldinggames.atari2600.developer;

final public class Binary {
  static public int binary(final String string) {
    Invariant.assertEquals(8, string.length());
    final int result = Integer.parseInt(string, 2);
    Invariant.assertInInclusiveRange(result, 0, 255);
    return result;
  }
}
