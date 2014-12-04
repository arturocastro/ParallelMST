import java.util.Iterator;
import java.util.PriorityQueue;

public class Main {

    public static void main(String[] args) {
        IGraph g = new AdjListGraph("C:\\Users\\Soluna\\IdeaProjects\\MST\\data\\tinyEWG.txt");

        Prim.prim(g);

        System.out.println(g.toString());
    }
}
