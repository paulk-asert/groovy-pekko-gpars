# groovy-pekko-gpars

Source code for:
https://groovy.apache.org/blog/groovy-pekko-gpars

The same "hello world" actor example is implemented four ways:

| Package  | Description |
|----------|-------------|
| `pekko`  | Apache Pekko typed actors (`pekko-actor-typed`). |
| `groovy6`| Groovy 6 built-in actors from the `groovy.concurrent` package (GEP-18). |
| `activeobject`| The same example written as plain classes using Groovy 6's `@ActiveObject`/`@ActiveMethod`. |
| `gpars`  | Legacy [GPars](http://www.gpars.org/) actors, kept as a pre-Groovy-6 reference. |

Select which one to run via the `mainClass` in [build.gradle](build.gradle),
then:

```
./gradlew run
```

Tested with Groovy 6.0.0-beta-2, Pekko 1.7.0, and JDK 21 and 25.
