package projects.template.models.connectivityModels;

import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;

public class myModel extends sinalgo.models.ConnectivityModel{

	@Override
	public boolean updateConnections(Node n) throws WrongConfigurationException {
		return true;
	}

}
