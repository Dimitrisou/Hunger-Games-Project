
/*Authors:Sourlantzis Dimitrios , AEM:9868, Phone number:6955757756 - 6948703383, e-mail:sourland@ece.auth.gr
 * 		  Sidiropoulos Evripidis, AEM:9679, Phone number:6971947855, e-mail:evripids@ece.auth.gr
 */

import java.util.*;

public class Node {
	
	Node parent;
	ArrayList<Node> children;
	int nodeDepth;
	int[] nodeMove;
	Board nodeBoard;
	double nodeEvaluation;
	
	/*
	 * ===CONSTRUCTORS===
	 */
	
	//no params
	public Node() {
		parent = null;
		children = new ArrayList<Node>();
		nodeDepth = 0;
	}
	/*
	 * @param parent the parent to be assigned to the new node
	 * @param children the children the node will have ( if it will have any)
	 * @param nodeMove an integer array including the next move and the chosen dice
	 * @param nodeBoard the cloned board used to test some moves
	 * @param nodeEvaluation the evaluation points that the move will award the player
	 */
	
	public Node(Node parent, ArrayList<Node> children, int[] nodeMove, Board nodeBoard, double nodeEvaluation) {
		this.parent = parent;
		this.children = children;
		this.nodeMove = nodeMove;
		this.nodeBoard = nodeBoard;
		this.nodeEvaluation = nodeEvaluation;
	}
	
	/*
	 * @param node the node to be cloned
	 */
	
	public Node(Node node) {
		this.parent = node.parent;
		this.children = node.children;
		this.nodeDepth = node.nodeDepth;
		this.nodeMove = node.nodeMove;
		this.nodeBoard = node.nodeBoard;
		this.nodeEvaluation = node.nodeEvaluation;
	}
	//SETTERS AND GETTERS
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setChildren(ArrayList<Node> children) {
			this.children = children;
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public void setNodeDepth(int nodeDepth) {
		this.nodeDepth = nodeDepth;
	}
	
	public int getNodeDepth() {
		return nodeDepth;
	}
	
	public void setNodeMove(int[] nodeMove) {
		this.nodeMove = nodeMove;
	}
	
	public int[] getNodeMove() {
		return nodeMove;
	}
	
	public void setNodeBoard(Board nodeBoard) {
		this.nodeBoard = nodeBoard;
	}
	
	public Board getNodeBoard() {
		return nodeBoard;
	}
	
	public void setNodeEvaluation(double nodeEvaluation) {
		this.nodeEvaluation = nodeEvaluation;
	}
	
	public double getNodeEvaluation() {
		return nodeEvaluation;
	}
}
	