import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Soluna on 16/12/2014.
 */
public class ParallelBoruvka {
    public static void parallelBoruvka(IGraph g) {
        int mst = 0;

        UF uf = new UF(g.getNumVertices());

        Edge[] closest = new Edge[g.getNumVertices()];

        // repeat at most log V times or until we have V-1 edges
        for (int t = 1; t < g.getNumVertices() && mst < g.getNumVertices() - 1; t = t + t) {
            Arrays.fill(closest, null);

            // foreach tree in forest, find closest edge
            // if edge weights are equal, ties are broken in favor of first edge in G.edges()
            for (int u = 0; u < g.getNumVertices(); ++u) { // PARALLEL!!!!!!!!!!!!!!!!!!!!!
                for (Iterator<Integer> it = g.iterateNeighbors(u); it.hasNext();) {
                    int v = it.next();
                    Edge e = g.getEdge(u, v);

                    int i = uf.find(u), j = uf.find(v);

                    if (i == j) {
                        continue;   // same tree
                    }

                    if (closest[i] == null || e._weight < closest[i]._weight) {
                        closest[i] = e;
                    }

                    if (closest[j] == null || e._weight < closest[j]._weight) {
                        closest[j] = e;
                    }
                }
            }

            // add newly discovered edges to MST
            for (int i = 0; i < g.getNumVertices(); i++) {
                Edge e = closest[i];

                if (e != null) {
                    // don't add the same edge twice
                    if (!uf.connected(e._u, e._v)) {
                        //mst.add(e);
                        System.out.println(e.toString());
                        mst++;
                        //weight += e.weight();
                        uf.union(e._u, e._v);
                    }
                }
            }
        }
    }
}
