package student_player;
import java.util.*;

import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurDrop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* Code structure adapted from https://www.baeldung.com/java-monte-carlo-tree-search, MCTS for Tic Tac Toe */

public class MCTNode {
	 int id;
	 ClonedState state;
	 MCTNode parent;
	 ArrayList<MCTNode> children;
	 int visitCount = 0;
	 SaboteurMove selectedMove;
	 int agentNumber;
	 
	 public MCTNode(ClonedState state) {
	        this.state = new ClonedState(state);
	        children = new ArrayList<>();
	        this.agentNumber = state.getAgentNumber();
	       
	    }

	   public MCTNode(MCTNode n) {
		   	this.agentNumber = n.getState().getAgentNumber();
	        this.children = new ArrayList<>();
	        this.state = new ClonedState(n.getState());
	        if (n.getParent() != null) {
	            this.parent = n.getParent();
	        }
	        this.children = (ArrayList<MCTNode>) n.getChildren();
	    }
	    
	   public void setId (int n) {
		   this.id= n;
	   }
	   public String toString() {
		   return "Id: " + this.id + " and selectedMove " + this.selectedMove;
		 
	   }
	 
	  public ClonedState getState() {
		  return this.state;
	  }
	  
	  public void setState(ClonedState s) {
		  this.state = new ClonedState(s);
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
	  
	  public void setSelectedMove(SaboteurMove m) {
		  this.selectedMove = m;
	  }
	  
	  public SaboteurMove getSelectedMove() {
		  return this.selectedMove;
	  }
	  
	  public MCTNode getBestChild() {
		  if(this.children.size() == 0) {
			  return this;
		  }
		  MCTNode max = this.children.get(0);
		  for (MCTNode child : this.children) {
			  if (child.getVisitCount() > max.getVisitCount()) {
				  if(child.getState().getTurnPlayer() != agentNumber || child.getSelectedMove().getCardPlayed() instanceof SaboteurDrop) {
					  continue;
				  }
				  max = child;
			  }
		  }
		  return max;
	  }

	}
