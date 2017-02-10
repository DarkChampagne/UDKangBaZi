/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingballs;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {
    // Define numbers

    Ball ball = new Ball();
    /* Below is for GUI data members */
    private int canvas_width = 18 * ball.radius; // canvas width
    private int canvas_height = 3 * ball.radius; // canvas heigh
    private Canvas canvas = new Canvas(canvas_width, canvas_height);
    private GraphicsContext gc = canvas.getGraphicsContext2D(); // define the
                                                                // canvas brush

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        /* Define the layout */
        primaryStage.setTitle("Sorting Balls");
        Ball ball = new Ball(canvas, gc);
        ball.reshuffleBalls();
        ball.showBall();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                //R for reshuffle
                if (ke.getCode().equals(KeyCode.R)) {
                    ball.reshuffleBalls();
                //B for bubble sort
                } else if (ke.getCode().equals(KeyCode.B)) {
                    ball.bubbleSort();
                }
            }
        });

        Group root = new Group();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

}
