package desireMapApplicationPackage.desireThreadPackage;



public abstract class CommandForDesireThread {
	DesireThread receiver;
	//--
	public abstract void execute() throws Exception;
}


