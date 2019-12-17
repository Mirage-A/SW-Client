local args = ...

local u = args["actions"]

local phase = args["phase"]

local playerID = args["playerID"]

print("PlayerID = ", playerID)
print("Phase = ", phase)
print("Map = ", u:getGameMapName())
print("Global quest phase has been updated!")