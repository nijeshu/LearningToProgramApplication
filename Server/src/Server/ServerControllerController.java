package Server;

import static Constants.Constants.GET_NEW_POSITION;
import static Constants.Constants.GET_ROBOT_COUNT;
import static Constants.Constants.SEND_HANDLE;
import static Constants.Constants.SEND_INSTRUCTIONS;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ServerControllerController implements Initializable {

    @FXML
    private TextArea textArea;

    private int clientNo = 0;
    private Simulation simulation;
    private Robot robot;
    private Program program;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Starting to initialize.");
        simulation = new Simulation();

        
        
        program = new Program();
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                System.out.println("Server is listening for connections.");
                while (true) {
                    Socket socket = serverSocket.accept();
                    clientNo++;
                    Platform.runLater(() -> {
                        
                        textArea.appendText("Starting thread for client " + clientNo
                                + " at " + new Date() + '\n');
                    });

                    new Thread(new ClientRobotHandler(socket, simulation, robot, program, textArea)).start();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }
}

class ClientRobotHandler implements Runnable, Constants.Constants {

    private Socket socket;
    private Simulation simulation;
    private Program program;
    private Robot robot;
    private TextArea textArea;
    private String handle;
    private int handler;
    private Color[] colors = {Color.ORANGE, Color.RED, Color.BLUE, Color.GREEN};
    private int currentColor = 0;
    private Color current;

    public ClientRobotHandler(Socket socket, Simulation simulation, Robot robot, Program program, TextArea textArea) {
        this.socket = socket;
        this.simulation = simulation;
        this.textArea = textArea;
        this.program = program;
        this.robot = robot;

    }

    public void run() {
        try {
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());

            while (true) {
                System.out.println("Entering the while loop");
                int request = Integer.parseInt(inputFromClient.readLine());
                
                switch (request) {
                    
                    case SEND_HANDLE: {
                        handle = inputFromClient.readLine();
                        
                        int randomNumber = (int) (Math.random() * 4);
                        RobotPosition p = new RobotPosition((int) (Math.random() * 100), (int) (Math.random() * 100));
                        current = colors[randomNumber];
                        robot = new Robot(handle, p, current);
                        robot.setHandle(handle);
                        simulation.Robotlist().add(robot);
                        robot.setColor(current);
                        break;
                    }
                    case SEND_INSTRUCTIONS: {
                        int instructionNumber =Integer.parseInt(inputFromClient.readLine());
                        for(int i=0; i<instructionNumber;i++){
                        String instruction= inputFromClient.readLine();
                        simulation.update(handle, instruction);
                        }
                        break;
                    }
                    case GET_ROBOT_COUNT: {
                        outputToClient.println(simulation.getRobotCount());
                        /** Missing: outputToClient.flush(); **/
                        break;
                    }
                    case GET_NEW_POSITION: {
                        handle = inputFromClient.readLine();
                        handler = simulation.RobotNumberFromList(handle);
                        
                         int Xcoordinate=simulation.Robotlist().get(handler).getRobotPosition().getX();
                         int Ycoordinate=simulation.Robotlist().get(handler).getRobotPosition().getY();
                         
                        outputToClient.println(Xcoordinate);
                        outputToClient.println(Ycoordinate);
                        outputToClient.flush();
                        break;
                    }
                    case GET_COLOR:{
                        double color_red= current.getRed();
                        double color_green= current.getGreen();
                        double color_blue= current.getBlue();
                        outputToClient.println(color_red);
                        outputToClient.println(color_green);
                        outputToClient.println(color_blue);
                        
                        outputToClient.flush();
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            Platform.runLater(() -> textArea.appendText("Exception in client thread: " + ex.toString() + "\n"));
        }
        
    }
    
    
}
