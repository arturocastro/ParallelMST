import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by Soluna on 01/12/2014.
 */
public class Prim {
    public static IGraph prim(IGraph g) {
        final int n = g.getNumVertices();

        int [] priorities = new int[n];
        Arrays.fill(priorities, -1);

        double [] keys = new double[n];
        Arrays.fill(keys, Integer.MAX_VALUE);

        keys[0] = 0.0;

        PriorityQueue<SimpleHash> pq = new PriorityQueue<SimpleHash>(n);

        boolean [] inQueue = new boolean[n];
        Arrays.fill(inQueue, true);

        for (int i = 0; i < n; ++i) {
            pq.add(new SimpleHash(i, keys[i]));
        }

        while (!pq.isEmpty()) {
            int u = pq.poll().v;



            for (Edge e : g) {
                for (int i = 0; i < keys.length; ++i) {
                    if (e._v == keys[i]) {

                    }
                }
            }
        }

        return g;
    }
}
