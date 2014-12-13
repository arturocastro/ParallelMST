public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            MyGlobal.Config.p = Integer.parseInt(args[0]);
        }

        System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        IGraph g = new AdjListGraph("C:\\Users\\MarthaAlexandra\\IdeaProjects\\ParallelMST\\data\\tinyEWG.txt");

        MST.prim(g);

        IGraph g2 = GraphGenerator.simple(10, 10);

        System.out.println(g.toString());
        System.out.println(g2.toString());
    }
}
