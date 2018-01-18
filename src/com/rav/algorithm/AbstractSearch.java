/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rav.algorithm;

import com.rav.util.Node;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author ravindu kaluarachchi
 */
public abstract class AbstractSearch {

    LinkedList<Node> openQueue = new LinkedList<>();
    LinkedList<Node> closedQueue = new LinkedList<>();
    HashMap<Node, Node> travelledPath = new HashMap<>();
    Node start;
    Node goal;
    Instant tStart;
    Instant tEnd;

    public AbstractSearch(Node start, Node goal) {
        this.start = start;
        this.goal = goal;
        openQueue.add(start);
    }

    protected abstract boolean traversalLogic();

    public boolean traverse() {
        tStart = Instant.now();
        boolean found = traversalLogic();
        tEnd = Instant.now();
        return found;
    }

    void addToTravelledPath(Node from, LinkedList<Node> tos) {
        for (Node to : tos) {
            addToTravelledPath(from, to);
        }
    }

    void addToTravelledPath(Node from, Node to) {
        travelledPath.put(to, from);
    }

    boolean isGoal(Node node) {
        if (goal.equals(node)) {
            return true;
        } else {
            return false;
        }
    }

    public LinkedList<Node> getTravelledPath() {
        return getTravelledPath(goal);
    }
    
    public LinkedList<Node> getTravelledPath(Node n) {
        Node cur = n;
        LinkedList<Node> path = new LinkedList<>();
        while (cur != null) {
            path.add(cur);
            cur = travelledPath.get(cur);
        }
        Collections.reverse(path);
        return path;
    }

    public Duration runTime() {
        return Duration.between(tStart, tEnd);
    }

    public int nodesInMemory() {
        return openQueue.size() + closedQueue.size();
    }
}
