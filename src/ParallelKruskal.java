import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by Soluna on 15/12/2014.
 */

public class ParallelKruskal {
    static final int CYCLE_EDGE = 1;
    static final int MSF_EDGE = 2;

    public static void parallelKruskal(IGraph g) {
        Edge [] edgeArray = new Edge[g.getNumEdges()];
        int [] edgeColorMain = new int[g.getNumEdges()];
        AtomicIntegerArray edgeColorHelper = new AtomicIntegerArray(g.getNumEdges());

        final int numHelpers = MyGlobal.Config.p - 1;

        ParallelKruskalHelperThread[] helper = null;

        if (numHelpers > 0) {
            helper = new ParallelKruskalHelperThread[numHelpers];
        }

        UF uf = new UF(g.getNumVertices());

        {
            int i = 0;

            for (Edge e : g) {
                edgeArray[i] = e;
                ++i;
            }
        }

        Arrays.sort(edgeArray);

        AtomicInteger currMain = new AtomicInteger(0);

        for (int i = 0; i < numHelpers; ++i) {
            final int left = (i + 1) * edgeArray.length / MyGlobal.Config.p;
            final int right = (i + 2) * edgeArray.length / MyGlobal.Config.p;

            helper[i] = new ParallelKruskalHelperThread(left, right, edgeColorHelper, currMain, uf, edgeArray);

            helper[i].start();
        }

        for (int i = currMain.get(); i < edgeArray.length; i = currMain.incrementAndGet()) {
            Edge e = edgeArray[i];

            if (edgeColorHelper.get(i) != CYCLE_EDGE) {
                if (!uf.connected(e._u, e._v)) {
                    uf.union(e._u, e._v);
                    System.out.println(e.toString());

                    edgeColorMain[i] = MSF_EDGE;
                } else {
                    edgeColorMain[i] = CYCLE_EDGE;
                }
            }
        }

        for (int i = 0; i < numHelpers; ++i) {
            try {
                helper[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ParallelKruskalHelperThread extends Thread {
    final int _left;
    final int _right;

    AtomicIntegerArray _edgeColorHelper;
    AtomicInteger _currMain;
    UF _uf;
    Edge [] _edgeArray;

    ParallelKruskalHelperThread(final int left, final int right, AtomicIntegerArray edgeColorHelper, AtomicInteger currMain, UF uf, Edge [] edgeArray) {
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
                if (_edgeColorHelper.get(i) == 0) {
                    if (_uf.connectedSafe(_edgeArray[i]._u, _edgeArray[i]._v)) {
                        _edgeColorHelper.set(i, ParallelKruskal.CYCLE_EDGE);
                    }
                }
            }
        }
    }
}