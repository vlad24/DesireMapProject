package desireMapApplicationPackage.actionQueryObjectPackage;

import desireMapApplicationPackage.codeConstantsPackage.CodesMaster;

public class MessageDeliverPack extends ActionQueryObject{
	public String from;
	public String to;
	public int hoursRadius;
	public MessageDeliverPack(String newFrom, String newTo, int newHoursRadius){
		super(CodesMaster.ActionCodes.MessageDeliverCode);
		from = newFrom;
		to = newTo;
		hoursRadius = newHoursRadius;
	}
}
