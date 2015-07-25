package projects.template.nodes.messages;

import sinalgo.nodes.messages.Message;

public class applyColorUpdateMessageComplete extends Message {

	private int forestNum;
	private int parentID;
	
	
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}
	
	public applyColorUpdateMessageComplete(int f) {
		forestNum = f;
	}
	
	public int getParentID() {
		return parentID;
	}
	
	public int getForestNum() {
		return forestNum;
	}
	

}
