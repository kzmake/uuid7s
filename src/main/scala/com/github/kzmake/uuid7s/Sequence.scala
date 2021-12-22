package com.github.kzmake.uuid7s

object Sequence {
  private var pre: Long = -1L
  private var seq: Long = 0L

  def get(now: Long): Long = {
    if (now == pre) {
      seq += 1
    } else {
      seq = 0
    }

    pre = now

    seq
  }
}
