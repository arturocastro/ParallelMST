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
    int _num_v;
    int _num_e;
    List<Edge>[] _adj;

    AdjListGraph(int nu) {
        _num_v = nu;
        _adj = new List[_num_v];

        for(List<Edge> l : _adj) {
            l = new ArrayList<Edge>();
        }
    }

    AdjListGraph(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            _num_v = Integer.parseInt(in.readLine());
            _num_e = Integer.parseInt(in.readLine());

            _adj = new List[_num_v];

            for (int i = 0; i < _num_v; ++i) {
                _adj[i] = new ArrayList<Edge>(_num_e);
            }

            String line;

            for (int i = 0; i < _num_e; ++i) {
                line = in.readLine();

                String [] tokens = line.split("\\s+");

                int u = Integer.parseInt(tokens[0]);
                int v = Integer.parseInt(tokens[1]);
                double w = Double.parseDouble(tokens[2]);

                addEdge(u, v, w);
                addEdge(v, u, w);
            }
        }
        catch (FileNotFoundException ex) {
            Global.abort(ex.getMessage());
        }
        catch (IOException ex) {
            Global.abort(ex.getMessage());
        }
    }

    @Override
    public int getNumVertices() {
        return 0;
    }

    @Override
    public double getEdgeWeight(final int u, final int v) {
        return _adj[u].get(v)._weight;
    }

    @Override
    public void addEdge(final int u, final int v, final double weight) {
        _adj[u].add(new Edge(u, v, weight));
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
                    return true;
                }

                int u = currU + 1;

                while(u < _adj.length) {
                    if (!_adj[u].isEmpty()) {
                        return true;
                    }

                    ++u;
                }

                return false;
            }

            @Override
            public Edge next() {
                if (currV < _adj[currU].size()) {
                    return _adj[currU].get(currV++);
                }

                ++currU;

                while(currU < _adj.length) {
                    if (!_adj[currU].isEmpty()) {
                        currV = 1;
                        return _adj[currU].get(0);
                    }

                    ++currU;
                }

                return null;
            }
        };

        return it;
    }

    public Iterator<Integer> iterateNeighbors(final int u) {
        Iterator<Integer> it = new Iterator<Integer>() {
            int currIdx = 0;

            @Override
            public boolean hasNext() {
                if (currIdx  < _adj[u].size()) {
                    return true;
                }

                return false;
            }

            @Override
            public Integer next() {
                return _adj[u].get(currIdx++)._v;
            }
        };

        return it;
    }
}
