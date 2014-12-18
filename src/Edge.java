/**
 * Created by Arturo Isai Castro Perpuli on 02/12/2014.
 */

class Edge implements Comparable<Edge> {
    int _u;
    int _v;
    double _weight;
    boolean _original;

    public Edge(final int u, final int v) {
        _u = u;
        _v = v;
        _weight = StdRandom.uniform() * 1000;
        _original = true;
    }

    public Edge(final int u, final int v, final boolean original) {
        _u = u;
        _v = v;
        _weight = StdRandom.uniform() * 1000;
        _original = original;
    }

    Edge(final int u, final int v, final double weight) {
        _u = u;
        _v = v;
        _weight = weight;
        _original = true;
    }

    Edge(final int u, final int v, final double weight, final boolean original) {
        _u = u;
        _v = v;
        _weight = weight;
        _original = original;
    }



    @Override
    public String toString() {
        return (new StringBuilder("n").append(Integer.toString(_u)).append(" -- n").append(Integer.toString(_v)).append(";")).toString();
    }

    @Override
    public int compareTo(Edge that) {
        if (_weight < that._weight) {
            return -1;
        }

        if (_weight > that._weight) {
            return +1;
        }

        return  0;
    }
}
