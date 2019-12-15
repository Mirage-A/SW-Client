args = ...

u = args["actions"]

local objProps = args["object"]:getProperties()
if objProps:get("type") == "entity" then
        local x = objProps:get("x")
        local y = objProps:get("y")
        local door = u:findObject("gate-door")
        local doorprops = door:getProperties()
        if ((x > 10) and (x < 20) and (y > 10) and (y < 16)) then
            if doorprops:get("state") == "closed" then
                doorprops:put("state", "opening")
                doorprops:put("animation", "MAIN_GATE_OPEN")
                u:updateObjectDrawer(door)
                local props = {["state"] = "opened"}
                u:runScriptAfterDelay("gate-state-update", props, 1000)
            end
        elseif doorprops:get("state") == "opened" then
            doorprops:put("state", "closing")
            doorprops:put("animation", "MAIN_GATE_CLOSE")
            u:updateObjectDrawer(door)
            local props = {["state"] = "closed"}
            u:runScriptAfterDelay("gate-state-update", props, 1000)
        end
end