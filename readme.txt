#summary readme.txt included in the download

= Java Simon - Simple Monitors for Java =

Version: 3.0.0

This software is distributed under the terms of the FSF Lesser Gnu Public License:
  * check "lgpl.txt" in the root directory of the project
  * or check it online http://www.gnu.org/licenses/lgpl.html

Check this readme online for updates: http://code.google.com/p/javasimon/wiki/Readme

== Build ==

  * Compiled Simon jars depend on:
    * JDK 1.6 or higher;
    * Java Simon Spring jar depends on `aopalliance.jar`, `commons-logging-1.1.1.jar`, `org.springframework.aop-3.0.5.RELEASE.jar` and `org.springframework.core-3.0.5.RELEASE.jar` - it is expected that these dependencies will be at least partially satisfied in a typical Spring environment.
    * Java EE module depends on Java EE 6 libs - these are always part of the EE platform
  * Use "ant" to build the Simon:
    * ant uses included `lib` directory;
    * newly built jars are placed into `build` directory;
    * new ZIP archive is placed into the main project directory.
  * Nothing special is needed for Maven build.

== Usage ==

There are two types of Simons available: `Counter` and `Stopwatch`. Counter tracks single long value, its maximum and minimum. Stopwatch measures time and tracks number of measurements (splits), total time, split minimum and maximum, etc.

=== Simon Manager ===

You obtain Simons from the `SimonManager`:
{{{
Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.HelloWorld-stopwatch");
}}}

Here we obtained stopwatch Simon. If the Simon is accessed first time it is created. If you access existing Simon, type of the Simon must match - you can't create counter with the same name (unless you destroy the Simon first).

=== Stopwatch ===

Using stopwatch is simple:
{{{
Split split = stopwatch.start(); // returns split object
// here goes the measured code
long time = split.stop(); // returns the split time in ns
}}}

After few runs of your measured code you can get additional information from stopwatch:
{{{
long totalNanos = stopwatch.getTotal();
long maxSplit = stopwatch.getMax();
long minSplit = stopwatch.getMin();
}}}

You can use convenient utility to print the results (note ns/us/ms/s unit after the number:
{{{
System.out.println("Total time: " + SimonUtils.presentNanoTime(totalNanos));
}}}

Or simply print the Simon itself, it has nice `toString` output.

== Resources ==

Project is hosted on Google Code as "javasimon":
  * Homepage: http://www.javasimon.org
  * Project page: http://code.google.com/p/javasimon/
  * Download: http://code.google.com/p/javasimon/downloads/list
  * Javadoc API: http://javasimon.googlecode.com/svn/javadoc/api-3.0/index.html
  * Source browser: http://code.google.com/p/javasimon/source/browse/
  * Issue tracker: http://code.google.com/p/javasimon/issues/list
  * Ohloh page: http://www.ohloh.net/p/javasimon

Project uses following libraries:
  * MVEL2: http://mvel.codehaus.org/ (core, not needed during runtime IF no filter callbacks are used)
    * `mvel2-2.0.19.jar`
  * TestNG: http://testng.org/ (test only)
    * `testng-5.11-jdk15.jar`
  * Spring (core/aop): http://www.springsource.org/ (Spring integration)
    * `org.springframework.core-3.0.5.RELEASE.jar`
    * `org.springframework.aop-3.0.5.RELEASE.jar`
  * AOP alliance: http://aopalliance.sourceforge.net/ (Spring integration)
    * `aopalliance.jar`
  * Commons logging: http://commons.apache.org/logging/ (Spring integration)
    * `commons-logging-1.1.1.jar`
  * AspectJ: http://www.eclipse.org/aspectj/downloads.php (AOP/Spring integration)
    * `aspectjrt.jar`
  * H2 Database: http://www.h2database.com/ (examples)
    * `h2-1.2.135.jar`
  * Java EE libraries for Java EE module compilation:
    * `servlet-api.jar`
    * `jboss-javaee.jar`

== Java Simon name ==

*Java Simon* is the official name of the project with _Simple Monitoring API_ as a subtitle. Codename of the project is *javasimon*. We use word Simon as a synonym for a "monitor" in javadoc or on our wiki - of course we use it only for monitors based on the API. We write Simon mostly with capital S, Java Simon with space and javasimon as a one word with all lowercase. Word javasimon is probably best to use in search engines.