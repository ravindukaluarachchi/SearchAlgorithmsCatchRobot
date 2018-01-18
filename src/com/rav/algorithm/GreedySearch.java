/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rav.algorithm;

import com.rav.util.Edge;
import com.rav.util.Node;
import java.util.LinkedList;

/**
 *
 * @author ravindu kaluarachchi
 */
public class GreedySearch extends AbstractSearch {

    public GreedySearch(Node start, Node goal) {
        super(start, goal);
        calculateHeuristic(start, goal);
    }

    private void calculateHeuristic(Node node, Node goal) {
        double h = Math.sqrt(Math.pow(goal.getX() - node.getX(), 2) + Math.pow(goal.getY() - node.getY(), 2));
        node.sethDistance(h);
    }

    private void calculateCost(Node n) {
        double cost = 0d;
        // Edge edge = null;
        //  n.getC
        LinkedList<Node> nodePath = getTravelledPath(n);
        for (int i = nodePath.size() - 1; i > 0; i--) {
            Node node = nodePath.get(i);
            Node previousNode = nodePath.get(i - 1);
            if (previousNode == null) {
                break;
            }
            System.out.println("i>" + i + " node : " + node + " prevnode : " + previousNode);
            for (Edge edge : node.getEdges()) {
                if (edge.getParentNode(node) != null && edge.getParentNode(node).equals(previousNode)) {
                    cost += edge.getCost();
                }
            }

        }

        n.setgDistance(cost);
    }

    @Override
    protected boolean traversalLogic() {
        boolean foundSolution = false;
        //Node goal = null;       
        while (!openQueue.isEmpty()) {

            Node n = openQueue.getFirst();
            for (Node nodeInQueue : openQueue) {
                if (nodeInQueue.equals(n)) {
                    continue;
                } else if ((nodeInQueue.gethDistance())
                        < (n.gethDistance())) {
                    n = nodeInQueue;
                }
            }
            openQueue.remove(n);
            closedQueue.add(n);
            System.out.println("children " + n.getChildNodes());
            //openQueue.addAll(0, n.getChildNodes());
            LinkedList<Node> connectedNodes = n.getConnectedNodes();
            for (Node visitedNode : closedQueue) {
                if (connectedNodes.contains(visitedNode)) {
                    connectedNodes.remove(visitedNode);
                }
            }
            for (Node connectedNode : connectedNodes) {
                calculateHeuristic(connectedNode, goal);
                //  calculateCost(connectedNode);
            }
            openQueue.addAll(0, connectedNodes);
            System.out.println(openQueue + " | " + closedQueue);
            //addToTravelledPath(n, n.getChildNodes());
            addToTravelledPath(n, connectedNodes);
            if (isGoal(n)) {
                foundSolution = true;
                //goal = n;
                break;
            }
        }
        if (foundSolution) {
            System.out.println("Travesal Path : " + closedQueue);
        }
        getTravelledPath().forEach(System.out::println);

        return foundSolution;
    }

}
