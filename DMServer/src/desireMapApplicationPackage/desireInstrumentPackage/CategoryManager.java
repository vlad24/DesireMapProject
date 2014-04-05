package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;

import desireMapApplicationPackage.desireContentPackage.DesireContent;

public abstract class CategoryManager {
	protected abstract void tryToAddDesire(int desId, DesireContent content) throws SQLException;
	protected abstract boolean myContent(DesireContent content);
}
