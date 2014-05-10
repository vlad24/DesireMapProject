package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.SatisfyPack;

public class StatePool {
	private ThreadStateStart stateStart;
	private ThreadStateBasic stateBasic;
	private ThreadStateMapScanning stateMapScanning;
	private ThreadStateMapScanningDesire stateMapScanningDesire;

	public ThreadStateStart getFreshStateStart(DesireThread ownerThread) {
		if (stateStart != null){
			System.out.println("+Returning existing");
			stateStart.owner = ownerThread;
			return stateStart;
		}
		else{
			System.out.println("+Creating state");
			stateStart = new ThreadStateStart(ownerThread);
			return stateStart;
		}
	}
	
	public ThreadStateBasic getFreshStateBasic(DesireThread ownerThread) {
		if (stateBasic != null){
			System.out.println("+Returning existing");
			stateBasic.owner = ownerThread;
			return stateBasic;
		}
		else{
			System.out.println("+Creating state");
			stateBasic = new ThreadStateBasic(ownerThread);
			return stateBasic;
		}
	}
	public ThreadStateMapScanning getFreshStateMapScanning(DesireThread ownerThread, SatisfyPack sPack, Integer categoryCode) {
		if (stateMapScanning != null){
			stateMapScanning.owner = ownerThread;
			stateMapScanning.refreshMinorFields(sPack, categoryCode);
			return stateMapScanning;
		}
		else{
			stateMapScanning = new ThreadStateMapScanning(ownerThread, sPack, categoryCode);
			return stateMapScanning;
		}
	}
	public ThreadStateMapScanningDesire getFreshStateMapScanningDesire(DesireThread ownerThread, SatisfyPack sPack, Integer categoryCode) {
		if (stateMapScanningDesire != null){
			System.out.println("+Returning existing");
			stateMapScanningDesire.owner = ownerThread;
			stateMapScanningDesire.refreshMinorFields(sPack, categoryCode);
			return stateMapScanningDesire;
		}
		else{
			System.out.println("+Creating state");
			stateMapScanningDesire = new ThreadStateMapScanningDesire(ownerThread, sPack, categoryCode);
			return stateMapScanningDesire;
		}
	}
	

}
