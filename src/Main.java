import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        IGraph g = new AdjListGraph("C:\\Users\\Soluna\\IdeaProjects\\MST\\data\\tinyEWG.txt");

        for (Edge edge : g) {
            System.out.println(edge.toString());
        }

        System.out.println(g.toString());
    }
}
