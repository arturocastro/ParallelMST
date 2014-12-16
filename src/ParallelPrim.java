/**
 * Created by Soluna on 16/12/2014.
 */

import mpi.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

public class ParallelPrim {
    static final int HOMEMADE_HASH = 10000000;

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

        //while (!pq.isEmpty()) {
        while (mst.getNumEdges() < g.getNumVertices()) {
            // Get minimum key
            int [] localMin = new int[1];

            int localMinId = g.getNearestEdge(vi, range._start, range._end, inQueue);

            localMin[0] = hashEdge(localMinId, key[localMinId]);

            int [] globalMin = new int[1];

            MPI.COMM_WORLD.AllReduce(localMin, 0, globalMin, 0, 1, MPI.INT, MPI.MIN);

            final int u = recoverEdgeId(globalMin[0]);

            final double w = recoverEdgeWeight(globalMin[0], u);

            inQueue[u] = false;

            mst.addEdge(vi, u, w);

            if (rank == 0) {
                System.out.println(vi + " -- " + u);
            }

            pred[u] = vi;
            key[u] = w;

            vi = u;

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
        return (int)(edgeWeight + HOMEMADE_HASH);
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