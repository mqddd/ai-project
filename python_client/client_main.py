import random
from base import BaseAgent, Action


class Agent(BaseAgent):

    def do_turn(self) -> Action:
        print(self.grid)
        return random.choice(
            [Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT, Action.TELEPORT, Action.NOOP, Action.TRAP])

    def get_yellow_gems_index(self):
        index = []
        for i in range(self.grid_height):
            for j in range(self.grid_width):
                if self.grid[i][j]=='1':
                    index.append([i, j])
        return index

    def get_green_gems_index(self):
        index = []
        for i in range(self.grid_height):
            for j in range(self.grid_width):
                if self.grid[i][j]=='2':
                    index.append([i, j])
        return index

    def get_red_gems_index(self):
        index = []
        for i in range(self.grid_height):
            for j in range(self.grid_width):
                if self.grid[i][j]=='3':
                    index.append([i, j])
        return index

    def get_blue_gems_index(self):
        index = []
        for i in range(self.grid_height):
            for j in range(self.grid_width):
                if self.grid[i][j]=='4':
                    index.append([i, j])
        return index

    def get_teleports_index(self):
        index = []
        for i in range(self.grid_height):
            for j in range(self.grid_width):
                if self.grid[i][j]=='T':
                    index.append([i, j])
        return index



if __name__ == '__main__':
    data = Agent().play()
    print("FINISH : ", data)
