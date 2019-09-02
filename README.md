# roboZen
RoboZen is a library that provides a central interface to the [Robocup Soccer Simulation League](http://wiki.robocup.org/wiki/Soccer_Simulation_League). To achieve centralized control over all clients, from within a single thread, the trainer is used.

![Alt text](/example.png)

## Requirements
- The automatic initialization of the soccer server and monitor is currently restricted to Windows.
Namely the method `edu.kit.robozen.Util.initEnvironmentWin`. Except for the initialization, roboZen works with every operating system.

- The `server::coach_w_referee` option contained in the **server.conf** needs to be enabled to allow for centralized control over the clients.
The **server.conf** is generated automatically by the RCSS server.

## Getting started
To get started follow the instruction in the [roboZen example repository](https://github.com/devgg/roboZen_examples).

## Logging
RoboZen uses the [log4j](http://logging.apache.org/log4j/2.x/index.html) logging API.   
Logging can be initialized by using the method `edu.kit.robozen.Util.initLogging`.

##License

MIT License

Copyright (c) 2016 Florian Gauger

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
