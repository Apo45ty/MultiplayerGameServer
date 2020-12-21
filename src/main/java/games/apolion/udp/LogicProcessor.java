package games.apolion.udp;

public class LogicProcessor implements Runnable {
	
	private StateEnumStateProcessor logicPerState=new StateEnumStateProcessor();
	private UDPGameServer owner;
	boolean done = false;
	public LogicProcessor(UDPGameServer owner) {
		super();
		this.owner = owner;
	}
	@Override
	public void run() {
		while(!done) {
			try {
				GameStateLogic logic = logicPerState.getLogicForState(owner.getState());
				if(logic !=null) {				
					logic.tick(owner);;
				}
			} catch(Exception e) {}
		}
	}
	
	void addLogicForState(GameServerStates state,GameStateLogic stateLogic) {
		logicPerState.addLogicForState(state,stateLogic);
	}
	

}
