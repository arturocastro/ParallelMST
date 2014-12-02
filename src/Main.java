import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        IGraph g = new AdjListGraph("C:\\Users\\Soluna\\IdeaProjects\\MST\\data\\tinyEWG.txt");

        for (Iterator<Integer> it = g.iterateNeighbors(0); it.hasNext();) {
            System.out.println(it.next());
        }

        System.out.println(g.toString());
    }
}
