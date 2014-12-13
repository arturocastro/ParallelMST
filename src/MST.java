import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by Soluna on 01/12/2014.
 */
public class MST {
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

        return g;
    }

    public static void boruvska(IGraph g) {

    }

    public static void parallelFast(IGraph g) {
        final int n = g.getNumVertices();

        AtomicIntegerArray color = new AtomicIntegerArray(n);
        AtomicIntegerArray visited = new AtomicIntegerArray(n);

        ParallelFastThread [] thread = new ParallelFastThread[MyGlobal.Config.p];

        for (int i = 0; i < MyGlobal.Config.p; ++i) {
            final int left = i * n / MyGlobal.Config.p;
            final int right = (i + 1) * n / MyGlobal.Config.p;

            thread[i] = new ParallelFastThread(left, right, color, visited, g, i);

            thread[i].start();
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
}

final class ParallelFastThread extends Thread {
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

    public ParallelFastThread(final int leftV, final int rightV, AtomicIntegerArray color, AtomicIntegerArray visited, IGraph g, int p) {
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
        while (/*n > nb*/true) {
            // 1
//            for (int v = _leftV; v < _rightV; ++v) {
//                _color[v] = _visited[v] = 0;
//            }

            // 2
            parallel2();

            // 3
            for (int v = _leftV; v < _rightV; ++v) {
                if (_visited.get(v) == 0) {
                    // find lightest incident edge e to v, label e to be in MST
                }
            }

            // 4
        }
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
                    break;
                }

                if (_visited.compareAndSet(w, 0, 1)) {
                    // label something-something

                    // For each of w's neighbors
                    for (Iterator<Integer> it = _g.iterateNeighbors(w); it.hasNext(); ) {
                        int u = it.next();

                        _visited.compareAndSet(w, 0, 1);

                        // if PQ contains u
                        if (_inQueue[w]) {
                            double u_weight = _g.getEdgeWeight(u, v);

                            if (u_weight < _key[v]) {
                                _pred[v] = u;
                                _key[v] = w;

                                for (SimpleHash pair : _pq) {
                                    if (pair.v == v) {
                                        pair.key = w;
                                        _pq.add(_pq.poll());

                                        break;
                                    }
                                }
                            }
                        } else {
                            _pq.add(new SimpleHash(u, Integer.MAX_VALUE));
                        }
                    }
                }
            }
        }
    }
}
