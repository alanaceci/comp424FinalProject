package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurMalus;
import Saboteur.cardClasses.SaboteurMap;

public class MyTools {
	
	public static SaboteurMove getNextMove(SaboteurBoardState boardState, int playerId) {
		ArrayList<SaboteurMove> moves = boardState.getAllLegalMoves();
		ArrayList<SaboteurCard> myCards = boardState.getCurrentPlayerCards();
		
		int [][] board = boardState.getHiddenIntBoard();
		
		for (SaboteurMove move : moves) {
			// play maps and malus's first
			SaboteurCard curr = move.getCardPlayed();
			SaboteurCard play = null;
			if(curr instanceof SaboteurMalus) {
				System.out.println("Heyyyy we up in malus b");
				play = curr;
			}
			else if (curr instanceof SaboteurMap) {
				System.out.println("Heyyy we up in mAP b");
				play = curr;
			}
			else if(play == null) {
				// clone boardState 
				System.out.println("pid" + playerId);
				ClonedState test = new ClonedState(boardState, myCards, playerId);
				
//				test.processMove(move);
//				test.applyMove(move, test);
				return move;
			}
		}
		
		return boardState.getRandomMove();
	}
	public static SaboteurBoardState cloneState (SaboteurBoardState boardState) {
//		SaboteurBoardState board = new SaboteurBoardState();
		
		
		return null;
	}
	
}




