local args = ...

local u = args["actions"]

local gateDoorID = u:findBuildingID("main-gate-door")
local gateDoor = u:findBuilding("main-gate-door")

local state = gateDoor:getState()

if state == "closed" then
    gateDoor:setState("opening")
    u:runSceneScriptAfterTimeout("main-gate-finish-opening", { ["gateDoor"] = gateDoor, ["gateDoorID"] = gateDoorID }, 1000)
elseif state == "opened" then
    gateDoor:setX(gateDoor:getX() + 2)
    gateDoor:setY(gateDoor:getY() + 2)
    gateDoor:setWidth(4)
    gateDoor:setHeight(0)
    u:markBuildingAsTeleported(gateDoorID)
    gateDoor:setState("closing")
    u:runSceneScriptAfterTimeout("main-gate-finish-closing", { ["gateDoor"] = gateDoor }, 1000)
end