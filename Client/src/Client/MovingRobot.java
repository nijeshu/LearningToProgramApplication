package Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MovingRobot extends Application {

    private TextArea textarea;
    private String[] movements;
    private Gateway gateway;
    private TextField textField;
    private String handle;
    private ArrayList<CirclePane> circlesList;
    private CirclePane pane;
    private static Lock lock;
    private static Condition playerIn;
    private int counter = 1;
    private int buttonCounter = 1;

    public MovingRobot() {
        gateway = new Gateway(textarea);
        movements = null;
        circlesList = new ArrayList<>();
        lock = new ReentrantLock();
        playerIn = lock.newCondition();

        //pane = new CirclePane(Color.WHITE,new RobotPosition(10,10));
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {

        VBox vBox = new VBox();
        textarea = new TextArea();
        pane = new CirclePane();
        vBox.getChildren().add(pane);
        pane.setVisible(false);
        vBox.getChildren().add(textarea);
        textField = new TextField();
        vBox.getChildren().add(textField);

        /*Button sendHandle = new Button("sendHandle");
         vBox.getChildren().add(sendHandle);*/
        Button submit = new Button("Submit");
        vBox.getChildren().add(submit);
        //pane = new CirclePane(Color.AQUAMARINE, new RobotPosition(9,9));

        BorderPane borderPane = new BorderPane();
        for (int i = 0; i < circlesList.size(); i++) {
            borderPane.setTop(circlesList.get(i));

        }

        borderPane.setBottom(vBox);

        Scene scene = new Scene(borderPane, 600, 600);
        primaryStage.setTitle("ROBOT MOVEMENTS");
        primaryStage.setScene(scene);
        primaryStage.show();
        counter++;//counter to count window
        //primaryStage.setScene(scene);

        submit.setOnAction(e -> {
            buttonCounter++;
            try {

                handle = textField.getText();
                gateway.sendHandle(handle);

                movements = textarea.getText().split("\n");
                //gateway.sendHandle(handle);
                gateway.sendInstructions(movements);

                if (counter == buttonCounter) {
                    lock.lock();
                    try {
                        playerIn.signal();
                    } finally {
                        lock.unlock();
                    }

                    for (int i = 1; i == gateway.getRobotCount(); i++) {
                       lock.lock();
                        try {
                            playerIn.await();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MovingRobot.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            lock.unlock();
                        }
                        RobotPosition p = gateway.getNewPosition(0);
                        pane.setDetails(Color.WHITE, p);
                        pane.updatePosition(p);
                        Color c = gateway.getColor();
                        pane.updateColor(c);
                        pane.setVisible(true);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MovingRobot.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        /*sendHandle.setOnAction(e -> {

         try {
         handle = textField.getText();
         gateway.sendHandle(handle);
         RobotPosition p = gateway.getNewPosition(0);
         pane.updatePosition(p);
         Color c = gateway.getColor();
         pane.updateColor(c);
         pane.setVisible(true);
         } catch (IOException ex) {
         Logger.getLogger(MovingRobot.class.getName()).log(Level.SEVERE, null, ex);
         }

         });*/
    }

    class CirclePane extends Pane {

        private Circle circle;

        public CirclePane() {
            this.circle = new Circle(15);
            this.circle.setCenterX(300);
            this.circle.setCenterY(175);
        }

        public void setDetails(Color color, RobotPosition p) {
            getChildren().add(circle);
            this.circle.setStroke(Color.BLACK);
            this.circle.setFill(color);
        }

        public void updatePosition(RobotPosition p) {
            this.circle.setCenterX(p.getX());
            this.circle.setCenterY(p.getY());

        }

        public void updateColor(Color color) {
            this.circle.setFill(color);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
