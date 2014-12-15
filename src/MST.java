import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by Soluna on 01/12/2014.
 */
public class MST {
    static final int CYCLE_EDGE = 1;
    static final int MSF_EDGE = 2;

    public static IGraph prim(IGraph g) {
        final int n = g.getNumVertices();

        int [] pred = new int[n];
        Arrays.fill(pred, -1);

        double [] key = new double[n];
        Arrays.fill(key, (double)Integer.MAX_VALUE);

        key[0] = 0.0;

        PriorityQueue<SimpleHash> pq = new PriorityQueue<SimpleHash>(n);

        boolean [] inQueue = new boolean[n];
        Arrays.fill(inQueue, true);

        for (int i = 0; i < n; ++i) {
            pq.add(new SimpleHash(i, key[i]));
        }

        while (!pq.isEmpty()) {
            // Get minimum key
            int u = pq.poll().v;
            inQueue[u] = false;

            // For each of u's neighbors
            for (Iterator<Integer> it = g.iterateNeighbors(u); it.hasNext();) {
                int v = it.next();

                // if PQ contains v
                if (inQueue[v]) {
                    double w = g.getEdgeWeight(u, v);

                    if (w < key[v]) {
                        pred[v] = u;
                        key[v] = w;

                        for (SimpleHash pair : pq) {
                            if (pair.v == v) {
                                pair.key = w;
                                pq.add(pq.poll());

                                break;
                            }
                        }
                    }
                }
            }
        }

        IGraph mst = new AdjListGraph(g.getNumVertices(), g.getNumVertices() - 1);

        for (int i = 0; i < g.getNumVertices(); ++i) {
            if (pred[i] != -1) {
                mst.addEdge(i, pred[i], key[i]);
            }
        }

        if (!check(g, mst)) {
            System.out.println("Something is wrong");
        }

        return mst;
    }

    public static void kruskal(IGraph g) {
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(g.getNumEdges());
        UF uf = new UF(g.getNumVertices());

        for (Edge e : g) {
            pq.add(e);
        }

        while (!pq.isEmpty()) {
            Edge e = pq.poll();

            if(!uf.connected(e._u, e._v)) {
                uf.union(e._u, e._v);
                System.out.println(e.toString());
            }
        }
    }

    public static void boruvska(IGraph g) {

    }

    public static void parallelKruskal(IGraph g) {
        Edge [] edgeArray = new Edge[g.getNumEdges()];
        int [] edgeColorMain = new int[g.getNumEdges()];
        AtomicIntegerArray edgeColorHelper = new AtomicIntegerArray(g.getNumEdges());

        ParallelKruskalHelperThread [] helper = new ParallelKruskalHelperThread[MyGlobal.Config.p - 1];

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

        for (int i = 0; i < helper.length; ++i) {
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

        for (int i = 0; i < helper.length; ++i) {
            try {
                helper[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cong(IGraph g) {
        final int n = g.getNumVertices();

        AtomicIntegerArray color = new AtomicIntegerArray(n);
        AtomicIntegerArray visited = new AtomicIntegerArray(n);

        CongThread[] thread = new CongThread[MyGlobal.Config.p];

        for (int i = 0; i < MyGlobal.Config.p; ++i) {
            final int left = i * n / MyGlobal.Config.p;
            final int right = (i + 1) * n / MyGlobal.Config.p;

            thread[i] = new CongThread(left, right, color, visited, g, i);

            thread[i].start();
        }
        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < MyGlobal.Config.p; ++i) {
            try {
                thread[i].join(MyGlobal.Config.TIMEOUT_MULTI);

                if (thread[i].isAlive()) {
                    MyGlobal.abort("Timeout after " + MyGlobal.Config.TIMEOUT_MULTI / 60 / 1000 + " min.");
                }
            } catch (InterruptedException ex) {
                MyGlobal.abort(ex.getMessage());
            }
        }
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    /*
    *  @author Robert Sedgewick
    *  @author Kevin Wayne
    *
    *  Modified by Arturo Isai Castro Perpuli
    */
    static boolean check(IGraph G, Iterable<Edge> mst) {

        // check weight
        double totalWeight = 0.0;
        for (Edge e : mst) {
            totalWeight += e._weight;
        }
        double EPSILON = 1E-12;
//        if (Math.abs(totalWeight - weight()) > EPSILON) {
//            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
//            return false;
//        }

        // check that it is acyclic
        UF uf = new UF(G.getNumVertices());
        for (Edge e : mst) {
            //int v = e.either(), w = e.other(v);
            if (uf.connected(e._u, e._v)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(e._u, e._v);
        }

        // check that it is a spanning forest
        for (Edge e : G) {
            //int v = e.either(), w = e.other(v);
            if (!uf.connected(e._u, e._v)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : mst) {

            // all edges in MST except e
            uf = new UF(G.getNumVertices());
            for (Edge f : mst) {
                //int x = f.either(), y = f.other(x);
                if (f != e) uf.union(f._u, f._v);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G) {
                //int x = f.either(), y = f.other(x);
                if (!uf.connected(f._u, f._v)) {
                    if (f._weight < e._weight) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }
}

final class CongThread extends Thread {
    final int _leftV;
    final int _rightV;

    final int _p;

    AtomicIntegerArray _color;
    AtomicIntegerArray _visited;

    IGraph _g;

    PriorityQueue<SimpleHash> _pq;
    boolean [] _inQueue;
    int [] _pred;
    double [] _key;

    public CongThread(final int leftV, final int rightV, AtomicIntegerArray color, AtomicIntegerArray visited, IGraph g, int p) {
        _leftV = leftV;
        _rightV = rightV;
        _color = color;
        _visited = visited;
        _g = g;
        _p = p;

        _pq = new PriorityQueue<SimpleHash>(_g.getNumVertices());
        _inQueue = new boolean[_g.getNumVertices()];
        _key = new double[_g.getNumVertices()];
        _pred = new int[_g.getNumVertices()];
    }

    // parallel1
    @Override
    public void run() {
        if (MyGlobal.Config.p == 1) {
            //MST.prim(_g);
            parallel1();
        } else {
            parallel1();
        }
    }

    void parallel1() {
        int n = _g.getNumVertices();
        Edge [] closest = new Edge[_g.getNumVertices()];

        UF uf = new UF(_g.getNumVertices());

        // "Epochs"
        while (n > MyGlobal.Config.nb) {
            // 1
            for (int v = _leftV; v < _rightV; ++v) {
                _color.set(v, 0);
                _visited.set(v, 0);
            }

            // 2
            parallel2();

            // 3
            for (Edge e : _g) {
                int i = uf.find(e._u);
                int j = uf.find(e._v);

                if (i == j) {
                    // same tree
                    continue;
                }

                if (closest[i] == null || e._weight < closest[i]._weight) {
                    closest[i] = e;
                }

                if (closest[j] == null || e._weight < closest[j]._weight) {
                    closest[j] = e;
                }
            }

//            for (int v = _leftV; v < _rightV; ++v) {
//                if (_visited.get(v) == 0) {
//                    // find lightest incident edge e to v, label e to be in MST
//                    min[v] = _g.getLightestIncidentEdge(v);
//                }
//            }

            // 4 (and 5?)
            // add newly discovered edges to MST
            for (int i = _leftV; i < _rightV; ++i) {
                Edge e = closest[i];
                if (e != null) {
                    // don't add the same edge twice
                    if (!uf.connected(e._u, e._v)) {
                        //mst.add(e);
                        System.out.println(e.toString());
                        //weight += e._weight;
                        uf.union(e._u, e._v);
                    }
                }
            }

            // 5

            // 6
            n = uf.count();
        }

        // 7
    }

    public void parallel2() {
        _pq.clear();
        Arrays.fill(_inQueue, false);

        Arrays.fill(_key, (double)Integer.MAX_VALUE);
        Arrays.fill(_pred, -1);

        // 1
        for (int v = _leftV; v < _rightV; ++v) {
            // 1.1
            if (_color.get(v) != 0) {
                continue;
            }

            // 1.2
            int myColor = v + 1;
            _color.set(v, myColor);

            // 1.3
            _pq.add(new SimpleHash(v, Integer.MAX_VALUE));
            _inQueue[v] = true;

            // 1.4
            while (!_pq.isEmpty()) {
                int w = _pq.poll().v;
                _inQueue[w] = false;

                if (_color.get(w) != myColor /*or something-something*/) {
                    // go to next in for loop
                    break;
                }

                if (_visited.compareAndSet(w, 0, 1)) {
                    // label something-something

                    // For each of w's neighbors
                    for (Iterator<Integer> it = _g.iterateNeighbors(w); it.hasNext(); ) {
                        int u = it.next();

                        _color.compareAndSet(u, 0, myColor);

                        // if PQ contains u
                        if (_inQueue[u]) {
                            double u_weight = _g.getEdgeWeight(w, u);

                            if (u_weight < _key[u]) {
                                _pred[u] = w;
                                _key[u] = u_weight;

                                for (SimpleHash pair : _pq) {
                                    if (pair.v == u) {
                                        pair.key = u_weight;
                                        _pq.add(_pq.poll());

                                        break;
                                    }
                                }
                            }
                        } else {
                            _pq.add(new SimpleHash(u, Integer.MAX_VALUE));
                            _inQueue[u] = true;
                        }
                    }
                }
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
                        _edgeColorHelper.set(i, MST.CYCLE_EDGE);
                    }
                }
            }
        }
    }
}

//class GoogleThread extends Thread {
//    @Override
//    public void run() {
//
//    }
//
//    void mst() {
//        boolean flag = true;
//
//        while (flag) {
//            if (_color[minNode] == -1) {
//
//            }
//        }
//    }
//}