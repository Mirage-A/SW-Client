args = ...

-- print( args["newPos"]:toString() )

utils = args["utils"]
model = utils:getModel()
controller = utils:getController()
player = model:getPlayer()
obj = args["object"]
if player == obj then
    local door = model:findObject("gate-door")
    if ((player:getProperties():get("x") > 10) and (player:getProperties():get("x") < 20)
        and (player:getProperties():get("y") > 10) and (player:getProperties():get("y") < 16)) then
        local doorprops = door:getProperties()
    print(doorprops:get("state"))
        if doorprops:get("state") == "closed" then
            doorprops:put("state", "opening")
            doorprops:put("animation", "MAIN_GATE_OPEN")
            controller:getScreen():addObjectDrawer(door)
            local props = {}
            props["state"] = "opened"
            props["utils"] = utils
        print(props["state"])
            utils:launchScriptAfterDelay("gate-state-update", 1000, props)
        elseif doorprops:get("state") == "opened" then
            doorprops:put("state", "closing")
            doorprops:put("animation", "MAIN_GATE_CLOSE")
            controller:getScreen():addObjectDrawer(door)
            local props = {}
            props["state"] = "closed"
            props["utils"] = utils
            utils:launchScriptAfterDelay("gate-state-update", 1000, props)
        end
    end
end