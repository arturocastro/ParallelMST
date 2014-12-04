import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by Soluna on 01/12/2014.
 */
public class Prim {
    public static IGraph prim(IGraph g) {
        final int n = g.getNumVertices();

        int [] pred = new int[n];
        Arrays.fill(pred, -1);

        double [] keys = new double[n];
        Arrays.fill(keys, (double)Integer.MAX_VALUE);

        keys[0] = 0.0;

        PriorityQueue<SimpleHash> pq = new PriorityQueue<SimpleHash>(n);

        boolean [] inQueue = new boolean[n];
        Arrays.fill(inQueue, true);

        for (int i = 0; i < n; ++i) {
            pq.add(new SimpleHash(i, keys[i]));
        }

        while (!pq.isEmpty()) {
            int u = pq.poll().v;
            inQueue[u] = false;

            for (Iterator<Integer> it = g.iterateNeighbors(u); it.hasNext();) {
                int v = it.next();

                if (inQueue[v]) {
                    double w = g.getEdgeWeight(u, v);

                    if (w < keys[u]) {
                        pred[v] = u;
                        keys[v] = w;

                        for (SimpleHash pair : pq) {
                            if (pair.v == v) {
                                pair.key = w;
                            }
                        }
                    }
                }
            }
        }

        return g;
    }
}
