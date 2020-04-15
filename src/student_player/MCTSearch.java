package student_player;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;

import java.util.*;
import java.math.*;

/* Code structure adapted from https://www.baeldung.com/java-monte-carlo-tree-search, MCTS for Tic Tac Toe */

public class MCTSearch {
    int idKeeper = 0;
 
    public SaboteurMove getNextMove(ClonedState state, int playerId, int agentNumber) {   
    	// RUN MCTS
    	// simulate for 2 seconds
    	long t= System.currentTimeMillis();
    	long end = t+800;
    	// initiate root of tree with current state
    	MCTree tree = new MCTree(state);
    	MCTNode root = tree.getRoot();
    	// while we have time, run simulations of MCTS
    	while(System.currentTimeMillis() < end) {
    		MCTNode n = selection(root);
    		if(n.getState().checkForGameOver() == Integer.MAX_VALUE) {
    			expansion(n);
    		}
    		// check if there are any children to choose for simulation
    		if (n.getChildren().size() > 0) {
    			// choose one child at random
    			int probability = (int) (Math.random() * n.getChildren().size());
    			MCTNode explore = n.getChildren().get(probability);
    
    			int result = simulation(explore, agentNumber);
    			backPropogation(explore, result);
    		} 
    		else { 
    			MCTNode explore = n;
    			int result = simulation(explore, agentNumber);
    			backPropogation(explore, result);
    		}
    	}
    	MCTNode bestNode = root.getBestChild();
    	if(bestNode.getState().getTurnNumber() != agentNumber) {
    		return state.getAllLegalMoves().get(0);
    	}
		tree.setRoot(bestNode);
		return bestNode.getSelectedMove();
    }
    
    private MCTNode selection(MCTNode rootNode) {
        if(rootNode.getChildren().isEmpty() || rootNode.getChildren() == null) {
        	return rootNode;
        } 
        else {
        MCTNode node = rootNode;
        while (node.getChildren().size() != 0) {
            node = UpperConfidenceTree.findBest(node);
        }
        return node;
        }
    }
    private void expansion(MCTNode node) {
    	
        ArrayList<SaboteurMove> possibleMoves = (ArrayList<SaboteurMove>) node.getState().getLegalMoves().clone();
       // ArrayList<ClonedState> possibleStates = new ArrayList<ClonedState>();
        // generate new states by playing move
        ArrayList<SaboteurCard> hand = node.getState().getHand();
        int length = possibleMoves.size();
        for(int i=0; i<length; i++) {
        	SaboteurMove move = possibleMoves.get(i);
        	ClonedState s = new ClonedState(node.getState());
        	s.setHand(hand);
        	s.applyMove(move);
        	MCTNode newChild = new MCTNode(s);
        	newChild.setSelectedMove(move);
        	newChild.setParent(node);
            node.getChildren().add(newChild);
            node.getState().setTurnPlayer();
        	
        }
    }
    
    public int simulation(MCTNode node, int agentNumber) {
    	ClonedState state = node.getState();
    	MCTNode temp = new MCTNode(node);
    	// check if lost => set winScore for state as -INF
    	int game = Integer.MAX_VALUE - 1;
    	// if opponent won, give that node a bad score
    	if(game == 1-agentNumber) {
    		temp.getParent().getState().setWinScore(Integer.MIN_VALUE);
    	}
    	
    	while(game==(Integer.MAX_VALUE - 1)) {
    		// game is in process, switch players
    		state.setTurnPlayer();
    		
    		ArrayList<SaboteurMove> legalMoves = (ArrayList<SaboteurMove>) state.getAllLegalMoves().clone();
    		if(legalMoves.size() < 1) {
    			return 2;
    		}
    		int probability = (int) (Math.random() * legalMoves.size());

    		SaboteurMove move = legalMoves.get(probability);
    		node.setId(idKeeper);
    		idKeeper++;
        	node.setSelectedMove(move);
        	state.processMove(move);
    
        	// check if move lead to terminal state
    		game = state.checkForGameOver();
    	}
    	
    	
    	// either 0, 1, 2 for players or draw
    	return game;
    }

	public void backPropogation(MCTNode node, int winningPlayer) {
		MCTNode temp = node;
		while (temp != null) {
           temp.updateVisitCount();
           if(temp.getState().getWinner() == winningPlayer) {
        	   // give it a high score
        	   temp.getState().addScore(100);
           }
           temp = temp.getParent();
        }
	}
	
		
}
