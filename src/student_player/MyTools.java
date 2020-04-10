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
	
		ClonedState clone = new ClonedState(boardState, myCards, playerId);
		
		return boardState.getRandomMove();
	}

	
}




