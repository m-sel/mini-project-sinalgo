package miniProject.nodes.nodeImplementations;

import projects.defaultProject.nodes.messages.IntMessage;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.messages.Inbox;

public class myNode extends sinalgo.nodes.Node {
	
	@Override
	public void handleMessages(Inbox inbox) {
		System.out.println("handle messege" + this.ID);
	}

	
	
	@Override
	public void preStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		System.out.println("init");
	}

	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		
	}
	
	@NodePopupMethod(menuText="Multicast 2")
	public void myPopupMethod() {
		IntMessage msg = new IntMessage(2);
		MessageTimer timer = new MessageTimer(msg);
		timer.startRelative(1, this);
	}
	
}
