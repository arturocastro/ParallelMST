import java.util.Arrays;

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

        Arrays.sort(edgeArray);

        for (int i = 0; i < edgeArray.length; ++i) {
            Edge e = edgeArray[i];

            if(!uf.connected(e._u, e._v)) {
                uf.union(e._u, e._v);
                result[j++] = e;

                if (MyGlobal.Config.verbose == 1) {
                    System.out.println(e.toString());
                }
            }
        }

        if (MyGlobal.Config.debug == 1) {
            IGraph mst = MyGlobal.createGraph(g.getNumVertices(), result.length);

            for (int i = 0; i < result.length; ++i) {
                mst.addEdge(result[i]._u, result[i]._v, result[i]._weight);
            }

            if (!MST.check(g, mst)) {
                MyGlobal.abort("Not correct!");
            }
        }
    }
}
