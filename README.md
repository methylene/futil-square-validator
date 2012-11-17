futil-square-validator
======================

An example project that uses [futil](https://github.com/methylene/futil). You have to download and install futil before you can build and run the main branch.

Build and run with 

	mvn clean jetty:run-exploded

Quickly update .xhtml files and static resouces (e.g. javascript) in running app:

	./explode.sh

There is also a [branch](https://github.com/methylene/futil-square-validator/tree/jsfun) that uses [Stateless JSF](http://industrieit.com/blog/2011/11/stateless-jsf-high-performance-zero-per-request-memory-overhead/). That branch has no dependency on the futil project.

I have not figured out yet how to override JSF's default messages, see [mesg.properties](https://github.com/methylene/futil-square-validator/blob/master/src/main/resources/org/somegroup/mesg.properties). Enter a non-number in the input field, to see that the override does not work.
