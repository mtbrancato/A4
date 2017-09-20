package cs2110.collision;
import java.util.ArrayList;
import java.util.Iterator;

/** An instance is a non-empty collection of points organized in a hierarchical
 * binary tree structure. */
public class BlockTree {
	private BoundingBox box; // bounding box of the blocks contained in this tree.

	private int numBlocks; // Number of blocks contained in this tree.

	private BlockTree left; // left subtree --null if this is a leaf

	private BlockTree right; // right subtree --null iff this is a leaf

	private Block block; //The block of a leaf node (null if not a leaf)

	// REMARK:
	// Leaf node: left, right == null && block != null
	// Intermediate node: left, right != null && block == null

	/** Constructor: a binary tree containing blocks.
	 * Precondition: The tree has no be non-empty,
	 * i.e. it must contain at least one block.	 */
	public BlockTree(ArrayList<Block> blocks) { // Leave the following two "if"
												// statements as they are.
		if (blocks == null)
			throw new IllegalArgumentException("blocks null");
		if (blocks.size() == 0)
			throw new IllegalArgumentException("no blocks");

		// TODO: implement me.
		// Find bounding box of the blocks
		Iterator<Block> blockIterator = blocks.iterator();
		box = BoundingBox.findBBox(blockIterator);

		// Split box in half along larger dimension
		double boxLength;
		double boxWidth;
		boxLength = box.upper.x - box.lower.x;
		boxWidth = box.upper.y - box.lower.y;
		ArrayList<Block> leftList = new ArrayList<>();
		ArrayList<Block> rightList = new ArrayList<>();

		if (boxLength >= boxWidth) {
			for (int x = 0; x < blocks.size(); x++) {
				if (blocks.get(x).position.x <= (box.upper.x + box.lower.x) / 2) {
					leftList.add(blocks.get(x));
				}
				else {
					rightList.add(blocks.get(x));
				}
			}
		}
		else {
			for (int x = 0; x < blocks.size(); x++) {
				if (blocks.get(x).position.y <= (box.upper.y + box.lower.y) / 2) {
					leftList.add(blocks.get(x));
				}
				else {
					rightList.add(blocks.get(x));
				}
			}
		}

		// Create binary tree

//		if (left == null){
//			if (leftList.size() > 1) {
//				numBlocks = leftList.size();
//				left = new BlockTree(leftList);
//				block = null;
//			}
//			else {
//				numBlocks = 1;
//				left = null;
//				right = null;
//				block = leftList.get(0);
//			}
//		}
//		if (right == null){
//			if (rightList.size() > 1){
//				numBlocks = rightList.size();
//				right = new BlockTree(rightList);
//				block = null;
//			}
//			else {
//				numBlocks = 1;
//				left = null;
//				right = null;
//				block = rightList.get(0);
//			}
//		}

		if (leftList.size() > 1){
			numBlocks = leftList.size();
			System.out.println("left numblocks: " + numBlocks);
			left = new BlockTree(leftList);
			block = null;
		}
		else {
			numBlocks = 1;
			System.out.println("leaf numblocks: " + numBlocks);
			left = null;
			right = null;
			block = leftList.get(0);
		}

		if (rightList.size() > 1){
			numBlocks = rightList.size();
			System.out.println("right numblocks: " + numBlocks);
			right = new BlockTree(rightList);
			block = null;
		}
		else {
			numBlocks = 1;
			System.out.println("leaf numblocks: " + numBlocks);
			left = null;
			right = null;
			block = rightList.get(0);
		}
	}

	/** Return the bounding box of the collection of blocks.
	 * @return The bounding box of this collection of blocks.
	 */
	public BoundingBox getBox() {
		return box;
	}

	/** Return true iff this is a leaf node.
	 * @return true iff this is a leaf node.
	 */
	public boolean isLeaf() {
		return (block != null);
	}

	/** Return true iff this is an intermediate node.
	 * @return true iff this is an intermediate node.
	 */
	public boolean isIntermediate() {
		return !isLeaf();
	}

	/** Return the number of blocks contained in this tree.
	 * @return Number of blocks contained in this tree.
	 */
	public int getNumBlocks() {
		return numBlocks;
	}

	/** Return true iff this collection of blocks contains  point p.
	 * @return True iff this collection of blocks contains  point p.
	 */
	public boolean contains(Vector2D p) {
		// TODO: Implement me.
		if ((this.left == null || this.right == null) && !this.isLeaf()) {
			System.out.println("error");
		}
		if (this.isLeaf()) {
			return p.x >= box.lower.x && p.x <= box.upper.x && p.y >= box.lower.y && p.y <= box.upper.y;
		}
		else {
			if (p.x >= box.lower.x && p.x <= box.upper.x && p.y >= box.lower.y && p.y <= box.upper.y) {
				boolean leftRes = false;
				if (this.left != null) {
					leftRes = this.left.contains(p);
				}

				boolean rightRes = false;
				if (this.right != null) {
					rightRes = this.right.contains(p);
				}

				return rightRes || leftRes;
			}
			else { return false;}
		}
	}

	/** Return true iff (this tree displaced by thisD) and (tree t 
	 * displaced by d) overlap.
	 * @param thisD
	 *            Displacement of this tree.
	 * @param t
	 *            A tree of blocks.
	 * @param d
	 *            Displacement of tree t.
	 * @return True iff this tree and tree t overlap (account for
	 *         displacements).
	 */
	public boolean overlaps(Vector2D thisD, BlockTree t, Vector2D d) {
		// TODO: Implement me
//		Vector2D newLower = new Vector2D(this.box.lower.x + thisD.x, this.box.lower.y + thisD.y);
//		Vector2D newUpper = new Vector2D(this.box.upper.x + thisD.x, this.box.upper.y + thisD.y);
		// BoundingBox thisBox = new BoundingBox(newLower, newUpper);
		BoundingBox thisBox = this.box.displaced(thisD);

//		Vector2D newTLower = new Vector2D(t.box.lower.x + d.x, t.box.lower.y + d.y);
//		Vector2D newTUpper = new Vector2D(t.box.upper.x + d.x, t.box.upper.y + d.y);
		// BoundingBox tBox = new BoundingBox(newTLower, newTUpper);
		BoundingBox tBox = t.box.displaced(d);

		Vector2D upperLeft = new Vector2D(thisBox.lower.x, thisBox.lower.y);
		Vector2D lowerLeft = new Vector2D(thisBox.lower.x, thisBox.upper.y);
		Vector2D upperRight = new Vector2D(thisBox.upper.x, thisBox.lower.y);
		Vector2D lowerRight = new Vector2D(thisBox.upper.x, thisBox.upper.y);

		Vector2D upperTLeft = new Vector2D(tBox.lower.x, tBox.lower.y);
		Vector2D lowerTLeft = new Vector2D(tBox.lower.x, tBox.upper.y);
		Vector2D upperTRight = new Vector2D(tBox.upper.x, tBox.lower.y);
		Vector2D lowerTRight = new Vector2D(tBox.upper.x, tBox.upper.y);

//		Vector2D upperLeft = new Vector2D(thisBox.lower.y, thisBox.lower.x);
//		Vector2D lowerLeft = new Vector2D(thisBox.lower.y, thisBox.upper.x);
//		Vector2D upperRight = new Vector2D(thisBox.upper.y, thisBox.lower.x);
//		Vector2D lowerRight = new Vector2D(thisBox.upper.y, thisBox.upper.x);
//
//		Vector2D upperTLeft = new Vector2D(tBox.lower.y, tBox.lower.x);
//		Vector2D lowerTLeft = new Vector2D(tBox.lower.y, tBox.upper.x);
//		Vector2D upperTRight = new Vector2D(tBox.upper.y, tBox.lower.x);
//		Vector2D lowerTRight = new Vector2D(tBox.upper.y, tBox.upper.x);


		if (this.isLeaf() || t.isLeaf()) {
			return ((upperTLeft.x >= upperLeft.x && upperTRight.x <= upperRight.x && upperLeft.y >= upperTRight.y && lowerRight.y <= lowerTRight.y));
			// return ((thisBox.contains(upperTLeft) || thisBox.contains(upperTRight) || thisBox.contains(lowerTLeft) || thisBox.contains(lowerTRight)));
			// return thisBox.lower.x >= tBox.lower.x && thisBox.upper.x <= tBox.upper.x && thisBox.lower.y >= tBox.lower.y && thisBox.upper.y <= tBox.upper.y;
		}
		else {
			// if ((thisBox.contains(upperTLeft) || thisBox.contains(upperTRight) || thisBox.contains(lowerTLeft) || thisBox.contains(lowerTRight))) {
			if ((upperTLeft.x >= upperLeft.x && upperTRight.x <= upperRight.x && upperLeft.y >= upperTRight.y && lowerRight.y <= lowerTRight.y)) {
				boolean leftRight = false;
				boolean leftLeft = false;
				boolean rightLeft = false;
				boolean rightRight = false;

				if (this.left != null && t.left != null) {
					leftLeft = this.left.overlaps(thisD, t.left, d);
				}

				if (this.left != null && t.right != null) {
					leftRight = this.left.overlaps(thisD, t.right, d);
				}

				if (this.right != null && t.left != null) {
					rightLeft = this.right.overlaps(thisD, t.left, d);
				}

				if (this.right != null && t.right != null) {
					rightRight = this.right.overlaps(thisD, t.right, d);
				}

				return leftRight || leftLeft || rightLeft || rightRight;

				// return this.left.overlaps(thisD, t.left, d) || this.left.overlaps(thisD, t.right, d) || this.right.overlaps(thisD, t.right, d) || this.right.overlaps(thisD, t.left, d);// || t.left.overlaps(d,this, thisD) || t.right.overlaps(d,this,thisD);
			}
			else { return false;}
		}
	}

	/** Return a representation of this instance. */
	public String toString() {
		return toString(new Vector2D(0, 0));
	}

	/** Return a represenation of d
	 * 
	 * @param d  Displacement vector.
	 * @return String representation of this tree (displaced by d).
	 */
	public String toString(Vector2D d) {
		return toStringAux(d, "");
	}

	/** Useful for creating appropriate indentation for function toString.  */
	private static final String indentation = "   ";

	/** Return a representation of this instance displaced by d, with
	 * indentation indent.
	 * @param d Displacement vector.
	 * @param indent  Indentation.
	 * @return String representation of this tree (displaced by d).
	 */
	private String toStringAux(Vector2D d, String indent) {
		String str = indent + "Box: ";
		str += "(" + (box.lower.x + d.x) + "," + (box.lower.y + d.y) + ")";
		str += " -- ";
		str += "(" + (box.upper.x + d.x) + "," + (box.upper.y + d.y) + ")";
		str += "\n";

		if (isLeaf()) {
			String vStr = "(" + (block.position.x + d.x) + "," + (block.position.y + d.y)
					+ ")" + block.halfwidth;
			str += indent + "Leaf: " + vStr + "\n";
		} else {
			String newIndent = indent + indentation;
			str += left.toStringAux(d, newIndent);
			str += right.toStringAux(d, newIndent);
		}

		return str;
	}

}
