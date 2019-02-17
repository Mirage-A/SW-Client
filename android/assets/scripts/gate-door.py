from com.mirage.model.scripts import IScript
from com.mirage.model import Model
from com.mirage.controller import Controller
from com.mirage.model.scripts import ScriptUtils
from com.badlogic.gdx.maps import MapProperties

class Script(IScript):
    def run(self, properties):
        player = Model.INSTANCE.getPlayer()
        obj = properties.get("object")
        if player == obj:
            door = Model.INSTANCE.findObject("gate-door")
            if 10 < float(player.properties.get("x")) < 20 and 10 < float(player.properties.get("y")) < 16:
                if door.properties.get("state") == "closed":
                    door.properties.put("state", "opening")
                    door.properties.put("animation", "MAIN_GATE_OPEN")
                    Controller.INSTANCE.getScreen().addObjectDrawer(door)
                    props = MapProperties()
                    props.put("state", "opened")
                    ScriptUtils.INSTANCE.launchScriptAfterDelay("gate-state-update.py", 1000L, props)
            else:
                if door.properties.get("state") == "opened":
                    door.properties.put("state", "closing")
                    door.properties.put("animation", "MAIN_GATE_CLOSE")
                    Controller.INSTANCE.getScreen().addObjectDrawer(door)
                    props = MapProperties()
                    props.put("state", "closed")
                    ScriptUtils.INSTANCE.launchScriptAfterDelay("gate-state-update.py", 1000L, props)


