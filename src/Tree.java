
public class Tree {
	Node root;
	
	public Tree() {
		
	}
	
	public Tree(Tree tree) {
		root = tree.root; 
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public Node getRoot() {
		return root;
	}
}
