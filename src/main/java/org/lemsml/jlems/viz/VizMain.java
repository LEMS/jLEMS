package org.lemsml.jlems.viz;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.Main;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.out.FileResultWriterFactory;
import org.lemsml.jlems.viz.datadisplay.SwingDataViewerFactory;

public final class VizMain {

    public static final String NO_GUI_FLAG = "-nogui";

    private VizMain() {

    }

    public static void main(String[] argv) throws ConnectionError, ContentError, RuntimeError, ParseError, ParseException, BuildException, XMLException {
        
        boolean useGui = true;
        FileResultWriterFactory.initialize();
        
        if (argv.length>=2 && argv[argv.length-1].equals(NO_GUI_FLAG)) {
        	useGui = false;
        }
        
        if (useGui) SwingDataViewerFactory.initialize();
        DefaultLogger.initialize();

        Main.main(argv);
    }
}
