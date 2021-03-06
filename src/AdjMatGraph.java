import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.MultiKeyMap;

/**
 * Created by Arturo Isai Castro Perpuli on 15/12/2014.
 */

public class AdjMatGraph implements IGraph {
    int _num_vertexes;
    int _num_edges;
    public double [][] _adj;

    MultiKeyMap _edges;

    final double NO_EDGE = 0.0;

    AdjMatGraph(final int num_vertexes, final int num_edges) {
        _num_vertexes = num_vertexes;

        _adj = new double[num_vertexes][num_vertexes];

        _edges = new MultiKeyMap();
    }

    AdjMatGraph(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            _num_vertexes = Integer.parseInt(in.readLine());
            final int num_edges = Integer.parseInt(in.readLine());

            _adj = new double[_num_vertexes][_num_vertexes];

            _edges = new MultiKeyMap();

            String line;

            for (int i = 0; i < num_edges; ++i) {
                line = in.readLine();

                String [] tokens = line.split("\\s+");

                int u = Integer.parseInt(tokens[0]);
                int v = Integer.parseInt(tokens[1]);
                double w = Double.parseDouble(tokens[2]);

                addEdge(u, v, w);
            }
        }
        catch (FileNotFoundException ex) {
            MyGlobal.abort(ex.getMessage());
        }
        catch (IOException ex) {
            MyGlobal.abort(ex.getMessage());
        }
    }

    @Override
    public int getNumVertices() {
        return _num_vertexes;
    }

    @Override
    public int getNumEdges() {
        return _num_edges;
    }

    @Override
    public double getEdgeWeight(int u, int v) {
        return _adj[u][v];
    }

    @Override
    public void addEdge(int u, int v, double weight) {
        _adj[u][v] = weight;
        _adj[v][u] = weight;
        ++_num_edges;

        Edge e = new Edge(u, v, weight);

        _edges.put(u, v, e);
    }

    @Override
    public String toString() {
        StringBuilder strBld = new StringBuilder("graph G{");

        for (Edge e : this) {
            strBld.append(e.toString());
        }

        strBld.append("}");

        return strBld.toString();
    }

    @Override
    public Iterator<Integer> iterateNeighbors(final int u) {
        Iterator<Integer> it = new Iterator<Integer>() {
            int currIdx = 0;

            @Override
            public boolean hasNext() {
                while (currIdx < _num_vertexes) {
                    if (_adj[u][currIdx] != NO_EDGE) {
                        return true;
                    }

                    ++currIdx;
                }

                return false;
            }

            @Override
            public Integer next() {
                return currIdx++;
            }

            @Override
            public void remove() {

            }
        };

        return it;
    }

    @Override
    public Edge getLightestIncidentEdge(int u) {
        return null;
    }

    public int getNearestEdge(int u, int left, int right, boolean [] in) {
        double min = (double)Integer.MAX_VALUE;
        int minV = -1;

        for (int v = left; v < right; ++v) {
	    //System.out.println(_adj[u][v] + " - " + min);
	    //System.out.println(in[v]);

            if (in[v] && v != u && _adj[u][v] != NO_EDGE && _adj[u][v] < min) {
		//System.out.println("YES!");
                min = _adj[u][v];
                minV = v;

		//System.out.println("yes!" + min);
            }
        }

        return minV;
    }

    @Override
    public Edge getEdge(int u, int v) {
        if (_edges.containsKey(u, v)) {
            return (Edge) _edges.get(u, v);
        }

        if (_edges.containsKey(v, u)) {
            return (Edge) _edges.get(v, u);
        }

        return null;
    }

    @Override
    public Iterator<Edge> iterator() {
        return new Iterator<Edge>() {
            MapIterator it = _edges.mapIterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Edge next() {
                it.next();

                return (Edge) it.getValue();
            }

            @Override
            public void remove() {

            }
        };

//        Iterator<Edge> it = new Iterator<Edge>() {
//            //int currU = 0;
//            //int currV = currU + 1;
//
//            @Override
//            public boolean hasNext() {
////                while (currU < _num_vertexes - 1) {
////                    currV = currU + 1;
////
////                    while (currV < _num_vertexes) {
////                        if (_adj[currU][currV] != 0) {
////                            return true;
////                        }
////
////                        ++currV;
////                    }
////
////                    ++currU;
////                }
//                if (currIx < _edges.size()) {
//                    return true;
//                }
//
//                return false;
//            }
//
//            @Override
//            public Edge next() {
//                return _edges.get(currIx++);
//            }
//
//            @Override
//            public void remove() {
//
//            }
//        };
    }
}
