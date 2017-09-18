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
		if (leftList.size() > 1){
			numBlocks = leftList.size();
			left = new BlockTree(leftList);
//			right = new BlockTree(rightList);
			block = null;
		}
		else {
			numBlocks = 1;
			left = null;
			right = null;
			block = leftList.get(0);
		}

		if (rightList.size() > 1){
			numBlocks = rightList.size();
//			left = new BlockTree(leftList);
			right = new BlockTree(rightList);
			block = null;
		}
		else {
			numBlocks = 1;
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
		if (this.isLeaf()) {
			return p.x >= box.lower.x && p.x <= box.upper.x && p.y >= box.lower.y && p.y <= box.upper.y;
		}
		else {
			if (p.x >= box.lower.x && p.x <= box.upper.x && p.y >= box.lower.y && p.y <= box.upper.y) {
				return this.left.contains(p) || this.right.contains(p);
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
		this.box = this.box.displaced(thisD);
		t.box = t.box.displaced(d);

		if (this.isLeaf()) {
			return this.box.lower.x >= t.box.lower.x && this.box.upper.x <= t.box.upper.x && this.box.lower.y >= t.box.lower.y && this.box.upper.y <= t.box.upper.y;
		}
		else {
			if (this.box.lower.x >= t.box.lower.x && this.box.upper.x <= t.box.upper.x && this.box.lower.y >= t.box.lower.y && this.box.upper.y <= t.box.upper.y) {
				return this.left.overlaps(thisD, t.left, d) || this.right.overlaps(thisD,t. right, d);
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
