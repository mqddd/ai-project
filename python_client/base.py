import abc
import enum
import json
import socket
import numpy as np


def read_utf(connection: socket.socket):
    # length = struct.unpack('>H', connection.recv(2))[0]
    return connection.recv(2048).decode('utf-8').strip()


def write_utf(connection: socket.socket, msg: str):
    # connection.send(struct.pack('>H', len(msg)))
    connection.send(msg.encode('utf-8'))


def get_config(config_path):
    with open(config_path) as config_file:
        config = json.load(config_file)

    return config


class Action(enum.Enum):
    UP = 'UP'
    DOWN = 'DOWN'
    LEFT = 'LEFT'
    RIGHT = 'RIGHT'
    NOOP = 'NOOP'
    TELEPORT = 'TELEPORT'
    TRAP = 'TRAP'


class BaseAgent(metaclass=abc.ABCMeta):

    def __init__(self):
        config_path = "client_config.json"
        self.config = get_config(config_path=config_path)
        self.connection = self.connect()
        data = read_utf(self.connection)
        height, width, character, agent_id, agent_score, max_turn_count, agent_count, trap_count = data.strip().split(
            " ")
        self.grid_height = int(height)
        self.grid_width = int(width)
        self.grid = None
        self.character = character
        self.id = int(agent_id)
        self.score = int(agent_score)
        self.max_turn_count = int(max_turn_count)
        self.agent_count = int(agent_count)
        self.trap_count = int(trap_count)
        self.turn_count = 0
        self.agent_scores = []
        write_utf(self.connection, msg="CONFIRM")

    def connect(self):
        self.connection = socket.socket()
        self.connection.connect((self.config["server_ip"], self.config["server_port"]))
        return self.connection

    def _read_turn_data(self, data: str):
        info = data.strip().split(" ")
        info = info[-(2 + self.agent_count + (self.grid_width * self.grid_height)):]

        self.turn_count = int(info[0])
        self.trap_count = int(info[1])
        self.agent_scores = [int(score) for score in info[2:2 + self.agent_count]]
        self.grid = np.array(info[2 + self.agent_count:]).reshape(self.grid_height, self.grid_width).tolist()

    def play(self):
        if self.connection is None:
            self.connect()
        while True:
            data = read_utf(self.connection)
            if "finish!" in data:
                return data

            self._read_turn_data(data=data)
            action = self.do_turn()
            write_utf(self.connection, str(action.value))

    @abc.abstractmethod
    def do_turn(self) -> Action:
        pass