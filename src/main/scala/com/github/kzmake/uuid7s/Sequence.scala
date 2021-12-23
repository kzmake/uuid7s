package com.github.kzmake.uuid7s

object Sequence {
  private var pre: Long = -1L
  private var seq: Long = 0L

  def get(now: Long): Long = {
    // - pre より now が低い場合(同期タイミングによって)
    // - pre と now が同じ場合
    // に seq を単調増加する
    if (now <= pre) {
      seq += 1
    } else {
      seq = 0
    }

    pre = now

    seq
  }
}
