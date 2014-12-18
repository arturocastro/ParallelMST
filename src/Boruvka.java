import java.util.Arrays;

/*
*  @author Robert Sedgewick
*  @author Kevin Wayne
*  Copyright © 2002–2010, Robert Sedgewick and Kevin Wayne.
*  http://algs4.cs.princeton.edu/43mst/BoruvkaMST.java.html
*/

/* Modified by Arturo Isai Castro Perpuli */

public class Boruvka {
    public static void boruvka(IGraph g) {
        int mst = 0;

        UF uf = new UF(g.getNumVertices());

        Edge[] closest = new Edge[g.getNumVertices()];

        // repeat at most log V times or until we have V-1 edges
        for (int t = 1; t < g.getNumVertices() && mst < g.getNumVertices() - 1; t = t + t) {
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
