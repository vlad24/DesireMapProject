package desireMapApplicationPackage.desireThreadPackage;


public abstract class CommandForDesireThread {
	protected DesireThread receiver;
	//--
	public abstract void execute() throws Exception;
	public abstract void unexecute();
}


