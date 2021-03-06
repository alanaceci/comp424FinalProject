package student_player;

/* Code structure adapted from https://www.baeldung.com/java-monte-carlo-tree-search, MCTS for Tic Tac Toe */

public class MCTree {
	public MCTNode root;
	
	public MCTree (ClonedState s) {
		this.root = new MCTNode(s);
	}
	
	public MCTree(MCTNode rootNode) {
		this.root = rootNode;
	}
	
	public MCTNode getRoot() {
		return this.root;
	}
	
	public void setRoot(MCTNode m) {
		this.root = m;
	}
	
	public void addChild(MCTNode parent, MCTNode child) {
		parent.getChildren().add(child);
	}
}
