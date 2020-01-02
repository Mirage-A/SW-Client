local args = ...

local u = args["actions"]
local door = args["gateDoor"]
local doorID = args["gateDoorID"]

door:setX(door:getX() - 2)
door:setY(door:getY() - 2)
door:setWidth(0)
door:setHeight(4)
u:markBuildingAsTeleported(doorID)
door:setState("opened")