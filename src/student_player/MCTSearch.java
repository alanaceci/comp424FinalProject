package student_player;
import Saboteur.SaboteurMove;
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
    	System.out.println("rooooot PLAYERID: " + root.getState().getTurnPlayer());
    	
    	while(System.currentTimeMillis() < end) {
    		MCTNode n = selection(root);
    		if(n.getState().checkForGameOver() == -1) {
    			expansion(n);
    		}
    		// check if there are any children to choose for simulation
    		if (n.getChildren().size() > 0) {
    			int probability = (int) Math.random() * n.getChildren().size();
    			MCTNode explore = n.getChildren().get(probability);
    			int result = simulation(explore, agentNumber);
    			backPropogation(explore, agentNumber);
    		} 
    		else { 
    			MCTNode explore = n;
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
        for(SaboteurMove move : possibleMoves) {
        	System.out.println(move.toPrettyString());
        	ClonedState s = new ClonedState(node.getState());
        	System.out.println("cloned copy contains moveee" + s.getLegalMoves().contains(move));
        	s.processMove(move);
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
    		int probability = (int) Math.random() * state.getLegalMoves().size();
    		System.out.println(state.getLegalMoves().size());
    		System.out.println(probability);
    		SaboteurMove move = state.getLegalMoves().get(probability);
    		node.setSelectedMove(move);
    		state.processMove(move);
    		// check if move lead to terminal state
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
