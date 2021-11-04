import random
import numpy as np
from base import BaseAgent, Action

from utils import *
import routing_analyzer
import ordering_analyzer

class Agent(BaseAgent):

    def do_turn(self) -> Action:
        route_analyzer = routing_analyzer.RoutingAnalyzer(self.grid)
        order_analyzer = ordering_analyzer.OrderingAnalyzer(self.grid)
        
        my_agent = get_my_agent(self.grid)
        current_goal = order_analyzer.heuristic(self)
        print(current_goal)
        
        solution_to_goal = route_analyzer.bfs(my_agent, current_goal)
        print(solution_to_goal)
        #start_tile = Tile(get_my_agent(self.grid)[0], get_my_agent(self.grid)[1])
        #came_from, cost = self.route_analyzer.a_star_search(start_tile, current_goal, self.grid)
        
        
        #print(came_from[0])
        return random.choice(
            [Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT, Action.TELEPORT, Action.NOOP, Action.TRAP])


if __name__ == '__main__':
    data = Agent().play()
    print("FINISH : ", data)
