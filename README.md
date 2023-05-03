jLEMS
=====

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.3550993.svg)](https://doi.org/10.5281/zenodo.3550993)
[![Java CI with Maven](https://github.com/LEMS/jLEMS/actions/workflows/ci.yml/badge.svg)](https://github.com/LEMS/jLEMS/actions/workflows/ci.yml)

Java Interpreter for the Low Entropy Model Specification language.

See https://docs.neuroml.org/Userdocs/Software/jLEMS.html.

For more details on LEMS see: 

Robert C. Cannon, Padraig Gleeson, Sharon Crook, Gautham Ganapathy, Boris Marin, Eugenio Piasini and R. Angus Silver, 
**LEMS: A language for expressing complex biological models in concise and hierarchical form and its use in underpinning NeuroML 2**, 
[Frontiers in Neuroinformatics 2014](http://journal.frontiersin.org/Journal/10.3389/fninf.2014.00079/abstract), doi: 10.3389/fninf.2014.00079

Quick installation
------------------

Clone this repository:

    git clone git://github.com/LEMS/jLEMS.git

Install with Maven:

    cd jLEMS
    mvn install

Run an example:

    ./lems src/test/resources/example1.xml        (Linux/Mac)
    lems.bat src\test\resources\example1.xml      (Windows)

This software is distributed under the terms of the MIT License.

