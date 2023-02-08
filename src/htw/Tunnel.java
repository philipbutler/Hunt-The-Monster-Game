package htw;

/**
 * Tunnel class. Not a player, wumpus, bat, nor pit can be in a tunnel. Tunnels do not decrement
 * arrow distance. When a player walks into a dark tunnel, they will immediately appear on the other
 * side; the next room at the end of any amount of tunnels that is.
 */
public class Tunnel extends Room implements TunnelI {

  /**
   * Default constructor.
   *
   * @param x        x
   * @param y        y
   * @param hasNorth hasNorth
   * @param hasSouth hasSouth
   * @param hasEast  hasEast
   * @param hasWest  hasWest
   */
  public Tunnel(int x, int y, boolean hasNorth, boolean hasSouth, boolean hasEast,
                boolean hasWest) {
    super(x, y, hasNorth, hasSouth, hasEast, hasWest);
  }

  /**
   * Tunnel's processing of arrow. Essentially tells the room on the other side of the tunnel to
   * process the arrow.
   *
   * @param d        direction entering from
   * @param distance remaining distance
   * @param state    game state
   * @return
   */
  @Override
  public String processArrow(IModel.Move d, int distance, IModel.State state) {
    d = otherSide(d);
    return getRoomFromDir(d).processArrow(d, distance, state);
  }

  /**
   * Tunnels spread the scent of a wumpus to the nearest room on the other side.
   *
   * @param d move
   */
  @Override
  public void setHasWumpusScent(IModel.Move d) {
    d = otherSide(d);
    getRoomFromDir(d).setHasWumpusScent(d);
  }

  /**
   * Breezes move across the tunnel to the nearest room on hte other side.
   *
   * @param d move
   */
  @Override
  public void setHasPitBreeze(IModel.Move d) {
    d = otherSide(d);
    getRoomFromDir(d).setHasPitBreeze(d);
  }

  @Override
  public IModel.Move otherSide(IModel.Move d) {
    if (d == IModel.Move.WEST) {
      // check NSW
      if (this.hasNorth()) {
        return IModel.Move.NORTH;
      } else if (this.hasSouth()) {
        return IModel.Move.SOUTH;
      } else if (this.hasWest()) {
        return IModel.Move.WEST;
      }
    } else if (d == IModel.Move.EAST) {
      // check NSE
      if (this.hasNorth()) {
        return IModel.Move.NORTH;
      } else if (this.hasSouth()) {
        return IModel.Move.SOUTH;
      } else if (this.hasEast()) {
        return IModel.Move.EAST;
      }
    } else if (d == IModel.Move.SOUTH) {
      // check SWE
      if (this.hasWest()) {
        return IModel.Move.WEST;
      } else if (this.hasSouth()) {
        return IModel.Move.SOUTH;
      } else if (this.hasEast()) {
        return IModel.Move.EAST;
      }
    } else if (d == IModel.Move.NORTH) {
      //check NWE
      if (this.hasWest()) {
        return IModel.Move.WEST;
      } else if (this.hasNorth()) {
        return IModel.Move.NORTH;
      } else if (this.hasEast()) {
        return IModel.Move.EAST;
      }
    }
    return null; //something's wrong
  }

  @Override
  public boolean isTunnel() {
    return true;
  }
}
