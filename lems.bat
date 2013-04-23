set CLASSPATH=.;.\target\jlems-0.9.4.jar;%LEMS_HOME%\target\jlems-0.9.4.jar

echo Running the LEMS application...

java -Xmx400M -classpath %CLASSPATH% org.lemsml.jlems.viz.VizMain %1 %2 %3 %4
