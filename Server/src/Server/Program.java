
package Server;
import java.util.ArrayList;

public class Program {
    private ArrayList<String> instructions;
    
    public Program(){
        instructions= new ArrayList<>();
    }
    
    public void updateProgram(String[] instructions){
        for(String ins : instructions)
            this.instructions.add(ins);
    }
    
}



