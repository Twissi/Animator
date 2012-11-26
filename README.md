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

	mvn assembly:single


