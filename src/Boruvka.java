import java.util.Arrays;

/**
 * Created by Soluna on 15/12/2014.
 */

/*
*  @author Robert Sedgewick
*  @author Kevin Wayne
*/

/* Modified by Arturo Isai Castro Perpuli */

public class Boruvka {
    public static void boruvka(IGraph g) {
        UF uf = new UF(g.getNumVertices());

        Edge[] closest = new Edge[g.getNumVertices()];

        // repeat at most log V times or until we have V-1 edges
        for (int t = 1; t < g.getNumVertices() && mst.size() < g.getNumVertices() - 1; t = t + t) {
            Arrays.fill(closest, null);

            // foreach tree in forest, find closest edge
            // if edge weights are equal, ties are broken in favor of first edge in G.edges()
            for (Edge e : g) {
                int i = uf.find(e._u), j = uf.find(e._v);
                if (i == j){
                    continue;   // same tree
                }

                if (closest[i] == null || e._weight < closest[i]._weight) {
                    closest[i] = e;
                }

                if (closest[j] == null || e._weight < closest[j]._weight) {
                    closest[j] = e;
                }
            }

            // add newly discovered edges to MST
            for (int i = 0; i < g.getNumVertices(); i++) {
                Edge e = closest[i];

                if (e != null) {
                    // don't add the same edge twice
                    if (!uf.connected(e._u, e._v)) {
                        mst.add(e);
                        //weight += e.weight();
                        uf.union(e._u, e._v);
                    }
                }
            }
        }
    }
}
