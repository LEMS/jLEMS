/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lemsml.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.Result;
import org.lemsml.behavior.Behavior;
import org.lemsml.behavior.OnCondition;
import org.lemsml.behavior.Record;
import org.lemsml.behavior.Run;
import org.lemsml.behavior.Show;
import org.lemsml.behavior.StateAssignment;
import org.lemsml.behavior.StateVariable;
import org.lemsml.behavior.TimeDerivative;
import org.lemsml.expression.ParseError;
import org.lemsml.run.ConnectionError;
import org.lemsml.sim.Sim;
import org.lemsml.type.Children;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentReference;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Dimension;
import org.lemsml.type.Exposure;
import org.lemsml.type.Lems;
import org.lemsml.type.Parameter;
import org.lemsml.type.Path;
import org.lemsml.type.Target;
import org.lemsml.type.Text;
import org.lemsml.type.Unit;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;

/**
 *
 * @author padraig
 */
public class ComponentTypeTest {

        public static boolean showGui = false;

        public ComponentTypeTest() {
        }



     
        /*
         * Testing creating a Lems specification/runnable model from scratch through the API
         */
        @Test public void testCreate() throws ContentError, ParseError, ConnectionError, RuntimeError, IOException {
                E.info("-------------------------------\nStarting testCreate()...");

                Lems lems = new Lems();

                Dimension time = new Dimension("time", 0, 0, 1, 0);
                lems.dimensions.add(time);

                Dimension volts = new Dimension("voltage", 1, 2, -3, -1);
                lems.dimensions.add(volts);

                Unit ms = new Unit("millisecs", "ms", time, -3, 1);
                lems.units.add(ms);
                Unit mV = new Unit("millivolts", "mV", volts, -3, 1);
                lems.units.add(mV);

                ComponentType ct = new ComponentType("IaF_CT");
                lems.addComponentType(ct);

                Parameter thresh = new Parameter("threshold", volts);
                ct.parameters.add(thresh);
                Parameter tau = new Parameter("tau", time);
                ct.parameters.add(tau);
                Parameter leak = new Parameter("leakReversal", volts);
                ct.parameters.add(leak);
                Parameter reset = new Parameter("reset", volts);
                ct.parameters.add(reset);


                Behavior b = new Behavior();
                ct.behaviors.add(b);

                Exposure vExp = new Exposure("v", volts);
                ct.exposures.add(vExp);

                StateVariable v = new StateVariable(vExp.getName(), vExp.getDimension(), vExp);
                b.stateVariables.add(v);

                //<TimeDerivative variable="v" value="(leakReversal - v) / tau"/>

                TimeDerivative td = new TimeDerivative(v.getName(), "(leakReversal - v) / tau");
                b.timeDerivatives.add(td);

                OnCondition oc = new OnCondition("v .gt. threshold");
                b.onConditions.add(oc);

                StateAssignment sa = new StateAssignment(v.getName(), reset.getName());
                oc.stateAssignments.add(sa);

                E.info("Created: "+lems.textSummary(true, true));

                Component c = new Component("IaF_1", ct);

                c.setParameter(thresh.getName(), "-50mV");
                c.setParameter(leak.getName(), "-49mV");
                c.setParameter(reset.getName(), "-70mV");
                c.setParameter(tau.getName(), "30ms");

                lems.components.add(c);


                ComponentType simCt = new ComponentType("Simulation");
                lems.addComponentType(simCt);

                Parameter length = new Parameter("length", time);
                simCt.parameters.add(length);
                Parameter step = new Parameter("step", time);
                simCt.parameters.add(step);

                ComponentReference cr = new ComponentReference("target", c.getComponentType().getName(), c.getComponentType());
                simCt.componentReferences.add(cr);

                Behavior simB = new Behavior();
                simCt.behaviors.add(simB);

                simB.stateVariables.add(new StateVariable("t", time));
                Run r = new Run(cr.getName(), "t", step.getName(), length.getName());
                simB.runs.add(r);

                simB.shows.add(new Show("displays"));


                ComponentType dispCt = new ComponentType("Display");
                lems.addComponentType(dispCt);
                dispCt.parameters.add(new Parameter("timeScale", time));
                dispCt.texts.add(new Text("title"));



                Behavior dispB = new Behavior();
                dispCt.behaviors.add(dispB);
                dispB.shows.add(new Show("lines", "timeScale"));

                ComponentType lineCt = new ComponentType("Line");
                lems.addComponentType(lineCt);
                lineCt.parameters.add(new Parameter("scale", new Dimension("*")));

                lineCt.paths.add(new Path("quantity"));
                lineCt.texts.add(new Text("color"));

                Behavior bL = new Behavior();
                lineCt.behaviors.add(bL);
                bL.records.add(new Record("quantity", "scale", "color"));
                

                dispCt.childrens.add(new Children("lines", lineCt));
                simCt.childrens.add(new Children("displays", dispCt));



                Component sim1 = new Component("sim1", simCt);
                
                sim1.setParameter(length.getName(), "400ms");
                sim1.setParameter(step.getName(), "0.1ms");
                sim1.setParameter(cr.getName(), c.getID());

                Component disp1 = new Component("disp1", dispCt);
                disp1.setParameter("timeScale", "1ms");
                disp1.setParameter("title", "Tester Frame!");


                sim1.addToChildren("displays", disp1);

                Component lineCpt = new Component("l1", lineCt);
                lineCpt.setParameter("scale", "1mV");
                lineCpt.setParameter("quantity", "v");
                lineCpt.setParameter("color", "#ee40FF");

                disp1.addToChildren("lines", lineCpt);

                lems.addComponent(sim1);

                Target dr = new Target();
                dr.component = sim1.getID();
                lems.targets.add(dr);

                lems.resolve();

                //E.info("Line: "+ lineCpt.get);

                Sim sim = new Sim(lems);

                //E.info(sim.canonicalText());

                E.info("Building model...");

                sim.build();
                sim.run(showGui);


                E.info("Done testCreate!");
        }

        public static void main(String[] args)
        {
                ComponentTypeTest ct = new ComponentTypeTest();
                showGui = true;
                Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
                MainTest.checkResults(r);

        }

}