# mini-project-sinalgo

#How to run:



1. In the sinalgo folder enter the command line
2. run sinalgo with the following command:
	1.java -agentlib:jdwp=transport=dt_socket,address=localhost:8000,suspend=n,server=y -Xmx800m -cp binaries/bin sinalgo.runtime.Main -project template -gen 1 template:ManagerNode Random StaticUDG -gen 4 template:NodeP Random StaticUDG
3. for debugging in eclipse :
	1. first add sinalgo to the projects directory
		* Debug configuration :
			* new remote java application
				* host:localhost
				* port:8000
	2. "Debug"


#change the port if need in the java command.



