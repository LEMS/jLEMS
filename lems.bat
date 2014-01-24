@echo off

set LEMS_VERSION=0.9.5.3

set CLASSPATH=.;.\target\jlems%LEMS_VERSION%-.jar;%LEMS_HOME%\target\jlems-%LEMS_VERSION%.jar

echo Running the LEMS application...

java -Xmx400M -classpath %CLASSPATH% org.lemsml.jlems.viz.VizMain %1 %2 %3 %4
