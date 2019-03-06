args = ...

utils = args["utils"]
model = utils:getModel()
model:findObject("gate-door"):getProperties():put("state", args["state"])