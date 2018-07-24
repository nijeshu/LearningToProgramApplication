package Server;

import javafx.scene.paint.Color;


public class Robot {

    private RobotPosition robotposition;
    private String handle;
    private Color color;
 
    
    public Robot(String handle, RobotPosition p, Color color) {
        this.robotposition = p;
        this.handle = handle;
        this.color= color;
    }

    public RobotPosition getRobotPosition() {
        return robotposition;
    }

    public void executingInstruction(String instruction) {

        switch (instruction) {
            case "left":
                robotposition.setX(robotposition.getX() - 10);
                break;
            case "right":
                robotposition.setX(robotposition.getX() + 10);
                break;
            case "up":
                robotposition.setY(robotposition.getY() - 10);
                break;
            case "down":
                robotposition.setY(robotposition.getY() + 10);
                break;
        }

    }

    public String getHandle() {
        return handle;
    }
    
    public Color getcolor(){
        return color;
    }

    public void setRobotPosition(int x, int y) {
        robotposition.setX(x);
        robotposition.setY(y);
    }
    
    public void setHandle(String handle){
        this.handle=handle;
    }
    
    public void setColor(Color current){
        this.color=current;
    }
    
    
}


