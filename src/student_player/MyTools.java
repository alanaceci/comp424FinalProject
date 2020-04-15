package student_player;

import java.util.ArrayList;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;
public class MyTools {
	
		public static SaboteurMove getNextMove(SaboteurBoardState boardState, int playerId) {
		ArrayList<SaboteurMove> moves = boardState.getAllLegalMoves();
		ArrayList<SaboteurCard> myCards = boardState.getCurrentPlayerCards();
		
		// check if other player played Malus and if we have Bonus 
		if(playerId == 1) {
			if(boardState.getNbMalus(0) > 1) {
				for(SaboteurMove m : moves) {
					if (m.getCardPlayed() instanceof SaboteurBonus) {
						return m;
					}
				}
			}
		}
		else {
			if(boardState.getNbMalus(1) > 1) {
				for(SaboteurMove m : moves) {
					if (m.getCardPlayed() instanceof SaboteurBonus) {
						return m;
					}
				}
			}
		}
		
		ClonedState clone = new ClonedState(boardState, playerId);
		MCTSearch searchForMove = new MCTSearch();
		SaboteurMove nextMove = searchForMove.getNextMove(clone, playerId, playerId);
		System.out.println("RETURNING MOVE: " + nextMove.toPrettyString());
		return nextMove;
	}

	
}




