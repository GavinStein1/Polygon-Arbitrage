import java.util.*;

public class Graph {

    // Class variables
    private int V = 0;  // Obsolete
    private ArrayList<String> vertices;
    private Hashtable<String, Double> edges;
    private ArrayList<String> path;


    public Graph() {
        // Initialise variables
        this.vertices = new ArrayList<>();
        this.path = new ArrayList<>();
        this.edges = new Hashtable<>();
    }

    public void addEdge(String s, String d, Double w) {
        /*
        s -> Starting vertex
        d -> Destination vertex
        w -> Edge weight
        */

        if (!this.vertices.contains(s)) {
            this.vertices.add(s);
            this.V++;
        }
        if (!this.vertices.contains(d)) {
            this.vertices.add(d);
            this.V++;
        }
        this.edges.put(String.format("%s-%s", s, d), w);
    }

    public void updateEdge(String s, String d, Double w) {
        /*
        s -> Starting vertex
        d -> Destination vertex
        w -> Edge weight
        */

        this.edges.replace(String.format("%s-%s", s, d), w);
    }

    public void printSolution() {
        String pathString = "Path: ";
        for (String v : this.path) {
            pathString += v + " ";
        }

        System.out.println(pathString);
        System.out.println(String.format("Profit: %.2f%%", this.calculateProfit()));
    }

    public Double calculateProfit() {
        /* 
		This function calculates the profit of the self.path arbitrage path.
		*/
        Double profit = 1.0;
        int i = 0;
        while (i < this.path.size() - 1) {
            String edge = String.format("%s-%s", this.path.get(i), this.path.get(i+1));
            Double rate = this.edges.get(edge);
            profit = profit * rate;
            i++;
        }
        profit = (profit - 1) * 100;
        return profit;
    }

    public void bellmanFord(String src) {
        /*
        src -> Source vertex
        */

        // Step 1: fill the distance array and predecessor array
        Hashtable<String, Double> dist = new Hashtable<>();
        Hashtable<String, ArrayList<String>> prev = new Hashtable<>();
        for (String v : this.vertices) {
            dist.put(v, Double.NEGATIVE_INFINITY);
            prev.put(v, new ArrayList<String>());
        }

        // Mark the source vertex
        dist.replace(src, 0.0);

        // Step 2: relax edges |V| -1 times
        int loopCtr = 0;
        while (loopCtr < this.V - 1) {
            Enumeration<String> keys = this.edges.keys();
            
            while (keys.hasMoreElements()) {
                String sd = keys.nextElement();
                // System.out.println(sd);
                String s = sd.split("-")[0];
                String d = sd.split("-")[1];
                Double w = this.edges.get(sd);
                // System.out.println(String.format("%s dist: %f", sd, dist.get(s) + Math.log(w)));

                if (dist.get(s) != Double.NEGATIVE_INFINITY && dist.get(s) + Math.log(w) > dist.get(d)) {
                    System.out.println(String.format("%s dist: %f", sd, dist.get(s) + Math.log(w)));
                    if (!prev.get(d).contains(s)) {
                        System.out.println(String.format("Replacing %s with: %f", d, dist.get(s) + Math.log(w)));
                        dist.replace(d, dist.get(s) + Math.log(w));
                        prev.get(d).clear();
                        prev.get(d).addAll(prev.get(s));
                        prev.get(d).add(s);
                    } 
                }
            }
            loopCtr++;
            System.out.println(prev);
            System.out.println(dist);
        }
        this.path = prev.get(src);
        this.path.add(src);

        this.printSolution();
        return;
    }
    
    public static void main(String[] args) {
        
        Graph graph = new Graph();
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("A", "C", 1.0);
        graph.addEdge("B", "C", 1.0);
        graph.addEdge("B", "A", 1.0);
        graph.addEdge("C", "A", 1.0);
        graph.addEdge("C", "B", 1.0);

        graph.addEdge("A", "Z", 0.5);
        graph.addEdge("Z", "A", 2.0);
        graph.addEdge("B", "Z", 0.5);
        graph.addEdge("Z", "B", 2.0);
        graph.addEdge("C", "Z", 1/2.01);
        graph.addEdge("Z", "C", 2.01);

        graph.addEdge("B", "S", 2.0);
        graph.addEdge("S", "T", 1.0);
        graph.addEdge("T", "B", 0.51);
        graph.addEdge("S", "B", 0.5);
        graph.addEdge("T", "S", 1.0);
        graph.addEdge("B", "T", 1.0/0.51);
        graph.addEdge("Q", "C", 1.0);
        graph.addEdge("C", "Q", 1.0);

        graph.bellmanFord("Q");

    }

}