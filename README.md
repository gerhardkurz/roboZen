# robocup
This API provides a central interface to the [Robocup Soccer Simulation League](http://wiki.robocup.org/wiki/Soccer_Simulation_League).
 It uses the trainer to control all clients from a single thread.

### Requirements
- The automatic initialization of the soccer server and monitor is currently restricted to Windows.
Namely the method `edu.kit.robocup.Util.initEnvironmentWin`. Except for the initialization, the API works with every operating system.

- The `server::coach_w_referee` option contained in the **server.conf** needs to be enabled to allow the centralized control of the clients.
The **server.conf** is generated automatically by the RCSS server.

### Getting started

### Examples
Basic and advanced policy examples can be found in the `edu.kit.robocup.example` package.

### Logging
Robocup uses the [log4j](http://logging.apache.org/log4j/2.x/index.html) logging API. 
The configuration file can be found in `src/main/resources/`.
