package htw;

/**
 * Room class keeps track of location, whether it has adjacent rooms, a wumpus, fowl stench, bats,
 * pits, and/or pit breeze.
 */
public class Room implements RoomI {
  private final int x;
  private final int y;

  private RoomI n;
  private RoomI s;
  private RoomI e;
  private RoomI w;

  private boolean hasNorth;
  private boolean hasSouth;
  private boolean hasEast;
  private boolean hasWest;
  private boolean hasWumpus;
  private boolean hasWumpusScent;
  private boolean hasBat;
  private boolean hasPit;
  private boolean hasPitBreeze;
  private boolean visited;

  /**
   * Default constructor.
   *
   * @param x x
   * @param y y
   */
  public Room(int x, int y) {
    this.x = x;
    this.y = y;
    this.hasNorth = true;
    this.hasSouth = true;
    this.hasEast = true;
    this.hasWest = true;
    this.hasWumpusScent = false;
    this.hasBat = false;
    this.hasPit = false;
    this.visited = false;
  }

  /**
   * Constructor (currently just used by tunnel) to set which paths are open.
   *
   * @param x        x
   * @param y        y
   * @param hasNorth hasNorth
   * @param hasSouth hasSouth
   * @param hasEast  hasEast
   * @param hasWest  hasWest
   */
  public Room(int x, int y, boolean hasNorth, boolean hasSouth, boolean hasEast, boolean hasWest) {
    this.x = x;
    this.y = y;
    this.hasNorth = hasNorth;
    this.hasSouth = hasSouth;
    this.hasEast = hasEast;
    this.hasWest = hasWest;
    this.hasWumpusScent = false;
    this.hasBat = false;
    this.hasPit = false;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public String toString() {
    return "";
  }

  @Override
  public boolean hasNorth() {
    return hasNorth;
  }

  @Override
  public boolean hasSouth() {
    return hasSouth;
  }

  @Override
  public boolean hasEast() {
    return hasEast;
  }

  @Override
  public boolean hasWest() {
    return hasWest;
  }

  @Override
  public boolean hasWumpus() {
    return hasWumpus;
  }

  @Override
  public boolean hasWumpusScent() {
    return hasWumpusScent;
  }

  @Override
  public boolean hasBat() {
    return hasBat;
  }

  @Override
  public boolean hasPit() {
    return hasPit;
  }

  @Override
  public boolean isVisited() {
    return visited;
  }

  @Override
  public boolean hasPitBreeze() {
    return hasPitBreeze;
  }

  @Override
  public void setHasPit() {
    hasPit = true;
  }

  @Override
  public void setHasPitBreeze(IModel.Move d) {
    hasPitBreeze = true;
  }

  @Override
  public void setHasWumpus() {
    this.hasWumpus = true;
  }

  @Override
  public void setHasWumpusScent(IModel.Move d) {
    hasWumpusScent = true;
  }

  @Override
  public void setHasBat() {
    this.hasBat = true;
  }

  @Override
  public void setVisited() {
    this.visited = true;
  }

  @Override
  public void setN(RoomI n) {
    this.n = n;
  }

  @Override
  public void setS(RoomI s) {
    this.s = s;
  }

  @Override
  public void setE(RoomI e) {
    this.e = e;
  }

  @Override
  public void setW(RoomI w) {
    this.w = w;
  }

  /**
   * Gives room in the direction of the given move.
   *
   * @param d move
   * @return room
   */
  @Override
  public RoomI getRoomFromDir(IModel.Move d) {
    if (d == IModel.Move.WEST) {
      return w;
    } else if (d == IModel.Move.EAST) {
      return e;
    } else if (d == IModel.Move.SOUTH) {
      return s;
    } else if (d == IModel.Move.NORTH) {
      return n;
    } else {
      throw new IllegalArgumentException();
    }
  }

  private boolean hasRoomFromDir(IModel.Move d) {
    if (d == IModel.Move.WEST) {
      return hasWest;
    } else if (d == IModel.Move.EAST) {
      return hasEast;
    } else if (d == IModel.Move.SOUTH) {
      return hasSouth;
    } else if (d == IModel.Move.NORTH) {
      return hasNorth;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public RoomI getN() {
    return n;
  }

  @Override
  public RoomI getS() {
    return s;
  }

  @Override
  public RoomI getE() {
    return e;
  }

  @Override
  public RoomI getW() {
    return w;
  }

  /**
   * Closes path based on direction given.
   *
   * @param d move
   */
  @Override
  public void close(IModel.Move d) {
    if (d == IModel.Move.WEST) {
      closeWest();
    } else if (d == IModel.Move.EAST) {
      closeEast();
    } else if (d == IModel.Move.SOUTH) {
      closeSouth();
    } else if (d == IModel.Move.NORTH) {
      closeNorth();
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void closeNorth() {
    hasNorth = false;
  }

  @Override
  public void closeSouth() {
    hasSouth = false;
  }

  @Override
  public void closeEast() {
    hasEast = false;
  }

  @Override
  public void closeWest() {
    hasWest = false;
  }

  @Override
  public boolean sameLocation(Cell c) {
    return (this.x == c.getX() && this.y == c.getY());
  }

  public String tempName() {
    return "Room[" + x + "][" + y + "]";
  }

  /**
   * Room processes arrow: decrements distance, kills wumpus if hit, stops at wall if hit, falls to
   * ground if falls short.
   *
   * @param d        direction entering from
   * @param distance remaining distance
   * @param state    game state
   * @return feedback output
   */
  @Override
  public String processArrow(IModel.Move d, int distance, IModel.State state) {
    if (distance == 0) {
      if (hasWumpus) {
        return IModel.ARROW_HIT;
      } else {
        return IModel.ARROW_MISS;
      }
    }
    if (hasRoomFromDir(d)) {
      return getRoomFromDir(d).processArrow(d, distance - 1, state);
    }
    return IModel.ARROW_WALLED;
  }

  @Override
  public boolean isTunnel() {
    return false;
  }
}
