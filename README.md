# roboZen
RoboZen is an API that provides a central interface to the [Robocup Soccer Simulation League](http://wiki.robocup.org/wiki/Soccer_Simulation_League). To achieve centralized control over all clients, from within a single thread, the trainer is used.

### Requirements
- The automatic initialization of the soccer server and monitor is currently restricted to Windows.
Namely the method `edu.kit.robozen.Util.initEnvironmentWin`. Except for the initialization, the API works with every operating system.

- The `server::coach_w_referee` option contained in the **server.conf** needs to be enabled to allow for centralized control over the clients.
The **server.conf** is generated automatically by the RCSS server.

### Getting started
RoboZen uses gradle to resolve dependencies. The associated wrapper can be found under `gradle/wrapper/`. The windows binaries of the robocup server and monitor are already included in the repository.

### Examples
Basic and advanced policy examples can be found in the `edu.kit.robozen.example` package.

### Logging
RoboZen uses the [log4j](http://logging.apache.org/log4j/2.x/index.html) logging API. 
The configuration file can be found in `src/main/resources/`.
