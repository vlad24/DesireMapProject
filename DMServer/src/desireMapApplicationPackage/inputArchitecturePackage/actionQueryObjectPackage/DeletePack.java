package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;


import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class DeletePack extends ActionQueryObject{
	public final String contents;
	public DeletePack(String contentsToDelete) {
		super(CodesMaster.ActionCodes.DeleteCode);
		contents = contentsToDelete;
	}
}
