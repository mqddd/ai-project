from utils import *
from queue import PriorityQueue

class RoutingAnalyzer:

    def __init__(self, grid, grid_weight, grid_height) -> None:
        self.grid = grid
        self.grid_weight = grid_weight
        self.grid_height = grid_height
        self.grid_as_graph = {}
        self.create_grid_as_graph()

    def create_grid_as_graph(self):
        for i in range(len(self.grid)):
            for j in range(len(self.grid[0])):
                if self.grid[i][j] != 'W':
                    self.grid_as_graph[(self.grid_weight * i) + j] = self.get_neighbors([i, j])
    
    def heuristic(self, start, end):
        return abs(start.x - end.x) + abs(start.y - end.y)

    def a_star_search(self, start, end, grid):
        frontier = PriorityQueue()
        frontier.put((0, start))
        came_from = {}
        cost_so_far = {}
        came_from[(start.x, start.y)] = None
        cost_so_far[(start.x, start.y)] = 0
        print(end.x , " e ", end.y)
        while not frontier.empty():
            current = frontier.get()

            if current[1].x == end.x and current[1].y == end.y:
                break
            
            for next in self.get_neighbors(current[1], grid):
                new_cost = cost_so_far[(current[1].x, current[1].y)] + 1
                if next not in cost_so_far or new_cost < cost_so_far[(next.x, next.y)]:
                    cost_so_far[(next.x, next.y)] = new_cost
                    priority = new_cost + self.heuristic(next, end)
                    frontier.put((priority, next))
                    came_from[(next.x, next.y)] = current
        
        return came_from, cost_so_far

    # breadth-first search
    def bfs_search(self, start, end):
        frontier = []
        explored_set = set()
        parent = {} 

        # initialize frontier with root 
        frontier.append([tuple(start)])

        while frontier:
            # select a node from frontier which was added at last
            path = frontier.pop(0)

            current = path[-1]
            # goal check
            if current[0] == end[0] and current[1] == end[1]:
                # get solution
                return path
            
            
            if current not in explored_set:
                for successor in self.get_neighbors(current):
                    new_path = list(path)
                    new_path.append(successor)
                    frontier.append(new_path)
                    #parent[successor] = tuple(current)
                    #frontier.append(successor)

                # add current node to explored set
                explored_set.add(tuple(current))

    def bfs(self, start, goal):
        explored = []
     
        # Queue for traversing the
        # graph in the BFS
        queue = [[start]]
        
        # If the desired node is
        # reached
        if start[0] == goal[0] and start[1] == goal[1]:
            print("Same Node")
            return
        
        # Loop to traverse the graph
        # with the help of the queue
        while queue:
            path = queue.pop(0)
            node = path[-1]
            
            # Condition to check if the
            # current node is not visited
            if node not in explored:
                neighbours = self.get_neighbors(node)
                
                # Loop to iterate over the
                # neighbours of the node
                for neighbour in neighbours:
                    new_path = list(path)
                    new_path.append(neighbour)
                    queue.append(new_path)
                    
                    # Condition to check if the
                    # neighbour node is the goal
                    if neighbour[0] == goal[0] and neighbour[1] == goal[1]:
                        print("Shortest path = ", *new_path)
                        return
                explored.append(node)

    # this function returns the path from start node to end node based on parents
    def get_solution(self, parent, start, end):
        path = [tuple(end)]
        while path[-1][0] != start[0] or path[-1][1] != start[1]:
            path.append(parent[path[-1]])
        path.reverse()
        return path
                
    # returns a list of tuples which are neighbors of a node
    def get_neighbors(self, node) -> list:
        neighbors = []
        left_node = [node[0] - 1, node[1]]
        right_node = [node[0] + 1, node[1]]
        up_node = [node[0], node[1] - 1]
        down_node = [node[0], node[1] + 1]
        
        # check if left node is not wall and is in grid index
        if left_node[0] >= 0 and self.grid[left_node[0]][left_node[1]] != 'W':
            neighbors.append(left_node)
        
        # check if right node is not wall and is in grid index
        if right_node[0] < len(self.grid[0]) - 1 and self.grid[right_node[0]][right_node[1]] != 'W':
            neighbors.append(right_node)
        
        # check if up node is not wall and is in grid index
        if up_node[1] >= 0 and self.grid[up_node[0]][up_node[1]] != 'W':
            neighbors.append(up_node)
        
        # check if down node is not wall and is in grid index
        if down_node[1] < len(self.grid) - 1 and self.grid[down_node[0]][down_node[1]] != 'W':
            neighbors.append(down_node)

        return neighbors