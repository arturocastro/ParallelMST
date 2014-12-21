/**
 * Created by Arturo Isai Castro Perpuli on 29/11/2014.
 */

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class MainParKruskal {

    public static void main(String[] args) throws InterruptedException {
        String file = null;

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

        IGraph g = null;

        if (file == null) {
            g = GraphGenerator.simple(V, E);
        } else {
            g = MyGlobal.createGraph(file);
        }

        if (MyGlobal.Config.verbose == 2) {
            System.out.println(g.toString());
        }

        final long startTime = System.nanoTime();

        ParallelKruskal4.parallelKruskal(g);

        final long endTime = System.nanoTime();

        final double duration = (endTime - startTime) / 1000000.0;

        System.out.println(duration);

        if (MyGlobal.Config.verbose == 1) {
            System.out.println("END");
        }
    }
}
