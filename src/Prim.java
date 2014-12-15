import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by Soluna on 15/12/2014.
 */

public class Prim {
    public static IGraph prim(IGraph g) {
        final int n = g.getNumVertices();

        int [] pred = new int[n];
        Arrays.fill(pred, -1);

        double [] key = new double[n];
        Arrays.fill(key, (double)Integer.MAX_VALUE);

        key[0] = 0.0;

        PriorityQueue<SimpleHash> pq = new PriorityQueue<SimpleHash>(n);

        boolean [] inQueue = new boolean[n];
        Arrays.fill(inQueue, true);

        for (int i = 0; i < n; ++i) {
            pq.add(new SimpleHash(i, key[i]));
        }

        while (!pq.isEmpty()) {
            // Get minimum key
            int u = pq.poll().v;
            inQueue[u] = false;

            // For each of u's neighbors
            for (Iterator<Integer> it = g.iterateNeighbors(u); it.hasNext();) {
                int v = it.next();

                // if PQ contains v
                if (inQueue[v]) {
                    double w = g.getEdgeWeight(u, v);

                    if (w < key[v]) {
                        pred[v] = u;
                        key[v] = w;

                        for (SimpleHash pair : pq) {
                            if (pair.v == v) {
                                pair.key = w;
                                pq.add(pq.poll());

                                break;
                            }
                        }
                    }
                }
            }
        }

        IGraph mst = new AdjListGraph(g.getNumVertices(), g.getNumVertices() - 1);

        for (int i = 0; i < g.getNumVertices(); ++i) {
            if (pred[i] != -1) {
                mst.addEdge(i, pred[i], key[i]);
            }
        }

        if (!MST.check(g, mst)) {
            System.out.println("Something is wrong");
        }

        return mst;
    }
}
