package htw;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for MazeModel. The state of the game is stored in MazeModel, only functions of the
 * model or functions of classes within it can alter its state.
 */
public interface IModel {

  /**
   * Gives player's status.
   *
   * @param p player
   * @return status
   */
  String playerStatus(PlayerI p);

  void checkGameOver();

  /**
   * Game State is either in_progress, win, or lose.
   */
  enum State { IN_PROGRESS, WIN, LOSE }

  /**
   * Each cardinal direction is a Move.
   */
  enum Move { NORTH, SOUTH, EAST, WEST }

  String ARROW_HIT = "The arrow killed the Wumpus!";
  String ARROW_MISS = "The arrow missed...";
  String ARROW_WALLED = "The arrow hit a wall.";

  /**
   * Gets game state.
   *
   * @return state
   */
  State getState();

  /**
   * Sets seed for testing.
   *
   * @param seed seed
   */
  void setSeed(int seed);

  /**
   * Grid used for displaying in view.
   *
   * @return rooms
   */
  Cell[][] getRooms();

  /**
   * Gets players.
   *
   * @return players
   */
  List<PlayerI> getPlayers();

  /**
   * Gives room player's in.
   *
   * @return room
   */
  RoomI playerLocation(PlayerI p);

  String inputMove(String line);

  String guiProcessArrow(PlayerI p, Move d, int arrowDistance);

  String processArrow(PlayerI p, String line);

  /**
   * Does all the processing of a single move, returns aggregated feedback as a string.
   *
   * @param d move
   * @return output
   */
  String processMove(PlayerI p, Move d);

  /**
   * Impossible moves.
   *
   * @param r room
   * @return impossible moves
   */
  ArrayList<Move> impossibleMoves(Cell r);

  ArrayList<Move> possibleMoves(ArrayList<Move> walledOffDirections);

  /**
   * Displays possible moves. Used to refresh StringBuilder use: https://www.geeksforgeeks.org/stringbuilder-class-in-java-with-examples/
   *
   * @param walledOffDirections list of walledOffDirections
   * @return moves
   */
  String possibleMoveString(ArrayList<Move> walledOffDirections);
}
