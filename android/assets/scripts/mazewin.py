from com.mirage.model.scripts import IScript
from com.mirage.model import Model
from com.mirage.controller import Controller
from com.mirage.model.scripts import ScriptUtils

class Script(IScript):
    def run(self, properties):
        player = Model.INSTANCE.getPlayer()
        obj = properties.get("object")
        if player == obj:
            door = Model.INSTANCE.findObject("gate-door")
            if 10 < float(player.properties.get("x")) < 20 and float(player.properties.get("y")) > 10:
                if not door.properties.containsKey("opened"):
                    door.properties.put("opened", "true")
                    door.properties.put("animation", "MAIN_GATE_OPEN")
                    Controller.INSTANCE.getScreen().addObjectDrawer(door)
                    ScriptUtils.INSTANCE.launchScriptAfterDelay("door_open_end.py", 4000L)
                    for i in range(13, 16):
                        Model.INSTANCE.gameLoop.setTileId(i, 14, 1)
                    Model.INSTANCE.gameLoop.setTileId(12, 13, 3)
                    Model.INSTANCE.gameLoop.setTileId(13, 12, 3)
                    Model.INSTANCE.gameLoop.setTileId(13, 13, 3)


