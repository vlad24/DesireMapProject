package desireMapApplicationPackage.handlerTubePackage;
import java.util.ArrayList;

import desireMapApplicationPackage.actionQueryObjectPackage.ActionQueryObject;
import desireMapApplicationPackage.desireThreadPackage.DesireThread;
import desireMapApplicationPackage.desireThreadPackage.QueueOfCommands;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.Handler;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerAdd;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerDelete;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerExit;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerLogIn;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerMessageDeliver;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerMessageSend;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerRegister;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerSatisfy;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerShowInfo;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerShowPersonalDesires;
import desireMapApplicationPackage.handlerTubePackage.handlersPackage.HandlerTiles;

public class HandlerTube {
	protected DesireThread userThread;
	private ArrayList<Handler> tube;
	////
	public HandlerTube(DesireThread inThread){
		System.out.println("*** Initializing desireTube");
		userThread = inThread;
		tube = new ArrayList<Handler>();
		tube.add(new HandlerExit(inThread));
		tube.add(new HandlerDelete(inThread));
		tube.add(new HandlerLogIn(inThread));
		tube.add(new HandlerMessageDeliver(inThread));
		tube.add(new HandlerMessageSend(inThread));
		tube.add(new HandlerRegister(inThread));
		tube.add(new HandlerAdd(inThread));
		tube.add(new HandlerTiles(inThread));
		tube.add(new HandlerSatisfy(inThread));
		tube.add(new HandlerShowPersonalDesires(inThread));
		tube.add(new HandlerShowInfo(inThread));
		System.out.println("*** Initialization completed");
	}
	

	public QueueOfCommands processClientQuery(ActionQueryObject query){
		System.out.println("*** Processing the client query");
		QueueOfCommands accumulatedCommandQueue =  new QueueOfCommands();
		for(Handler handler : tube ){
			handler.tryToHandleClientQuery(query, accumulatedCommandQueue);
		}
		return accumulatedCommandQueue;
	}
}