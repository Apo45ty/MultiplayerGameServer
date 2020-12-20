package udpServer;

import java.util.LinkedList;
import java.util.List;

public class StateEnumStateProcessor {
	private List<StateAndStateLogic> ss=new LinkedList<StateAndStateLogic>();
	
	GameStateLogic getLogicForState(GameServerStates state) {
		for(StateAndStateLogic s :ss)
			if(state == s.states)
				return s.LogicForState;
		return null;
	}
	void addLogicForState(GameServerStates state,GameStateLogic stateLogic) {
		ss.add(new StateAndStateLogic(stateLogic,state));
	}
	
	public List<StateAndStateLogic> getSs() {
		return ss;
	}
	public void setSs(List<StateAndStateLogic> ss) {
		this.ss = ss;
	}
	class StateAndStateLogic{
		GameStateLogic LogicForState;
		GameServerStates states;
		public StateAndStateLogic(GameStateLogic logicForState, GameServerStates states) {
			super();
			LogicForState = logicForState;
			this.states = states;
		}
		public GameStateLogic getLogicForState() {
			return LogicForState;
		}
		public void setLogicForState(GameStateLogic logicForState) {
			LogicForState = logicForState;
		}
		public GameServerStates getStates() {
			return states;
		}
		public void setStates(GameServerStates states) {
			this.states = states;
		}
	}
}

