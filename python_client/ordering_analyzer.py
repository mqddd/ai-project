from utils import *
from client_main import Agent

class OrderingAnalyzer:

    def __init__(self, grid) -> None:
        self.grid = grid

    def heuristic(self, agent: Agent):
        #my_score = agent.agent_scores[0]
        yellow_gems = get_remained_yellow_gems(agent.grid)
        green_gems = get_remained_green_gems(agent.grid)
        red_gems = get_remained_red_gems(agent.grid)
        blue_gems = get_remained_blue_gems(agent.grid)
        my_agent = get_my_agent(agent.grid)
        print(my_agent)
        nearest_gem = self.get_nearest_goal(my_agent, np.concatenate((yellow_gems, green_gems, red_gems, blue_gems)))
        return nearest_gem

    def get_nearest_goal(self, source ,goals):
        min = 50 # maximum grid size is 20*20
        goal = None
        for g in goals:
            distance = abs(g[0] - source[0]) + abs(g[1] - source[1])
            if distance < min:
                min = distance
                goal = g
        return goal
        
    def a_star_search(self):
        pass