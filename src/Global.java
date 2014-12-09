/**
 * Created by Soluna on 30/11/2014.
 */
public final class Global {
    public static void abort(String msg) {
        System.err.println(msg);
        System.exit(-1);
    }

    public final static class Config {
        /* Graph representations: Adjacency list -->   1
                                  Adjacency matrix --> ???
         */
        int GRAPH_REPRESENTATION = 1;
    }
}
