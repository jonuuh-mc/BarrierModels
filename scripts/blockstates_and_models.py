import os
import json
from itertools import chain, combinations

MODEL_NAMES = []
BARRIER_MODELS = {}


def create_power_set(iterable):
    s = list(iterable)
    return chain.from_iterable(combinations(s, r) for r in range(len(s) + 1))


def direction_set_to_block_state_property_str(direction_set):
    block_state = {
        "down": False,
        "east": False,
        "north": False,
        "south": False,
        "up": False,
        "west": False,
    }

    for direction in direction_set:
        match direction:
            case "D":
                block_state["down"] = True
            case "E":
                block_state["east"] = True
            case "N":
                block_state["north"] = True
            case "S":
                block_state["south"] = True
            case "U":
                block_state["up"] = True
            case "W":
                block_state["west"] = True

    return str(block_state).lower().replace("'", "").replace(":", "=").replace(" ", "").strip("{}")


def write_barrier_block_states():
    power_set = list(create_power_set("DENSUW"))

    print(power_set)
    print(len(power_set))

    block_state_variants = {}

    for connection_direction_set in power_set:
        num_connections = len(connection_direction_set)

        model_name = "barrier" if num_connections == 0 else "barrier-" + "".join(connection_direction_set)
        MODEL_NAMES.append(model_name)

        property_str = direction_set_to_block_state_property_str(connection_direction_set)
        block_state_variants[property_str] = {"model": f"barrier/barrier{num_connections}/{model_name}"}

    block_states = {"variants": block_state_variants}

    # ummmmm don't read this
    with open("minecraft/blockstates/barrier.json", "w", encoding="utf-8") as f:
        json_str = json.dumps(block_states)

        json_str = json_str.replace("},", "},\n   ")

        i = json_str.index("{") + 1
        json_str = json_str[:i] + "\n  " + json_str[i:]

        j = json_str.index("{", i) + 1
        json_str = json_str[:j] + "\n    " + json_str[j:]

        # # # # #
        i = json_str.rindex("}")
        json_str = json_str[:i] + "\n" + json_str[i:]

        j = json_str.index("}", i - 1)
        json_str = json_str[:j] + "\n  " + json_str[j:]

        # print(json_str)
        f.write(json_str)


def create_texture_face(block_connections, texture_face):
    # a block connection exists on the current texture face (obscures texture, default to ensw: 4 connections)
    if texture_face in block_connections:
        return "blocks/barrier/barrier-ensw"

    # per texture face, map 3d adjacent connected blocks to directions on a 2d texture face (to know which sides of the texture face should be removed)
    block_connection_texture_connection_maps = {
        "up": {
            "north": "n",
            "east": "e",
            "south": "s",
            "west": "w",
        },
        "down": {
            "south": "n",
            "east": "e",
            "north": "s",
            "west": "w",
        },
        "west": {
            "up": "n",
            "south": "e",
            "down": "s",
            "north": "w",
        },
        "east": {
            "up": "n",
            "north": "e",
            "down": "s",
            "south": "w",
        },
        "north": {
            "up": "n",
            "west": "e",
            "down": "s",
            "east": "w",
        },
        "south": {
            "up": "n",
            "east": "e",
            "down": "s",
            "west": "w",
        },
    }

    block_connection_texture_connection_map = block_connection_texture_connection_maps[texture_face]
    connected_texture_faces = ""

    for block_connection in block_connections:
        try:
            connected_texture_faces += block_connection_texture_connection_map[block_connection]
        except KeyError:
            pass

    tex = "blocks/barrier/barrier"

    return tex if not connected_texture_faces else tex + "-" + "".join(sorted(connected_texture_faces))


def remove_duplicate_models(map_in):
    seen = {}

    for key, value in map_in.items():
        if value not in seen.values():
            seen[key] = value

    return seen


def create_barrier_models():
    for model_name in reversed(MODEL_NAMES):
        direction_connections = model_name.replace("barrier", "").replace("-", "")

        direction_property_map = {"D": "down", "E": "east", "N": "north", "S": "south", "U": "up", "W": "west"}

        block_connections = [direction_property_map[direction] for direction in direction_connections]
        # print(block_connections)

        texture_map = {
            "particle": "blocks/barrier/barrier",
            "down": "",
            "east": "",
            "north": "",
            "south": "",
            "up": "",
            "west": "",
        }

        for texture_face in texture_map:
            if texture_face == "particle":
                continue
            texture_map[texture_face] = create_texture_face(block_connections, texture_face)

        model_json = {
            "__comment": "",
            "parent": "block/cube",
            "textures": texture_map,
        }

        BARRIER_MODELS[model_name] = model_json


def write_barrier_models():
    create_barrier_models()
    # print(len(BARRIER_MODELS))
    # print(BARRIER_MODELS)

    unique_barrier_models = remove_duplicate_models(BARRIER_MODELS)
    # print(len(unique_barrier_models))
    # print(unique_barrier_models.keys())
    # print(unique_barrier_models)

    last_connections = 7

    for model_name, model_json in unique_barrier_models.items():
        connections = len(model_name.replace("barrier", "").replace("-", ""))

        if connections < last_connections:
            os.mkdir(f"minecraft/models/block/barrier/barrier{connections}")
            last_connections = connections

        # print(model_json)

        with open(f"minecraft/models/block/barrier/barrier{connections}/{model_name}.json", "w", encoding="utf-8") as f:
            json.dump(model_json, f, ensure_ascii=False, indent=4)


def main():
    # os.makedirs("minecraft/blockstates")
    # os.makedirs("minecraft/models/block/barrier")

    # write_barrier_block_states()
    # write_barrier_models()


if __name__ == "__main__":
    main()
