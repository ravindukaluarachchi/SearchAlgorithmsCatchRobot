/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rav.util;

import java.util.Objects;

/**
 *
 * @author ravindu kaluarachchi
 */
public class Edge {
    private Node node1;
    private Node node2;
    private Double cost;

    public Edge() {
    }

    public Edge(Node node1, Node node2) {
        this(node1,node2, 1d);
    }

    public Edge(Node node1, Node node2, Double cost) {
        this.node1 = node1;
        this.node2 = node2;
        this.cost = cost;
        node1.getEdges().addLast(this);
        node2.getEdges().addLast(this);
    }
    
    public Edge(Node node1, Node node2, Integer cost) {
       this(node1,node2, cost.doubleValue());
    }
    
    public Node getNode(Node node){
        if (node.equals(node1)) {
            return node2;
        }
        return node1;
    }
    
    public Node getChildNode(Node node){
        if (node.equals(node1)) {
            return node2;
        }
        return null;
    }
    
    public Node getParentNode(Node node){
        if (node.equals(node2)) {
            return node1;
        }
        return null;
    }
    
    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.node1);
        hash = 19 * hash + Objects.hashCode(this.node2);
        hash = 19 * hash + Objects.hashCode(this.cost);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.node1, other.node1)) {
            return false;
        }
        if (!Objects.equals(this.node2, other.node2)) {
            return false;
        }
        if (!Objects.equals(this.cost, other.cost)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Edge{"+ node1 + " --(" + cost + ")--> " + node2 + "}";
    }
    
}
