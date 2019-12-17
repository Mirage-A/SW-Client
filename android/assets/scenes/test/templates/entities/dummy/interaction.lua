local args = ...

local u = args["actions"]

local gate = u:findBuilding("main-gate")
local gateDoor = u:findBuilding("main-gate-door")

local state = gateDoor:getState()

if state == "closed" then
    gateDoor:setState("opening")
    gate:setRigid(false)
    u:runSceneScriptAfterTimeout("main-gate-finish-opening", {["gateDoor"] = gateDoor}, 1000)
elseif state == "opened" then
    gateDoor:setState("closing")
    gate:setRigid(true)
    u:runSceneScriptAfterTimeout("main-gate-finish-closing", {["gateDoor"] = gateDoor}, 1000)
end