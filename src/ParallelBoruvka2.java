import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Soluna on 16/12/2014.
 */
public class ParallelBoruvka2 {
    public static void parallelBoruvka(IGraph g) {
        int mst = 0;
        final UF uf = new UF(g.getNumVertices());

        final Edge [] closest = new Edge[g.getNumVertices()];

        final Edge [] edges = new Edge[g.getNumEdges()];

        long s = System.nanoTime();
        for (Edge e : g) {
            edges[mst++] = e;
        }
        System.out.println((System.nanoTime() - s) / 1000000.0);

        mst = 0;

        // repeat at most log V times or until we have V-1 edges
        for (int t = 1; t < g.getNumVertices() && mst < g.getNumVertices() - 1; t = t + t) {
            Arrays.fill(closest, null);

            // foreach tree in forest, find closest edge
            // if edge weights are equal, ties are broken in favor of first edge in G.edges()
            Parallel.For(0, edges.length, new LoopBody<Integer>() {
                @Override
                public void run(Integer k) {
                    Edge e = edges[k];

                    int i = uf.find(e._u), j = uf.find(e._v);

                    if (i != j) {
                        if (closest[i] == null || e._weight < closest[i]._weight) {
                            closest[i] = e;
                        }

                        if (closest[j] == null || e._weight < closest[j]._weight) {
                            closest[j] = e;
                        }
                    }
                }
            });

            // add newly discovered edges to MST
            for (int i = 0; i < g.getNumVertices(); ++i) {
                Edge e = closest[i];

                if (e != null) {
                    // don't add the same edge twice
                    if (!uf.connected(e._u, e._v)) {
                        //mst.add(e);
                        //System.out.println(e.toString());
                        mst++;
                        //weight += e.weight();
                        uf.union(e._u, e._v);
                    }
                }
            }
        }
    }
}
