package desireMapApplicationPackage.inputArchitecturePackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.messageSystemPackage.Message;

public class MessageSendPack extends ActionQueryObject{
	
	public final Message message;
	
	public MessageSendPack(Message newMessage) {
		super(CodesMaster.ActionCodes.MessageSendCode);
		message = newMessage;
	}
}
