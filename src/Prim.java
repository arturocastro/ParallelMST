import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Created by Soluna on 01/12/2014.
 */
public class Prim {
    public static IGraph prim(IGraph g) {
        final int n = g.getNumVertices();

        int [] priorities = new int[n];
        Arrays.fill(priorities, -1);

        int [] keys = new int[n];
        Arrays.fill(keys, Integer.MAX_VALUE);

        keys[0] = 0;

        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(n);

        for (int i = 0; i < n; ++i) {
            //pq.add()
        }

        return g;
    }
}
