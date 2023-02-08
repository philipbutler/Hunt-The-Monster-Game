package htw;

/**
 * Interface for Tunnel.
 */
public interface TunnelI extends RoomI {

  /**
   * Given the move used to enter the tunnel, this returns the move that puts the player through the
   * other side of the tunnel.
   *
   * @param d move from
   * @return move to
   */
  IModel.Move otherSide(IModel.Move d);
}
