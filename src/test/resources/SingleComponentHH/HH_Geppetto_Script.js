G.addWidget(Widgets.PLOT);
Plot1.setName("Hodgkin-Huxley Spiking Neuron");
options = {yaxis:{min:-.1,max:0.1},xaxis:{min:0,max:400,show:false}};
Plot1.setOptions(options);
Plot1.setPosition(113, 90);
Plot1.setSize(230,445);
Plot1.plotData(hhcell.electrical.SimulationTree.hhpop[0].v);
G.addBrightnessFunction(hhcell.electrical, hhcell.electrical.SimulationTree.hhpop[0].v, function(x){return (x+0.07)/0.1;});
