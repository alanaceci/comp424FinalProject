package student_player;
import Saboteur.SaboteurMove;
import java.util.*;
import java.math.*;

public class MCTSearch{
	
	public static SaboteurMove iSMCTS(ArrayList<SaboteurMove> infoState, int levels) {
		int wins = 0;
		int losses = 0;
		MCTNode root = new MCTNode(wins, losses, infoState);
		MCTree tree = new MCTree(root);
		for(int i=0;i<levels;i++) {
			// determinization step
			SaboteurMove nextMove = infoState.get((int)Math.random()*infoState.size());
		}
		
		SaboteurMove bestMove = root.moves.get(0);
		return bestMove;
	}
	
}