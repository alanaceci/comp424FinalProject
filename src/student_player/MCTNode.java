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
	 
	 public MCTNode(ClonedState state) {
	        this.state = new ClonedState(state);
	        children = new ArrayList<>();
	       
	    }

	   public MCTNode(MCTNode n) {
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
		  System.out.println("adding to visit count");
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
		  System.out.println("SETTING SELECTED MOVE OF NODE " + this.id + " TO " + m.toPrettyString());
		  this.selectedMove = m;
	  }
	  
	  public SaboteurMove getSelectedMove() {
		  return this.selectedMove;
	  }
	  
	  public MCTNode getBestChild() {
		  if(this.children.size() == 0) {
			  System.out.println("found no children / : ");
			  return this;
		  }
		  MCTNode max = this.children.get(0);
		  for (MCTNode child : this.children) {
			  if (child.getVisitCount() > max.getVisitCount()) {
				  System.out.println("found most visited child");
				  if(child.getSelectedMove().getCardPlayed() instanceof SaboteurDrop) {
					  continue;
				  }
				  max = child;
			  }
		  }
		  return max;
	  }

	}
