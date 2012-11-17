futil-square-validator
======================

This branch demonstrates, and proposes a solution for, a problem with [Stateless JSF](http://industrieit.com/blog/2011/11/stateless-jsf-high-performance-zero-per-request-memory-overhead/).

After a few GET requests, the value of an f:viewParam can not be accessed from the view anymore.

The cause appears to be that, in the case of a SJSF cache hit, all lifecycle phases after RestoreView are skipped.

Commenting out a line in RestoreViewPhase.java "fixes it" for now. I'm not quite sure what other effects this might have.

Uncomment the following line in faces-config.xml, to test the fix:

	<!-- 		<lifecycle-factory>org.meth4j.jsf.Meth4jLifecycleFactory</lifecycle-factory> -->

Build and run with 

	mvn clean jetty:run-exploded

[Lincoln Baxter](https://github.com/lincolnthree) has created a related [jira issue](http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-1055) on java.net.

I could not find a maven artifact of Stateless JSF, so I just copied its java source files to src/main/java.

The [wstate branch](https://github.com/methylene/futil-square-validator/tree/wstate) is very similar but does not use SJSF (for comparison).
