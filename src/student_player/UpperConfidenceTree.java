package student_player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* Code adapted from https://www.baeldung.com/java-monte-carlo-tree-search, MCTS for Tic Tac Toe */


public class UpperConfidenceTree {

    public static double uct(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

   public static MCTNode findBest(MCTNode node) {
        int parentVisit = node.getVisitCount();
        MCTNode first = node.getChildren().get(0);
        double max = uct(first.getParent().getVisitCount(), first.getState().getWinScore(), first.getVisitCount());
        MCTNode maxNode = first;
        for(MCTNode n : node.getChildren()) {
        	if(uct(n.getParent().getVisitCount(), n.getState().getWinScore(), n.getVisitCount()) > max);
        	maxNode = n;
        }
        return maxNode;
    }
}