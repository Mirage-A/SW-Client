local args = ...

local u = args["actions"]
local entityID = args["entityID"]
local entity = u:getEntity(entityID)

u:destroyEntity(args["entityID"])
entity:setHealth(entity:getMaxHealth())
u:runSceneScriptAfterTimeout("create-entity", {["entity"] = entity}, 2000)