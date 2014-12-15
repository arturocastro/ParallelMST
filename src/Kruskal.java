import java.util.Arrays;

/**
 * Created by Soluna on 15/12/2014.
 */

public class Kruskal {
    public static void kruskal(IGraph g) {
        Edge [] edgeArray = new Edge[g.getNumEdges()];
        UF uf = new UF(g.getNumVertices());

        {
            int i = 0;

            for (Edge e : g) {
                edgeArray[i] = e;
                ++i;
            }
        }

        Arrays.sort(edgeArray);

        for (int i = 0; i < edgeArray.length; ++i) {
            Edge e = edgeArray[i];

            if(!uf.connected(e._u, e._v)) {
                uf.union(e._u, e._v);
                System.out.println(e.toString());
            }
        }
    }
}
