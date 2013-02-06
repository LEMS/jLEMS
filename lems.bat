set CLASSPATH=.;.\builtjars\lems-viz.jar;%LEMS_HOME%\builtjars\lems-viz.jar

echo Running the LEMS application...

java -Xmx400M -classpath %CLASSPATH% org.lemsml.jlemsviz.VizMain %1 %2 %3 %4
