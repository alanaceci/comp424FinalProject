package student_player;
import java.util.*;

import Saboteur.SaboteurMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MCTNode {
	 ClonedState state;
	 MCTNode parent;
	 ArrayList<MCTNode> children;
	 int visitCount = 0;
	 
	 public MCTNode(ClonedState state) {
	        this.state = state;
	        children = new ArrayList<>();
	    }

	 public MCTNode(ClonedState state, ArrayList<MCTNode> children, MCTNode parent) {
	        this.state = state;
	        this.parent = parent;
	        this.children = children;
	    }

	    public MCTNode(MCTNode n) {
	        this.children = new ArrayList<>();
	        this.state = new ClonedState(n.getState());
	        if (n.getParent() != null)
	            this.parent = n.getParent();
	        ArrayList<MCTNode> childArray = n.getChildren();
	        for (MCTNode child : children) {
	            this.children.add(new MCTNode(child));
	        }
	    }
	 
	  public ClonedState getState() {
		  return this.state;
	  }
	  
	  public void setState(ClonedState s) {
		  this.state = s;
	  }
	  
	  public int getVisitCount() {
		  return this.visitCount;
	  }
	  
	  public void updateVisitCount() {
		  this.visitCount++;
	  }
	  
	  public ArrayList<MCTNode> getChildren(){
		  return this.children;
	  }
	  
	  public void addChild(MCTNode m) {
		  this.children.add(m);
	  }
	  
	  public MCTNode getParent() {
	        return this.parent;
	    }
	
	  public void setParent(MCTNode m) {
		this.parent = m;
	  }
	  
	  public MCTNode getChild() {
		  int probability = (int) Math.random() * this.getChildren().size();
		  return this.getChildren().get(probability);
	  }

	}


//	public int wins;
//	public int losses;
//	public ArrayList<SaboteurMove> moves;
//	public MCTNode parent;
//	public ArrayList<MCTNode> nextMoves = new ArrayList<MCTNode>();
//	
//	
//	public MCTNode(int win, int loss, ArrayList<SaboteurMove> nextIS) {
//		this.wins = win;
//		this.losses = loss;
//		this.moves = nextIS;
//	}
//}
