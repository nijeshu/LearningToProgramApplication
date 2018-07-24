
package Server;

public class RobotPosition {
    
    private int x;
    private int y;
    
    public RobotPosition(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setX(int x){
        this.x= x;
    }
    
    public void setY(int y){
        this.y= y;
    }
    
    public void update(int x, int y){
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return x + " " + y;
    }
            
}

