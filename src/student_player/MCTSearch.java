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
    		System.out.println("\n\n-------WE ARE SELECTING------\n\n");
    		MCTNode n = selection(root);
    		System.out.println("SELECTED NODE: " + n.getSelectedMove());
    		System.out.println("WINNER: " + n.getState().getWinner());
    		
    		if(n.getState().getWinner() == Integer.MAX_VALUE-1) {
    			System.out.println("\n\n------WE ARE EXPANDING-----\n\n");
    			expansion(n,agentNumber);
    		}
    		// check if there are any children to choose for simulation
    		if (n.getChildren().size() > 0) {
    			// choose one child at random
    			int probability = (int) (Math.random() * n.getChildren().size());
    			MCTNode explore = n.getChildren().get(probability);
    			System.out.println("ENTERING SIMULATION, chosen child node has " + explore.getState().getAllLegalMoves().size() + " legal moves");
    			int result = simulation(explore, agentNumber);
    			System.out.println("RESULT FROM SIMULATION: " + result);
    			backPropogation(explore, result);
    		} 		
    		else if(n.getState().getWinner() <=2) {
    			break;
    		}
    		
    	}
    	MCTNode bestNode = root.getBestChild();
		tree.setRoot(bestNode);
	    System.out.println("returning a move ");
	    System.out.println("BEST MOVE RETURNED: " + bestNode.getSelectedMove().toPrettyString());
		return bestNode.getSelectedMove();
    }
    
    private MCTNode selection(MCTNode rootNode) {
        if(rootNode.getChildren().isEmpty() || rootNode.getChildren() == null) {
        	System.out.println("WE EMPTY");
        	return rootNode;
        } 
        else {
        MCTNode node = rootNode;
        while (node.getChildren().size() != 0) {
        	node = UpperConfidenceTree.findBest(node);
        	
        }
        System.out.println("UCT Best Node: " + node.getSelectedMove().toPrettyString());
        return node;
        }
    }
    private void expansion(MCTNode node, int agentNumber) {
    	
        ArrayList<SaboteurMove> possibleMoves = (ArrayList<SaboteurMove>) node.getState().getLegalMoves().clone();
       // ArrayList<ClonedState> possibleStates = new ArrayList<ClonedState>();
        // generate new states by playing move
        ArrayList<SaboteurCard> hand = node.getState().getHand();
        int length = possibleMoves.size();
        for(int i=0; i<length; i++) {
        	SaboteurMove move = possibleMoves.get(i);
        	ClonedState s = new ClonedState(node.getState());
        	int opponent = 1 - agentNumber;
        	SaboteurCard card = move.getCardPlayed();
    		int[] movePos = move.getPosPlayed();
    		if(card instanceof SaboteurBonus && !(s.getNbMalus(opponent) > 0)) {
    			continue;
    		}
    		if(card instanceof SaboteurDestroy) {
    			continue;
    		}
    		if(card instanceof SaboteurTile) {
    			if(movePos[1] < 5) {
        			continue;
        		}
    			if(card.getName().equals("Tile:1")) {
    				continue;
    				
    			}
    			if(card.getName().equals("Tile:2")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:2_flip")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:3")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:3_flip")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:4")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:4_flip")){
    				continue;
    			}
    			if(card.getName().equals("Tile:11")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:11_flip")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:12")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:12_flip")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:13")) {
    				continue; 
    			}
    			if(card.getName().equals("Tile:14")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:14_flip")) {
    				continue;
    			}
    			if(card.getName().equals("Tile:15")) {
    				continue;
    			}
    		}
        	s.setHand(hand);
        	s.applyMove(move);
        	MCTNode newChild = new MCTNode(s);
        	newChild.setSelectedMove(move);
        	System.out.println("ADDED MOVE TO CHILD: " + newChild.getSelectedMove().toPrettyString());
        	newChild.setParent(node);
            node.getChildren().add(newChild);
            node.getState().setTurnPlayer();
        	
        }
    }
    
    public int simulation(MCTNode node, int agentNumber) {
    	ClonedState state = node.getState();
    	System.out.println("\n---------AT BEGINNING OF SIMULATION SELECTED MOVE FOR NODE : " + node.getSelectedMove().toPrettyString() + "-----------\n");
    	System.out.println("LEGAL MOVES AT BEGIN OF SIM: " + state.getAllLegalMoves().size());
    	MCTNode temp = new MCTNode(node);
    	// check if lost => set winScore for state as -INF
    	int game = state.checkForGameOver();
    	
    	// if opponent won, give that node a bad score
    	if(game == 1-agentNumber) {
    		temp.getParent().getState().setWinScore(Integer.MIN_VALUE);
    	}
    	
    	while(game==(Integer.MAX_VALUE - 1)) {
    		System.out.println("GAME : " + game);
    		// game is in process, switch players
    		state.setTurnPlayer();
    		//System.out.println("current player "  + state.getTurnPlayer());
    		ArrayList<SaboteurMove> legalMoves = (ArrayList<SaboteurMove>) state.getAllLegalMoves().clone();
    		//System.out.println("LEGAL MOVES" + legalMoves.size());
    		
    		int probability = (int) (Math.random() * legalMoves.size());

 
    		SaboteurMove move = legalMoves.get(probability);
    		int opponent = 1-agentNumber;
    		
    		SaboteurCard card = move.getCardPlayed();
    		System.out.println("CARD PLAYED FROM MOVE:" + card.getName());
    		int[] movePos = move.getPosPlayed();
    		if(card instanceof SaboteurBonus && !(state.getNbMalus(opponent) > 0)) {
    			legalMoves.remove(move);
    			continue;
    		}
    		if(card instanceof SaboteurDestroy) {
    			legalMoves.remove(move);
    			continue;
    		}
    		if(card instanceof SaboteurTile) {
    			if(movePos[1] < 5) {
        			legalMoves.remove(move);
        			continue;
        		}
    			if(card.getName().equals("Tile:1")) {
    				legalMoves.remove(move);
    				continue;
    				
    			}
    			if(card.getName().equals("Tile:2")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:2_flip")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:3")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:3_flip")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:4")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:4_flip")){
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:11")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:11_flip")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:12")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:12_flip")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:13")) {
    				legalMoves.remove(move);
    				continue; 
    			}
    			if(card.getName().equals("Tile:14")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:14_flip")) {
    				legalMoves.remove(move);
    				continue;
    			}
    			if(card.getName().equals("Tile:15")) {
    				legalMoves.remove(move);
    				continue;
    			}
    		}
    		
    		
    		temp.setId(idKeeper);
    		idKeeper++;
        	temp.setSelectedMove(move);
        	System.out.println("SELECTED MOVE: " + temp.getSelectedMove().toPrettyString());
        	state.processMove(move);
    
        	// check if move lead to terminal state
    		game = state.checkForGameOver();
    		
    	}
    	
    	
    	// either 0, 1, 2 for players or draw
    	return game;
    }

	public void backPropogation(MCTNode node, int winningPlayer) {
		System.out.println("back propogating");
		System.out.println("winning player " + winningPlayer);
		MCTNode temp = node;
		while (temp != null) {
           temp.updateVisitCount();
           System.out.println("state winner = " + temp.getState().getWinner());
           if(temp.getState().getWinner() == winningPlayer && winningPlayer == 0) {
        	   // give it a high score
        	   System.out.println("FOUND A WINNING NODE");
        	   temp.getState().addScore(100);
           }
           else if(temp.getState().getWinner() == winningPlayer && winningPlayer == 2) {
        	   // give it a medium score
        	   System.out.println("FOUND A TYING NODE");
        	   temp.getState().addScore(50);
           }
           temp = temp.getParent();
        }
	}
	
		
}
