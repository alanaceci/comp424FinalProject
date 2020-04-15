package student_player;

import java.util.*;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;
import boardgame.Board;
import boardgame.BoardState;
import boardgame.Move;

// class variables, some taken from SaboteurBoardState.java
public class ClonedState {
public static final int BOARD_SIZE = 14;
public static final int originPos = 5;

public static final int EMPTY = -1;
public static final int TUNNEL = 1;
public static final int WALL = 0;

private SaboteurTile[][] board;
private int[][] intBoard;
private ArrayList<SaboteurCard> player1Cards; //hand of player 1
private ArrayList<SaboteurCard> player2Cards; //hand of player 2
private int player1nbMalus;
private int player2nbMalus;
private boolean[] player1hiddenRevealed = {false,false,false};
private boolean[] player2hiddenRevealed = {false,false,false};

private ArrayList<SaboteurCard> Deck; //deck form which player pick
public static final int[][] hiddenPos = {{originPos+7,originPos-2},{originPos+7,originPos},{originPos+7,originPos+2}};
protected SaboteurTile[] hiddenCards = new SaboteurTile[3];
private boolean[] hiddenRevealed = {false,false,false}; //whether hidden at pos1 is revealed, hidden at pos2 is revealed, hidden at pos3 is revealed.


private int turnPlayer;
private int turnNumber;
private int winner = Integer.MAX_VALUE-1;
private Random rand;

public int playCount;
public int winScore;

private int agentNumber;
private ArrayList<SaboteurMove> legal_moves = new ArrayList<>();

// constructor to clone state from SaboteurBoardState 
public ClonedState(SaboteurBoardState state, int agentNumber) {
		// copy board over from state
		this.board = new SaboteurTile[BOARD_SIZE][BOARD_SIZE];
		for (int i=0; i<board.length; i++) {
			for(int j=0; j<board[0].length; j++) {
				this.board[i][j] = state.getHiddenBoard()[i][j];
			}
		}
		
		// copy int board
		this.intBoard = new int[BOARD_SIZE*3][BOARD_SIZE*3];
		for (int i=0; i<intBoard.length; i++) {
			for(int j=0; j<intBoard[0].length; j++) {
				this.intBoard[i][j] = state.getHiddenIntBoard()[i][j];
			}
		}
		
		// initialize Deck 
		this.Deck = SaboteurCard.getDeck();

		// initialize hiddenCards from Board : with either be Tile:8 or hidden1 or hidden2
		for(int i = 0; i < 3; i++) {
			this.hiddenCards[i] = this.board[hiddenPos[i][0]][hiddenPos[i][1]];
		}
		
		// copy player cards
		if(agentNumber == 1) {
			this.player1Cards = (ArrayList<SaboteurCard>) state.getCurrentPlayerCards().clone();
			for (SaboteurCard card : this.player1Cards) {
				Deck.remove(card);
			}
			this.player2Cards = (ArrayList<SaboteurCard>) Deck.clone();
		}
		else {
			this.player2Cards = (ArrayList<SaboteurCard>) state.getCurrentPlayerCards().clone();
			for (SaboteurCard card : this.player2Cards) {
				Deck.remove(card);
			}
			this.player1Cards = (ArrayList<SaboteurCard>) Deck.clone();
		}
		
		this.rand = new Random(2019);
	    this.winner = Integer.MAX_VALUE;
	    this.turnPlayer = state.getTurnPlayer();
	    this.turnNumber = state.getTurnNumber();
	    this.agentNumber = agentNumber;
	    this.legal_moves = (ArrayList<SaboteurMove>) state.getAllLegalMoves().clone();

	}

// constructor to clone from existing clonedState

public ClonedState (ClonedState state) {
	this.board = new SaboteurTile[BOARD_SIZE][BOARD_SIZE];
	for (int i=0; i<board.length; i++) {
		for(int j=0; j<board[0].length; j++) {
			this.board[i][j] = state.getHiddenBoard()[i][j];
		}
	}
	
	// copy int board
	this.intBoard = new int[BOARD_SIZE*3][BOARD_SIZE*3];
	for (int i=0; i<intBoard.length; i++) {
		for(int j=0; j<intBoard[0].length; j++) {
			this.intBoard[i][j] = state.getHiddenIntBoard()[i][j];
		}
	}
	
	// initialize Deck 
	this.Deck = SaboteurCard.getDeck();
	
	// initialize hiddenCards from Board : with either be Tile:8 or hidden1 or hidden2
	for(int i = 0; i < 3; i++) {
		this.hiddenCards[i] = this.board[hiddenPos[i][0]][hiddenPos[i][1]];
	}
	
	// copy player cards
	if(agentNumber == 1) {
		this.player1Cards = (ArrayList<SaboteurCard>) state.getPlayer1Cards().clone();
		this.player2Cards = (ArrayList<SaboteurCard>) state.getPlayer2Cards().clone();
	}
	else {
		this.player2Cards = (ArrayList<SaboteurCard>) state.getPlayer2Cards().clone();
		this.player1Cards = (ArrayList<SaboteurCard>) state.getPlayer1Cards().clone();
	}
	
	this.rand = new Random(2019);
    this.winner = state.getWinner();
    this.turnPlayer = state.getTurnPlayer();
    this.turnNumber = state.getTurnNumber();
    this.agentNumber = state.getAgentNumber();
    this.winScore = state.getWinScore();
    this.legal_moves = (ArrayList<SaboteurMove>) state.getLegalMoves().clone();
	
}



public ArrayList<SaboteurMove> getLegalMoves () {
	return this.legal_moves;
	
}

public SaboteurTile[][] getHiddenBoard() {
	return this.board;
}

public int[][] getHiddenIntBoard() {
	return this.intBoard;
}

public ArrayList<SaboteurCard> getPlayer2Cards() {
	return this.player2Cards;
}

public ArrayList<SaboteurCard> getPlayer1Cards() {
	return this.player1Cards;
}

public int getAgentNumber() {
	return this.agentNumber;
}

public int getTurnNumber() {
	return this.turnNumber;
}

public int getWinScore() {
	return this.winScore;
}

public int getTurnPlayer() {
	return this.turnPlayer;
}

public int getWinner() {
	return this.winner;
}

public void setPlayCount(int visitCount) {
    this.playCount = visitCount;
}

public  void setWinScore(int winScore) {
	this.winScore = winScore;
}

public  void addVisit() {
	this.playCount++;
}

public void addScore(double score) {
	if (this.winScore != Integer.MIN_VALUE) {
	    this.winScore += score;
	}
}

public void setTurnPlayer() {
	this.turnPlayer = 1- this.turnPlayer;
}

public int checkForGameOver() {
	if(pathToGoldFound(new SaboteurTile[]{new SaboteurTile("nugget"),new SaboteurTile("hidden1"),new SaboteurTile("hidden2")})) {
		System.out.println("path to gold found");
		this.winner = this.turnPlayer;
	}
	if(gameOver() && this.winner == Integer.MAX_VALUE-1 ) {
		this.winner = 2;
	}
	return this.winner;
}


public boolean gameOver() {
    return this.player1Cards.size()==0 || this.player2Cards.size()==0 || this.winner != Integer.MAX_VALUE - 1;
}
 
public void applyMove(SaboteurMove m) throws IllegalArgumentException {
	 if (!isLegal(m)) {
	        this.legal_moves.remove(m);
	        return;
	    }
	 SaboteurCard testCard = m.getCardPlayed();
	 int[] pos = m.getPosPlayed();
	 // if it is a tile, place the tile at the position played and remove from legal moves
	 if(testCard instanceof SaboteurTile){
	        this.board[pos[0]][pos[1]] = new SaboteurTile(((SaboteurTile) testCard).getIdx());
	        this.legal_moves.remove(m);
	        return;
	    }
	 else if (testCard instanceof SaboteurBonus) {
		 if(turnPlayer==1){
	            player1nbMalus --;
	            this.legal_moves.remove(m);
	            return;
		 }
		 else{
	            player2nbMalus --;
	            this.legal_moves.remove(m);
	            return;
		 }
	 }
	 
	 else if  (testCard instanceof SaboteurMalus){
	        if(turnPlayer==1){
	            player2nbMalus ++;
	            this.legal_moves.remove(m);
	            return;
	        }
	        else{
	            player1nbMalus ++;
	            this.legal_moves.remove(m);
	            return;
	        }
	    }
	   
	 else if(testCard instanceof SaboteurMap){
	        if(turnPlayer==1){
	            for(SaboteurCard card : this.player1Cards) {
	                if (card instanceof SaboteurMap) {
	                    int ph = 0;
	                    for(int j=0;j<3;j++) {
	                        if (pos[0] == hiddenPos[j][0] && pos[1] == hiddenPos[j][1]) ph=j;
	                    }
	                    this.player1hiddenRevealed[ph] = true;
	                    break; //leave the loop....
	                }
	            }
	            this.legal_moves.remove(m);
	            return;
	        }
	        else {
	            for(SaboteurCard card : this.player2Cards) {
	                if (card instanceof SaboteurMap) {
	                    int ph = 0;
	                    for(int j=0;j<3;j++) {
	                        if (pos[0] == hiddenPos[j][0] && pos[1] == hiddenPos[j][1]) ph=j;
	                    }
	                    this.player2hiddenRevealed[ph] = true;
	                    break; //leave the loop....
	                }
	            }
	            this.legal_moves.remove(m);
	            return;
	        }
	    }
	 else if (testCard instanceof SaboteurDestroy) {
	        int i = pos[0];
	        int j = pos[1];
	        if(turnPlayer==1){
	            for(SaboteurCard card : this.player1Cards) {
	                if (card instanceof SaboteurDestroy) {
	                    this.board[i][j] = null;
	                    this.legal_moves.remove(m);
	                    break; //leave the loop....
	                }
	            }
	        }
	        else{
	            for(SaboteurCard card : this.player2Cards) {
	                if (card instanceof SaboteurDestroy) {
	                    this.board[i][j] = null;
	                    this.legal_moves.remove(m);
	                    break; //leave the loop....
	                }
	            }
	        }
	        
	    }
     else if(testCard instanceof SaboteurDrop){
        this.legal_moves.remove(m);
        return;
     }

	 
}

public void processMove(SaboteurMove m) throws IllegalArgumentException {
	
    if (!isLegal(m)) {

        throw new IllegalArgumentException("Invalid move. Move: " + m.toPrettyString());
    }

    SaboteurCard testCard = m.getCardPlayed();
    int[] pos = m.getPosPlayed();

    if(testCard instanceof SaboteurTile){
        this.board[pos[0]][pos[1]] = new SaboteurTile(((SaboteurTile) testCard).getIdx());
        if(turnPlayer==1){
            //Remove from the player card the card that was used.
            for(SaboteurCard card : this.player1Cards) {
                if (card instanceof SaboteurTile) {
                    if (((SaboteurTile) card).getIdx().equals(((SaboteurTile) testCard).getIdx())) {
                        this.player1Cards.remove(card);
                        break; //leave the loop....
                    }
                    else if(((SaboteurTile) card).getFlipped().getIdx().equals(((SaboteurTile) testCard).getIdx())) {
                        this.player1Cards.remove(card);
                        break; //leave the loop....
                    }
                }
            }
        }
        else {
            for (SaboteurCard card : this.player2Cards) {
                if (card instanceof SaboteurTile) {
                    if (((SaboteurTile) card).getIdx().equals(((SaboteurTile) testCard).getIdx())) {
                        this.player2Cards.remove(card);
                        break; //leave the loop....
                    }
                    else if(((SaboteurTile) card).getFlipped().getIdx().equals(((SaboteurTile) testCard).getIdx())) {
                        this.player2Cards.remove(card);
                        break; //leave the loop....
                    }
                }
            }
        }
    }
    else if(testCard instanceof SaboteurBonus){
        if(turnPlayer==1){
            player1nbMalus --;
            for(SaboteurCard card : this.player1Cards) {
                if (card instanceof SaboteurBonus) {
                    this.player1Cards.remove(card);
                    break; //leave the loop....
                }
            }
        }
        else{
            player2nbMalus --;
            for(SaboteurCard card : this.player2Cards) {
                if (card instanceof SaboteurBonus) {
                    this.player2Cards.remove(card);
                    break; //leave the loop....
                }
            }
        }
    }
    else if(testCard instanceof SaboteurMalus){
        if(turnPlayer==1){
            player2nbMalus ++;
            for(SaboteurCard card : this.player1Cards) {
                if (card instanceof SaboteurMalus) {
                    this.player1Cards.remove(card);
                    break; //leave the loop....
                }
            }
        }
        else{
            player1nbMalus ++;
            for(SaboteurCard card : this.player2Cards) {
                if (card instanceof SaboteurMalus) {
                    this.player2Cards.remove(card);
                    break; //leave the loop....
                }
            }
        }
    }
    else if(testCard instanceof SaboteurMap){
        if(turnPlayer==1){
            for(SaboteurCard card : this.player1Cards) {
                if (card instanceof SaboteurMap) {
                    this.player1Cards.remove(card);
                    int ph = 0;
                    for(int j=0;j<3;j++) {
                        if (pos[0] == hiddenPos[j][0] && pos[1] == hiddenPos[j][1]) ph=j;
                    }
                    this.player1hiddenRevealed[ph] = true;
                    break; //leave the loop....
                }
            }
        }
        else{
            for(SaboteurCard card : this.player2Cards) {
                if (card instanceof SaboteurMap) {
                    this.player2Cards.remove(card);
                    int ph = 0;
                    for(int j=0;j<3;j++) {
                        if (pos[0] == hiddenPos[j][0] && pos[1] == hiddenPos[j][1]) ph=j;
                    }
                    this.player2hiddenRevealed[ph] = true;
                    break; //leave the loop....
                }
            }
        }
    }
    else if (testCard instanceof SaboteurDestroy) {
        int i = pos[0];
        int j = pos[1];
        if(turnPlayer==1){
            for(SaboteurCard card : this.player1Cards) {
                if (card instanceof SaboteurDestroy) {
                    this.player1Cards.remove(card);
                    this.board[i][j] = null;
                    break; //leave the loop....
                }
            }
        }
        else{
            for(SaboteurCard card : this.player2Cards) {
                if (card instanceof SaboteurDestroy) {
                    this.player2Cards.remove(card);
                    this.board[i][j] = null;
                    break; //leave the loop....
                }
            }
        }
    }
    else if(testCard instanceof SaboteurDrop){
        if(turnPlayer==1) this.player1Cards.remove(pos[0]);
        else this.player2Cards.remove(pos[0]);
    }
    turnNumber++;
}


// MODIFIED FROM SABOTEURBOARDSTATE
private boolean pathToGoldFound(SaboteurTile[] objectives){
    this.getHiddenIntBoard(); //update the int board.
    boolean atLeastOnefound = false;
    for(SaboteurTile target : objectives){
        ArrayList<int[]> originTargets = new ArrayList<>();
        originTargets.add(new int[]{originPos,originPos}); //the starting points
        //get the target position
        int[] targetPos = {0,0};
        int currentTargetIdx = -1;
        for(int i =0;i<3;i++){
            if(this.hiddenCards[i].getIdx().equals(target.getIdx())){
                targetPos = SaboteurBoardState.hiddenPos[i];
                currentTargetIdx = i;
                break;
            }
        }
        if (cardPath(originTargets, targetPos, true)) { //checks that there is a cardPath
            //next: checks that there is a path of ones.
            ArrayList<int[]> originTargets2 = new ArrayList<>();
            //the starting points
            originTargets2.add(new int[]{originPos*3+1, originPos*3+1});
            originTargets2.add(new int[]{originPos*3+1, originPos*3+2});
            originTargets2.add(new int[]{originPos*3+1, originPos*3});
            originTargets2.add(new int[]{originPos*3, originPos*3+1});
            originTargets2.add(new int[]{originPos*3+2, originPos*3+1});
            //get the target position in 0-1 coordinate
            int[] targetPos2 = {targetPos[0]*3+1, targetPos[1]*3+1};
            if (cardPath(originTargets2, targetPos2, false)) {
               // System.out.println("0-1 path found");
                atLeastOnefound =true;
            }
            else{
                //System.out.println("0-1 path was not found");
            }
        }
    }
    return atLeastOnefound;
}

// TAKEN FROM SABOTEURBOARDSTATE
// check if there is a cardPath
private Boolean cardPath(ArrayList<int[]> originTargets,int[] targetPos,Boolean usingCard){
    ArrayList<int[]> queue = new ArrayList<>(); //will store the current neighboring tile. Composed of position (int[]).
    ArrayList<int[]> visited = new ArrayList<int[]>(); //will store the visited tile with an Hash table where the key is the position the board.
    visited.add(targetPos);
    if(usingCard) addUnvisitedNeighborToQueue(targetPos,queue,visited,BOARD_SIZE,usingCard);
    else addUnvisitedNeighborToQueue(targetPos,queue,visited,BOARD_SIZE*3,usingCard);
    while(queue.size()>0){
        int[] visitingPos = queue.remove(0);
        if(containsIntArray(originTargets,visitingPos)){
            return true;
        }
        visited.add(visitingPos);
        if(usingCard) addUnvisitedNeighborToQueue(visitingPos,queue,visited,BOARD_SIZE,usingCard);
        else addUnvisitedNeighborToQueue(visitingPos,queue,visited,BOARD_SIZE*3,usingCard);
//        System.out.println(queue.size());
    }
    return false;
}
// TAKEN FROM SABOTEURBOARDSTATE
private void addUnvisitedNeighborToQueue(int[] pos,ArrayList<int[]> queue, ArrayList<int[]> visited,int maxSize,boolean usingCard){
    int[][] moves = {{0, -1},{0, 1},{1, 0},{-1, 0}};
    int i = pos[0];
    int j = pos[1];
    for (int m = 0; m < 4; m++) {
        if (0 <= i+moves[m][0] && i+moves[m][0] < maxSize && 0 <= j+moves[m][1] && j+moves[m][1] < maxSize) { //if the hypothetical neighbor is still inside the board
            int[] neighborPos = new int[]{i+moves[m][0],j+moves[m][1]};
            if(!containsIntArray(visited,neighborPos)){
                if(usingCard && this.board[neighborPos[0]][neighborPos[1]]!=null) queue.add(neighborPos);
                else if(!usingCard && this.intBoard[neighborPos[0]][neighborPos[1]]==1) queue.add(neighborPos);
            }
        }
    }
}
// TAKEN FROM SABOTEURBOARDSTATE
private boolean containsIntArray(ArrayList<int[]> a,int[] o){ //the .equals used in Arraylist.contains is not working between arrays..
    if (o == null) {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) == null)
                return true;
        }
    } else {
        for (int i = 0; i < a.size(); i++) {
            if (Arrays.equals(o, a.get(i)))
                return true;
        }
    }
    return false;
}

// TAKEN FROM SABOTEURBOARDSTATE 
public ArrayList<SaboteurMove> getAllLegalMoves() {
    // Given the current player hand, gives back all legal moves he can play.
    ArrayList<SaboteurCard> hand;
    boolean isBlocked;
    if(turnPlayer == 1){
        hand = this.player1Cards;
        isBlocked= player1nbMalus > 0;
    }
    else {
        hand = this.player2Cards;
        isBlocked= player2nbMalus > 0;
    }

    ArrayList<SaboteurMove> legalMoves = new ArrayList<>();

    for(SaboteurCard card : hand){
        if( card instanceof SaboteurTile && !isBlocked) {
            ArrayList<int[]> allowedPositions = possiblePositions((SaboteurTile)card);
            for(int[] pos:allowedPositions){
                legalMoves.add(new SaboteurMove(card,pos[0],pos[1],turnPlayer));
            }
            //if the card can be flipped, we also had legal moves where the card is flipped;
            if(SaboteurTile.canBeFlipped(((SaboteurTile)card).getIdx())){
                SaboteurTile flippedCard = ((SaboteurTile)card).getFlipped();
                ArrayList<int[]> allowedPositionsflipped = possiblePositions(flippedCard);
                for(int[] pos:allowedPositionsflipped){
                    legalMoves.add(new SaboteurMove(flippedCard,pos[0],pos[1],turnPlayer));
                }
            }
        }
        else if(card instanceof SaboteurBonus){
            if(turnPlayer ==1){
                if(player1nbMalus > 0) legalMoves.add(new SaboteurMove(card,0,0,turnPlayer));
            }
            else if(player2nbMalus>0) legalMoves.add(new SaboteurMove(card,0,0,turnPlayer));
        }
        else if(card instanceof SaboteurMalus){
            legalMoves.add(new SaboteurMove(card,0,0,turnPlayer));
        }
        else if(card instanceof SaboteurMap){
            for(int i =0;i<3;i++){ //for each hidden card that has not be revealed, we can still take a look at it.
                if(! this.hiddenRevealed[i]) legalMoves.add(new SaboteurMove(card,hiddenPos[i][0],hiddenPos[i][1],turnPlayer));
            }
        }
        else if(card instanceof SaboteurDestroy){
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) { //we can't destroy an empty tile, the starting, or final tiles.
                    if(this.board[i][j] != null && (i!=originPos || j!= originPos) && (i != hiddenPos[0][0] || j!=hiddenPos[0][1] )
                       && (i != hiddenPos[1][0] || j!=hiddenPos[1][1] ) && (i != hiddenPos[2][0] || j!=hiddenPos[2][1] ) ){
                        legalMoves.add(new SaboteurMove(card,i,j,turnPlayer));
                    }
                }
            }
        }
    }
    // we can also drop any of the card in our hand
    for(int i=0;i<hand.size();i++) {
        legalMoves.add(new SaboteurMove(new SaboteurDrop(), i, 0, turnPlayer));
    }
    return legalMoves;
}

public void setHand(ArrayList<SaboteurCard> cards) {
	if(this.agentNumber == 1) {
		this.player1Cards = cards;
	}
	this.player2Cards = cards;
}

public ArrayList<SaboteurCard> getHand () {
	if(this.agentNumber == 1) {
		return this.player1Cards;
	}
	return this.player2Cards;
}


// TAKEN FROM SABOTEURBOARDSTATE 
public boolean isLegal(SaboteurMove m) {
    // For a move to be legal, the player must have the card in its hand
    // and then the game rules apply.
    // Note that we do not test the flipped version. To test it: use the flipped card in the SaboteurMove object.

    SaboteurCard testCard = m.getCardPlayed();
    int[] pos = m.getPosPlayed();

    
    ArrayList<SaboteurCard> hand;
    boolean isBlocked;
    
    if(turnPlayer == 1){
        hand = this.player1Cards;
        isBlocked= player1nbMalus > 0;
    }
    else {
        hand = this.player2Cards;
        isBlocked= player2nbMalus > 0;
    }
    if(testCard instanceof SaboteurDrop){
        if(hand.size()>=pos[0]){
            return true;
        }
    }
    boolean legal = false;
    
    for(SaboteurCard card : hand){
    	
        if (card instanceof SaboteurTile && testCard instanceof SaboteurTile) {
            if(((SaboteurTile) card).getIdx().equals(((SaboteurTile) testCard).getIdx())){
            	boolean x = verifyLegit(((SaboteurTile) card).getPath(),pos);
                return x;
            }
            else if(((SaboteurTile) card).getFlipped().getIdx().equals(((SaboteurTile) testCard).getIdx())){
            	boolean x = verifyLegit(((SaboteurTile) card).getFlipped().getPath(),pos);
                return x;
            }
        }
        else if (card instanceof SaboteurBonus && testCard instanceof SaboteurBonus) {
            if (turnPlayer == 1) {
                if (player1nbMalus > 0) return true;
            } else if (player2nbMalus > 0) return true;
            return false;
        }
        else if (card instanceof SaboteurMalus && testCard instanceof SaboteurMalus ) {
            return true;
        }
        else if (card instanceof SaboteurMap && testCard instanceof SaboteurMap) {
            int ph = 0;
            for(int j=0;j<3;j++) {
                if (pos[0] == hiddenPos[j][0] && pos[1] == hiddenPos[j][1]) ph=j;
            }
            if (!this.hiddenRevealed[ph]) {
            	return true;
            }

        }
        else if (card instanceof SaboteurDestroy && testCard instanceof SaboteurDestroy) {
            int i = pos[0];
            int j = pos[1];
            if (this.board[i][j] != null && (i != originPos || j != originPos) && (i != hiddenPos[0][0] || j != hiddenPos[0][1])
                    && (i != hiddenPos[1][0] || j != hiddenPos[1][1]) && (i != hiddenPos[2][0] || j != hiddenPos[2][1])) {
                return true;
            }
        }
    }
    return legal;
}

// TAKEN FROM SABOTEURBOARDSTATE 
public boolean verifyLegit(int[][] path,int[] pos){
    // Given a tile's path, and a position to put this path, verify that it respects the rule of positionning;
    if (!(0 <= pos[0] && pos[0] < BOARD_SIZE && 0 <= pos[1] && pos[1] < BOARD_SIZE)) {
        return false;
    }
    if(board[pos[0]][pos[1]] != null) {
    	return false;
    }

    //the following integer are used to make sure that at least one path exists between the possible new tile to be added and existing tiles.
    // There are 2 cases:  a tile can't be placed near an hidden objective and a tile can't be connected only by a wall to another tile.
    int requiredEmptyAround=4;
    int numberOfEmptyAround=0;

    ArrayList<SaboteurTile> objHiddenList=new ArrayList<>();
    for(int i=0;i<3;i++) {
        if (!hiddenRevealed[i]){
            objHiddenList.add(this.board[hiddenPos[i][0]][hiddenPos[i][1]]);
        }
    }
    //verify left side:
    if(pos[1]>0) {
        SaboteurTile neighborCard = this.board[pos[0]][pos[1] - 1];
        if (neighborCard == null) numberOfEmptyAround += 1;
        else if(objHiddenList.contains(neighborCard)) requiredEmptyAround -= 1;
        else {
            int[][] neighborPath = neighborCard.getPath();
            if (path[0][0] != neighborPath[2][0] || path[0][1] != neighborPath[2][1] || path[0][2] != neighborPath[2][2] ) return false;
            else if(path[0][0] == 0 && path[0][1]== 0 && path[0][2] ==0 ) numberOfEmptyAround +=1;
        }
    }
    else numberOfEmptyAround+=1;

    //verify right side
    if(pos[1]<BOARD_SIZE-1) {
        SaboteurTile neighborCard = this.board[pos[0]][pos[1] + 1];
        if (neighborCard == null) numberOfEmptyAround += 1;
        else if(objHiddenList.contains(neighborCard)) requiredEmptyAround -= 1;
        else {
            int[][] neighborPath = neighborCard.getPath();
            if (path[2][0] != neighborPath[0][0] || path[2][1] != neighborPath[0][1] || path[2][2] != neighborPath[0][2]) return false;
            else if(path[2][0] == 0 && path[2][1]== 0 && path[2][2] ==0 ) numberOfEmptyAround +=1;
        }
    }
    else numberOfEmptyAround+=1;

    //verify upper side
    if(pos[0]>0) {
        SaboteurTile neighborCard = this.board[pos[0]-1][pos[1]];
        if (neighborCard == null) numberOfEmptyAround += 1;
        else if(objHiddenList.contains(neighborCard)) requiredEmptyAround -= 1;
        else {
            int[][] neighborPath = neighborCard.getPath();
            int[] p={path[0][2],path[1][2],path[2][2]};
            int[] np={neighborPath[0][0],neighborPath[1][0],neighborPath[2][0]};
            if (p[0] != np[0] || p[1] != np[1] || p[2] != np[2]) return false;
            else if(p[0] == 0 && p[1]== 0 && p[2] ==0 ) numberOfEmptyAround +=1;
        }
    }
    else numberOfEmptyAround+=1;

    //verify bottom side:
    if(pos[0]<BOARD_SIZE-1) {
        SaboteurTile neighborCard = this.board[pos[0]+1][pos[1]];
        if (neighborCard == null) numberOfEmptyAround += 1;
        else if(objHiddenList.contains(neighborCard)) requiredEmptyAround -= 1;
        else {
            int[][] neighborPath = neighborCard.getPath();
            int[] p={path[0][0],path[1][0],path[2][0]};
            int[] np={neighborPath[0][2],neighborPath[1][2],neighborPath[2][2]};
            if (p[0] != np[0] || p[1] != np[1] || p[2] != np[2]) return false;
            else if(p[0] == 0 && p[1]== 0 && p[2] ==0 ) numberOfEmptyAround +=1; //we are touching by a wall
        }
    }
    else numberOfEmptyAround+=1;

    if(numberOfEmptyAround==requiredEmptyAround)  {
    	return false;
    }

    return true;
}

//TAKEN FROM SABOTEURBOARDSTATE 
public ArrayList<int[]> possiblePositions(SaboteurTile card) {
    // Given a card, returns all the possiblePositions at which the card could be positioned in an ArrayList of int[];
    // Note that the card will not be flipped in this test, a test for the flipped card should be made by giving to the function the flipped card.
    ArrayList<int[]> possiblePos = new ArrayList<int[]>();
    int[][] moves = {{0, -1},{0, 1},{1, 0},{-1, 0}}; //to make the test faster, we simply verify around all already placed tiles.
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (this.board[i][j] != null) {
                for (int m = 0; m < 4; m++) {
                    if (0 <= i+moves[m][0] && i+moves[m][0] < BOARD_SIZE && 0 <= j+moves[m][1] && j+moves[m][1] < BOARD_SIZE) {
                        if (this.verifyLegit(card.getPath(), new int[]{i + moves[m][0], j + moves[m][1]} )){
                            possiblePos.add(new int[]{i + moves[m][0], j +moves[m][1]});
                        }
                    }
                }
            }
        }
    }
    return possiblePos;
}

public int getNbMalus(int playerNb){
    if(playerNb==1) return this.player1nbMalus;
    return this.player2nbMalus;
}



}