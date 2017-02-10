package sortingballs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Ball {

    // private int index;
    int radius = 95;
    int radius_center = 45;
    private Point2D pt[] = new Point2D[16];
    // private Canvas canvas;
    private GraphicsContext gc;
    private int canvas_width = 18 * radius; // canvas width
    private int canvas_height = 3 * radius; // canvas heigh
    private Vector<Integer> random_sequence = new Vector<Integer>();
    private Vector<Integer> sorted_sequence = new Vector<Integer>();
    private int idx[] = new int[16]; // the index of ball
    private SimpleDoubleProperty[] pros = new SimpleDoubleProperty[16];
    int m = 0, n = 0;//use for bubble sort
    int motionTime = 1;
    SequentialTransition trans = new SequentialTransition();

    //use map to match index and color
    Paint setColor(int i) {
        Map<Integer, Paint> map = new HashMap<Integer, Paint>();
        Paint[] colorArray = { Color.RED, Color.BLUE, Color.BLACK, Color.AQUA,
                Color.BLUEVIOLET, Color.AQUAMARINE, Color.CHOCOLATE,
                Color.CADETBLUE, Color.CHARTREUSE, Color.DARKMAGENTA,
                Color.FIREBRICK, Color.HOTPINK, Color.ROSYBROWN, Color.YELLOW,
                Color.SLATEGRAY, Color.SEAGREEN };
        Paint colors = colorArray[i];
        map.put(i, colors);
        return colors;
    }

    public Ball(Canvas canvas, GraphicsContext gc) {
        this.gc = gc;
        for (int i = 0; i < 16; i++) {
            pros[i] = new SimpleDoubleProperty(radius * 1.1 * i + 10);
            pros[i].addListener(new ChangeListener<Number>() {

                @Override
                public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) {
                    showBall();
                }

            });
        }
    }

    public Ball() {
    }

    void reshuffleBalls() {
        random_sequence.clear();
        sorted_sequence.clear();

        Random rand = new Random();

        for (int i = 0; i < 16; i++) {
            int rand_num = rand.nextInt(16);
            int pass = 0;

            while (pass == 0) {
                pass = 1;
                for (int j = 0; j < random_sequence.size(); j++) {
                    if ((int) random_sequence.get(j) == rand_num) {
                        pass = 0;
                        break;
                    }
                }

                if (pass == 0) {
                    rand_num = rand.nextInt(16);
                }
            }
            random_sequence.addElement(rand_num);
            sorted_sequence.addElement(i);

        }
        for (int i = 0; i < 16; i++) {
            idx[i] = random_sequence.get(i); // the index of ball
        }
        showBall();
        m = 0;
        n = 0;
    }
    
    //show the arrow under balls
    void showTriangle(int i,int j){
        
        pt[i] = new Point2D(pros[i].get(), radius);
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillPolygon(
            new double[]{pt[i].getX()+radius*0.5, pt[i].getX()+radius*0.4, pt[i].getX()+radius*0.6},
            new double[]{pt[i].getY()+radius,pt[i].getY()+radius*1.4,pt[i].getY()+radius*1.4}, 3);
        pt[j] = new Point2D(pros[j].get(), radius);
        gc.setFill(Color.YELLOWGREEN);
        gc.fillPolygon(
            new double[]{pt[j].getX()+radius*0.5, pt[j].getX()+radius*0.4, pt[j].getX()+radius*0.6},
            new double[]{pt[j].getY()+radius,pt[j].getY()+radius*1.4,pt[j].getY()+radius*1.4}, 3);

    }

    void showBall() {
        gc.clearRect(0, 0, canvas_width, canvas_height - radius);//clear the ball part
        for (int i = 0; i < 16; i++) {
            
            pt[i] = new Point2D(pros[i].get(), radius); // point
                                                        // position
            gc.setFill(setColor(idx[i])); // set the customed color for specific
                                          // index
            gc.setStroke(Color.BLACK);
            gc.fillOval(pt[i].getX(), pt[i].getY(), radius, radius);
            gc.setFill(Color.WHITE); // set the customed color for specific
                                     // index
            gc.fillOval(pt[i].getX() + radius / 3.7,
                    pt[i].getY() + radius / 3.7, radius_center, radius_center);
            gc.setFill(Color.BLACK);
            Font font = Font.font("verdana", FontWeight.BOLD, radius / 3);
            gc.setFont(font);

            //make numbers can be showed at the center of balls
            if (idx[i] <= 9)
                gc.fillText(Integer.toString(idx[i]),
                        pt[i].getX() + radius / 2.5,
                        pt[i].getY() + radius / 1.6);
            else
                gc.fillText(Integer.toString(idx[i]),
                        pt[i].getX() + radius / 3.5,
                        pt[i].getY() + radius / 1.6);
        }
    }

    //handler for bubble sort
    EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println(m + " " + n);
            if (m >= 15) {
                return;
            }
            Timeline timeline = new Timeline();
            if (idx[n] > idx[n + 1]) {
                int temp = idx[n];
                idx[n] = idx[n + 1];
                idx[n + 1] = temp;
                gc.clearRect(0, radius*2, canvas_width, radius);//clear arrow part

                SimpleDoubleProperty t = pros[n];
                pros[n] = pros[n + 1];
                pros[n + 1] = t;
                timeline.getKeyFrames()
                        .add(new KeyFrame(Duration.seconds(motionTime), handler,
                                new KeyValue(pros[n], pros[n + 1].get()),
                                new KeyValue(pros[n + 1], pros[n].get())));
                System.out.println(n);
                System.out.println(n + 1);
                System.out.println(Arrays.toString(idx));
                showTriangle(n,n+1);
            } else {
                timeline.getKeyFrames()
                        .add(new KeyFrame(Duration.seconds(0.01), handler));
            }
            timeline.play();
            n++;
            if (n >= 15 - m) {
                n = 0;
                m++;
            }
        }
    };

    void bubbleSort() {
        System.out.println(Arrays.toString(idx));
        
        Timeline timeline = new Timeline();
        timeline.getKeyFrames()
                .add(new KeyFrame(Duration.seconds(0.01), handler));
        timeline.play();

    }

}
