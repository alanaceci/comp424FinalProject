package student_player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class UCT {

    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static MCTNode findBest(MCTNode node) {
        int parentVisit = node.getVisitCount();
        return null;
//        return Collections.max(
//          node.getChildren(),
//          Comparator.comparing(c -> uctValue(parentVisit, c.getWinScore(), c.getVisitCount())));
    }
}