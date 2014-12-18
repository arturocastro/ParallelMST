import javax.net.ssl.SSLContext;

public class MainParKruskal {

    public static void main(String[] args) throws InterruptedException {
        String file = null;

        // File path and name
        if (args.length > 0) {
            file = args[0];

            //System.out.println(file);

            if (file.equals("nofile")) {
                file = null;
            }
        }

        // List or matrix
        if (args.length > 1) {
            MyGlobal.Config.op = Integer.parseInt(args[1]);
        }

        // Number of threads
        if (args.length > 2) {
            MyGlobal.Config.p = Integer.parseInt(args[2]);
        }

        // Nb
        if (args.length > 3) {
            MyGlobal.Config.nb = Integer.parseInt(args[3]);
        }

        // Verbose
        if (args.length > 4) {
            MyGlobal.Config.verbose = Integer.parseInt(args[4]);
        }

        // Debug
        if (args.length > 5) {
            MyGlobal.Config.debug = Integer.parseInt(args[5]);
        }

        MyGlobal.verbosePrint("file:"+args[0]);
        MyGlobal.verbosePrint("op:"+args[1]);
        MyGlobal.verbosePrint("p:"+args[2]);
        MyGlobal.verbosePrint("nb:"+args[3]);
        MyGlobal.verbosePrint("verb:"+args[4]);
        MyGlobal.verbosePrint("db:"+args[5]);

        //System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());

        IGraph g = null;

        if (file == null) {
            g = GraphGenerator.simple(1000, 2000);
        } else {
            g = MyGlobal.createGraph(file);
        }

        if (MyGlobal.Config.verbose == 2) {
            System.out.println(g.toString());
        }

        final long startTime = System.nanoTime();

        ParallelKruskal2.parallelKruskal(g);

        final long endTime = System.nanoTime();

        final double duration = (endTime - startTime) / 1000000.0;

        System.out.println(duration);

        if (MyGlobal.Config.verbose == 1) {
            System.out.println("END");
        }
    }
}
