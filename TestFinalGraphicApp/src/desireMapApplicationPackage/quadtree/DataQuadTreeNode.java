package desireMapApplicationPackage.quadtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import desireMapApplicationPackage.desireContentPackage.DesireContent;

public class DataQuadTreeNode implements Serializable{
	
	private DataQuadTreeNode northWestNode;
	private DataQuadTreeNode northEastNode;
	private DataQuadTreeNode southWestNode;
	private DataQuadTreeNode southEastNode;
	private QuadTreeNodeBox nodeBox;

	public int count = 0;
	public static final int boxCapacity = 25;
	private ArrayList<DesireContent> points;


	public DataQuadTreeNode(QuadTreeNodeBox nodeBox){
		this.nodeBox = nodeBox;
		points = new ArrayList<DesireContent>();
	}
	public DataQuadTreeNode(){
		this.nodeBox = new QuadTreeNodeBox(-90,-180,90,180);
		points = new ArrayList<DesireContent>();
	}
	

	private void subdivide(double x, double y){
		double xMid = (nodeBox.x0 + nodeBox.x1) / 2;
		double yMid = (nodeBox.y0 + nodeBox.y1) / 2;

		if(y > yMid){
			if(x < xMid){
				if(northWestNode == null){
					QuadTreeNodeBox northWestBox = new QuadTreeNodeBox(nodeBox.x0, yMid, xMid, nodeBox.y1);
					northWestNode = new DataQuadTreeNode(northWestBox);
				}
				return;
			}
			if(northEastNode == null){
				QuadTreeNodeBox northEastBox = new QuadTreeNodeBox(xMid, yMid, nodeBox.x1, nodeBox.y1);
				northEastNode = new DataQuadTreeNode(northEastBox);
			}
			return;
		}

		if(x < xMid){
			if(southWestNode == null){
				QuadTreeNodeBox southWestBox = new QuadTreeNodeBox(nodeBox.x0, nodeBox.y0, xMid, yMid);
				southWestNode = new DataQuadTreeNode(southWestBox);
			}
			return;
		}
		if(southEastNode == null){
			QuadTreeNodeBox southEastBox = new QuadTreeNodeBox(xMid, nodeBox.y0, nodeBox.x1, yMid);
			southEastNode = new DataQuadTreeNode(southEastBox);
		}
		return;

	}


	public boolean insertData(DataQuadTreeNode node, DesireContent desire){
		if(node == null){
			return false;
		}
		if(!node.nodeBox.contains(desire.coordinates.latitude, desire.coordinates.longitude))
			return false;

		//we can suppose that data will be inserted
		count++;
		if(node.points.size() < boxCapacity){
			node.points.add(desire);
			return true;
		}

		node.subdivide(desire.coordinates.latitude, desire.coordinates.longitude);

		if(insertData(node.northEastNode, desire)) return true;
		if(insertData(node.northWestNode, desire)) return true;
		if(insertData(node.southWestNode, desire)) return true;
		if(insertData(node.southEastNode, desire)) return true;

		return false;			

	}
	
	public boolean insertData(DesireContent desire){
		return insertData(this, desire); 
	}

	public void getData(DataQuadTreeNode node, QuadTreeNodeBox screenBox, HashSet<DesireContent> resultSet){
		if(node.nodeBox.notIntersect(screenBox))
				return;
		
		for(DesireContent desire: node.points)
			if(screenBox.contains(desire.coordinates.latitude, desire.coordinates.longitude))
				resultSet.add(desire);
		
		if(node.northWestNode != null)
			getData(node.northWestNode, screenBox, resultSet);
		if(node.northEastNode != null)
			getData(node.northEastNode, screenBox, resultSet);
		if(node.southWestNode != null)
			getData(node.southWestNode, screenBox, resultSet);
		if(node.southEastNode != null)
			getData(node.southEastNode, screenBox, resultSet);
	}
	
	//works just for one root level DataQuadTrees
	public void merge(DataQuadTreeNode node){
		//try to add childs
		if(node.northWestNode != null){
			if(northWestNode == null){
				northWestNode = node.northWestNode;
			} else
				northWestNode.merge(node.northWestNode);
		}
		
		if(node.northEastNode != null){
			if(northEastNode == null){
				northEastNode = node.northEastNode;
			} else
				northEastNode.merge(node.northEastNode);
		}
		
		if(node.southEastNode != null){
			if(southEastNode == null){
				southEastNode = node.southEastNode;
			} else
				southEastNode.merge(node.southEastNode);
		}
		
		if(node.southWestNode != null){
			if(southWestNode == null){
				southWestNode = node.southWestNode;
			} else
				southWestNode.merge(node.southWestNode);
		}
		
		//now we have already added all childs and want to add node points
		if(node.points.size() + points.size() <= boxCapacity){
			points.addAll(node.points);
		} else{
			//iterate and add to current node
			for(DesireContent desire : node.points){
				insertData(this, desire);
			}
		}
	}

	public void print(){
		printHelper(this);
	}
	private void printHelper(DataQuadTreeNode node) {
		if (node == null){
			return;
		}
		else{
			for (DesireContent cont : this.points){
				System.out.println(cont.desireID + " " + cont.login + " " + cont.description);
			}
			printHelper(node.northEastNode);
			printHelper(node.northWestNode);
			printHelper(node.southEastNode);
			printHelper(node.southWestNode);
		}
	}

}
