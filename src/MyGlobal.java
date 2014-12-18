/**
 * Created by Soluna on 30/11/2014.
 */
public final class MyGlobal {
    public static void abort(String msg) {
        System.err.println(msg);
        System.exit(-1);
    }

    public static IGraph createGraph(String file) {
        if (MyGlobal.Config.op == 0) {
            return new AdjMatGraph(file);
        } else {
            return new AdjListGraph(file);
        }
    }

    public static IGraph createGraph(int V, int E) {
        if (MyGlobal.Config.op == 0) {
            return new AdjMatGraph(V, E);
        } else {
            return new AdjListGraph(V, E);
        }
    }

    public static void verbosePrint(String s) {
        if (MyGlobal.Config.verbose == 1) {
            System.out.println(s);
        }
    }

    public static void verbosePrint(String s, int level) {
        if (MyGlobal.Config.verbose == 2) {
            System.out.println(s);
        }
    }

    public final static class Config {
        static final int GRAPH_REPRESENTATION = 1;
        static int p = 1; // Num of processors
        static int nb = 10;
        static final long TIMEOUT_SINGLE = 10 * 60 * 1000;
        static final long TIMEOUT_MULTI = 10 * 60 * 1000;
        static int verbose = 0;
        static int debug = 0;
        static int op = 0; // 0 is matrix, 1 is list
    }
}
