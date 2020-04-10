package student_player;
import Saboteur.SaboteurMove;
import java.util.*;
import java.math.*;


public class MCTSearch {
    static final int WIN_SCORE = 10;
    int level;
    int opponent;
 
    public SaboteurMove findNextMove(ClonedState board, int playerId) {
    	long start = System.currentTimeMillis();
        long end = start + 60 * 2;
   
        MCTree tree = new MCTree();
        MCTNode rootNode = tree.getRoot();
        rootNode.setState(board);
 
        while(System.currentTimeMillis() < end) {
        	MCTNode selectedNode = selection(rootNode);
        	expansion(selectedNode, playerId);
        	if(selectedNode.getChildren().size() > 0) {
        		// select child 
        		MCTNode randomChild = selectedNode.getChild();
            	simulation(randomChild, playerId);
            	backPropogation(randomChild, playerId);
        	}
        	else {
        		// simulate 
        		simulation(selectedNode, playerId);
            	backPropogation(selectedNode, playerId);
        	}
        }
        
        return null;
    }
    
    private MCTNode selection(MCTNode rootNode) {
        MCTNode node = rootNode;
        while (node.getChildren().size() != 0) {
            node = UCT.findBest(node);
        }
        return node;
    }
    
    private void expansion(MCTNode node, int playerId) {
        ArrayList<SaboteurMove> possibleMoves = node.getState().getAllLegalMoves();
        ArrayList<ClonedState> possibleStates = new ArrayList<ClonedState>();
        for(SaboteurMove move : possibleMoves) {
        	ClonedState s = new ClonedState(node.getState());
        	s.setTurnPlayer();
        	s.processMove(move);
        	possibleStates.add(s);
        }
        for (ClonedState state : possibleStates) {
        	 MCTNode newNode = new MCTNode(state);
             newNode.setParent(node);
             node.getChildren().add(newNode);
        }
    }
    
    public int simulation(MCTNode node, int playerId) {
    	ClonedState state = node.getState();
    	MCTNode temp = new MCTNode(node);
    	// check if loss => set winScore for state as -INF
    	
    	// while (! won){
    		// simulate move 
    		// switch player
    	// } 
    	
    	// return 1 for win
    	// return -1 for loss
    	// return 0 for draw
    	return 0;
    }

	public void backPropogation(MCTNode node, int playerId) {
		MCTNode temp = node;
		while(temp!=null) {
			//update score
		        temp = temp.getParent();
		    }
	}
	
		
}



//public class MCTSearch{
//	
//	public static SaboteurMove iSMCTS(ArrayList<SaboteurMove> infoState, int levels) {
//		int wins = 0;
//		int losses = 0;
//		MCTNode root = new MCTNode();
//		MCTree tree = new MCTree(root);
//		for(int i=0;i<levels;i++) {
//			// determinization step
//			SaboteurMove nextMove = infoState.get((int)Math.random()*infoState.size());
//		}
//		
//		SaboteurMove bestMove = root.moves.get(0);
//		return bestMove;
//	}
//	
//}