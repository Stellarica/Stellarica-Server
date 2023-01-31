import argparse
import os
from os import path

parser = argparse.ArgumentParser()
parser.add_argument("id", help="Block/item ID")
parser.add_argument("--namespace", action="store", default="stellarica", help="Mod namespace")
parser.add_argument("--no-item", action="store_true", help="Don't create an item for this block.")
args = parser.parse_args()

assets_dir = f"{os.getcwd()}/src/main/resources/assets/{args.namespace}"
if not path.exists(assets_dir):  ## a lame check to see if we're in the right place
    print(f"Assets folder {assets_dir} not found!")
    exit()

blockstates_dir = path.join(assets_dir, "blockstates")
models_dir = path.join(assets_dir, "models")
block_models_dir = path.join(models_dir, "block")
item_models_dir = path.join(models_dir, "item")

if not args.no_item:
    with open(path.join(item_models_dir, args.id + ".json"), "x") as f:
        f.write(f'{{\n\t"parent": "{args.namespace}:block/{args.id}"\n}}')

with open(path.join(block_models_dir, args.id + ".json"), "x") as f:
    f.write(
        f'{{\n\t"parent": "block/cube_all",\n\t"textures": {{\n\t\t"all": "{args.namespace}:block/{args.id}"\n\t}}\n}}'
    )
with open(path.join(blockstates_dir, args.id + ".json"), "x") as f:
    f.write(
        f'{{\n\t"variants": {{\n\t\t"": {{\n\t\t\t"model": "{args.namespace}:block/{args.id}"\n\t\t}}\n\t}}\n}}'
    )
print("Done!")