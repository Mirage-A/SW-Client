args = ...

u = args["utils"]

if u:getPlayer() == args["object"] then
    local door = u:findObject("gate-door")
    local doorprops = door:getProperties()
    if ((u:getPlayerX() > 10) and (u:getPlayerX() < 20)
        and (u:getPlayerY() > 10) and (u:getPlayerY() < 16)) then
        if doorprops:get("state") == "closed" then
            doorprops:put("state", "opening")
            doorprops:put("animation", "MAIN_GATE_OPEN")
            u:updateObjectDrawer(door)
            local props = {["state"] = "opened", ["utils"] = u}
            u:runScriptAfterDelay("gate-state-update", 1000, props)
        end
    elseif doorprops:get("state") == "opened" then
        doorprops:put("state", "closing")
        doorprops:put("animation", "MAIN_GATE_CLOSE")
        u:updateObjectDrawer(door)
        local props = {["state"] = "closed", ["utils"] = u}
        u:runScriptAfterDelay("gate-state-update", 1000, props)
    end
end