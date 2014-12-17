import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by Soluna on 15/12/2014.
 */

public class ParallelKruskal2 {
    static final int CYCLE_EDGE = 1;
    static final int MSF_EDGE = 2;

    public static void parallelKruskal(IGraph g) {
        Edge [] edgeArray = new Edge[g.getNumEdges()];
        int [] edgeColorMain = new int[g.getNumEdges()];
        int [] edgeColorHelper = new int [g.getNumEdges()];

        final int numHelpers = MyGlobal.Config.p - 1;

        ParallelKruskalHelperThread2[] helper = null;

        if (numHelpers > 0) {
            helper = new ParallelKruskalHelperThread2[numHelpers];
        }

        UF uf = new UF(g.getNumVertices());

        long a = System.nanoTime();

        {
            int i = 0;

            for (Edge e : g) {
                edgeArray[i] = e;
                ++i;
            }
        }

        long b = System.nanoTime();

        Arrays.sort(edgeArray);

        long c = System.nanoTime();

        AtomicInteger currMain = new AtomicInteger(0);

        for (int i = 0; i < numHelpers; ++i) {
            final int left = (i + 1) * edgeArray.length / MyGlobal.Config.p;
            final int right = (i + 2) * edgeArray.length / MyGlobal.Config.p;

            MyGlobal.verbosePrint("left=" + left + ", right=" + right);

            helper[i] = new ParallelKruskalHelperThread2(left, right, edgeColorHelper, currMain, uf, edgeArray);

            helper[i].start();
        }

        long d = System.nanoTime();

        for (int i = currMain.get(); i < edgeArray.length; i = currMain.incrementAndGet()) {
            Edge e = edgeArray[i];

            if (edgeColorHelper[i] != CYCLE_EDGE) {
                if (!uf.connected(e._u, e._v)) {
                    uf.union(e._u, e._v);
                    //System.out.println(e.toString());

                    edgeColorMain[i] = MSF_EDGE;
                } else {
                    edgeColorMain[i] = CYCLE_EDGE;
                }
            }
//            else {
//                MyGlobal.verbosePrint("NICE");
//            }
        }

        long f = System.nanoTime();

        for (int i = 0; i < numHelpers; ++i) {
            try {
                helper[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (MyGlobal.Config.verbose == 1) {
            System.out.println("assign array: " + (b - a)/1000000.0);
            System.out.println("sort: " + (c - b)/1000000.0);
            System.out.println("threading: " + (d - c)/1000000.0);
            System.out.println("main kruskal: " + (f - d)/1000000.0);
        }
    }
}

class ParallelKruskalHelperThread2 extends Thread {
    final int _left;
    final int _right;

    int [] _edgeColorHelper;
    AtomicInteger _currMain;
    UF _uf;
    Edge [] _edgeArray;

    ParallelKruskalHelperThread2(final int left, final int right, int [] edgeColorHelper, AtomicInteger currMain, UF uf, Edge [] edgeArray) {
        _left = left;
        _right = right;
        _edgeColorHelper = edgeColorHelper;
        _currMain = currMain;
        _uf = uf;
        _edgeArray = edgeArray;
    }

    @Override
    public void run() {
        while (_currMain.get() < _left) {
            for (int i = _left; i < _right; ++i) {
                if (_edgeColorHelper[i] == 0) {
                    if (_uf.connectedSafe(_edgeArray[i]._u, _edgeArray[i]._v)) {
                        _edgeColorHelper[i] = ParallelKruskal.CYCLE_EDGE;

                        //MyGlobal.verbosePrint("YES!");
                    }
                }
            }
        }

        MyGlobal.verbosePrint("ok...");
    }
}