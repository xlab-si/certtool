# Server certificate packages

THis repository contains simple certificate packager that can bundle multiple
self-signed certificates into single key store that can be used from Java
programs.


## Usage

First, download lates jar from release section and place it anywhere on the
disk (we will assume that jar was placed in `~/certtool.jar`). Now open
terminal and run:

    $ java -jar ~/certtool.jar
    Usage:
      java -jar certtool.jar OUTPUT ALIAS1 URL1 [ALIAS2 URL2 ...]

To download self-signed certificate from `https://10.10.43.120` and place it
into `ks.jks`, run:

    $ java -jar ~/certtool.jar ks.jks dds https://10.10.43.120

It is also possible to bundle multiple certificates into single store:

    $ java -jar ~/certtool.jar ks.jks \
        dds https://10.10.43.120 \
        cfy https://10.10.43.79

And this is all there is to it.


## Preparing jar for release

Releasing new version is done by running next two commands:

    $ javac si/xlab/certtool/CertTool.java
    $ jar cvfm certool-<version>.jar Manifest si/xlab/certtool/CertTool*.class


## License

Copyright (c) 2017 XLAB d.o.o.

Licensed under the Apache License, Version 2.0 (the "License"). You
may not use this file except in compliance with the License. A copy of
the License is located at

    https://www.apache.org/licenses/LICENSE-2.0.txt

or in the "LICENSE" file accompanying this file. This file is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.
