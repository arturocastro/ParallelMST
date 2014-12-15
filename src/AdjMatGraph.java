import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Soluna on 15/12/2014.
 */

public class AdjMatGraph implements IGraph {
    int _num_vertexes;
    int _num_edges;
    double [][] _adj;

    ArrayList<Edge> _edges;

    AdjMatGraph(final int num_vertexes, final int num_edges) {
        _num_vertexes = num_vertexes;

        _adj = new double[num_vertexes][num_vertexes];

        _edges = new ArrayList<Edge>(num_edges);
    }

    AdjMatGraph(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            _num_vertexes = Integer.parseInt(in.readLine());
            final int num_edges = Integer.parseInt(in.readLine());

            _adj = new double[_num_vertexes][_num_vertexes];

            _edges = new ArrayList<Edge>(num_edges);

            String line;

            for (int i = 0; i < num_edges; ++i) {
                line = in.readLine();

                String [] tokens = line.split("\\s+");

                int u = Integer.parseInt(tokens[0]);
                int v = Integer.parseInt(tokens[1]);
                double w = Double.parseDouble(tokens[2]);

                addEdge(u, v, w);
                //addEdge(v, u, w, false);
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

        _edges.add(new Edge(u, v, weight));
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
                    if (_adj[u][currIdx] != 0) {
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

    @Override
    public Iterator<Edge> iterator() {
        Iterator<Edge> it = new Iterator<Edge>() {
            //int currU = 0;
            //int currV = currU + 1;

            int currIx = 0;

            @Override
            public boolean hasNext() {
//                while (currU < _num_vertexes - 1) {
//                    currV = currU + 1;
//
//                    while (currV < _num_vertexes) {
//                        if (_adj[currU][currV] != 0) {
//                            return true;
//                        }
//
//                        ++currV;
//                    }
//
//                    ++currU;
//                }
                if (currIx < _edges.size()) {
                    return true;
                }

                return false;
            }

            @Override
            public Edge next() {
                return _edges.get(currIx++);
            }

            @Override
            public void remove() {

            }
        };

        return it;
    }
}
