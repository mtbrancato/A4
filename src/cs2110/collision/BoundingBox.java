package cs2110.collision;
import java.util.Iterator;

/** An instance is a 2D bounding box. */
public class BoundingBox {
	/** The corner of the bounding box with the smaller x,y coordinates. */
	public Vector2D lower; // (minX,minY)

	/** The corner of the bounding box with the larger x,y coordinates.	 */
	public Vector2D upper; // (maxX,maxY)

	/** Constructor: an instance is a copy of bounding box b.*/
	public BoundingBox(BoundingBox b) {
		lower = new Vector2D(b.lower);
		upper = new Vector2D(b.upper);
	}

	/** Constructor: An instance with lower as smaller coordinates and
	 * upper as larger coordinates.
	 * @param lower  Corner with smaller coordinates.
	 * @param upper  Corner with larger coordinates.
	 */
	public BoundingBox(Vector2D lower, Vector2D upper) {
		if (upper.x < lower.x)
			throw new IllegalArgumentException("invalid bbox");
		if (upper.y < lower.y)
			throw new IllegalArgumentException("invalid bbox");

		this.lower = lower;
		this.upper = upper;
	}

	/** Return the width of this bounding box (along x-dimension).
	 * @return Width of this bounding box.
	 */
	public double getWidth() {
		return upper.x - lower.x;
	}

	/** Return the height of this bounding box (along y-dimension).
	 * @return Height of this bounding box.
	 */
	public double getHeight() {
		return upper.y - lower.y;
	}

	/** Return the larger of the width and height of this bounding box.
	 * @return Returns the dimension (width or height) of maximum length.
	 */
	public double getLength() {
		// TODO: Implement me.
		if (upper.x - lower.x >= upper.y - lower.y){ return upper.x - lower.x; }
		else { return upper.y - lower.y; }
	}

	/** Return the center of this bounding box.
	 * @return The center of this bounding box.
	 */
	public Vector2D getCenter() {
		// TODO: Implement me.
		return new Vector2D((lower.x + upper.x) / 2, (lower.y + upper.y) / 2);
	}

	/** Return the result of displacing this bounding box by d.
	 * @param d
	 *            A displacement vector.
	 * @return The result of displacing this bounding box by vector d.
	 */
	public BoundingBox displaced(Vector2D d) {
		// TODO: Implement me.
		Vector2D newLower = new Vector2D(lower.x + d.x, lower.y + d.y);
		Vector2D newUpper = new Vector2D(upper.x + d.x, upper.y + d.y);
		return new BoundingBox(newLower, newUpper);
	}

	/** Return true iff this bounding box contains p.
	 * @param p A point.
	 * @return True iff this bounding box contains point p.
	 */
	public boolean contains(Vector2D p) {
		boolean inX = lower.x <= p.x && p.x <= upper.x;
		boolean inY = lower.y <= p.y && p.y <= upper.y;
		return inX && inY;
	}

	/** Return the area of this bounding box.
	 * @return The area of this bounding box.
	 */
	public double getArea() {
		// TODO: Implement me.
		return (upper.x - lower.x) * (upper.y - lower.y);
	}

	/** Return true iff this bounding box overlaps with box.
	 * @param box  A bounding box.
	 * @return True iff this bounding box overlaps with box.
	 */
	public boolean overlaps(BoundingBox box) {
		// TODO: Implement me.
		if (box.lower.x >= lower.x && box.lower.x <= upper.x && box.lower.y >= lower.y && box.lower.y <= upper.y) {
			return true;
		}
		else { return false; }
	}


	/** Return the bounding box of blocks given by iter.
	 * @param iter   An iterator of blocks.
	 * @return The bounding box of the blocks given by the iterator.
	 */
	public static BoundingBox findBBox(Iterator<Block> iter) {
		// Do not modify the following "if" statement.
		if (!iter.hasNext()) {
			throw new IllegalArgumentException("empty iterator");
		}
		Block block = iter.next();
		BoundingBox blockBB = block.getBBox();
		// TODO: Implement me.
		while (iter.hasNext()){
			block = iter.next();
			BoundingBox newBBox = block.getBBox();
			if (newBBox.lower.x < blockBB.lower.x) {
				blockBB.lower.x = newBBox.lower.x;
			}
			if (newBBox.upper.x > blockBB.upper.x) {
				blockBB.upper.x = newBBox.upper.x;
			}
			if (newBBox.lower.y < blockBB.lower.y) {
				blockBB.lower.y = newBBox.lower.y;
			}
			if (newBBox.upper.y > blockBB.upper.y) {
				blockBB.upper.y = newBBox.upper.y;
			}
		}
		return blockBB;
	}

	/** Return a representation of this bounding box. */
	public String toString() {
		return lower + " -- " + upper;
	}
}
