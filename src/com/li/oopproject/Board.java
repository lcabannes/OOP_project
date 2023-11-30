import java.util.ArrayList;
import java.util.List;

public class Board {
    private String name;
    private Place entrance;
    private Place exit;
    private List<Alien> Aliens;
    private Human human;
   
    public Place(String name, Place exit) {
        this.name = name;
        this.exit = exit;
        this.aliens = new ArrayList<>();
        this.human = null;
        this.entrance = null;

        // Phase 1: Add an entrance to the exit
        if (this.exit != null) {
            this.exit.entrance = this;
        }
    }

    
}
