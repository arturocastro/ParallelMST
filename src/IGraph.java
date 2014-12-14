import java.util.Iterator;

/**
 * Created by Soluna on 29/11/2014.
 */
public interface IGraph extends Iterable<Edge> {
    int getNumVertices();
    int getNumEdges();
    double getEdgeWeight(final int u, final int v);

    void addEdge(final int u, final int v, final double weight);

    public Iterator<Integer> iterateNeighbors(final int u);

    public Edge getLightestIncidentEdge(final int u);
}
