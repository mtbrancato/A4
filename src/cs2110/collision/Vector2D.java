package cs2110.collision;
import javax.vecmath.Point2d;

/**
 *
 * A 2D vector with components of double precision. We also think of this as a
 * point on a 2D plane.
 * 
 * Remark: This data structure can be used both for something that is
 * conceptually a vector (e.g. a displacement vector) and for something that is
 * conceptually a point on the plane (because a point can be identified with the
 * vector from the origin to the point).
 *
 */
public class Vector2D {
	/** The first component of the vector. */
	public double x;

	/** The second component of the vector. */
	public double y;

	/** Constructor: A copy of p.
	 * @param p  A vector.
	 */
	public Vector2D(Vector2D p) {
		x = p.x;
		y = p.y;
	}

	/**Constructor: a copy of p.
	 * @param p A Point2d object from the javax.vecmath package.
	 */
	public Vector2D(Point2d p) {
		x = p.x;
		y = p.y;
	}

	/** Constructor a Vector with components x and y.
	 * @param x  First component of vector.
	 * @param y  Second component of vector.
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/** Return a representation (x, y) of this instance. */
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	/** Return the result of adding this vector to a.
	 * @param a  A vector.
	 * @return The result of adding this vector to the vector a.
	 */
	public Vector2D add(Vector2D a) {
		return add(this, a);
	}

	/** Return the result of adding a and b.
	 * @param a  A vector.
	 * @param b  A vector.
	 * @return The result of adding vectors a and b.
	 */
	public static Vector2D add(Vector2D a, Vector2D b) {
		return new Vector2D(a.x + b.x, a.y + b.y);
	}

	/** Return the Euclidean distance between a and b.
	 * @param a  A vector (point).
	 * @param b  A vector (point).
	 * @return The Euclidean distance between the points a and b.
	 */
	public static double dist(Vector2D a, Vector2D b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/** Add v to this vector.
	 * @param v  Vector to add on to this vector.
	 */
	public void addOn(Vector2D v) {
		x += v.x;
		y += v.y;
	}

	/** Subtract b from this vector.
	 * @param b  A Vector.
	 * @return The result of subtracting b from this vector.
	 */
	public Vector2D minus(Vector2D b) {
		return new Vector2D(x - b.x, y - b.y);
	}

}
