# Graph object class

import math


class Graph:
    """
    A class to represent a graph.

    Attributes
    -------------
    V : int
        number of vertices in the graph.
    vertices : [str]
        list of vertices in the graph.
    edges : {str: double}
        dictionary of edges. the key is the start and destination vertex in the format {start}-{destination}.
        the value of each key is the weight of that edge.
    path : [str]
        the path found by the algorithm.
    ----------
    """

    def __init__(self):
        self.V = 0  # Number of vertices (Obsolete)
        self.vertices = []  # Array of vertices
        self.edges = {}  # Array of edges
        self.path = []

    # Add edges
    def add_edge(self, s, d, w):
        """
        Parameters
        ----------
        s: str
            starting vertex
        d: str
            destination vertex
        w: double
            weight of edge - exchange rate from asset s to asset d
        ----------
        """
        if s not in self.vertices:
            self.vertices.append(s)
            self.V += 1
        if d not in self.vertices:
            self.vertices.append(d)
            self.V += 1
        self.edges["{}-{}".format(s, d)] = float(w)

    def update_edge(self, s, d, w):
        """
        Parameters
        ----------
        s : str
            starting vertex
        d: str
            destination vertex
        w: double
            weight of edge - exchange rate from asset s to asset d
        ----------
        """
        self.edges["{}-{}".format(s, d)] = w

    def print_solution(self):
        """
        Prints the solution to the terminal
        """
        path_string = "Path: "
        for v in self.path:
            path_string += v + " "
        print(path_string)
        print("Profit: {:.2f}%".format(self.calculate_profit()))

    def calculate_profit(self):
        """
        This function calculates the profit of the self.path arbitrage path.
        """
        profit = 1.0
        i = 0
        while i < len(self.path) - 1:
            edge = "{}-{}".format(self.path[i], self.path[i + 1])
            rate = self.edges[edge]
            profit = profit * rate
            i += 1
        profit = (profit - 1) * 100
        return profit

    def bellman_ford(self, src):
        """
        Parameters
        ----------
        src: str
            source vertex
        ----------
        Modified Bellman-Ford algorithm.
        """
        self.path = []

        # Step 1: fill the distance array and predecessor array
        dist = {}
        prev = {}
        for v in self.vertices:
            dist[v] = -float("Inf")
            prev[v] = []

        # Mark the source vertex
        dist[src] = 0

        # Step 2: "relax" edges |V| - 1 times
        for _ in range(self.V - 1):
            for edge in self.edges:
                s, d = edge.split("-")  # assign source and destination vertex
                w = self.edges[edge]  # assign weight
                if dist[s] != -float("Inf") and dist[s] + math.log(w) > dist[d]:
                    if s not in prev[d]:
                        dist[d] = dist[s] + math.log(w)
                        prev[d] = prev[s] + [s]

        # Set class attribute path
        self.path = prev[src] + [src]

        self.print_solution()
        # self.build_path(prev, dist, src)
        return


g = Graph()
g.add_edge("A", "B", 1.0)
g.add_edge("A", "C", 1.0)
g.add_edge("B", "C", 1.0)
g.add_edge("B", "A", 1.0)
g.add_edge("C", "A", 1.0)
g.add_edge("C", "B", 1.0)

g.add_edge("A", "Z", 0.5)
g.add_edge("Z", "A", 2)
g.add_edge("B", "Z", 0.5)
g.add_edge("Z", "B", 2.0)
g.add_edge("C", "Z", 1 / 2.01)
g.add_edge("Z", "C", 2.01)

g.add_edge("B", "S", 2.0)
g.add_edge("S", "T", 1.0)
g.add_edge("T", "B", 0.51)
g.add_edge("S", "B", 0.5)
g.add_edge("T", "S", 1.0)
g.add_edge("B", "T", 1 / 0.51)
g.add_edge("Q", "C", 1.0)
g.add_edge("C", "Q", 1.0)

ins = input("Enter starting vertex: ")
g.bellman_ford(ins)
