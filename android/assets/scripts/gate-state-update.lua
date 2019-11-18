args = ...

u = args["actions"]

u:findObject("gate-door"):getProperties():put("state", args["state"])
