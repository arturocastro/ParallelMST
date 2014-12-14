public class Main {

    public static void main(String[] args) {
        // Number of threads
        if (args.length > 0) {
            MyGlobal.Config.p = Integer.parseInt(args[0]);
        }

        // Verbose
        if (args.length > 1) {
            MyGlobal.Config.verbose = Integer.parseInt(args[1]);
        }

        // Debug
        if (args.length > 2) {
            MyGlobal.Config.debug = Integer.parseInt(args[2]);
        }

        System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        IGraph g = new AdjListGraph("C:\\Users\\Soluna\\IdeaProjects\\MST\\data\\oreilly.txt");

        MST.prim(g);

        IGraph g2 = GraphGenerator.simple(10, 10);

        System.out.println(g.toString());
        System.out.println(g2.toString());
    }
}
