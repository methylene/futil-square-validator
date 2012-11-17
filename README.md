futil-square-validator
======================

An example project that uses [futil](https://github.com/methylene/futil). You have to download and install futil before you can build and run the main branch.

Build and run with 

	mvn clean jetty:run-exploded

Quickly update .xhtml files and static resouces (e.g. javascript) in running app:

	./explode.sh

When you enter a non-number in the input field in squareCalculator.xhtml, the validator doesn't run, and the consequently the input field doesn't get its red border to indicate a validation error (try a negative number, or leave the input field empty, to see the desired validation-fail behaviour). It can probably be fixed by replacing the Validator with a Converter, or changing the type of the request value to String, but that would be a bit tedious. Isn't there a way to tell JSF that the Validator should _always_ run?

There is also a [branch](https://github.com/methylene/futil-square-validator/tree/jsfun) that uses [Stateless JSF](http://industrieit.com/blog/2011/11/stateless-jsf-high-performance-zero-per-request-memory-overhead/). That branch has no dependency on the futil project.
