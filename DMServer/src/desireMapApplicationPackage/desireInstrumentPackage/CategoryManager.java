package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;
import desireMapApplicationPackage.desireContentPackage.DesireContent;

public abstract class CategoryManager {
	protected abstract void tryToAddDesire(int desId, AddPack content) throws SQLException;
	protected abstract boolean myContent(DesireContent content);
}
