package Server;

import java.util.ArrayList;

public class Simulation {

    private ArrayList<Robot> Robots;
    private ArrayList<Program> Programs;
    private Robot robot;

    public Simulation() {
        Robots = new ArrayList<>();
        Programs = new ArrayList<>();

    }

    public void update(String handle, String instruction) {
        for (int i = 0; i < Robots.size(); i++) {
            if ((Robots.get(i).getHandle()).equals(handle)) {
                Robots.get(i).executingInstruction(instruction);
            }
        }

    }

    public RobotPosition robotPosition(String handle) {
        for (int i = 0; i < Robots.size(); i++) {
            if ((Robots.get(i).getHandle()).equals(handle)) {
                return Robots.get(i).getRobotPosition();
            }
        }
        return null;
    }

    public ArrayList<Robot> Robotlist() {
        return Robots;
    }

    public int RobotNumberFromList(String handle) {
        for (int i = 0; i < Robots.size(); i++) {
            if (Robots.get(i).getHandle().equals(handle)) {
                int robotNumber = Robots.indexOf(i);
                return robotNumber;
            }
        }
        return 0;
    }

    public void addRobot(Robot playername) {
        Robots.add(playername);
    }

    public int getRobotCount() {
        return Robots.size();
    }
}
