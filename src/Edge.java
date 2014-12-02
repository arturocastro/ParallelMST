/**
 * Created by Soluna on 02/12/2014.
 */

class Edge {
    int _u;
    int _v;
    double _weight;

    Edge(final int u, final int v, final double weight) {
        _u = u;
        _v = v;
        _weight = weight;
    }

    @Override
    public String toString() {
        return (new StringBuilder("n").append(Integer.toString(_u)).append(" -- n").append(Integer.toString(_v)).append(";")).toString();
    }
}
