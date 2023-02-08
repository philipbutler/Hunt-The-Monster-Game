package htw;

/**
 * Room Interface.
 */
public interface RoomI extends Cell {

  int getX();

  int getY();

  String toString();

  boolean hasNorth();

  boolean hasSouth();

  boolean hasEast();

  boolean hasWest();

  boolean hasWumpus();

  boolean hasWumpusScent();

  boolean hasBat();

  boolean hasPit();

  boolean hasPitBreeze();

  boolean isVisited();

  void setHasPit();

  void setHasWumpusScent(IModel.Move d);

  void setHasPitBreeze(IModel.Move d);

  void setHasWumpus();

  void setHasBat();

  void setVisited();

  void setN(RoomI n);

  void setS(RoomI s);

  void setE(RoomI e);

  void setW(RoomI w);

  RoomI getRoomFromDir(IModel.Move d);

  RoomI getN();

  RoomI getS();

  RoomI getE();

  RoomI getW();

  void close(IModel.Move d);

  void closeNorth();

  void closeSouth();

  void closeEast();

  void closeWest();

  boolean sameLocation(Cell c);

  String tempName();

  String processArrow(IModel.Move d, int distance, IModel.State state);

  boolean isTunnel();
}
