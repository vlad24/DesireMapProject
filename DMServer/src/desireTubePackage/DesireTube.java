package desireTubePackage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import queryObjectPackage.QueryObject;

import desireThreadPackage.CommandForDesireThread;
import desireThreadPackage.CommandToExit;
import desireThreadPackage.DesireThread;
import codesPackage.Codes;

public class DesireTube {

	protected DesireThread userThread;
	private HashMap<Character, Handler> tube;
	//--
	public DesireTube(DesireThread inThread){
		userThread = inThread;
		tube = new HashMap<Character,Handler>();
//		tube.put(Codes.ActionCodes.exitCode, new HandlerExiting(inThread));
//		tube.add(new HandlerLogIn(inThread));
//		tube.add(new HandlerRegister(inThread));
//		tube.add(new HandlerAddingDesire(inThread));
//		tube.add(new HandlerShowerDesires(inThread));
//		tube.add(new HandlerShowerInfo(inThread));
//		tube.add(new HandlerAddSatisfy(inThread));
//		tube.add(new HandlerDeleting(inThread));
	}
	
	private Handler chooseHandler(char actionCode){
		return(tube.get(actionCode));
	}

	public CommandForDesireThread processClientQuery(QueryObject query){
		System.out.println("Processing the client query");
		Handler forthHandler = chooseHandler(query.getActionCode());
		if (forthHandler != null){
			System.out.println(forthHandler.getClass().toString() + " working");
			return (forthHandler.handleString(input));
		}
		else{
			System.out.println("No handler found to process the query. Time to exit");
			return (new CommandToExit(userThread));
		}
	}
}