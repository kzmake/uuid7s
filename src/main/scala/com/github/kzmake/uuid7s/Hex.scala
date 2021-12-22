package com.github.kzmake.uuid7s

object Hex {
  def valueOf(bytes: Array[Byte]): String = bytes.map("%02x".format(_)).mkString
  def toBytes(hex: String): Array[Byte]   = hex.sliding(2, 2).map(BigInt(_, 16).toByte).toArray
}
