package com.richardfieldinggames.atari2600.developer;

final public class Invariant {
  static public void assertFail() {
    throw new Error("fail");
  }

  static public void assertTrue(final boolean value) {
    if (!(value)) {
      assertFail();
    }
  }

  static public void assertFalse(final boolean value) {
    if (!(!value)) {
      assertFail();
    }
  }

  static public void assertEquals(final long lhs, final long rhs) {
    if (!(lhs == rhs)) {
      assertFail();
    }
  }

  static public void assertNotEquals(final long lhs, final long rhs) {
    if (!(lhs != rhs)) {
      assertFail();
    }
  }

  static public void assertGreaterThan(final long lhs, final long rhs) {
    if (!(lhs > rhs)) {
      assertFail();
    }
  }

  static public void assertGreaterThanEqual(final long lhs, final long rhs) {
    if (!(lhs >= rhs)) {
      assertFail();
    }
  }

  static public void assertLessThan(final long lhs, final long rhs) {
    if (!(lhs < rhs)) {
      assertFail();
    }
  }

  static public void assertLessThanEqual(final long lhs, final long rhs) {
    if (!(lhs <= rhs)) {
      assertFail();
    }
  }

  static public void assertInInclusiveRange(final long value, final long min, final long max) {
    assertGreaterThanEqual(value, min);
    assertLessThanEqual(value, max);
  }

  static public void assertInExclusiveRange(final long value, final long min, final long max) {
    assertGreaterThanEqual(value, min);
    assertLessThan(value, max);
  }
}
