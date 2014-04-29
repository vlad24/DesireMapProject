package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.desireContentPackage.DesireContent;

public abstract class CategoryManager {
	protected DesireInstrument owner;
	protected abstract void tryToAddDesire(String thisDesireID, AddPack content) throws SQLException;
	protected abstract boolean myContent(DesireContent content);
}
