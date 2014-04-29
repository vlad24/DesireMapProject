package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;
import desireMapApplicationPackage.messageSystemPackage.ClientMessage;

public class MessageSendPack extends ActionQueryObject{
	
	public final ClientMessage clientMessage;
	
	public MessageSendPack(ClientMessage newMessage) {
		super(CodesMaster.ActionCodes.MessageSendCode);
		clientMessage = newMessage;
	}
}
