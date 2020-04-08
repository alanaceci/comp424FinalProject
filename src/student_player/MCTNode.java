package student_player;
import java.util.*;
import Saboteur.SaboteurMove;

public class MCTNode {
	public int wins;
	public int losses;
	public ArrayList<SaboteurMove> moves;
	public MCTNode parent;
	public ArrayList<MCTNode> nextMoves = new ArrayList<MCTNode>();
	
	
	public MCTNode(int win, int loss, ArrayList<SaboteurMove> nextIS) {
		this.wins = win;
		this.losses = loss;
		this.moves = nextIS;
	}
}
