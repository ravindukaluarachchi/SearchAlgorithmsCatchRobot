/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rav.view;

import com.rav.algorithm.AStarSearch;
import com.rav.algorithm.AbstractSearch;
import com.rav.algorithm.BreadthFirstSearch;
import com.rav.algorithm.DepthFirstSearch;
import com.rav.algorithm.GreedySearch;
import com.rav.util.Block;
import com.rav.util.Edge;
import com.rav.util.Node;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author ravindu kaluarachchi
 */
public class ViewInCanvas extends Application {

    LinkedList<Node> nodes = new LinkedList<>();
    LinkedList<Node> blocks = new LinkedList<>();
    LinkedList<Edge> edges = new LinkedList<>();
    int ycount = 7;
    int xcount = 7;
    int xOffset = 100;
    int yOffset = 100;
    int nodeLength = 20;
    double interNodeDistance = 40d;
    LinkedList<Node> travelledPath;
    Node goal;
    Node robot;
    Thread timeThread;
    int resetInterval = 5;
    boolean found = false;
    VBox rightPane;
    Text txtTitle = new Text();
    ComboBox cmbSearchMethod;
    int tryCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        blocks.addAll(
                Arrays.asList(
                        new Node("[2,1]"), new Node("[3,1]"), new Node("[4,1]"),
                        new Node("[2,5]"), new Node("[3,5]"), new Node("[4,5]"), new Node("[6,3]")
                ));

        for (int i = 0; i < ycount; i++) {
            for (int j = 0; j < xcount; j++) {
                Node node = new Node("[" + i + "," + j + "]", (j * interNodeDistance), (i * interNodeDistance), 1d);
                if (blocks.contains(node)) {
                    nodes.add(new Block());
                    continue;
                }
                if (j != 0) {
                    Node lastNode = nodes.getLast();
                    if (!(lastNode instanceof Block)) {
                        Edge edge = new Edge(lastNode, node);
                        edges.add(edge);
                    }
                }
                if (i != 0) {
                    System.out.println(i + " " + j + "  size " + nodes.size() + " get " + ((i * xcount) + j));
                    Node lastNode = nodes.get(((i * xcount) + j) - xcount);
                    if (!(lastNode instanceof Block)) {
                        Edge edge = new Edge(lastNode, node);
                        edges.add(edge);
                    }
                }
                nodes.add(node);
            }

        }
        /*for (Edge edge : edges) {
         System.out.println(edge);
         }
         for(Node n : nodes){
         n.getConnectedNodes();
         }*/
        // System.exit(0);
        // goal = nodes.getLast();

        //search = new BreadthFirstSearch(nodes.getFirst(), nodes.getLast());
        primaryStage.setTitle("Search Alogrithms");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        Group root = new Group();

        BorderPane pane = new BorderPane();
        root.getChildren().add(pane);

        Canvas canvas = new Canvas(500, 500);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        //root.setStyle("-fx-background-color: red");
        //root.getChildren().add(canvas);
        pane.setCenter(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        rightPane = new VBox(
                new Text(" "),
                new Text("")
        );
        rightPane.setPadding(new Insets(5));
        txtTitle.setFont(Font.font("Verdana", 20));
        rightPane.getChildren().add(txtTitle);
        //root.getChildren().add(rightPane);
        pane.setRight(rightPane);

        FlowPane bottomPane = new FlowPane(Orientation.HORIZONTAL, 10d, 10d);

        pane.setBottom(bottomPane);
        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "BFS",
                        "DFS",
                        "A*"
                );
        cmbSearchMethod = new ComboBox(options);
        cmbSearchMethod.getSelectionModel().selectFirst();

        bottomPane.getChildren().add(cmbSearchMethod);
        Button btnStart = new Button("Start");
        btnStart.setStyle("-fx-font-size: 2em; ");

        //btnQuit.setStyle("-fx-width: 100px; ");
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnStart.setVisible(false);
                cmbSearchMethod.setEditable(false);
                timeThread = new Thread(() -> {
                    newSearch(gc);
                });
                timeThread.start();
            }
        });
        bottomPane.getChildren().add(btnStart);

        Button btnReset = new Button("Reset");
        btnReset.setStyle("-fx-font-size: 2em; ");

        //btnQuit.setStyle("-fx-width: 100px; ");
        btnReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (timeThread != null && timeThread.isAlive()) {
                    timeThread.stop();
                }
                robot = null;
                goal = null;
                tryCount = 0;
                travelledPath.clear();
                rightPane.getChildren().clear();
                timeThread = new Thread(() -> {
                    newSearch(gc);
                });
                timeThread.start();
            }
        });
        //  bottomPane.getChildren().add(btnReset);

        Button btnQuit = new Button("Quit");
        btnQuit.setStyle("-fx-font-size: 2em; ");

        //btnQuit.setStyle("-fx-width: 100px; ");
        btnQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        bottomPane.getChildren().add(btnQuit);
        /*timeThread = new Thread(() -> {
         newSearch(gc);
         });
         timeThread.start();*/
    }

    private void addText(String... text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < text.length; i++) {
                    Text tryText = new Text(text[i]);
                    rightPane.getChildren().addAll(tryText);
                }

            }
        });
    }

    private void addTextTitle(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                txtTitle.setText(text);

            }
        });
    }

    private void restGoal() {
        boolean isANearNode = false;
        boolean isSameNode = false;
        boolean isRobotNode = false;
        boolean isBlock = false;
        Node newGoal = null;
        do {
            isANearNode = false;
            isSameNode = false;
            isRobotNode = false;
            isBlock = false;
            newGoal = nodes.get(new Random().nextInt(nodes.size()));

            if (newGoal.equals(goal)) {
                isSameNode = true;
            }
            if (robot.equals(newGoal)) {
                isRobotNode = true;
            }
            if (newGoal.getName().equals("X") || blocks.contains(newGoal)) {
                isBlock = true;
            }
            for (Node connectedNode : robot.getConnectedNodes()) {
                if (connectedNode.equals(newGoal)) {
                    isANearNode = true;
                }
            }
            System.out.println(">>>>>>> " + robot + " >>> " + newGoal);
            System.out.println(isRobotNode + " && " + isSameNode + " && " + isANearNode + " && " + isBlock);
        } while (isRobotNode || isSameNode || isANearNode || isBlock);
        
        goal = newGoal;

    }

    private void newSearch(GraphicsContext gc) {
        AbstractSearch search;
        if (robot == null) {
            robot = nodes.getFirst();
        }
        if (goal == null) {
            goal = nodes.getLast();
        } else {
            restGoal();
            System.out.println("from reset " + goal);
        }
        
        switch (cmbSearchMethod.getValue().toString()) {
            case "A*":
                search = new AStarSearch(robot, goal);
                break;
            case "DFS":
                search = new DepthFirstSearch(robot, goal);               
                break;
            default:
                search = new BreadthFirstSearch(robot, goal);
        }      

        addTextTitle(search.getClass().getSimpleName());

        drawShapes(gc, robot);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(ViewInCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
        search.traverse();
        travelledPath = search.getTravelledPath();


       for (int i = 0; i < travelledPath.size(); i++) {

            robot = travelledPath.get(i);

        
            drawShapes(gc, robot);

            if (robot.equals(goal)) {
                found = true;
                addText("Try " + ++tryCount + " : ",
                        "Runtime " + search.runTime().toMillis() + " milli seconds.",
                        "Nodes In Memory " + search.nodesInMemory()
                );
                addText("Goal Captured!");
                timeThread.stop();
            }
            if (i == resetInterval) {
                if (timeThread.isAlive()) {
                    break;
                    // timeThread.stop();
                }

            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ViewInCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        addText("Try " + ++tryCount + " : ",
                "Runtime " + search.runTime().toMillis() + " milli seconds.",
                "Nodes In Memory " + search.nodesInMemory()
        );

        if (!found) {
            newSearch(gc);
        }

    }

    private void drawShapes(GraphicsContext gc, Node robot) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                gc.clearRect(0, 0, 800, 600);
                gc.setStroke(Color.BLACK);
                for (Node node : nodes) {
                    if (node instanceof Block) {
                        continue;
                    }
                    if (travelledPath != null && travelledPath.contains(node)) {
                        gc.setFill(Color.BLUE);
                    } else {
                        gc.setFill(Color.DARKSEAGREEN);
                    }
                    double x = node.getX() + xOffset;
                    double y = node.getY() + yOffset;
                    gc.fillRect(x, y, nodeLength, nodeLength);

                }

                for (Edge edge : edges) {
                    if (edge.getNode1().getY().equals(edge.getNode2().getY())) {
                        gc.strokeLine(edge.getNode1().getX() + nodeLength + xOffset,
                                edge.getNode1().getY() + nodeLength / 2 + yOffset,
                                edge.getNode2().getX() + xOffset,
                                edge.getNode2().getY() + nodeLength / 2 + yOffset);
                    } else if (edge.getNode1().getX().equals(edge.getNode2().getX())) {
                        gc.strokeLine(edge.getNode1().getX() + nodeLength / 2 + xOffset,
                                edge.getNode1().getY() + nodeLength + yOffset,
                                edge.getNode2().getX() + nodeLength / 2 + xOffset,
                                edge.getNode2().getY() + yOffset);
                    }

                }

                //draw target
                double x = 0d;// goal.getX() + xOffset;
                double y = 0d;

                x = goal.getX() + xOffset;
                y = goal.getY() + yOffset;

                gc.setFill(Color.RED);
                gc.fillPolygon(new double[]{x, x + 30, x, x + 30},
                        new double[]{y - 5, y - 5, y + 25, y + 25}, 4);

                //draw robot
                x = robot.getX() + xOffset;
                y = robot.getY() + yOffset;

                gc.setFill(Color.DARKORANGE);
                gc.fillPolygon(new double[]{x, x + 30, x, x + 30},
                        new double[]{y - 5, y - 5, y + 25, y + 25}, 4);
               
            }
        });
    }

}
