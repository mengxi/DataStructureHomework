Author: Mengxi Li

To compile:
  Under linux: run
    make
  under terminal, will generate 'main.jar'

Files and Libraries:

  lib/json-simple-1.1.1.jar:
    A library provides java toolkit for JSON, see
    http://code.google.com/p/json-simple/
 
  lib/plexus-utils-1.5.6.jar
    A library provides wildcard matching to scan files under one directory. See
    http://plexus.codehaus.org/plexus-utils/apidocs/overview-summary.html

  Flags.java
    Common static flags and constants to be used as parameters accross the 
    program

  ReadWriteFile.java:
    A modular provides support for read and write files. 
    JSON format is applied to store a database to file.
    This modular can write java classes to JSON format files, as well as 
    read JSON format files to java classes

  Logging.java:
    Provides utilities to output notifications or debug information to stdout.
    Provides support for colorful output for different types of notifications.

  Interface.java:
    An interface provides utilities for human-computer interaction. To show
    options to users and to read user's input.
    The main() function is also provided here.

  VirusDatabase.java:
    The kernel of this program. Provides database utilities to store the 
    benigh/virus information; provides methods for database storage, database
    loading, virus prediction, and so on.

  prog_files/generate_prog_test.py:
    A python program to automatically generate benigh programs and virus 
    programs for testing. All benigh programs generated  are random sequences 
    of A-Z; and all virus programs generated are random sequences of a-z

  support_files/:
    Testing programs provided by TAs.

  makefile:
    The makefile for compilation.


To run:
  after make
  run java -jar main.jar

  the default database directory is the current directory.
