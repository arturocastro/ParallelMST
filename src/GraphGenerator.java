/*************************************************************************
 *  Compilation:  javac GraphGenerator.java
 *  Execution:    java GraphGenerator V E
 *  Dependencies: Graph.java
 *
 *  A graph generator.
 *
 *  For many more graph generators, see
 *  http://networkx.github.io/documentation/latest/reference/generators.html
 *
 *************************************************************************/

/**
 *  The <tt>GraphGenerator</tt> class provides static methods for creating
 *  various graphs, including Erdos-Renyi random graphs, random bipartite
 *  graphs, random k-regular graphs, and random rooted trees.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/41undirected">Section 4.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  *  Copyright © 2002–2010, Robert Sedgewick and Kevin Wayne.
 *  http://algs4.cs.princeton.edu/41undirected/GraphGenerator.java.html
 */

/* Modified by Arturo Isai Castro Perpuli */

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiKeyMap;

public class GraphGenerator {
//    private static final class Edge implements Comparable<Edge> {
//        private int v;
//        private int w;
//        private Edge(int v, int w) {
//            if (v < w) {
//                this.v = v;
//                this.w = w;
//            }
//            else {
//                this.v = w;
//                this.w = v;
//            }
//        }
//        public int compareTo(Edge that) {
//            if (this.v < that.v) return -1;
//            if (this.v > that.v) return +1;
//            if (this.w < that.w) return -1;
//            if (this.w > that.w) return +1;
//            return 0;
//        }
//    }

    /**
     * Returns a random simple graph containing <tt>V</tt> vertices and <tt>E</tt> edges.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple graph on <tt>V</tt> vertices, containing a total
     *     of <tt>E</tt> edges
     * @throws IllegalArgumentException if no such simple graph exists
     */
    public static IGraph simple(int V, int E) {
        if (E > (long) V*(V-1)/2) throw new IllegalArgumentException("Too many edges");
        if (E < 0)                throw new IllegalArgumentException("Too few edges");

        IGraph G = MyGlobal.createGraph(V, E);

        MultiKeyMap set = new MultiKeyMap();

        while (G.getNumEdges() < E) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);

            if ((v != w) && !set.containsKey(v, w)) {
                set.put(v, w, 1);
                set.put(w, v, 1);

                Edge e = new Edge(v, w);

                G.addEdge(e._u, e._v, e._weight);
            }
        }
        return G;
    }

    /**
     * Returns a random simple graph on <tt>V</tt> vertices, with an 
     * edge between any two vertices with probability <tt>p</tt>. This is sometimes
     * referred to as the Erdos-Renyi random graph model.
     * @param V the number of vertices
     * @param p the probability of choosing an edge
     * @return a random simple graph on <tt>V</tt> vertices, with an edge between
     *     any two vertices with probability <tt>p</tt>
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    public static IGraph simple(int V, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        IGraph G = new AdjListGraph(V, 0);
        for (int v = 0; v < V; v++)
            for (int w = v+1; w < V; w++)
                if (StdRandom.bernoulli(p)) {
                    Edge e = new Edge(v, w);
                    G.addEdge(e._u, e._v, e._weight);
                }
        return G;
    }

    /**
     * Returns the complete graph on <tt>V</tt> vertices.
     * @param V the number of vertices
     * @return the complete graph on <tt>V</tt> vertices
     */
    public static IGraph complete(int V) {
        return simple(V, 1.0);
    }

    /**
     * Returns a uniformly random <tt>k</tt>-regular graph on <tt>V</tt> vertices
     * (not necessarily simple). The graph is simple with probability only about e^(-k^2/4),
     * which is tiny when k = 14.
     * @param V the number of vertices in the graph
     * @return a uniformly random <tt>k</tt>-regular graph on <tt>V</tt> vertices.
     */
   /* public static IGraph regular(int V, int k) {
        if (V*k % 2 != 0) throw new IllegalArgumentException("Number of vertices * k must be even");
        Graph G = new Graph(V);

        // create k copies of each vertex
        int[] vertices = new int[V*k];
        for (int v = 0; v < V; v++) {
            for (int j = 0; j < k; j++) {
                vertices[v + V*j] = v;
            }
        }

        // pick a random perfect matching
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V*k/2; i++) {
            G.addEdge(vertices[2*i], vertices[2*i + 1]);
        }
        return G;
    }*/

    // http://www.proofwiki.org/wiki/Labeled_Tree_from_Prüfer_Sequence
    // http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.36.6484&rep=rep1&type=pdf
    /**
     * Returns a uniformly random tree on <tt>V</tt> vertices.
     * This algorithm uses a Prufer sequence and takes time proportional to <em>V log V</em>.
     * @param V the number of vertices in the tree
     * @return a uniformly random tree on <tt>V</tt> vertices
     */
//    public static Graph tree(int V) {
//        Graph G = new Graph(V);
//
//        // special case
//        if (V == 1) return G;
//
//        // Cayley's theorem: there are V^(V-2) labeled trees on V vertices
//        // Prufer sequence: sequence of V-2 values between 0 and V-1
//        // Prufer's proof of Cayley's theorem: Prufer sequences are in 1-1
//        // with labeled trees on V vertices
//        int[] prufer = new int[V-2];
//        for (int i = 0; i < V-2; i++)
//            prufer[i] = StdRandom.uniform(V);
//
//        // degree of vertex v = 1 + number of times it appers in Prufer sequence
//        int[] degree = new int[V];
//        for (int v = 0; v < V; v++)
//            degree[v] = 1;
//        for (int i = 0; i < V-2; i++)
//            degree[prufer[i]]++;
//
//        // pq contains all vertices of degree 1
//        MinPQ<Integer> pq = new MinPQ<Integer>();
//        for (int v = 0; v < V; v++)
//            if (degree[v] == 1) pq.insert(v);
//
//        // repeatedly delMin() degree 1 vertex that has the minimum index
//        for (int i = 0; i < V-2; i++) {
//            int v = pq.delMin();
//            G.addEdge(v, prufer[i]);
//            degree[v]--;
//            degree[prufer[i]]--;
//            if (degree[prufer[i]] == 1) pq.insert(prufer[i]);
//        }
//        G.addEdge(pq.delMin(), pq.delMin());
//        return G;
//    }

    /**
     * Unit tests the <tt>GraphGenerator</tt> library.
     */
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int V1 = V/2;
        int V2 = V - V1;

        StdOut.println("complete graph");
        StdOut.println(complete(V));
        StdOut.println();

        StdOut.println("simple");
        StdOut.println(simple(V, E));
        StdOut.println();

        StdOut.println("Erdos-Renyi");
        double p = (double) E / (V*(V-1)/2.0);
        StdOut.println(simple(V, p));
        StdOut.println();

        /*StdOut.println("4-regular");
        StdOut.println(regular(V, 4));
        StdOut.println();*/
    }
}

