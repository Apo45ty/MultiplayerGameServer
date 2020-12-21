package games.apolion.udp;

public enum ServerCommands {
	chat("chat"),
	registerPort("registerPort"),
	movePile("movePile"),
	movePlayer("MovePlayer"),
	moveItem("moveItem"),
	addItemToInventoryv("addItemToInventory"),
	addEffectToPlayer("	addEffectToPlayer"),
	instantiatePile("instantiatePile"),
	instantiatePlayer("instantiatePlayer"),
	inputCommand("inputComands"),
	setPosRot("setPositionAndRotation");
	
	private ServerCommands(String s) {
		this.commandStr= s;
	}
	public String commandStr;
	
}
