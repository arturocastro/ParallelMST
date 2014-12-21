import java.nio.channels.MulticastChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Arturo Isai Castro Perpuli on 15/12/2014.
 */

public class ParallelKruskal4 {
    static final int CYCLE_EDGE = 1;

    private static HeavySort.ArrayFactory<Edge> edgeArrayFactory =
            new HeavySort.ArrayFactory<Edge>(){

                @Override
                public Edge[] buildArray(int length) {
                    return new Edge[length];
                }

            };

    public static void parallelKruskal(IGraph g) {
        Edge [] edgeArray = new Edge[g.getNumEdges()];
        int [] edgeColorHelper = new int [g.getNumEdges()];
        Edge [] result = new Edge[g.getNumVertices() - 1];
        AtomicInteger currMain = new AtomicInteger(0);
        final ExecutorService executor = Executors.newFixedThreadPool(MyGlobal.Config.p);

        final int numHelpers = MyGlobal.Config.p - 1;

        ParallelKruskalHelperThread4[] helper = null;

        if (numHelpers > 0) {
            helper = new ParallelKruskalHelperThread4[numHelpers];
        }

        UF uf = new UF(g.getNumVertices());

        long a = System.nanoTime();

        int j = 0;

        for (Edge e : g) {
            edgeArray[j++] = e;
        }

        j = 0;

        long b = System.nanoTime();

        HeavySort.sort(edgeArray, executor, MyGlobal.Config.p, edgeArrayFactory);

        executor.shutdown();

        System.out.println(edgeArray.length);

        long c = System.nanoTime();

        for (int i = 0; i < numHelpers; ++i) {
            helper[i] = new ParallelKruskalHelperThread4(edgeColorHelper, currMain, uf, edgeArray, i, g.getNumEdges());

            helper[i].start();
        }

        long d = System.nanoTime();

        for (int i = currMain.get(); i < edgeArray.length; i = currMain.incrementAndGet()) {
            Edge e = edgeArray[i];

            if (edgeColorHelper[i] == 0) {
                if (!uf.connected(e._u, e._v)) {
                    uf.union(e._u, e._v);
                    result[j++] = e;
                    //System.out.println(e.toString());
                }
            }
        }

        long f = System.nanoTime();

        for (int i = 0; i < numHelpers; ++i) {
            try {
                helper[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (MyGlobal.Config.verbose == 1) {
                System.out.println("helper " + i + " runtime " + helper[i].runtime);
            }
        }

        if (MyGlobal.Config.verbose == 1) {
            System.out.println("assign array: " + (b - a)/1000000.0);
            System.out.println("sort: " + (c - b)/1000000.0);
            System.out.println("threading: " + (d - c)/1000000.0);
            System.out.println("main kruskal: " + (f - d)/1000000.0);
        }

        if (MyGlobal.Config.debug == 1) {
            IGraph mst = MyGlobal.createGraph(g.getNumVertices(), result.length);

            for (int i = 0; i < j; ++i) {
                mst.addEdge(result[i]._u, result[i]._v, result[i]._weight);
            }

            if (!MST.check(g, mst)) {
                MyGlobal.abort("Not correct!");
            }
        }
    }
}

class ParallelKruskalHelperThread4 extends Thread {
    int _left;
    int _right;

    int [] _edgeColorHelper;
    AtomicInteger _currMain;
    UF _uf;
    Edge [] _edgeArray;

    int _id;
    int _n;
    public double runtime;

    int current;

    ParallelKruskalHelperThread4(int [] edgeColorHelper, AtomicInteger currMain, UF uf, Edge [] edgeArray, int id, int n) {
        _edgeColorHelper = edgeColorHelper;
        _currMain = currMain;
        _uf = uf;
        _edgeArray = edgeArray;
        _id = id;
        _n = n;

        current = n / MyGlobal.Config.p;

        _left = (id + 1) * current;
        _right = (id + 2) * current - 1;
        MyGlobal.verbosePrint("id=" + _id + ", left=" + _left + ", right=" + _right);
    }

    @Override
    public void run() {
        long start = System.nanoTime();

        while (_currMain.get() < _n - 100) {
            if (_currMain.get() >= current) {
                final int size = _n - current;
                final int chunk = size / MyGlobal.Config.p;

                _left = current + (_id + 1) * chunk;
                _right = current + (_id + 2) * chunk - 1;

                current += chunk;

                MyGlobal.verbosePrint("id=" + _id + ", left=" + _left + ", right=" + _right);
            }

            for (int i = _left; i < _right; ++i) {
                if (_edgeColorHelper[i] == 0) {
                    if (_uf.connectedSafe(_edgeArray[i]._u, _edgeArray[i]._v)) {
                        _edgeColorHelper[i] = ParallelKruskal.CYCLE_EDGE;

                        //MyGlobal.verbosePrint("YES!");
                    }
                }
            }
        }

        runtime = (System.nanoTime() - start) / 1000000.0;

        //MyGlobal.verbosePrint("ok...");
    }
}