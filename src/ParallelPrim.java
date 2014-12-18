import mpi.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by Arturo Isai Castro Perpuli on 29/11/2014.
 */

public class ParallelPrim {
    static final int HOMEMADE_HASH = 100;

    public static IGraph parallelPrim(AdjMatGraph g) {
        AdjMatGraph mst = new AdjMatGraph(g.getNumVertices(), g.getNumVertices());

        final int numtasks = MPI.COMM_WORLD.Size();
        final int rank = MPI.COMM_WORLD.Rank();

        ParaRange range = new ParaRange(0, g.getNumVertices(), numtasks, rank);

        final int n = g.getNumVertices();

        int [] pred = new int[n];
        Arrays.fill(pred, -1);

        double [] key = new double[n];
        Arrays.fill(key, (double)Integer.MAX_VALUE);

        key[0] = 0.0;

        boolean [] inQueue = new boolean[n];
        Arrays.fill(inQueue, true);

        int vi = 0;

        PriorityQueue<SimpleHash> pq = new PriorityQueue<SimpleHash>(n);

        for (int i = 0; i < n; ++i) {
            pq.add(new SimpleHash(i, key[i]));
        }

        //while (!pq.isEmpty()) {
        while (mst.getNumEdges() < g.getNumVertices() - 1) {

//	    int vi = pq.peek().v;

            // Get minimum key
            int [] localMin = new int[1];

            int localMinId = g.getNearestEdge(vi, range._start, range._end, inQueue);

            localMin[0] = hashEdge(localMinId, g.getEdgeWeight(vi, localMinId));

	        System.out.println("rank " + rank + " got min=" + localMinId + ", hash is " + localMin[0]);

            int [] globalMin = new int[1];

            MPI.COMM_WORLD.Allreduce(localMin, 0, globalMin, 0, 1, MPI.INT, MPI.MIN);

            final int u = recoverEdgeId(globalMin[0]);

            final double w = recoverEdgeWeight(globalMin[0], u);

	        System.out.println("result is " + globalMin[0] + ", global min is " + u);

            inQueue[vi] = false;
	        inQueue[u] = false;

            mst.addEdge(vi, u, w);

            if (rank == 0) {
                System.out.println(vi + " -- " + u);
            }

            pred[u] = vi;
            key[u] = w;

            for (int i = range._start; i < range._end; ++i) {
            if (inQueue[i]){
                //double wi = g.getEdgeWeight(
            }
	    }

//            // For each of u's neighbors
//            for (int v = range._start; v < range._end; ++v) {
//                // if PQ contains v
//                if (inQueue[v]) {
//                    double w = g.getEdgeWeight(u, v);
//
//                    if (w < key[v]) {
//                        pred[v] = u;
//                        key[v] = w;
//
//                        for (SimpleHash pair : pq) {
//                            if (pair.v == v) {
//                                pair.key = w;
//                                pq.add(pq.poll());
//
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
        }

        return null;
    }

    static int hashEdge(int edgeId, double edgeWeight) {
        return (int)(edgeWeight * HOMEMADE_HASH + edgeId);
    }

    static int recoverEdgeId(int result) {
        return result % HOMEMADE_HASH;
    }

    static double recoverEdgeWeight(int result, int edgeId) {
        return (result - edgeId) / HOMEMADE_HASH;
    }
}

class ParaRange {
    final int _start;
    final int _end;

    ParaRange(final int n1, final int n2, final int nprocs, final int rank) {
        final int work = ((n2 - n1) / nprocs) + 1;

        _start = Math.min(rank * work + n1, n2 + 1);
        _end = Math.min(_start + work - 1, n2);
    }
}
