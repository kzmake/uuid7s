# uuid7s
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
