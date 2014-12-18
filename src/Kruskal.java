import java.util.Arrays;

/**
 * Created by Arturo Isai Castro Perpuli on 29/11/2014.
 */

public class Kruskal {
    public static void kruskal(IGraph g) {
        Edge [] result = new Edge[g.getNumVertices() - 1];
        Edge [] edgeArray = new Edge[g.getNumEdges()];
        UF uf = new UF(g.getNumVertices());

        int j = 0;

        for (Edge e : g) {
            edgeArray[j++] = e;
        }

        j = 0;

        long a = System.nanoTime();

        Arrays.sort(edgeArray);

        long b = System.nanoTime();

        for (int i = 0; i < edgeArray.length; ++i) {
            Edge e = edgeArray[i];

            if(!uf.connected(e._u, e._v)) {
                uf.union(e._u, e._v);
                result[j++] = e;
            }
        }

        long c = System.nanoTime();

        if (MyGlobal.Config.debug == 1) {
            IGraph mst = MyGlobal.createGraph(g.getNumVertices(), result.length);

            for (int i = 0; i < j; ++i) {
                mst.addEdge(result[i]._u, result[i]._v, result[i]._weight);
            }

            if (!MST.check(g, mst)) {
                MyGlobal.abort("Not correct!");
            }
        }

        System.out.println("sort " + (b - a) / 1000000.0);
        System.out.println("merge " + (c - a) / 1000000.0);
    }
}
