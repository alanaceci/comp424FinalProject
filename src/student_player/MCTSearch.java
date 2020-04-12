package student_player;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;

import java.util.*;
import java.math.*;


public class MCTSearch {
    static final int WIN_SCORE = 10;
    int level;
    int opponent;
 
    public SaboteurMove getNextMove(ClonedState state, int playerId, int agentNumber) {   
    	// RUN MCTS
    	// simulate for 2 seconds
    	long t= System.currentTimeMillis();
    	long end = t+2000;
    	// initiate root with current state
    	MCTree tree = new MCTree(state);
    	MCTNode root = tree.getRoot();
    	
    	while(System.currentTimeMillis() < end) {
    		MCTNode n = selection(root);
    		if(n.getState().checkForGameOver() == -1) {
    			expansion(n);
    		}
    		System.out.println("Finished expansion!");
    		// check if there are any children to choose for simulation
    		if (n.getChildren().size() > 0) {
    			int probability = (int) (Math.random() * n.getChildren().size());
    			MCTNode explore = n.getChildren().get(probability);
    			System.out.println("Trying to simulate now with one of children");
    			int result = simulation(explore, agentNumber);
    			backPropogation(explore, agentNumber);
    		} 
    		else { 
    			MCTNode explore = n;
    			System.out.println("Trying to simulate now with n");
    			int result = simulation(explore, agentNumber);
    			backPropogation(explore, agentNumber);
    		}
    	}
    	MCTNode bestNode = root.getBestChild();
		tree.setRoot(bestNode);
		return bestNode.getSelectedMove();
    }
    
    private MCTNode selection(MCTNode rootNode) {
        MCTNode node = rootNode;
        while (node.getChildren().size() != 0) {
            node = UCT.findBest(node);
        }
        return node;
    }
    
    
    private void expansion(MCTNode node) {
    	
        ArrayList<SaboteurMove> possibleMoves = node.getState().getLegalMoves();
        ArrayList<ClonedState> possibleStates = new ArrayList<ClonedState>();
        // generate new states by playing move
        ArrayList<SaboteurCard> hand = node.getState().getHand();
        for(int i=0; i<possibleMoves.size(); i++) {
        	SaboteurMove move = possibleMoves.get(i);
        	if(move.getCardPlayed() instanceof SaboteurDrop) {
        		possibleMoves.remove(move);
        		continue;
        	}
//        	System.out.println("ENTERED NEW MOVE");
//        	System.out.println(move.toPrettyString());
        	ClonedState s = new ClonedState(node.getState());
        	s.setHand(hand);
        	s.applyMove(move);
        	possibleStates.add(s);
        }
        // expand tree by generating moves 
        for (ClonedState state : possibleStates) {
        	 MCTNode newChild = new MCTNode(state);
             newChild.setParent(node);
             node.getChildren().add(newChild);
             node.getState().setTurnPlayer();
        }
    }
    
    public int simulation(MCTNode node, int agentNumber) {
    	System.out.println("ENTERED SIMULATION PHASE");
    	ClonedState state = node.getState();
    	MCTNode temp = new MCTNode(node);
    	// check if lost => set winScore for state as -INF
    	int game = state.checkForGameOver();
    	if(game == 1-agentNumber) {
    		temp.getParent().getState().setWinScore(Integer.MIN_VALUE);
    	}
    	while(game==-1) {
    		// game is in process, switch players
    		state.setTurnPlayer();
    		// choose random move 
    		int probability = (int) (Math.random() * state.getAllLegalMoves().size());
    		System.out.println("size of LEGAL MOVES : " + state.getAllLegalMoves().size());
    		System.out.println("probability: " + probability);
    		if(state.getAllLegalMoves().size() <1) {
    			System.out.println("no legal moves left");
    			break;
    		}
    		else {
    			SaboteurMove move = state.getAllLegalMoves().get(probability);
    			System.out.println("simulated move chosen: " + move.toPrettyString());
        		node.setSelectedMove(move);
        		state.processMove(move);
        		System.out.println("\n\nGAME: " + game + "\n\n");
        		// check if move lead to terminal state
    		}
    		game = state.checkForGameOver();
    		
    	}
    		
    	System.out.println("WINNER IS: " + game);
    	
    	// either 0, 1, 2 for players or draw
    	return game;
    }

	public void backPropogation(MCTNode node, int agentNumber) {
		MCTNode temp = node;
		while (temp != null) {
           temp.updateVisitCount();
           if(temp.getState().getTurnPlayer() == agentNumber) {
        	   temp.getState().addScore(WIN_SCORE);
           }
           temp = temp.getParent();
        }
	}
	
		
}
