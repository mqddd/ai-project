from utils import PriorityQueue

class RoutingAnalyzer:
    def __init__(self, grid) -> None:
        self.grid = grid

    def heuristic(self, start, end):
        return abs(start[0] - end[0]) + abs(start[1] - end[1])

    def a_star_search(self, start, end):
        frontier = PriorityQueue() 
        frontier.insert(self.grid[start[0][start[1]]])
