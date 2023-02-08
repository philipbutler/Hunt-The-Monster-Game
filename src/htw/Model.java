package htw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Model for Hunt The Wumpus. Creates the maze, player, wumpus, and bats, and handles the logic for
 * the game. Communicates with the MazeController, getting input and giving state. Game state is
 * stored in MazeModel, only functions of the or functions of classes within it can alter its
 * state.
 */
public class Model implements IModel {

  private final Random rand;
  private final ArrayList<Edge> savedEdges;
  private final ArrayList<Edge> walledEdges;
  private final ArrayList<Edge> perimeterEdges;
  private final ArrayList<ArrayList<Cell>> connected;
  private final int wallsRemaining;
  private final boolean wrapping;
  private final boolean perfect;
  private final int numPlayers;
  private final int rows;
  private final int cols;
  private final int percentBats;
  private final int percentPits;
  private final int numArrows;
  private RoomI[][] rooms;
  private final ArrayList<RoomI> nRooms;
  private List<PlayerI> players;
  private State state;
  private boolean visual;

  /**
   * Maze constructor.
   *
   * @param rows           rows
   * @param cols           cols
   * @param wrapping       wrapping
   * @param perfect        perfect
   * @param wallsRemaining walls remaining
   */
  public Model(int numPlayers, int rows, int cols, boolean wrapping, boolean perfect,
               int wallsRemaining, int percentBats, int percentPits, int numArrows, int seed) {
    if (rows < 2 || cols < 2) {
      throw new IllegalArgumentException("Rows and cols must be greater than 1");
    }
    if (!perfect && wallsRemaining < 0) {
      throw new IllegalArgumentException("For imperfect Mazes walls must be non-negative");
    }
    if (numArrows < 0) {
      throw new IllegalArgumentException("Number of arrows must be non-negative");
    }
    if (seed == 0) {
      rand = new Random();
    } else {
      rand = new Random(seed);
    }
    savedEdges = new ArrayList<Edge>();
    walledEdges = new ArrayList<Edge>();
    perimeterEdges = new ArrayList<Edge>();
    connected = new ArrayList<ArrayList<Cell>>();
    nRooms = new ArrayList<RoomI>();
    players = new ArrayList<PlayerI>();
    this.wallsRemaining = wallsRemaining;
    this.wrapping = wrapping;
    this.perfect = perfect;
    this.numPlayers = numPlayers;
    this.rows = rows;
    this.cols = cols;
    this.percentBats = percentBats;
    this.percentPits = percentPits;
    this.numArrows = numArrows;
    buildMaze();
    this.state = State.IN_PROGRESS;
    this.visual = visual;
  }

  /**
   * Calls helper functions to build Maze.
   */
  private void buildMaze() {
    createRooms();
    createWalls();
    if (wrapping) {
      walledEdges.addAll(perimeterEdges);
    }
    removeFirstWalls();
    if (!perfect) {
      removeSecondWalls();
    }
    if (!wrapping) {
      savedEdges.addAll(perimeterEdges);
    }

    populateHasDirections();
    createTunnels();
    pointDirections();
    numberRooms();
    placeWumpus();
    placeBats();
    placePits();
    placePlayers();
  }

  /**
   * Creates grid of rooms using number of rows and columns.
   */
  private void createRooms() {
    rooms = new Room[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        rooms[i][j] = new Room(i, j);
        ArrayList<Cell> n = new ArrayList<Cell>();
        n.add(rooms[i][j]);
        connected.add(n);
      }
    }
  }

  /**
   * Initializes the grid with walls between all rooms.
   */
  private void createWalls() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (i < rows - 1) {
          walledEdges.add(new Edge(rooms[i][j], rooms[i + 1][j]));
        }
        if (j < cols - 1) {
          walledEdges.add(new Edge(rooms[i][j], rooms[i][j + 1]));
        }
      }
    }

    for (int i = 0; i < rows; i++) {
      perimeterEdges.add(new Edge(rooms[i][0], rooms[i][cols - 1]));
    }

    for (int i = 0; i < cols; i++) {
      perimeterEdges.add(new Edge(rooms[0][i], rooms[rows - 1][i]));
    }

  }

  /**
   * Removes the given wall.
   *
   * @param e wall to be removed
   */
  private void removeWall(Edge e) {
    if (isConnected(e.getA(), e.getB())) {
      savedEdges.add(e);
    } else {
      merge(e.getA(), e.getB());
    }
    walledEdges.remove(e);
  }

  /**
   * Removes potential walls.
   */
  private void removeFirstWalls() {
    while (walledEdges.size() != 0) {
      removeWall(walledEdges.get(rand.nextInt(walledEdges.size())));
    }
  }

  /**
   * Removes saved walls until reached given number.
   */
  private void removeSecondWalls() {
    if (!perfect) {
      while (savedEdges.size() > wallsRemaining) {
        savedEdges.remove(rand.nextInt(savedEdges.size()));
      }
    }
  }

  /**
   * Returns true if both rooms are already in the same set.
   *
   * @param a Room A
   * @param b Room B
   * @return if rooms are connected/in the same set
   */
  private boolean isConnected(Cell a, Cell b) {
    for (ArrayList<Cell> c : connected) {
      if (c.contains(a) & c.contains(b)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Merge function is only called when rooms aren't already in the same set. It finds the sets of
   * rooms that contain either Room A and Room B, adds that set to a new set, removes the original
   * sets, then adds the combined set back in. Used to avoid ConcurrentModificationException:
   * https://www.baeldung.com/java-concurrentmodificationexception.
   *
   * @param a Room A
   * @param b Room B
   */
  private void merge(Cell a, Cell b) {
    ArrayList<Cell> n = new ArrayList<Cell>();
    ArrayList<ArrayList<Cell>> toRemove = new ArrayList<ArrayList<Cell>>();
    for (ArrayList<Cell> c : connected) {
      if (c.contains(a)) {
        n.addAll(c);
        toRemove.add(c);
      }
      if (c.contains(b)) {
        n.addAll(c);
        toRemove.add(c);
      }
    }
    connected.removeAll(toRemove);
    connected.add(n);
  }

  private void populateHasDirections() {
    RoomI r;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        r = rooms[i][j];
        for (Edge e : savedEdges) {
          if (e.contains(r)) {
            r.close(e.directionOfWall(r, rows, cols));
          }
        }
      }
    }
  }

  private void createTunnels() {
    int doorCount;
    RoomI r;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        doorCount = 0;
        r = rooms[i][j];
        if (r.hasNorth()) {
          doorCount++;
        }
        if (r.hasSouth()) {
          doorCount++;
        }
        if (r.hasEast()) {
          doorCount++;
        }
        if (r.hasWest()) {
          doorCount++;
        }
        if (doorCount == 2) {
          rooms[i][j] = new Tunnel(i, j, r.hasNorth(), r.hasSouth(), r.hasEast(), r.hasWest());
        }
      }
    }
  }

  private void pointDirections() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (i == 0) {
          rooms[i][j].setN(rooms[rows - 1][j]);
        } else {
          rooms[i][j].setN(rooms[i - 1][j]);
        }

        if (i == rows - 1) {
          rooms[i][j].setS(rooms[0][j]);
        } else {
          rooms[i][j].setS(rooms[i + 1][j]);
        }

        if (j == 0) {
          rooms[i][j].setW(rooms[i][cols - 1]);
        } else {
          rooms[i][j].setW(rooms[i][j - 1]);
        }

        if (j == cols - 1) {
          rooms[i][j].setE(rooms[i][0]);
        } else {
          rooms[i][j].setE(rooms[i][j + 1]);
        }
      }
    }
  }

  private void numberRooms() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (!rooms[i][j].isTunnel()) {
          nRooms.add(rooms[i][j]);
        }
      }
    }
  }

  private RoomI randomOpenRoom() {
    RoomI p = nRooms.get(rand.nextInt(nRooms.size()));
    while (p.hasWumpus() || p.hasBat() || p.hasPit()) {
      p = nRooms.get(rand.nextInt(nRooms.size()));
    }
    return p;
  }

  private void placePlayers() {
    for (int i = 0; i < numPlayers; i++) {
      RoomI p = randomOpenRoom();
      players.add(new Player(p.getX(), p.getY(), numArrows, "Player " + (i + 1)));
      playerLocation(players.get(i)).setVisited();
    }
  }

  /**
   * Used by bats to relocate Player.
   *
   * @param x new x
   * @param y new y
   */
  private void placePlayer(PlayerI p, int x, int y) {
    p.setX(x);
    p.setY(y);
    playerLocation(p).setVisited();
  }

  private void placeWumpus() {
    RoomI w = nRooms.get(rand.nextInt(nRooms.size()));
    w.setHasWumpus();
    placeWumpusScent(w);
  }

  private void placeWumpusScent(RoomI w) {
    if (w.hasNorth()) {
      w.getN().setHasWumpusScent(Move.NORTH);
    }
    if (w.hasSouth()) {
      w.getS().setHasWumpusScent(Move.SOUTH);
    }
    if (w.hasEast()) {
      w.getE().setHasWumpusScent(Move.EAST);
    }
    if (w.hasWest()) {
      w.getW().setHasWumpusScent(Move.WEST);
    }
  }

  private void placeBats() {
    if (percentBats < 0 || percentBats > 100) {
      throw new IllegalArgumentException("The percentage of bats must be within 0-100");
    }

    int numBats = nRooms.size() * percentBats / 100;
    int ind;
    ArrayList<Integer> roomNumsWithBats = new ArrayList<Integer>();
    while (roomNumsWithBats.size() < numBats) {
      ind = rand.nextInt(nRooms.size());
      if (roomNumsWithBats.contains(ind)) {
        continue;
      }
      nRooms.get(ind).setHasBat();
      roomNumsWithBats.add(ind);
    }
  }

  private void placePits() {
    if (percentPits < 0 || percentPits > 100) {
      throw new IllegalArgumentException("The percentage of pits must be within 0-100");
    }

    int numPits = nRooms.size() * percentPits / 100;
    int ind;
    ArrayList<Integer> roomNumsWithPits = new ArrayList<Integer>();
    while (roomNumsWithPits.size() < numPits) {
      ind = rand.nextInt(nRooms.size());
      if (roomNumsWithPits.contains(ind)) {
        continue;
      }
      nRooms.get(ind).setHasPit();
      placePitBreeze(nRooms.get(ind));
      roomNumsWithPits.add(ind);
    }
  }

  private void placePitBreeze(RoomI r) {
    if (r.hasNorth()) {
      r.getN().setHasPitBreeze(Move.NORTH);
    }
    if (r.hasSouth()) {
      r.getS().setHasPitBreeze(Move.SOUTH);
    }
    if (r.hasEast()) {
      r.getE().setHasPitBreeze(Move.EAST);
    }
    if (r.hasWest()) {
      r.getW().setHasPitBreeze(Move.WEST);
    }
  }

  /**
   * Sets seed for testing.
   *
   * @param seed seed
   */
  @Override
  public void setSeed(int seed) {
    rand.setSeed(seed);
  }

  // Getters

  @Override
  public State getState() {
    return state;
  }

  @Override
  public RoomI[][] getRooms() {
    return rooms;
  }

  @Override
  public List<PlayerI> getPlayers() {
    return players;
  }

  @Override
  public RoomI playerLocation(PlayerI p) {
    return rooms[p.getX()][p.getY()];
  }


  private Move moveFromInput(String line) {
    if (line.equals("w") || line.equals("W")) {
      return Move.NORTH;
    } else if (line.equals("a") || line.equals("A")) {
      return Move.WEST;
    } else if (line.equals("s") || line.equals("S")) {
      return Move.SOUTH;
    } else if (line.equals("d") || line.equals("D")) {
      return Move.EAST;
    } else {
      throw new IllegalArgumentException("Invalid input.");
    }
  }

  /**
   * Initially receives input and determains whether move or arrow.
   *
   * @param line input
   * @return output
   */
  @Override
  public String inputMove(String line) {
    if (line.charAt(0) == 'b' || line.charAt(0) == 'B') {
      return processArrow(players.get(0), line);
    }
    return processMove(players.get(0), moveFromInput(line));
  }

  /**
   * Process arrow from GUI.
   *
   * @param p             player
   * @param d             move
   * @param arrowDistance distance
   * @return
   */
  @Override
  public String guiProcessArrow(PlayerI p, Move d, int arrowDistance) {
    p.shootArrow();
    String arrowResult = playerLocation(p).processArrow(d, arrowDistance, state);
    if (arrowResult.equals(ARROW_HIT)) {
      state = State.WIN;
    } else if (p.getArrows() == 0) {
      p.setDead();
      arrowResult += p.getName() + "ran out of arrows!";
    }
    return arrowResult;
  }

  /**
   * Processes arrow input. Alters game state if necessary and returns feedback output.
   *
   * @param line input
   * @return output
   */
  @Override
  public String processArrow(PlayerI p, String line) {
    if (line.length() < 5) {
      throw new IllegalArgumentException("Arrow input needs direction (w/a/s/d) & distance (#).");
    }
    String dir = String.valueOf(line.charAt(2));
    Move d = moveFromInput(dir);
    int arrowDistance = Integer.parseInt(line.split("\\s+")[2]);
    if (arrowDistance < 1) {
      throw new IllegalArgumentException("Arrow distance must be > 0");
    }
    players.get(0).shootArrow();
    String arrowResult = playerLocation(p).processArrow(d, arrowDistance, state);
    if (arrowResult.equals(ARROW_HIT)) {
      state = State.WIN;
    } else if (players.get(0).getArrows() == 0) {
      state = State.LOSE;
      arrowResult += "\nYou've run out of arrows!";
    }
    return arrowResult;
  }

  /**
   * Processes player's movement.
   *
   * @param d move
   * @return output
   */
  @Override
  public String processMove(PlayerI p, Move d) {
    StringBuilder sb = new StringBuilder();
    movePlayer(p, d);
    RoomI nextRoom = playerLocation(p);
    if (nextRoom.isTunnel()) {
      processMove(p, ((Tunnel) nextRoom).otherSide(d));
    }
    processMoveHelper(p, sb);
    return sb.toString();
  }

  private String processMoveHelper(PlayerI p, StringBuilder sb) {
    checkForPitBreeze(p, sb);
    checkForWumpusScent(p, sb);
    checkForBats(p, sb);
    checkForWumpus(p, sb);
    checkForPit(p, sb);
    return sb.toString();
  }

  private void movePlayer(PlayerI p, Move d) {
    if (impossibleMoves(playerLocation(p)).contains(d)) {
      throw new IllegalArgumentException("You cannot move in this direction.");
    }

    if (d == Move.WEST) {
      if (p.getY() == 0) {
        p.setY(cols - 1);
      } else {
        p.decY();
      }

    } else if (d == Move.EAST) {
      if (p.getY() == cols - 1) {
        p.setY(0);
      } else {
        p.incY();
      }

    } else if (d == Move.SOUTH) {
      if (p.getX() == rows - 1) {
        p.setX(0);
      } else {
        p.incX();
      }

    } else if (d == Move.NORTH) {
      if (p.getX() == 0) {
        p.setX(rows - 1);
      } else {
        p.decX();
      }
    }
    playerLocation(p).setVisited();
  }

  private void checkForWumpus(PlayerI p, StringBuilder sb) {
    if (playerLocation(p).hasWumpus()) {
      p.setDead();
      sb.append("\n" + p.getName() + " ran into the room with the Wumpus👹 It ate them!💀");
    }
  }

  private void checkForWumpusScent(PlayerI p, StringBuilder sb) {
    if (playerLocation(p).hasWumpusScent()) {
      sb.append("\nYou smell the fowl stench of the Wumpus🤢 "
              + "be careful⚠️ try shooting an arrow!🏹");
    }
  }

  private void checkForBats(PlayerI p, StringBuilder sb) {
    if (playerLocation(p).hasBat()) {
      sb.append("\nYou encountered Super Bats 🦇🦇");
      if (rand.nextBoolean()) {
        RoomI nextRoom = nRooms.get(rand.nextInt(nRooms.size()));
        placePlayer(p, nextRoom.getX(), nextRoom.getY());
        sb.append("\nThey Grabbed you!😧🦇💨");
        processMoveHelper(p, sb);
      } else {
        sb.append("\nYou ducked them!🦇  🤸");
      }
    }
  }

  private void checkForPit(PlayerI p, StringBuilder sb) {
    if (playerLocation(p).hasPit()) {
      p.setDead();
      sb.append("\n" + p.getName() + " fell to their death in a pit 🕳💀");
    }
  }

  private void checkForPitBreeze(PlayerI p, StringBuilder sb) {
    if (playerLocation(p).hasPitBreeze()) {
      sb.append("\nYou can feel a breeze indicating a bottomless pit nearby, be careful!💨⚠️");
    }
  }

  /*
   * Generates impossible moves based on current location.
   *
   * @param r room
   * @return impossible move list

  @Override
  public ArrayList<Move> impossibleMoves(Cell r) {
    ArrayList<Move> walledOffDirections = new ArrayList<Move>();

    if (!wrapping) {
      if (r.getX() == 0) {
        walledOffDirections.add(Move.NORTH); //North
      } else if (r.getX() == rows - 1) {
        walledOffDirections.add(Move.SOUTH); //South
      }
      if (r.getY() == 0) {
        walledOffDirections.add(Move.WEST); //West
      } else if (r.getY() == cols - 1) {
        walledOffDirections.add(Move.EAST); //East
      }
    }

    for (Edge e : savedEdges) {
      if (e.contains(r)) {
        walledOffDirections.add(e.directionOfWall(r, rows, cols));
      }
    }
    return walledOffDirections;
  }

   */

  /**
   * Generates impossible moves based on current location.
   *
   * @param r room
   * @return invalid moves
   */
  @Override
  public ArrayList<Move> impossibleMoves(Cell r) {
    ArrayList<Move> walledOffDirections = new ArrayList<Move>();
    RoomI room = (Room) r;
    if (!room.hasNorth()) {
      walledOffDirections.add(Move.NORTH);
    }
    if (!room.hasSouth()) {
      walledOffDirections.add(Move.SOUTH);
    }
    if (!room.hasEast()) {
      walledOffDirections.add(Move.EAST);
    }
    if (!room.hasWest()) {
      walledOffDirections.add(Move.WEST);
    }
    return walledOffDirections;
  }

  /**
   * Returns listed possible moves.
   *
   * @param walledOffDirections list of walledOffDirections
   * @return possible moves
   */
  @Override
  public ArrayList<Move> possibleMoves(ArrayList<Move> walledOffDirections) {
    ArrayList<Move> moves = new ArrayList<Move>();
    if (!walledOffDirections.contains(Move.NORTH)) {
      moves.add(Move.NORTH);
    }
    if (!walledOffDirections.contains(Move.SOUTH)) {
      moves.add(Move.SOUTH);
    }
    if (!walledOffDirections.contains(Move.EAST)) {
      moves.add(Move.EAST);
    }
    if (!walledOffDirections.contains(Move.WEST)) {
      moves.add(Move.WEST);
    }
    return moves;
  }

  /**
   * Returns listed possible moves.
   *
   * @param walledOffDirections list of walledOffDirections
   * @return possible moves
   */
  @Override
  public String possibleMoveString(ArrayList<Move> walledOffDirections) {
    StringBuilder str = new StringBuilder();
    str.append("Possible Options:\n");
    if (!walledOffDirections.contains(Move.NORTH)) {
      str.append("North ⬆️ (w) ");
    }
    if (!walledOffDirections.contains(Move.SOUTH)) {
      str.append("South ⬇️ (s) ");
    }
    if (!walledOffDirections.contains(Move.EAST)) {
      str.append("East ➡️ (d) ");
    }
    if (!walledOffDirections.contains(Move.WEST)) {
      str.append("West ⬅️ (a) ");
    }
    str.append("Shoot arrow 🏹 (b *direction* *arrowdistance*)");
    return str.toString();
  }

  /**
   * Reports status of player.
   *
   * @return status
   */
  @Override
  public String playerStatus(PlayerI p) {
    return "\nCurrent Location: Room [" + players.get(0).getX() + "][" + players.get(0).getY()
            + "]\tArrows🏹: "
            + players.get(0).getArrows()
            + "\n" + possibleMoveString(impossibleMoves(playerLocation(p)))
            + "\n" + (visual ? TextView.render(rooms, players.get(0)) : "");
  }

  @Override
  public void checkGameOver() {
    for (PlayerI p : players) {
      if (p.isAlive()) {
        return;
      }
    }
    state = State.LOSE;
  }

}
