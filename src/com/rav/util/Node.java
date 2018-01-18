/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rav.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author ravindu kaluarachchi
 */
public class Node {
    private String name;
    private Double x;
    private Double y;
    private Double hDistance;
    private Double gDistance;
    private LinkedList<Edge> edges;    

    public Node() {
        edges = new LinkedList<>();
    }

    public Node(String name) {
        this();
        this.name = name;
    }

    public Node(String name, Double hDistance) {
        this();
        this.name = name;
        this.hDistance = hDistance;
    }

    public Node(String name, Double x, Double y, Double hDistance) {
        this();
        this.name = name;
        this.x = x;
        this.y = y;
        this.hDistance = hDistance;
    }

    public LinkedList<Node> getConnectedNodes(){
        LinkedList<Node> connectedNodes = new LinkedList<>();
        Iterator<Edge> it = edges.iterator();
       // System.out.println("+++++++++++++++++++++++++++");
       // System.out.println("+Node : " + this);
       // System.out.println("+Edges : " +edges.size());
        while(it.hasNext()){
            Edge edge = it.next();
            connectedNodes.addLast(edge.getNode(this));
         //   System.out.println("+ " + connectedNodes.getLast());
        }
      //  System.out.println("+++++++++++++++++++++++++++");
        return connectedNodes;
    }
    public LinkedList<Node> getChildNodes(){
        LinkedList<Node> connectedNodes = new LinkedList<>();
        Iterator<Edge> it = edges.iterator();
        //System.out.println("+++++++++++++++++++++++++++");
       // System.out.println("+Node : " + this);
        //System.out.println("+Edges : " +edges.size());
        while(it.hasNext()){
            Edge edge = it.next();
            Node child = edge.getChildNode(this);
           // System.out.println("+ " + child);
            if (child != null) {
                connectedNodes.addLast(child);
            }            
        }
       // System.out.println("+++++++++++++++++++++++++++");
        return connectedNodes;
    }
    
    public LinkedList<Node> getParentNodes(){
        LinkedList<Node> connectedNodes = new LinkedList<>();
        Iterator<Edge> it = edges.iterator();
        while(it.hasNext()){
            Edge edge = it.next();
            Node parent = edge.getParentNode(this);
            if (parent != null) {
                connectedNodes.addLast(parent);
            }            
        }
        return connectedNodes;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double gethDistance() {
        return hDistance;
    }

    public void sethDistance(Double hDistance) {
        this.hDistance = hDistance;
    }

    public Double getgDistance() {
        return gDistance;
    }

    public void setgDistance(Double gDistance) {
        this.gDistance = gDistance;
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Edge> edges) {
        this.edges = edges;
    }  

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.name);
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
        final Node other = (Node) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + name + "]";
    }
    
}
