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
		if(nextMove == null) {
			System.out.println("PLAYING A RANDOM MOVE >:(");
			ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();
			for(int i=0; i<legalMoves.size(); i++) {
				SaboteurMove m = legalMoves.get(i);
				SaboteurCard card = m.getCardPlayed();
    			if(card instanceof SaboteurTile) {
    				// if we have better moves to play, remove the end pieces
    				if(card.getName().equals("Tile:1") || card.getName().equals("Tile:2") || card.getName().equals("Tile:3") || card.getName().equals("Tile:4") || card.getName().equals("Tile:11") || card.getName().equals("Tile:12") || card.getName().equals("Tile:13") || card.getName().equals("Tile:14") || card.getName().equals("Tile:15") ) {
    					legalMoves.remove(m);
    				}
    			}
    			else if(card instanceof SaboteurDrop) {
    				legalMoves.remove(m);
    			}
			}
			if(legalMoves.size() == 0) {
				legalMoves = boardState.getAllLegalMoves();
			}
			int probability = (int)(Math.random() * legalMoves.size());
			return legalMoves.get(probability);
		}
		System.out.println("RETURNING MOVE: " + nextMove.toPrettyString());
		return nextMove;
	}

	
}




