public class Main {

    public static void main(String[] args) {
        IGraph g = new AdjListGraph("C:\\Users\\Soluna\\IdeaProjects\\MST\\data\\oreilly.txt");

        MST.prim(g);

        System.out.println(g.toString());
    }
}
