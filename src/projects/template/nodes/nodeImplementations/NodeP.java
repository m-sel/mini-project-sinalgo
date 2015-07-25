package projects.template.nodes.nodeImplementations;

import java.awt.Color;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.print.attribute.standard.NumberOfDocuments;
import javax.swing.text.html.HTMLDocument.Iterator;
import javax.tools.Tool;

import org.omg.CosNaming.NamingContextPackage.NotEmpty;

import miniProject.nodes.edges.myEdge;

import com.sun.jndi.ldap.ManageReferralControl;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Step;

import projects.defaultProject.nodes.messages.IntMessage;
import projects.defaultProject.nodes.timers.MessageTimer;
import projects.sample1.nodes.messages.S1Message;
import projects.template.ForestData;
import projects.template.nodes.messages.addNodeToForestMessage;
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
import sinalgo.nodes.messages.NackBox;
import sinalgo.tools.Tools;
import sinalgo.tools.storage.ReusableListIterator;
import sun.net.www.content.audio.wav;

public class NodeP extends sinalgo.nodes.Node {
	
	private final static int MASK = 0x1;
	
	//TODO : calculate delta before - number of forests - the vertex with max edges as source
	private static int DELTA = 0;
	private static boolean first = true;
	private static Node ManagerNode;
	
	public HashMap<Integer,ForestData> forestList;
	private int ColorP;	
	private int counter;
	private boolean inMatch;
	private boolean first1;
	private HashSet<Integer> lv;

	
	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
				Message m = inbox.next();
				if(m instanceof firstStepMessage){
					//delete edges from bigger id to lower
					if(first1){
						removeEdgeFirstStep();
						first1=false;
					}
				}
				if(m instanceof secondStepMessage){
					forestCreate();
				}
				if(m instanceof updateParentMessage){
					updateParentInForests(((updateParentMessage)
							m).getForestNum(),((updateParentMessage)
									m).getParentID());
				}
				
				if(m instanceof threeVertexMessage){
					if(first){
						first=false;
						calcDelta();
					}
					updateColor();
					send(new thirdStepMessageComplete(1),ManagerNode);
				}
				if(m instanceof applyColorUpdateMessage){
					applyColorUpdate();
					//printForestData();
					send(new applyColorUpdateMessageComplete(1),ManagerNode);
				}
				if(m instanceof fourthStepMessage){
					updateColorShiftDown();
					send(new fourthStepMessageComplete(1),ManagerNode);
				}
				if(m instanceof applyColorUpdatefourthMessage){
					applyColorUpdate();
					send(new applyColorUpdatefourthMessageComplete(1,1),ManagerNode);
				}
				if(m instanceof eliminationMessage){
					if(counter>=6){
						send(new eliminationMessageComplete(1),ManagerNode);
						printForestData();
						counter=0;
					}
					else {
						eliminate(counter);
						counter++;
						send(new applyColorUpdatefourthMessageComplete(1,1),ManagerNode);	
					}
				}
				if(m instanceof forestEdgeColorMessage){
					if(counter>=3){
						send(new forestEdgeColorMessageComplete(1),ManagerNode);
					}else{
						vertexForestEdgeColor(counter);
						maximalMatch(counter);
						send(new eliminationMessageComplete(1),ManagerNode);
						counter++;
					}
				}
				else{
					//System.out.println();
				}
				
			}

		}

	

	

	private void calcDelta() {
		for (Node node : Tools.getNodeList()) {
			DELTA = Math.max(DELTA, node.outgoingConnections.size());
		}
	}


	private void maximalMatch(int color) {
		java.util.Iterator<Entry<Integer, ForestData>> it = forestList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, ForestData> data1 = (Entry<Integer, ForestData>) it.next();
			ForestData data = data1.getValue();
			if(data.getColor()==color){
				//TODO: can be more the one child
				if(data.getChild()!=0){
					NodeP child= ((NodeP)Tools.getNodeByID(data.getChild()));
					if(!this.inMatch && !child.inMatch)
					{
						this.inMatch = true;
						child.inMatch = true;
						updateEdgeInMatch(this.ID,child.ID);
						return;
					}
				}
			}
		}
		
	}
	

	private void vertexForestEdgeColor(int color) {
		java.util.Iterator<Entry<Integer, ForestData>> it = forestList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, ForestData> data1 = (Entry<Integer, ForestData>) it.next();
			ForestData data = data1.getValue();
			if(data.getColor()==color){
				//TODO: can be more the one child
				if(data.getChild()!=0){
					NodeP child= ((NodeP)Tools.getNodeByID(data.getChild()));
					int nextColor = findFreeColorForEdgeColoring(child);
					this.lv.add(nextColor);
					child.lv.add(nextColor);
					updateEdgeColor(nextColor,data.getID(),child.ID);
				}
				
				
			}
		}
		
	}
	
	private void updateEdgeInMatch(int v, int w) {
		for(Edge e : outgoingConnections) {
			if(e.startNode.ID == v &&  e.endNode.ID == w){
				e.setInMatch(true);
				return;
			}
		}
		
	}


	private int findFreeColorForEdgeColoring(NodeP child) {
		for (int i = 0; i < 2 * DELTA - 1; i++) {
			if (!this.lv.contains(i) && !child.lv.contains(i)) {
				return i;
			}
		}
		return -1;

	}


	private void updateEdgeColor(int nextColor, int v, int w) {
		int i = 1;
		for(Edge e : outgoingConnections) {
			if(e.startNode.ID == 1 || e.endNode.ID == 1)
				e.defaultColor = Color.white;
			if(e.startNode.ID == v &&  e.endNode.ID == w){
					e.setColor(nextColor);
					switch (nextColor) {
					case 0:
						e.defaultColor = Color.cyan;
						break;
					case 1:
						e.defaultColor = Color.RED;
						break;
					case 2:
						e.defaultColor = Color.GREEN;
						break;
					case 3:
						e.defaultColor = Color.yellow;
						break;
					default:
						e.defaultColor = Color.black;
						break;
					}
			}
		}
	}




	private void eliminate(int color) { 
		java.util.Iterator<Entry<Integer, ForestData>> it = forestList.entrySet().iterator();
		Set<Integer> forbiddenSet = new HashSet<Integer>();
		while (it.hasNext()) {
			Map.Entry<Integer, ForestData> data1 = (Entry<Integer, ForestData>) it.next();
			ForestData data = data1.getValue();
			if(data.getColor()==color){
				if(data.getParent()!=0)
					forbiddenSet.add(((NodeP) Tools.getNodeByID(data.getParent())).colorByForestId(data1.getKey()));
				if(data.getChild()!=0)
					forbiddenSet.add(((NodeP) Tools.getNodeByID(data.getChild())).colorByForestId(data1.getKey()));
				data.setColor(findFreeColor(forbiddenSet));
			}
			
		}
		
	}


	private int findFreeColor(Set<Integer> forbiddenSet) {
		for (int i = 0; i < DELTA; i++) {
			if(!forbiddenSet.contains(i))
				return i;
		}
		return -1;
	}


	private void printForestData() {
		java.util.Iterator<Entry<Integer, ForestData>> it = forestList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, ForestData> data1 = (Entry<Integer, ForestData>) it.next();
			ForestData data = data1.getValue();
			//System.out.println(data);
		}
	}


	private void updateColorShiftDown() {
		java.util.Iterator<Entry<Integer, ForestData>> it = forestList.entrySet().iterator();
		while (it.hasNext()) {
			
			Map.Entry<Integer, ForestData> data1 = (Entry<Integer, ForestData>) it.next();
			ForestData data = data1.getValue();
			if(data.getParent()==0){
				data.setNextColor((data.getColor() == 0) ? 1 : 0);
			}else{
				NodeP parent =(NodeP) Tools.getNodeByID(data.getParent());
				int pc =  parent.colorByForestId(data1.getKey()); 
				data.setNextColor(pc);
			}
		}
		
	}


	private void applyColorUpdate() {
		java.util.Iterator<Entry<Integer, ForestData>> it = forestList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, ForestData> data1 = (Entry<Integer, ForestData>) it.next();
			ForestData data = data1.getValue();
			data.setColor(data.getNextColor());
		}
	}


	private void updateColor() {
		java.util.Iterator<Entry<Integer, ForestData>> it = forestList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, ForestData> data1 = (Entry<Integer, ForestData>) it.next();
			ForestData data = data1.getValue();
			if(data.getParent()==0){
				data.setNextColor(getRandomBit(data.getColor()));
			}else{
				NodeP parent =(NodeP) Tools.getNodeByID(data.getParent());
				int parentColor =  parent.colorByForestId(data1.getKey()); 
				if(data.getColor()==parentColor)
					throw new ArithmeticException();
				data.setNextColor(findRigtmostBit(data.getColor(),parentColor));
			}
		}
		
	}


	public int colorByForestId(int i) {
		ForestData data = forestList.get(i);
		if(data!=null)
			return data.getColor();
		return -1;
	}
	


	private int findRigtmostBit(int vC , int pC) {
		int v = vC;
		int u = pC;
		int i = 0;
		int uMask, vMask;
		
		while (true) {
			vMask = v & MASK;
			uMask = u & MASK;
			if (uMask != vMask) {
				int ans = i;
				ans = ans << 1;
				ans += vMask;
				
				return ans;				
			}
			i++;
			v = v >> 1;
			u = u >> 1;			
		}
	}


	private int getRandomBit(int color) {
		int vMask = color & MASK;
		return vMask;
	}


	private void updateParentInForests(int forestNum, int parentID) {
		ForestData hf;
		if(forestList.containsKey(forestNum)){
			hf = forestList.get(forestNum);
		}else{
			hf = new ForestData(this.ID,0);
		}
		hf.setParent(parentID);
		forestList.put(forestNum, hf);
		
	}


	private void forestCreate() {
		int i=1;
		for(Edge e : outgoingConnections) {
			if(e.endNode.ID!=ManagerNode.ID){
				ForestData hf;
				if(forestList.containsKey(i)){
					hf = forestList.get(i);
					hf.setChild(e.endNode.ID);
				}else{
					hf = new ForestData(this.ID, e.endNode.ID);
					hf.setForestNumber(i);
				}
				forestList.put(i,hf);
				send(new updateParentMessage(i,this.ID), e.endNode);
				i++;
			}
		}
		send(new secondStepMessageComplete(1), ManagerNode);
	}


	private void removeEdgeFirstStep() {
		ArrayList<Node> removeNodesTo = new ArrayList<Node>(); 
		for(Edge e : outgoingConnections) {
			if(e.endNode.ID!=1 && this.ID>e.endNode.ID){
				removeNodesTo.add(e.endNode);
			}
		}
		for(Node n: removeNodesTo){
			outgoingConnections.remove(this, n);
		}
		broadcast(new firstStepMessageComplete(1));
	}
			

	
	
	
	@Override
	public void preStep() {	
	
	}

	@Override
	public void init() {
		//System.out.println("init");
		this.ColorP = this.ID;
		ManagerNode = Tools.getNodeByID(1);
		forestList = new HashMap<Integer, ForestData>();
		lv = new HashSet<Integer>();
		first = true;
		first1 = true;
		inMatch = false;
		counter =3;
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
//		IntMessage msg = new IntMessage(2);
//		MessageTimer timer = new MessageTimer(msg);
//		timer.startRelative(1, this);
	}
	
}
