import java.util.*;

public class ipv4_fragmentation {

    public static int[] fragmentPayload(int payloadSize, int MTU) {
        int headerSize = 20;
        int dataSize = MTU - headerSize;
        int numFragments = (int) Math.ceil((double) payloadSize / dataSize);
        int[] fragmentSizes = new int[numFragments];
        int remainingPayloadSize = payloadSize;

        for (int i = 0; i < numFragments; i++) {
            if (remainingPayloadSize <= dataSize) {
                fragmentSizes[i] = headerSize + remainingPayloadSize;
            } else {
                fragmentSizes[i] = MTU;
                remainingPayloadSize -= dataSize;
            }
        }
        return fragmentSizes;
    }

    public static int[][] dijkstra(int[][] graph, int source, int dest) {
        int n = graph.length;
        int[] dist = new int[n];
        int[][] path = new int[n][2];
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            path[i][0] = -1;
            path[i][1] = Integer.MAX_VALUE;
        }
        dist[source] = 0;
        path[source][0] = source;
        path[source][1] = 0;
        for (int i = 0; i < n; i++) {
            int u = findMin(dist);
            if (u == dest) {
                break;
            }
            for (int v = 0; v < n; v++) {
                if (graph[u][v] != 0 && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    path[v][0] = u;
                    path[v][1] = graph[u][v];
                }
            }
            dist[u] = Integer.MAX_VALUE;
        }
        int[][] sp = new int[n][2];
        int idx = n - 1;
        sp[idx][0] = dest;
        sp[idx][1] = 0;
        while (idx > 0) {
            int u = sp[idx][0];
            int weight = sp[idx][1];
            idx--;
            int v = path[u][0];
            sp[idx][0] = v;
            sp[idx][1] = weight + path[u][1];
        }
        return sp;
    }

    public static int findMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        int idx = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                idx = i;
            }
        }
        return idx;
    }

    private static void printPath(int[][] path) {
        System.out.print("Shortest path: ");
        int prevNode = -1;
        for (int[] ints : path) {
            int node = ints[0];
            if (node != -1 && node != prevNode) {
                System.out.print(ints[0] + " ");
                prevNode = node;
            }
        }
        System.out.println("\nTotal cost: " + path[0][1]);
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter payload size (in bytes): ");
        int payloadSize = sc.nextInt();

        System.out.print("Enter MTU size (in bytes): ");
        int mtuSize = sc.nextInt();

        int n = 9; // number of nodes

//        System.out.print("Enter number of nodes: ");
//        int n = sc.nextInt();

        int[][] graph = {{0, 0 , 11, 0, 0, 0, 0, 0, 0}};

//        System.out.println("Enter the link weights between nodes (0 if no link):");
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                graph[i][j] = sc.nextInt();
//            }
//        }

        System.out.print("Enter source node: ");
        int source = sc.nextInt();

        System.out.print("Enter destination node: ");
        int dest = sc.nextInt();

        if (source > n || dest > n) {
            System.out.println("No path found from source node to destination node.");
            return;
        }

        int[] fragments = fragmentPayload(payloadSize, mtuSize);

        System.out.println("\nFragmenting payload into " + fragments.length + " fragments...\n");
        for (int i = 0; i < fragments.length; i++) {
            int fragSize = fragments[i];
            System.out.println();
            System.out.println("Sending fragment " + (i + 1) + " of size " + fragSize);
            int[][] path = dijkstra(graph, source, dest);
            printPath(path);
        }
    }
}
