#Contributing building styles

In this directory is a zip archive containing a world in which all SettlerCraft's vanilla buildings are built per building style. If you wish to contribute a new style, fork and clone this repository to your local machine. Then create a structure for every building while following the instructions below. After you are finished create a pull request which will be reviewed and if everything is alright it will be merged.



## Instructions
To get started creating a building style, follow these steps:
 1. Fork and clone this repository to your machine.
 2. Extract the zip, which is a world save, and copy it to a new minecraft instance's saves folder
 3. Install Forge and SettlerCraft or the SettlerCraft Schematic Creator Tool mod, which can be found here.
 4. Load up Minecraft and open the buildings world. You will see a bunch of buildings delimited by wool blocks and a sign to identify each building.
 5. Build a new building in every woolen encasement for your style (while staying inside the limits, as mentioned below under constraints)
 6. When a building is finished, use the schematic creator tool and consecutively right click both red wool blocks to save the building to a json schematic. The directory where those json files are saved can be changed in the configs.
 7. Rename the json file to the relevant file name, put it in the right directory (see contraints), define the home position (also see contraints) and define the fuzzy blocks (again, see constraints).
 8. Zip up the save directory of the minecraft world with your new buildings and put it in this folder.
 9. When you have created a json for every building, push your changes to your branch of this repo and open a pull request, the buildings will then be reviewed and if everything is in order and the builds are of sufficient quality, your building style will be merged.
 

## Constraints
When building structures, there are a couple of constraints you should keep in mind:
 - Building size: the size of each building should be the same for every style. Stay within the wool boundaries (you'll see what is meant when you load the world).
 - Chests and beds: invantories and beds are an important part of SettlerCraft's mechanics, make sure your building has the same amount of beds and inventories as the original default building.
 - Quarry: the quarry is a special building. The quarry hole has a specific size and position, this position MUST be the same regardless of building style. The ladders in the hole should also be placed identically.
 - Home location: the first three coordinates in the jsons define the home location relative to the building's origin (the coordinate with lowest x-, y- and z-values). When pathing towards a building, this is used as final waypoint, keep its location reasonable.
 - Fuzzy blocks: Some blocks (doors, levers, redstone, ...) can have multiple states, if you do not define these blocks as fuzzy, this will cause major overhead as builders will try to remove and replace those blocks. To make a block fuzzy, simply toggle its fuzzy state.
 - File structure: all building jsons are saved per style under src/main/resources/assets/settlercraft/buildings/<stylename>/<buildingtype>/<buildingname>, this is required for the files to be loaded from the code. The recommended way is to copy the default style and rename it. Then replace each json file one by one.



## Modded blocks
Modded blocks are supported, however SettlerCraft has to know the mod ids of all blocks used in a building style. If one of the required mods is not loaded, the SettlerCraft code will not load that building style and the style will not be available ingame.
When creating the pull request, also submit a list of all used mods, their mod id and download link. I do not mind two or three mod depencencies, but do note that a building stye with an excessive amount of required mods or very exotic mods will only be merged when the builds are of extroardinary quality.



## Rewards
Besides my eternal gratitude, there are currently no rewards for building style contributors. For now, your name will be mentioned in the credits. However I do wish to give something back and in the future I will create a small, rendering effect to your player (only if you wish this), similar to what I did in my [Companions](http://minecraft.curseforge.com/projects/companions) mod. Do note that I will only implement this when the main mod and all its features are implemented.
 What this little effect will be is not decided yet, but it will not be too eccentric. This effect will be visible to you and any player on the server if SettlerCraft is loaded.
 
