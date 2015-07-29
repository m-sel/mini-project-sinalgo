package projects.template.nodes.nodeImplementations;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;

import projects.defaultProject.nodes.timers.MessageTimer;
import projects.template.ForestData;
import projects.template.nodes.messages.applyColorUpdateMessage;
import projects.template.nodes.messages.applyColorUpdateMessageComplete;
import projects.template.nodes.messages.applyColorUpdatefourthMessage;
import projects.template.nodes.messages.applyColorUpdatefourthMessageComplete;
import projects.template.nodes.messages.eliminationMessage;
import projects.template.nodes.messages.eliminationMessageComplete;
import projects.template.nodes.messages.firstStepMessage;
import projects.template.nodes.messages.firstStepMessageComplete;
import projects.template.nodes.messages.forestEdgeColorMessage;
import projects.template.nodes.messages.forestEdgeColorMessageComplete;
import projects.template.nodes.messages.fourthStepMessage;
import projects.template.nodes.messages.fourthStepMessageComplete;
import projects.template.nodes.messages.secondStepMessage;
import projects.template.nodes.messages.secondStepMessageComplete;
import projects.template.nodes.messages.thirdStepMessageComplete;
import projects.template.nodes.messages.threeVertexMessage;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;
import sinalgo.tools.storage.ReusableListIterator;

public class ManagerNode extends sinalgo.nodes.Node {
	
	private ReusableListIterator<Edge> edgeIteratorInstance;
	private static HashMap<Integer, HashMap<Integer,ForestData>> forests;
	private static boolean firstTime=true;
	private static boolean first = true;
	private static boolean first1 = true;
	private static Node toDeleteNode = null;
	private static int NumberOfanswers;
	private static int iteration;
	
	//for output results
	private static int delta;
	public static PrintWriter writer;
	private static int edges;
	private static StringBuilder outputString;
	
	public static PrintWriter getWriter() {
		return writer;
	}
	
	@Override
	public void handleMessages(Inbox inbox) {
		//Manager node handle the iteration
		if(firstTime){
			this.iteration = calcIters(Tools.getNodeList().size()-1);
			firstTime = false;
			broadcast(new firstStepMessage(1));
		}
		while (inbox.hasNext()) {
				Message m = inbox.next();
				if(m instanceof firstStepMessageComplete){
					waitAndResponseIfFinish(1);
				}
				if(m instanceof secondStepMessageComplete){
					waitAndResponseIfFinish(2);
				}
				if(m instanceof thirdStepMessageComplete){
					waitAndResponseIfFinish(3);
				}
				if(m instanceof applyColorUpdateMessageComplete){
					waitAndResponseIfFinish(4);
				}
				if(m instanceof fourthStepMessageComplete){
					waitAndResponseIfFinish(5);
				}
				if(m instanceof applyColorUpdatefourthMessageComplete){
					waitAndResponseIfFinish(6);
				}
				if(m instanceof eliminationMessageComplete){
					waitAndResponseIfFinish(7);
				}
				if(m instanceof forestEdgeColorMessageComplete){
					waitAndResponseIfFinish(8);
				}
				
				
				else {
					//debug	
				}
		}
		
		//debuging
//		for(Edge e:outgoingConnections){
//			NodeP p = (NodeP)e.endNode;
//			for (Edge e1:e.endNode.outgoingConnections) {
//				System.out.println(e.endNode.ID + " naighbor of "+ e1.endNode.ID);
//			}
//		}
	}



	private void waitAndResponseIfFinish(int messageType) {
		NumberOfanswers++;
		if(NumberOfanswers == Tools.getNodeList().size()-1){
			
			switch (messageType) {
			case 1:
				broadcast(new secondStepMessage(1));
				break;
			case 2:
				iteration--;
				broadcast(new threeVertexMessage(1));
				break;
			case 3:
				broadcast(new applyColorUpdateMessage(1,1));
				break;
			case 4:
				if(iteration>0)
				{
					iteration--;
					broadcast(new threeVertexMessage(1));
				}
				else{
					broadcast(new fourthStepMessage(1));
				}
				break;
			case 5:
				broadcast(new applyColorUpdatefourthMessage(1,1));
				break;
			case 6:
				broadcast(new eliminationMessage(1));
				break;
			case 7:
				broadcast(new forestEdgeColorMessage(1));
				break;
			case 8:
				removeEdgesFromManagerAndPrintResult();
				printMaximalMatch();
				try {
					writeResultsToFile();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			default:
				break;
			}
			NumberOfanswers = 0;
			
		}
	}
			
	
	
	private void writeResultsToFile() throws FileNotFoundException, UnsupportedEncodingException {
		int size;
		System.out.println(outputString.toString());
		size = Tools.getNodeList().size()-1;
		System.out.println("Number Of vertex : " + size);
		calcDeltaAndNumberOfEdges();
		System.out.println("Number Of edges : " + edges);
		System.out.println("Delta : " + delta);
		System.out.println("Running Time : " + Tools.getGlobalTime());
		System.out.println("Number Of events (Message sent from manager) : " + Tools.getNumberOfSentMessages());
		
	}



	private void calcDeltaAndNumberOfEdges() {
		for(Edge e : outgoingConnections) {
			edges += e.endNode.outgoingConnections.size()-1;
			delta = Math.max(delta,e.endNode.outgoingConnections.size()-1);
		}
		
	}



	private void printMaximalMatch() {
		int counter = 0;
		for(Edge e : outgoingConnections) {
			for (Edge e1 : ((NodeP)e.endNode).outgoingConnections) {
				if(e1.inMatch){
					System.out.println("InMatch: V: " + e1.startNode.ID + " V: " + e1.endNode.ID);
					if(e1.endNode.ID != 1){
						counter++;
						e1.startNode.setColor(Color.pink);
						e1.endNode.setColor(Color.pink);
					}
				}
			}
		}
		
		outputString.append("Maximal Match : " + counter);
	}



	private void removeEdgesFromManagerAndPrintResult() {
		HashSet<Edge> set = new HashSet<Edge>();
		int maxColor = 0 ;
		for(Edge e : outgoingConnections) {
			if(e.startNode.ID == 1){
				set.add(e);
			}
			for (Edge e1 : ((NodeP)e.endNode).outgoingConnections) {
				if(e1.endNode.ID!=1 && e1.startNode.ID!=1){
					maxColor = Math.max(maxColor, e1.getColorp());
					System.out.println("vertex " + e1.startNode.ID + 
							"---color:" + e1.getColorp()
							+ "---->" + " vertex " + e1.endNode.ID);
				}else{
					e1.defaultColor=Color.white;
				}
			}
		}
		for (Edge edge : set) {
			edge.defaultColor = Color.white;
		}
		
		maxColor++;
		outputString.append("Number of color used : " + maxColor);
		outputString.append("\n");
	}



	@Override
	public void preStep() {	
		
	}

	@Override
	public void init() {
		setColor(Color.yellow);
		outputString = new StringBuilder();
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postStep() {
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		
	}
	
	@NodePopupMethod(menuText="start async run")
	public void myPopupMethod() {

		firstStepMessage msg = new firstStepMessage(2);
		MessageTimer timer = new MessageTimer(msg);
		timer.startRelative(1, this);
	}
	
	
	
	private static int calcIters(double n) {
		int count = 0;
	    while (n >= 1) {		//check if it 2
	        n = Math.log(n) / Math.log(2);		//logb(n) = log(n) / log(b)
	        count++;
	    }
	    return count;
	}
	
}
