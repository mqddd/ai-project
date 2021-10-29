import random
from base import BaseAgent, Action

import utils
import routing_analyzer

class Agent(BaseAgent):

    def do_turn(self) -> Action:
        if self.turn_count == 1:
            self.env_dict = utils.get_all_env_elements_indices(self.grid, self.grid_height, self.grid_width)
        else:
            pass
        routingAnalyzer = routing_analyzer.RoutingAnalyzer(self.grid)
        #came_from, cost = routingAnalyzer.a_star_search(our_agent[0], yellow_gems[0])

        return random.choice(
            [Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT, Action.TELEPORT, Action.NOOP, Action.TRAP])


if __name__ == '__main__':
    data = Agent().play()
    print("FINISH : ", data)
