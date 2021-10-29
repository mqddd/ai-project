import enum

class Tile:
    class TileType(enum.Enum):
        WALL = 'W'
        EMPTY = 'E'
        TELEPORT = 'T'
        GEM1 = '1'
        GEM2 = '2'
        GEM3 = '3'
        GEM4 = '4'

    def __init__(self, x, y, tile_type=TileType.EMPTY):
        self.x = x
        self.y = y
        self.tile_type = tile_type
        self.teleports = []

    @property
    def address(self):
        return self.y, self.x

    def __repr__(self):
        return self.tile_type.value

    def is_teleport(self):
        return self.tile_type == self.TileType.TELEPORT

    def get_gem(self):
        if self.tile_type in [self.TileType.GEM1, self.TileType.GEM2, self.TileType.GEM3, self.TileType.GEM4]:
            return self.tile_type
        else:
            return None

    def is_wall(self):
        return self.tile_type == self.TileType.WALL

    def is_empty(self):
        return self.tile_type == self.TileType.EMPTY


class PriorityQueue(object):
    def __init__(self):
        self.queue = []
  
    def __str__(self):
        return ' '.join([str(i) for i in self.queue])
  
    def isEmpty(self):
        return len(self.queue) == 0
  
    def insert(self, data):
        self.queue.append(data)
  
    def delete(self):
        try:
            max = 0
            for i in range(len(self.queue)):
                if self.queue[i] > self.queue[max]:
                    max = i
            item = self.queue[max]
            del self.queue[max]
            return item
        except IndexError:
            print()
            exit()


def get_all_env_elements_indices(grid, grid_height, grid_width): 
    dict = {
        '1': [],
        '2': [],
        '3': [],
        '4': [],
        'T': [],
        'W': [],
        'A': []
    }
    for i in range(grid_height):
        for j in range(grid_width):
            if ('1' in grid[i][j]):
                dict['1'].append([i, j])
            if ('2' in grid[i][j]):
                dict['2'].append([i, j])
            if ('3' in grid[i][j]):
                dict['3'].append([i, j])
            if ('4' in grid[i][j]):
                dict['4'].append([i, j])
            if ('W' in grid[i][j]):
                dict['W'].append([i, j])
            if ('T' in grid[i][j]):
                dict['T'].append([i, j])
            if ('A' in grid[i][j]):
                dict['A'].append([i, j])
    return dict

