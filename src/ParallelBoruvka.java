import java.util.Arrays;

/**
 * Created by Arturo Isai Castro Perpuli on 16/12/2014.
 */
public class ParallelBoruvka {
    public static void parallelBoruvka(IGraph g) {
        int mst = 0;
        final UF uf = new UF(g.getNumVertices());

        final Edge[] closest = new Edge[g.getNumVertices()];

        final Edge[] edges = new Edge[g.getNumEdges()];

        T[] th = new T[MyGlobal.Config.p];

        long s = System.nanoTime();
        for (Edge e : g) {
            edges[mst++] = e;
        }
        System.out.println((System.nanoTime() - s) / 1000000.0);

        mst = 0;

//        for (int i = 0; i < th.length; ++i) {
//            final int left = (i) * edges.length / MyGlobal.Config.p;
//            final int right = (i + 1) * edges.length / MyGlobal.Config.p;
//
//            MyGlobal.verbosePrint("left=" + left + ", right=" + right);
//
//            th[i] = new T(edges, closest, left, right, uf);
//        }

        // repeat at most log V times or until we have V-1 edges
        for (int t = 1; t < g.getNumVertices() && mst < g.getNumVertices() - 1; t = t + t) {
            Arrays.fill(closest, null);

            for (int i = 0; i < th.length; ++i) {
                final int left = (i) * edges.length / MyGlobal.Config.p;
                final int right = (i + 1) * edges.length / MyGlobal.Config.p;

                //MyGlobal.verbosePrint("left=" + left + ", right=" + right);

                th[i] = new T(edges, closest, left, right, uf);

                th[i].start();
            }

            for (int i = 0; i < th.length; ++i) {
                try {
                    th[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

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

class T extends Thread {
    Edge [] closest;
    Edge [] edges;

    int left;
    int right;

    UF uf;

    public T(Edge [] edges, Edge [] closest, int left, int right, UF uf) {
        this.closest = closest;
        this.edges = edges;
        this.left = left;
        this.right = right;
        this.uf = uf;
    }

    public void run() {
        for (int k = left; k < right; ++k) {
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
    }
}
