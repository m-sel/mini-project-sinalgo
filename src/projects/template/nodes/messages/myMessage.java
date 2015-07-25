package projects.template.nodes.messages;

import sinalgo.nodes.messages.Message;

public class myMessage extends sinalgo.nodes.messages.Message{

	private int _data;
	
	public myMessage(int data) {
		_data = data;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
		
	}
	
	public int get_data() {
		return _data;
	}

}
