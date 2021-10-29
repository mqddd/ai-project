import utils

class OrderingAnalyzer:

    def __init__(self, grid) -> None:
        self.grid = grid

    def heuristic(self, start, end) -> int:
        return abs(start[0] - end[0]) + abs(start[1] - end[1])
        

    def a_star_search(self):
        pass