//import mpi.*;

public class Main {

    public static void main(String[] args) {
        // Number of threads
        if (args.length > 0) {
            MyGlobal.Config.p = Integer.parseInt(args[0]);
        }

        // Nb
        if (args.length > 1) {
            MyGlobal.Config.nb = Integer.parseInt(args[1]);
        }

        // Verbose
        if (args.length > 2) {
            MyGlobal.Config.verbose = Integer.parseInt(args[2]);
        }

        // Debug
        if (args.length > 3) {
            MyGlobal.Config.debug = Integer.parseInt(args[3]);
        }

        System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        AdjMatGraph g = new AdjMatGraph("C:\\Users\\Soluna\\IdeaProjects\\MST\\data\\oreilly.txt");

        //IGraph a = Prim.prim(g);

        //Kruskal.kruskal(g);

        IGraph g2 = GraphGenerator.simple(10, 10);

        //System.out.println(g.toString());
        //System.out.println(a.toString());

        ParallelKruskal.parallelKruskal(g);

        System.out.println();

        Boruvka.boruvka(g);

        System.out.println();

        ParallelBoruvka.parallelBoruvka(g);

//        MPI.Init(args);
//
//        ParallelPrim.parallelPrim(g);
    }
}
