/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rav.algorithm;

import com.rav.util.Node;
import java.util.LinkedList;

/**
 *
 * @author ravindu kaluarachchi
 */
public class DepthFirstSearch extends AbstractSearch {

    public DepthFirstSearch(Node start, Node goal) {
        super(start, goal);
    }

    protected boolean traversalLogic() {

        boolean foundSolution = false;
        Node goal = null;

        while (!openQueue.isEmpty()) {
            Node n = openQueue.poll();
            closedQueue.add(n);

            LinkedList<Node> connectedNodes = n.getConnectedNodes();
            for (Node visitedNode : closedQueue) {
                if (connectedNodes.contains(visitedNode)) {
                    connectedNodes.remove(visitedNode);
                }
            }
            openQueue.addAll(0, connectedNodes);
            System.out.println(openQueue + " | " + closedQueue);

            addToTravelledPath(n, connectedNodes);
            if (isGoal(n)) {
                foundSolution = true;
                break;
            }
        }
        if (foundSolution) {
            System.out.println("Travesal Path : " + closedQueue);
        }

        return foundSolution;
    }

}
