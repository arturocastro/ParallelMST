import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Created by Arturo Isai Castro Perpuli on 29/11/2014.
 */

public class Main {

    public static void main(String[] args) {

        OptionParser parser = new OptionParser("V:E:vdp:l");
        OptionSet options = parser.parse(args);

        int V = 1000;
        double E = 0.5;

        if (options.has("V") && options.hasArgument("V")) {
            V = Integer.parseInt((String) options.valueOf("V"));
        }

        if (options.has("E") && options.hasArgument("E")) {
            E = Integer.parseInt((String)options.valueOf("E")) / 100.0;
        }

        if (options.has("p") && options.hasArgument("p")) {
            MyGlobal.Config.p = Integer.parseInt((String) options.valueOf("p"));
        }

        if (options.has("l")) {
            MyGlobal.Config.op = 1;
        }

        if (options.has("v")) {
            MyGlobal.Config.verbose = 1;
        }

        if (options.has("d")) {
            MyGlobal.Config.debug = 1;
        }

        //System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        //AdjMatGraph g = new AdjMatGraph("C:\\Users\\Soluna\\IdeaProjects\\MST\\data\\oreilly.txt");

        //IGraph a = Prim.prim(g);

        //Kruskal.kruskal(g);

        IGraph g = GraphGenerator.simple(4, 5);

        //System.out.println(g.toString());
        //System.out.println(a.toString());

        System.out.println(g.toString());

        Kruskal.kruskal(g);

        System.out.println();

//        MPI.Init(args);
//
//        ParallelPrim.parallelPrim(g);
    }
}
