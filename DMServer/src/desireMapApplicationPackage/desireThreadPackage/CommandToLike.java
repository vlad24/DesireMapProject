package desireMapApplicationPackage.desireThreadPackage;

import desireMapApplicationPackage.actionQueryObjectPackage.LikePack;


public class CommandToLike extends CommandForDesireThread {
	
	private LikePack likePack;
	//--
	public CommandToLike(DesireThread newReceiver, LikePack newPack){
		receiver = newReceiver;
		likePack = newPack;
	}
	
	@Override
	public void execute() throws Exception {
			System.out.println("***CommandToLike has been executed");
			receiver.likeDesire(likePack);
			System.out.println("***CommandToLike is successfully finished");
	}

	@Override
	public void unexecute() {
		System.out.println("Unexecuting liking...");
		try {
			receiver.likeDesire(new LikePack(likePack.desireID, !likePack.isLiked));
			System.out.println("Unexecuted.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unexecuting failed.");
		}
	}

}
