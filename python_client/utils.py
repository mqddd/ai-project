import numpy as np

class PriorityQueue():
    
    def __init__(self) -> None:
        self.queue = []
  
    def __str__(self) -> str:
        return ' '.join([str(i) for i in self.queue])
  
    def isEmpty(self) -> bool:
        return len(self.queue) == 0
  
    def insert(self, data, cost) -> None:
        self.queue.append(tuple((data, cost)))
  
    def remove(self) -> tuple:
        try:
            max = 0
            for i in range(len(self.queue)):
                if self.queue[i][1] < self.queue[max][1]:
                    max = i
            item = self.queue[max]
            del self.queue[max]
            return item
        except IndexError:
            print()
            exit()

def get_remained_yellow_gems(grid):
    arr = np.array(grid)
    yellow_gems = np.argwhere(np.frompyfunc(lambda grid: '1' in grid, 1, 1)(arr))
    return yellow_gems

def get_remained_green_gems(grid):
    arr = np.array(grid)
    green_gems = np.argwhere(np.frompyfunc(lambda grid: '2' in grid, 1, 1)(arr))
    return green_gems

def get_remained_red_gems(grid):
    arr = np.array(grid)
    red_gems = np.argwhere(np.frompyfunc(lambda grid: '3' in grid, 1, 1)(arr))
    return red_gems

def get_remained_blue_gems(grid):
    arr = np.array(grid)
    blue_gems = np.argwhere(np.frompyfunc(lambda grid: '4' in grid, 1, 1)(arr))
    return blue_gems

def get_teleports(grid):
    arr = np.array(grid)
    teleports_gems = np.argwhere(np.frompyfunc(lambda grid: 'T' in grid, 1, 1)(arr))
    return teleports_gems

def get_my_agent(grid):
    arr = np.array(grid)
    my_agent_gems = np.argwhere(np.frompyfunc(lambda grid: 'A' in grid, 1, 1)(arr))[0]
    return my_agent_gems

def get_walls(grid):
    arr = np.array(grid)
    yellow_gems = np.argwhere(np.frompyfunc(lambda grid: 'W' in grid, 1, 1)(arr))
    return yellow_gems