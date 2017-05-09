# SACRE (Smart Adaptation through Contextual REquirements)

SACRE is an approach for effectively, efficiently and accurately supporting the adaptation of software systems’ contextual requirements in the presence of uncertainty at runtime.

This repository contains an implementation of SACRE for the smart vehicles domain. Particularly, the current configuration has been designed for detecting and correctly supporting drowsy drivers through adaptation at runtime.

Repository structure:
- SACRE: Java ME implementation of the MAPE-K adaptation feedback loop and the smart vehicle logic.
- SmartVehicleView: Java SE implementation of the smart vehicle dashboard and the street view window. Alternatively to these components the view bridge class can be used.
- ViewService: RESTful service for communicating the SmartVehicleView with SACRE.
- WekaService: RESTful service in charge of aplying data mining algorithms over monitored data (using the WekaAPI-http://www.cs.waikato.ac.nz/ml/weka/).

This implementation of SACRE is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

Main contact: Edith Zavala <zavala@essi.upc.edu>
