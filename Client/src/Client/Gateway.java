
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;


public class Gateway implements Constants.Constants {
    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private TextArea textArea;

    
    public Gateway(TextArea textArea) {
        this.textArea = textArea;
        try {
            
            Socket socket = new Socket("localhost", 8000);

            outputToServer = new PrintWriter(socket.getOutputStream());

            inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ex) {
            Platform.runLater(() -> textArea.appendText("Exception in gateway constructor: " + ex.toString() + "\n"));
        }
    }

    public void sendHandle(String handle) {
        outputToServer.println(SEND_HANDLE);
        outputToServer.println(handle);
        outputToServer.flush();
    }

    public void sendInstructions(String[] instructions) {
        outputToServer.println(SEND_INSTRUCTIONS);
        outputToServer.println(instructions.length);
        for(String str : instructions)
            outputToServer.println(str);
        outputToServer.flush();
    }

  
    public int getRobotCount() {
        outputToServer.println(GET_ROBOT_COUNT);
        outputToServer.flush();
        int numberOfRobot = 0;
        try {
            numberOfRobot = Integer.parseInt(inputFromServer.readLine());
        } catch (IOException ex) {
            Platform.runLater(() -> textArea.appendText("Error in getCommentCount: " + ex.toString() + "\n"));
        }
        return numberOfRobot;
    }

 
    public RobotPosition getNewPosition(int n) {
        outputToServer.println(GET_NEW_POSSITION);
        outputToServer.println(n);
        outputToServer.flush();
        RobotPosition p = new RobotPosition();
        
        try {
            p.setX(Integer.parseInt(inputFromServer.readLine()));  
            p.setY( Integer.parseInt(inputFromServer.readLine()));
            //Receiving color
            System.out.println("X = "+ p.getX()+ "   " + "Y = "+p.getY());
        } catch (IOException ex) {
            Platform.runLater(() -> textArea.appendText("Error in getComment: " + ex.toString() + "\n"));
        }
        return p;
    }
    
    public Color getColor() throws IOException{
        outputToServer.println(GET_COLOR);
        outputToServer.flush();
       
        double color_red= Double.parseDouble(inputFromServer.readLine());
        double color_green=Double.parseDouble(inputFromServer.readLine());
        double color_blue= Double.parseDouble(inputFromServer.readLine());
                
        Color ActualColor= Color.color(color_red, color_green, color_blue);
        System.out.println(" ActualColor=  " +ActualColor);
        return ActualColor;
    }
    
    
    
}
