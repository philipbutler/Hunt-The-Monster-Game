package htw;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Controller for the GUI. Controller passes grid of images to view.
 */
public class GUIController implements Features {

  private IModel m;
  private IView v;
  private int rows;
  private int cols;
  private Map<String, BufferedImage> images;
  private ImagePanel[][] cellImgs;
  private Random rand;
  private int seed;
  private String msg;

  public GUIController() {
    rand = new Random();
  }

  private void initImages() {
    images = new HashMap<String, BufferedImage>();
    try {
      BufferedImage bat =
              images.put("bat",
                      ImageIO.read(this.getClass().getResourceAsStream("/bat.png")));
      images.put("hallCurved",
              ImageIO.read(this.getClass().getResourceAsStream("/hallCurved.png")));
      images.put("hallStraight",
              ImageIO.read(this.getClass().getResourceAsStream("/hallStraight.png")));
      images.put("room1",
              ImageIO.read(this.getClass().getResourceAsStream("/room1.png")));
      images.put("room3",
              ImageIO.read(this.getClass().getResourceAsStream("/room3.png")));
      images.put("room4",
              ImageIO.read(this.getClass().getResourceAsStream("/room4.png")));
      images.put("pit",
              ImageIO.read(this.getClass().getResourceAsStream("/pit.png")));
      images.put("breeze",
              ImageIO.read(this.getClass().getResourceAsStream("/breeze.png")));
      images.put("stench",
              ImageIO.read(this.getClass().getResourceAsStream("/stench.png")));
      images.put("wumpus",
              ImageIO.read(this.getClass().getResourceAsStream("/wumpus.png")));
      images.put("Player 1",
              ImageIO.read(this.getClass().getResourceAsStream("/player1.png")));
      images.put("Player 2",
              ImageIO.read(this.getClass().getResourceAsStream("/player2.png")));
      images.put("Player 3",
              ImageIO.read(this.getClass().getResourceAsStream("/player3.png")));
      images.put("Player 4",
              ImageIO.read(this.getClass().getResourceAsStream("/player4.png")));
      images.put("bow", ImageIO.read(this.getClass().getResourceAsStream("/bow.png")));

    } catch (IOException e) {
      e.printStackTrace();
    }
    cellImgs = new ImagePanel[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        cellImgs[i][j] = new ImagePanel();
      }
    }
  }

  @Override
  public void setView(IView v) {
    this.v = v;
    v.setFeatures(this);
  }

  @Override
  public void generateModel(int numPlayers, int rows, int cols, boolean wrapping, boolean perfect,
                            int wallsRemaining, int percentBats, int percentPits, int numArrows,
                            boolean newMaze) {
    if (newMaze) {
      seed = rand.nextInt(1000);
    }
    m = new Model(numPlayers, rows, cols, wrapping, perfect, wallsRemaining, percentBats,
            percentPits, numArrows, seed);
    this.rows = rows;
    this.cols = cols;
    initImages();
  }

  @Override
  public void moveNorth(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      if (p.isShooting()) {
        shootArrowNorth(p);
      } else {
        msg = m.processMove(m.getPlayers().get(p.getIndex()), IModel.Move.NORTH);
        push();
      }
    }
  }

  @Override
  public void moveWest(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      if (p.isShooting()) {
        shootArrowWest(p);
      } else {
        msg = m.processMove(m.getPlayers().get(p.getIndex()), IModel.Move.WEST);
        push();
      }
    }
  }

  @Override
  public void moveSouth(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      if (p.isShooting()) {
        shootArrowSouth(p);
      } else {
        msg = m.processMove(m.getPlayers().get(p.getIndex()), IModel.Move.SOUTH);
        push();
      }
    }
  }

  @Override
  public void moveEast(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      if (p.isShooting()) {
        shootArrowEast(p);
      } else {
        msg = m.processMove(m.getPlayers().get(p.getIndex()), IModel.Move.EAST);
        push();
      }
    }
  }

  @Override
  public void shootArrowNorth(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      msg = m.guiProcessArrow(m.getPlayers().get(p.getIndex()), IModel.Move.NORTH, 1);
      push();
    }
  }

  @Override
  public void shootArrowSouth(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      msg = m.guiProcessArrow(m.getPlayers().get(p.getIndex()), IModel.Move.SOUTH, 1);
      push();
    }
  }

  @Override
  public void shootArrowEast(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      msg = m.guiProcessArrow(m.getPlayers().get(p.getIndex()), IModel.Move.EAST, 1);
      push();
    }
  }

  @Override
  public void shootArrowWest(JFrameView.PlayerMoving p) {
    if (m.getPlayers().get(p.getIndex()).isAlive()) {
      msg = m.guiProcessArrow(m.getPlayers().get(p.getIndex()), IModel.Move.WEST, 1);
      push();
    }
  }


  @Override
  public void processCellClick(int i, int j) {
    if (m.playerLocation(m.getPlayers().get(2)).getN() == m.getRooms()[i][j]) {
      moveNorth(JFrameView.PlayerMoving.PLAYER3);
    } else if (m.playerLocation(m.getPlayers().get(2)).getW() == m.getRooms()[i][j]) {
      moveWest(JFrameView.PlayerMoving.PLAYER3);
    } else if (m.playerLocation(m.getPlayers().get(2)).getS() == m.getRooms()[i][j]) {
      moveSouth(JFrameView.PlayerMoving.PLAYER3);
    } else if (m.playerLocation(m.getPlayers().get(2)).getE() == m.getRooms()[i][j]) {
      moveEast(JFrameView.PlayerMoving.PLAYER3);
    }
  }

  /**
   * Pushes state to view.
   */
  public void push() {
    Cell[][] rooms = m.getRooms();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        ImagePanel c = cellImgs[i][j];
        Room r = (Room) rooms[i][j];
        if (r.isVisited()) {
          if ((r.hasNorth() ? 1 : 0) + (r.hasSouth() ? 1 : 0)
                  + (r.hasEast() ? 1 : 0) + (r.hasWest() ? 1 : 0) == 1) {
            if (r.hasEast()) {
              c.addImage(images.get("room1"));
            } else if (r.hasSouth()) {
              c.addImage(rotateImage(images.get("room1"), 1));
            } else if (r.hasWest()) {
              c.addImage(rotateImage(images.get("room1"), 2));
            } else if (r.hasNorth()) {
              c.addImage(rotateImage(images.get("room1"), 3));
            }
          } else if ((r.hasNorth() ? 1 : 0) + (r.hasSouth() ? 1 : 0)
                  + (r.hasEast() ? 1 : 0) + (r.hasWest() ? 1 : 0) == 2) {
            if (r.hasEast() && r.hasWest()) {
              c.addImage(images.get("hallStraight"));
            } else if (r.hasSouth() && r.hasNorth()) {
              c.addImage(rotateImage(images.get("hallStraight"), 1));
            } else if (r.hasEast() && r.hasSouth()) {
              c.addImage(images.get("hallCurved"));
            } else if (r.hasSouth() && r.hasWest()) {
              c.addImage(rotateImage(images.get("hallCurved"), 1));
            } else if (r.hasNorth() && r.hasWest()) {
              c.addImage(rotateImage(images.get("hallCurved"), 2));
            } else if (r.hasNorth() && r.hasEast()) {
              c.addImage(rotateImage(images.get("hallCurved"), 3));
            }
          } else if ((r.hasNorth() ? 1 : 0) + (r.hasSouth() ? 1 : 0)
                  + (r.hasEast() ? 1 : 0) + (r.hasWest() ? 1 : 0) == 3) {
            if (!r.hasEast()) {
              c.addImage(rotateImage(images.get("room3"), 1));
            } else if (!r.hasSouth()) {
              c.addImage(rotateImage(images.get("room3"), 2));
            } else if (!r.hasWest()) {
              c.addImage(rotateImage(images.get("room3"), 3));
            } else if (!r.hasNorth()) {
              c.addImage(images.get("room3"));
            }
          } else if ((r.hasNorth() ? 1 : 0) + (r.hasSouth() ? 1 : 0)
                  + (r.hasEast() ? 1 : 0) + (r.hasWest() ? 1 : 0) == 4) {
            c.addImage(images.get("room4"));
          } else {
            //something's wrong
          }

          if (r.hasBat()) {
            c.addImage(images.get("bat"));
          }
          if (r.hasPit()) {
            c.addImage(images.get("pit"));
          }
          if (r.hasPitBreeze()) {
            c.addImage(images.get("breeze"));
          }
          if (r.hasWumpusScent()) {
            c.addImage(images.get("stench"));
          }
          if (r.hasWumpus()) {
            c.addImage(images.get("wumpus"));
          }
          for (PlayerI p : m.getPlayers()) {
            if (r.sameLocation(p) && p.isAlive()) {
              c.addImage(images.get(p.getName()));
            }
          }
        }
      }
    }
    checkGameState();
    v.setCells(cellImgs);
    v.setOutput(msg);
  }

  private void checkGameState() {
    m.checkGameOver();
    if (m.getState() == IModel.State.LOSE) {
      v.lose();
    } else if (m.getState() == IModel.State.WIN) {
      v.win();
    }
  }

  private BufferedImage rotateImage(BufferedImage image, int quarterTurns) {
    final double rads = Math.toRadians(90 * quarterTurns);
    final double sin = Math.abs(Math.sin(rads));
    final double cos = Math.abs(Math.cos(rads));
    final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
    final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
    final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
    final AffineTransform at = new AffineTransform();
    at.translate(w / 2, h / 2);
    at.rotate(rads, 0, 0);
    at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
    final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    rotateOp.filter(image, rotatedImage);
    return rotatedImage;
  }
}