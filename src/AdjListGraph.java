import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Soluna on 29/11/2014.
 */

public class AdjListGraph implements IGraph {
    int _num_vertexes;
    int _num_edges;
    List<Edge> [] _adj;

    AdjListGraph(final int num_vertexes, final int num_edges) {
        _num_vertexes = num_vertexes;
        _adj = new List[_num_vertexes];

        for(int i = 0; i < _num_vertexes; ++i) {
            _adj[i] = new ArrayList<Edge>(num_edges);
        }
    }

    AdjListGraph(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            _num_vertexes = Integer.parseInt(in.readLine());
            final int num_edges = Integer.parseInt(in.readLine());

            _adj = new List[_num_vertexes];

            for (int i = 0; i < _num_vertexes; ++i) {
                _adj[i] = new ArrayList<Edge>(num_edges);
            }

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
    public int getNumEdges() { return _num_edges; }

    @Override
    public double getEdgeWeight(final int u, final int v) {
        for (Edge e : _adj[u]) {
            if (e._v == v && e._u == u) {
                return e._weight;
            }
        }

        return -1;
    }

    @Override
    public void addEdge(final int u, final int v, final double weight) {
        _adj[u].add(new Edge(u, v, weight, true));
        _adj[v].add(new Edge(v, u, weight, false));
        ++_num_edges;
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
    public Iterator<Edge> iterator() {
        Iterator<Edge> it = new Iterator<Edge>() {
            int currU = 0;
            int currV = 0;

            @Override
            public boolean hasNext() {
                if (currV < _adj[currU].size()) {
                    for (int v = currV; v < _adj[currU].size(); ++v) {
                        if (_adj[currU].get(v)._original) {
                            currV = v;

                            return true;
                        }
                    }
                }

                int u = currU + 1;

                while(u < _adj.length) {
                    if (!_adj[u].isEmpty()) {
                        for (int v = 0; v < _adj[u].size(); ++v) {
                            if (_adj[u].get(v)._original) {
                                currU = u;
                                currV = v;

                                return true;
                            }
                        }
                    }

                    ++u;
                }

                return false;
            }

            @Override
            public Edge next() {
                return _adj[currU].get(currV++);
            }

            @Override
            public void remove() {

            }
        };

        return it;
    }

    public Iterator<Integer> iterateNeighbors(final int u) {
        Iterator<Integer> it = new Iterator<Integer>() {
            int currIdx = 0;

            @Override
            public boolean hasNext() {
                if (currIdx < _adj[u].size()) {
                    return true;
                }

                return false;
            }

            @Override
            public Integer next() {
                return _adj[u].get(currIdx++)._v;
            }

            @Override
            public void remove() {

            }
        };

        return it;
    }

    @Override
    public Edge getLightestIncidentEdge(final int u) {
        double minWeight = (double)Integer.MAX_VALUE;
        Edge minEdge = null;

        for (Edge e : _adj[u]) {
            if (e._weight < minWeight) {
                minEdge = e;
            }
        }

        return minEdge;
    }
}
