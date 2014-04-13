package desireMapApplicationPackage.outputSetPackage;

import java.io.Serializable;
import java.util.HashSet;

import desireMapApplicationPackage.desireContentPackage.DesireContent;
import desireMapApplicationPackage.quadtree.DataQuadTreeNode;
import desireMapApplicationPackage.quadtree.QuadTreeNodeBox;

public class DesireSet implements Serializable{
	public DesireSet(){
		dTree = new DataQuadTreeNode();
	}
	public DataQuadTreeNode dTree;
}