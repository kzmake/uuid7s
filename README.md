# uuid7s

[![uuid7s Scala version support](https://index.scala-lang.org/kzmake/uuid7s/uuid7s/latest-by-scala-version.svg)](https://index.scala-lang.org/kzmake/uuid7s/uuid7s) [![CI](https://github.com/kzmake/uuid7s/actions/workflows/ci.yml/badge.svg)](https://github.com/kzmake/uuid7s/actions/workflows/ci.yml)

Experimental implementation of the UUID version 7 in Scala. **This is personal project.**

- [New UUID Formats](https://www.ietf.org/archive/id/draft-peabody-dispatch-new-uuid-format-01.html#name-uuidv7-layout-and-bit-order)

## Installation

```scala
libraryDependencies += "io.github.kzmake" %% "uuid7s" % "..."
```

## Usage

```scala
val s = UUID.generate().toString

println(s) // 061c8046-f151-7000-ba5c-3c953d538347
```

### Using Scastie

[Try online with Scastie](https://scastie.scala-lang.org/kzmake/VSnFlx97R42uyDgEPmfZ6Q)

### Using Ammonite REPL

```bash
$ amm
@ import $ivy.`io.github.kzmake::uuid7s:latest.integration`
@ import io.github.kzmake.uuid7s.UUID

@ (1 to 10).map(_ => UUID.generate().toString)
res: IndexedSeq[String] = Vector(
  "061c8046-f151-7000-ba5c-3c953d538347",
  "061c8046-f151-7001-867a-56c7f5084c1d",
  "061c8046-f151-7002-a68c-61367b526e5e",
  "061c8046-f152-7000-8f5f-5e6b704fb1d1",
  "061c8046-f152-7001-8c82-50ea10dc7229",
  "061c8046-f152-7002-b74a-366670d09d08",
  "061c8046-f152-7003-8ce5-f74d82bac15e",
  "061c8046-f152-7004-b822-baed58895a9e",
  "061c8046-f152-7005-a266-46e729bc6a4e",
  "061c8046-f152-7006-ada8-1e5b8b30c32e"
)
```

```bash
$ amm -s -c "import \$ivy.`io.github.kzmake::uuid7s:latest.integration`; import io.github.kzmake.uuid7s.UUID; println(UUID.generate())"
061c8046-f151-7000-ba5c-3c953d538347
```

## CLI

```bash
$ docker run -it --rm ghcr.io/kzmake/uuid7s -n 3
061c87c0-1006-7000-a49b-f706d0b7e98e
061c87c0-100f-7000-8233-15823e030f80
061c87c0-100f-7001-92c5-c3a5e22f7220

$ docker run -it --rm ghcr.io/kzmake/uuid7s 061c87c0-1006-7000-a49b-f706d0b7e98e
2021-12-26T14:28:17.006Z
```

## UUIDv7 Field and Bit Layout
### Millisecond Precision

```
     0                   1                   2                   3
     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                            unixts                             |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |unixts |         msec          |  ver  |          seq          |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |var|                         rand                              |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                             rand                              |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
```

## Benchmark

```bash
$ sbt benchmark
...
[info] Benchmark                                            Mode     Cnt        Score    Error  Units
[info] UXIDBenchmark.airframe_ULID                        sample  387547      270.747 ± 18.105  ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p0.00    sample              168.000           ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p0.50    sample              220.000           ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p0.90    sample              289.000           ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p0.95    sample              316.000           ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p0.99    sample              477.000           ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p0.999   sample             8070.464           ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p0.9999  sample            33755.699           ns/op
[info] UXIDBenchmark.airframe_ULID:airframe_ULID·p1.00    sample          1333248.000           ns/op
[info] UXIDBenchmark.java_UUID                            sample  264030      350.506 ± 23.652  ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p0.00            sample              142.000           ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p0.50            sample              289.000           ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p0.90            sample              347.000           ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p0.95            sample              364.000           ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p0.99            sample              426.000           ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p0.999           sample            11373.024           ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p0.9999          sample           235309.645           ns/op
[info] UXIDBenchmark.java_UUID:java_UUID·p1.00            sample           702464.000           ns/op
[info] UXIDBenchmark.kzmake_UUID                          sample  242593      409.998 ± 19.901  ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p0.00        sample              230.000           ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p0.50        sample              306.000           ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p0.90        sample              431.000           ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p0.95        sample              451.000           ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p0.99        sample              503.000           ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p0.999       sample            16364.992           ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p0.9999      sample           172524.749           ns/op
[info] UXIDBenchmark.kzmake_UUID:kzmake_UUID·p1.00        sample           271360.000           ns/op
```
