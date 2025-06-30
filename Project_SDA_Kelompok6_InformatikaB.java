import java.util.*;

public class Project_SDA_Kelompok9_InformatikaB{

    static class PointNode {
        int x, y, index;
        List<PointNode> neighbors = new ArrayList<>();

        PointNode(int x, int y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

        public String toString() {
            return "T" + index + " (" + x + ", " + y + ")";
        }
    }

    static class Edge {
        PointNode p1, p2;
        double weight;

        Edge(PointNode p1, PointNode p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.weight = kalkulasijarak(p1, p2);
        }

        static double kalkulasijarak(PointNode a, PointNode b) {
            int dx = a.x - b.x;
            int dy = a.y - b.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public String toString() {
            return "Edge: " + p1 + " <--> " + p2 + " | Distance: " + weight;
        }
    }

    static void selectionSort(List<Edge> edges) {
        int n = edges.size();
        for (int i = 0; i < n - 1; i++) {
            int urutKur = i;
            for (int j = i + 1; j < n; j++) {
                if (edges.get(j).weight < edges.get(urutKur).weight) {
                    urutKur = j;
                }
            }
            if (urutKur != i) {
                Edge temp = edges.get(i);
                edges.set(i, edges.get(urutKur));
                edges.set(urutKur, temp);
            }
        }
    }

    static class UnionFind {
        int[] parent;

        UnionFind(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) parent[rootY] = rootX;
        }
    }

    static void kruskalMST(List<PointNode> points, List<Edge> edges) {
        UnionFind uf = new UnionFind(points.size());
        List<Edge> mst = new ArrayList<>();
        double totalWeight = 0;

        for (Edge e : edges) {
            int u = e.p1.index;
            int v = e.p2.index;
            if (uf.find(u) != uf.find(v)) {
                uf.union(u, v);
                mst.add(e);
                totalWeight += e.weight;
            }
        }
        System.out.println("\n--- Minimum Spanning Tree (Kruskal) ---");
        for (Edge e : mst) {
            System.out.println(e);
        }
        System.out.printf("Total Panjang MST: %.2f\n", totalWeight);
    }

    static void bruteForceTSP(List<PointNode> points) {
        List<List<PointNode>> allPermutations = new ArrayList<>();
        permute(points, 0, allPermutations);

        double minDistance = Double.MAX_VALUE;
        List<PointNode> bestPath = null;

        for (List<PointNode> perm : allPermutations) {
            double dist = 0;
            for (int i = 0; i < perm.size() - 1; i++) {
                dist += Edge.kalkulasijarak(perm.get(i), perm.get(i + 1));
            }
            dist += Edge.kalkulasijarak(perm.get(perm.size() - 1), perm.get(0));

            if (dist < minDistance) {
                minDistance = dist;
                bestPath = perm;
            }
        }
        System.out.println("\n--- TSP Brute Force (NP Problem) ---");
        for (PointNode p : bestPath) {
            System.out.print("T" + p.index + " -> ");
        }
        System.out.println("T" + bestPath.get(0).index);
        System.out.printf("Total Jarak: %.2f\n", minDistance);
    }

    static void permute(List<PointNode> points, int start, List<List<PointNode>> result) {
        if (start == points.size() - 1) {
            result.add(new ArrayList<>(points));
            return;
        }
        for (int i = start; i < points.size(); i++) {
            Collections.swap(points, start, i);
            permute(points, start + 1, result);
            Collections.swap(points, start, i);
        }
    }

    public static boolean bfs(List<PointNode> points, int start, int goal) {
        Queue<PointNode> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(points.get(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            PointNode current = queue.poll();

            if (current.index == goal) {
                return true;
            }

            for (PointNode neighbor : current.neighbors) {
                if (!visited.contains(neighbor.index)) {
                    visited.add(neighbor.index);
                    queue.offer(neighbor);
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<PointNode> points = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        int n;
        do {
            System.out.print("Masukkan jumlah titik (maks 6): ");
            n = scanner.nextInt();
            if (n > 6 || n < 1) {
                System.out.println("Jumlah titik harus antara 1 hingga 6.");
            }
        } while (n > 6 || n < 1);

        for (int i = 0; i < n; i++) {
            System.out.print("Titik ke-" + i + " (x y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points.add(new PointNode(x, y, i));
        }
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Edge edge = new Edge(points.get(i), points.get(j));
                edges.add(edge);

                points.get(i).neighbors.add(points.get(j));
                points.get(j).neighbors.add(points.get(i));
            }
        }

        System.out.println("\n--- Daftar Titik ---");
        for (PointNode p : points) {
            System.out.println(p);
        }

        System.out.println("\n--- Daftar Edge ---");
        for (Edge e : edges) {
            System.out.println(e);
        }

        selectionSort(edges);

        kruskalMST(points, edges);

        bruteForceTSP(points);

        System.out.print("\nMasukkan titik awal (index): ");
        int start = scanner.nextInt();
        System.out.print("Masukkan titik tujuan (index): ");
        int goal = scanner.nextInt();

        boolean found = bfs(points, start, goal);
        if (found) {
            System.out.println("Terdapat jalur dari T" + start + " ke T" + goal);
        } else {
            System.out.println("Tidak terdapat jalur dari T" + start + " ke T" + goal);
        }

        System.out.println("\n=== HASIL SELESAI ===");

        scanner.close();
    }
}
