from com.mirage.model.scripts import IScript
from com.mirage.model import Model

class Script(IScript):
    def run(self, properties):
        Model.INSTANCE.findObject("gate-door").properties.put("state", properties.get("state"))
