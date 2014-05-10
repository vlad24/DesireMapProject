package desireMapApplicationPackage.desireInstrumentPackage;

import java.sql.SQLException;

import desireMapApplicationPackage.actionQueryObjectPackage.AddPack;

public abstract class CategoryManager {
	protected DesireInstrument owner;
	protected abstract void addDesire(String thisDesireID, AddPack content) throws SQLException;
}
