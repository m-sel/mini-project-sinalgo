package projects.template.models.connectivityModels;

import java.awt.Color;

import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.tools.Tools;
import sun.security.x509.DeltaCRLIndicatorExtension;

public class myModel extends sinalgo.models.ConnectivityModelHelper{

	private static int delta = 700;
	@Override
	protected boolean isConnected(Node from, Node to) {
//		if(from.ID==1 || to.ID==1)
//			return true;
//		
//		if(to.outgoingConnections.contains(to, from))
//			return true;
//		
//		if(from.ID <to.ID && from.outgoingConnections.size()<delta+1)
//			return true;
//		
//		return false;
		return true;
	}

	private Edge findToFromEdge(Node to, Node from) {
		for(Edge e : to.outgoingConnections) {
			if(e.endNode.ID==from.ID)
				return e;
		}
		return null;
		
	}

}
