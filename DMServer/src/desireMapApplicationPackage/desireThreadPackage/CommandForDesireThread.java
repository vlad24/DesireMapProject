package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.TilesPack;



public abstract class CommandForDesireThread {
	DesireThread receiver;
	//--
	public abstract void execute() throws Exception;
}


