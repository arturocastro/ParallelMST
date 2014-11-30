import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soluna on 29/11/2014.
 */

public class AdjListGraph implements IGraph {
    private class Edge {
        int _v;
        double _weight;

        Edge(final int v, final double weight) {
            _v = v;
            _weight = weight;
        }
    }

    int _n;
    List<Edge>[] _adj;

    AdjListGraph(int nu) {
        _n = nu;
        _adj = (List<Edge>[]) new List[_n];

        for(List<Edge> l : _adj) {
            l = new ArrayList<Edge>();
        }
    }

    AdjListGraph(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            _n = Integer.parseInt(in.readLine());
            int nv = Integer.parseInt(in.readLine());

            _adj = (List<Edge>[]) new List[_n];

            for(List<Edge> l : _adj) {
                l = new ArrayList<Edge>();
            }

            String line;

            for (int i = 0; i < nv; ++i) {
                line = in.readLine();

                String [] toks = line.split("\\s+");

                int u1 = Integer.parseInt(toks[0]);
                int u2 = Integer.parseInt(toks[1]);
                double w = Integer.parseInt(toks[2]);


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
        _adj[u].add(new Edge(v, weight));
    }

    @Override
    public void draw() {

    }
}
