package projects.template.nodes.nodeImplementations;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import javax.print.attribute.standard.NumberOfDocuments;

import miniProject.nodes.edges.myEdge;

import com.sun.jndi.ldap.ManageReferralControl;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Step;
import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

import projects.defaultProject.nodes.messages.IntMessage;
import projects.defaultProject.nodes.timers.MessageTimer;
import projects.sample1.nodes.messages.S1Message;
import projects.sample4.nodes.nodeImplementations.S4Node;
import projects.sample4.nodes.timers.S4SendDirectTimer;
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
import projects.template.nodes.messages.myMessage;
import projects.template.nodes.messages.secondStepMessage;
import projects.template.nodes.messages.secondStepMessageComplete;
import projects.template.nodes.messages.thirdStepMessageComplete;
import projects.template.nodes.messages.threeVertexMessage;
import projects.template.nodes.messages.updateColorsMessage;
import projects.template.nodes.messages.updateParentMessage;
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
			default:
				break;
			}
			NumberOfanswers = 0;
			
		}
	}
			
	
	
	private void printMaximalMatch() {
		for(Edge e : outgoingConnections) {
			for (Edge e1 : ((NodeP)e.endNode).outgoingConnections) {
				if(e1.inMatch){
					System.out.println("InMatch: V: " + e1.startNode.ID + " V: " + e1.endNode.ID);
					if(e1.endNode.ID != 1){
						e1.startNode.setColor(Color.pink);
						e1.endNode.setColor(Color.pink);
//						e1.defaultColor= Color.pink;
				
					}
				}
			}
		}
		
	}



	private void removeEdgesFromManagerAndPrintResult() {
		int i=1;
		HashSet<Edge> set = new HashSet<Edge>();
		for(Edge e : outgoingConnections) {
			if(e.startNode.ID == 1){
				set.add(e);
			}
			for (Edge e1 : ((NodeP)e.endNode).outgoingConnections) {
				if(e1.endNode.ID!=1 && e1.startNode.ID!=1){
					System.out.println("vertex " + e1.startNode.ID + 
							"---color:" + e1.color
							+ "---->" + " vertex " + e1.endNode.ID);
				}else{
					e1.defaultColor=Color.white;
				}
			}
		}
		for (Edge edge : set) {
			edge.defaultColor = Color.white;
		}
		
	}



	@Override
	public void preStep() {	
		
	}

	@Override
	public void init() {
		setColor(Color.yellow);
		
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
