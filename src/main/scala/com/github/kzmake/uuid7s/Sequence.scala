package com.github.kzmake.uuid7s

object Sequence {
  private var pre: Long = -1L
  private var seq: Long = 0L

  private[uuid7s] def reset(): Unit = {
    pre = -1L
  }

  private[uuid7s] def get(now: Long): Long = {
    // - pre より now が(同期タイミングなどによって)小さい
    // - pre と now が等しい
    // 場合に seq を単調増加する
    if (now <= pre) {
      seq += 1
    } else {
      seq = 0
    }

    pre = now

    seq
  }
}
