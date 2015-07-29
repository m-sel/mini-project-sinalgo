# mini-project-sinalgo

How to run:

1.Enter Sinalgo main directory
2.run command line
3.execute : java -cp binaries/bin sinalgo.runtime.Main -project template
4.click f3 to generate graph
	- create 1 ManagerNode with connectivityModel:myModel
	- create N NodeP with the same connectivityModel:myModel
5.for clique click f6 otherwise tou can paint an edge between nodes by deging the mouse from one node to another
6.right click the manager node and click "start async run"
7.click on the play simbol in the right menu with big refresh rate to avoid delay 


Debugging with eclipse:
=======

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



change the port if need in the java command.


