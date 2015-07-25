package projects.template.nodes.messages;

import sinalgo.nodes.messages.Message;

public class applyColorUpdateMessage extends Message {

	private int forestNum;
	private int parentID;
	
	
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}
	
	public applyColorUpdateMessage(int f,int p) {
		forestNum = f;
		parentID = p;
	}
	
	public int getParentID() {
		return parentID;
	}
	
	public int getForestNum() {
		return forestNum;
	}
	

}
