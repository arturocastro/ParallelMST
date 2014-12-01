/**
 * Created by Soluna on 29/11/2014.
 */
public interface IGraph {
    int getNumVertices();
    double getEdgeWeight(final int u, final int v);

    void addEdge(final int u, final int v, final double weight);
}
