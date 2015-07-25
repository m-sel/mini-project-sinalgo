package projects.template.nodes.messages;

import projects.template.nodes.nodeImplementations.NodeP;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class addNodeToForestMessage extends Message {
	private NodeP parentNode;
	private int forestId;
	
	public addNodeToForestMessage(int forest,NodeP parent) {
		parentNode = parent;
		forestId = forest;
	}
	
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}
	
	public int getForestId() {
		return forestId;
	}
	
	public NodeP getParentNode() {
		return parentNode;
	}

}
