Animator
========

Animator for hacklace. See hacklace.org for further information.

Installation steps
==================

1. Install and setup [maven](http://maven.apache.org/).
2. Clone this repo.
3. In a terminal, change directory where the **pom.xml** resides.
4. Run `mvn clean install eclipse:eclipse`
5. Import your project in eclipse.
6. Party!

Everytime the **pom.xml** is changed, one need to rerun 3+4.
A refresh in eclipse after it is enough.

Build
=====

Build the jar:
	mvn assembly:single

Build the documentation:
	mvn pre-site


PortInUse Exception under Mac OS
================================

If you get the error message "Error flashing hacklace: gnu.io.PortInUseException: Unknown Application" while trying to flash the hacklace try this:

1. Add directory **lock** under ~/dev
2. Set write permissions of **lock** for your user
