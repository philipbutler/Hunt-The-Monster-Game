package htw;

/**
 * Edge class contains info of room on each side.
 */
public class Edge {

  private final Cell a;
  private final Cell b;

  /**
   * Default constructor.
   *
   * @param a Room a
   * @param b Room b
   */
  public Edge(Cell a, Cell b) {
    this.a = a;
    this.b = b;
  }

  public Cell getA() {
    return a;
  }

  public Cell getB() {
    return b;
  }

  public boolean contains(Cell r) {
    return a.sameLocation(r) | b.sameLocation(r);
  }

  /**
   * Provides the direction of the wall based on the given room.
   *
   * @param r room
   * @return direction
   */
  public IModel.Move directionOfWall(Cell r, int rows, int cols) {
    if (a.sameLocation(r)) {
      if (a.getX() == b.getX()) {
        if ((a.getY() == b.getY() + 1) || (a.getY() == 0 && b.getY() == cols - 1)) {
          return IModel.Move.WEST;
        } else if ((a.getY() == b.getY() - 1) || (a.getY() == cols - 1 && b.getY() == 0)) {
          return IModel.Move.EAST;
        }
      } else if (a.getY() == b.getY()) {
        if ((a.getX() == b.getX() - 1) || (a.getX() == rows - 1 && b.getX() == 0)) {
          return IModel.Move.SOUTH;
        } else if ((a.getX() == b.getX() + 1) || (a.getX() == 0 && b.getX() == rows - 1)) {
          return IModel.Move.NORTH;
        }
      }
    } else if (b.sameLocation(r)) {
      if (a.getX() == b.getX()) {
        if ((a.getY() == b.getY() + 1) || (b.getY() == cols - 1 && a.getY() == 0)) {
          return IModel.Move.EAST;
        } else if ((a.getY() == b.getY() - 1) || (b.getY() == 0 && a.getY() == cols - 1)) {
          return IModel.Move.WEST;
        }
      } else if (a.getY() == b.getY()) {
        if ((a.getX() == b.getX() - 1) || (b.getX() == 0 && a.getX() == rows - 1)) {
          return IModel.Move.NORTH;
        } else if ((a.getX() == b.getX() + 1) || (b.getX() == rows - 1 && a.getX() == 0)) {
          return IModel.Move.SOUTH;
        }
      }
    }
    return null; //something's wrong
  }

  public String toString() {
    return "(" + ((Room) a).tempName() + " - " + ((Room) b).tempName() + ")";
  }
}
