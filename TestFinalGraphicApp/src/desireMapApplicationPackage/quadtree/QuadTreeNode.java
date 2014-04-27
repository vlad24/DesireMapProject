package desireMapApplicationPackage.quadtree;
import java.util.HashSet;

import com.google.android.gms.maps.model.LatLng;



public class QuadTreeNode {
	private QuadTreeNode northWestNode;
	private QuadTreeNode northEastNode;
	private QuadTreeNode southWestNode;
	private QuadTreeNode southEastNode;
	private QuadTreeNodeBox nodeBox;
	public final String nodeID;
	public final int depth;
	public final static int maxDepth = 30;

	public QuadTreeNode(QuadTreeNodeBox nodeBox, String nodeID, int depth){
		this.nodeBox = nodeBox;
		this.depth = depth;
		this.nodeID = nodeID;
	}

	private QuadTreeNode subdivide(double x, double y){
		double xMid = (nodeBox.x0 + nodeBox.x1) / 2;
		double yMid = (nodeBox.y0 + nodeBox.y1) / 2;

		if(y > yMid){
			if(x < xMid){
				if(northWestNode == null){
					QuadTreeNodeBox northWestBox = new QuadTreeNodeBox(nodeBox.x0, yMid, xMid, nodeBox.y1);
					northWestNode = new QuadTreeNode(northWestBox, nodeID+"0", depth+1);
				}
				return northWestNode;
			}
			if(northEastNode == null){
				QuadTreeNodeBox northEastBox = new QuadTreeNodeBox(xMid, yMid, nodeBox.x1, nodeBox.y1);
				northEastNode = new QuadTreeNode(northEastBox, nodeID+"1", depth+1);
			}
			return northEastNode;
		}

		if(x < xMid){
			if(southWestNode == null){
				QuadTreeNodeBox southWestBox = new QuadTreeNodeBox(nodeBox.x0, nodeBox.y0, xMid, yMid);
				southWestNode = new QuadTreeNode(southWestBox, nodeID+"2", depth+1);
			}
			return southWestNode;
		}
		if(southEastNode == null){
			QuadTreeNodeBox southEastBox = new QuadTreeNodeBox(xMid, nodeBox.y0, nodeBox.x1, yMid);
			southEastNode = new QuadTreeNode(southEastBox, nodeID+"3", depth+1);
		}
		return southEastNode;

	}
	
	public String geoPointToQuad(double latitude, double longitude, int depth){

		if(this.depth == depth)
			return nodeID;

		if(this.depth < maxDepth){
			QuadTreeNode currentNode = subdivide(latitude, longitude);
			return currentNode.geoPointToQuad(latitude, longitude, depth);
		}
		
		return "";
	}
	
	
	public HashSet<String> getMapTiles(LatLng leftBottom, LatLng rightTop, int depth){
		HashSet<String> mapTiles = new HashSet<String>();
		
		mapTiles.add(geoPointToQuad(leftBottom.latitude, leftBottom.longitude, depth));
		mapTiles.add(geoPointToQuad(leftBottom.latitude, rightTop.longitude, depth));
		mapTiles.add(geoPointToQuad(rightTop.latitude, rightTop.longitude, depth));
		mapTiles.add(geoPointToQuad(rightTop.latitude, leftBottom.longitude, depth));
		return mapTiles;
	}
	
	public static int getQuadDepth(double screenVerticalSize){
		double scale = 180 / screenVerticalSize;
		return (int) (Math.log(scale)/Math.log(2)+1e-10);
	}

	public void printTree(){
		System.out.println(" "+nodeID+" ( ");
		if(northWestNode != null)
			northWestNode.printTree();
		if(northEastNode != null)
			northEastNode.printTree();
		if(southWestNode != null)
			southWestNode.printTree();
		if(southEastNode != null)
			southEastNode.printTree();
		System.out.println(" ) ");
	}

}
