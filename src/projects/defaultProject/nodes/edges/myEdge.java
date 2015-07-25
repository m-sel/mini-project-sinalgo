package projects.defaultProject.nodes.edges;

import java.awt.Color;

public class myEdge extends sinalgo.nodes.edges.Edge{

	private int color;
	
	
	@Override
	public void initializeEdge() {
		// TODO Auto-generated method stub
		System.out.println("creating edge");
		this.color = 0;
		super.initializeEdge();
		
	}
	
	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return super.getColor();
	}
	
	
	public int getMyColor(){
		return this.color;
	}
	
	public void setColor(int color) {
		
		this.color = color;
	}
	
	
	
}
