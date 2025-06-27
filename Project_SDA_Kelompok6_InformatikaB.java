import java.util.*;

public class Project_SDA_Kelompok6_InformatikaB{

    static class PointNode {
        int x, y, index;

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

        scanner.close();
    }
}
