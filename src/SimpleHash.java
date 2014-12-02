/**
 * Created by Soluna on 02/12/2014.
 */

public class SimpleHash implements Comparable<SimpleHash> {
    int v;
    double key;

    SimpleHash(int pv, double pkey) {
        v = pv;
        key = pkey;
    }

    @Override
    public int compareTo(SimpleHash o) {
        if (key == o.key) {
            return 0;
        }
        else {
            return key > o.key ? 1 : -1;
        }
    }
}
