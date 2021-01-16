package games.apolion.udp.server.messages;

public enum ServerCommands {
	chat("chat"),
	registerPort("registerPort"),
	portHasBeenRegister("portHasBeenRegistered"),
	movePile("movePile"),
	movePlayer("MovePlayer"),
	moveItem("moveItem"),
	addItemToInventoryv("addItemToInventory"),
	addEffectToPlayer("	addEffectToPlayer"),
	instantiateNewPlayer("instantiateNewPlayer"),
	inputCommand("inputCommands"),
	setPosRot("setPositionAndRotation");
	
	private ServerCommands(String s) {
		this.commandStr= s;
	}
	public String commandStr;
	
}
