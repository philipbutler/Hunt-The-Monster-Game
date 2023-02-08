package htw;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import java.awt.Component;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.border.Border;

/**
 * Implementation of the view.
 */
public class JFrameView extends JFrame implements IView {
  private static final long serialVersionUID = -2179965453492637485L;
  private GUIController c;
  private JSplitPane game;
  private JPanel cards;
  private JPanel gameGrid;
  private JPanel gameOutput;
  private CardLayout cl;
  private JLabel output;
  private JButton restartButton;
  private JButton menuButton;
  private JLabel winLabel;
  private JLabel loseLabel;
  private JComboBox players;
  private JCheckBox perfect;
  private JCheckBox wrapping;
  private JSlider percentBats;
  private JSlider percentPits;
  private JSpinner rowsSpinner;
  private JSpinner colsSpinner;
  private JSpinner wallsSpinner;
  private JTextField arrows;
  private JButton generateButton;
  private static final int BATS_MIN = 0;
  private static final int BATS_MAX = 90;
  private static final int BATS_INIT = 20;
  private static final int PITS_MIN = 0;
  private static final int PITS_MAX = 90;
  private static final int PITS_INIT = 20;
  private int walls;
  private int rows;
  private int cols;
  private int numPlayers;
  private BufferedImage targetImage;
  private ImagePanel[][] cells;

  /**
   * Represents which player is moving or shooting.
   */
  public enum PlayerMoving {
    PLAYER1(0),
    PLAYER2(1),
    PLAYER3(2);
    private final int index;
    private boolean shooting;

    PlayerMoving(int index) {
      this.index = index;
    }

    public int getIndex() {
      return index;
    }

    public boolean isShooting() {
      return shooting;
    }
  }

  /**
   * Public constructor.
   *
   * @param c controller
   */
  public JFrameView(GUIController c) {
    this.c = c;
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel settings = new JPanel();
    settings.setLayout(new GridLayout(11, 2));
    JLabel playersLabel = new JLabel("Players");
    settings.add(playersLabel);
    String[] playersOptions = new String[]{"1", "2", "3"};
    players = new JComboBox(playersOptions);
    players.setSelectedIndex(0);
    settings.add(players);
    JLabel rowsLabel = new JLabel("Rows");
    settings.add(rowsLabel);
    SpinnerNumberModel rowsSpinnerModel = new SpinnerNumberModel(9, 2, 50, 1);
    rowsSpinner = new JSpinner(rowsSpinnerModel);
    settings.add(rowsSpinner);
    JLabel colsLabel = new JLabel("Columns");
    settings.add(colsLabel);
    SpinnerNumberModel colsSpinnerModel = new SpinnerNumberModel(9, 2, 50, 1);
    colsSpinner = new JSpinner(colsSpinnerModel);
    settings.add(colsSpinner);
    JLabel wallsRemaining = new JLabel("Walls Remaining");
    settings.add(wallsRemaining);
    SpinnerNumberModel wallsSpinnerModel = new SpinnerNumberModel(20, 0, 1000, 1);
    wallsSpinner = new JSpinner(wallsSpinnerModel);
    settings.add(wallsSpinner);
    JLabel numArrows = new JLabel("Number of Arrows");
    settings.add(numArrows);
    arrows = new JTextField("10", 3);
    settings.add(arrows);
    JLabel batsLabel = new JLabel("Percent Bats");
    settings.add(batsLabel);
    percentBats = new JSlider(JSlider.HORIZONTAL, BATS_MIN, BATS_MAX, BATS_INIT);
    settings.add(percentBats);
    percentBats.setMajorTickSpacing(20);
    percentBats.setMinorTickSpacing(5);
    percentBats.setPaintTicks(true);
    percentBats.setPaintLabels(true);
    JLabel pitsLabel = new JLabel("Percent Pits");
    settings.add(pitsLabel);
    percentPits = new JSlider(JSlider.HORIZONTAL, PITS_MIN, PITS_MAX, PITS_INIT);
    percentPits.setMajorTickSpacing(20);
    percentPits.setMinorTickSpacing(5);
    percentPits.setPaintTicks(true);
    percentPits.setPaintLabels(true);
    settings.add(percentPits);
    JLabel perfectLabel = new JLabel("Perfect Maze");
    settings.add(perfectLabel);
    perfect = new JCheckBox();
    settings.add(perfect);
    JLabel wrappingLabel = new JLabel("Wrapping Maze");
    settings.add(wrappingLabel);
    wrapping = new JCheckBox();
    wrapping.setSelected(true);
    settings.add(wrapping);
    generateButton = new JButton("Generate");
    settings.add(generateButton);
    winLabel = new JLabel("THE WUMPUS IS SLAIN,\nYOU WIN!");
    winLabel.setForeground(Color.GREEN);
    winLabel.setFont(new Font("Arial", Font.BOLD, 30));
    loseLabel = new JLabel("EVERYONE DIED, YOU LOSE!");
    loseLabel.setForeground(Color.RED);
    loseLabel.setFont(new Font("Arial", Font.BOLD, 30));
    Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    settings.setBorder(padding);
    settings.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    try {
      targetImage = ImageIO.read(this.getClass().getResourceAsStream("/target.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    gameGrid = new JPanel(new GridLayout());
    final JScrollPane gameScroll = new JScrollPane(gameGrid);
    gameOutput = new JPanel();
    output = new JLabel("Output will go here");
    output.setAlignmentX(Component.CENTER_ALIGNMENT);
    gameOutput.add(output);
    menuButton = new JButton("Back to Menu");
    restartButton = new JButton("Restart With Same Settings");
    gameOutput.add(menuButton);
    gameOutput.add(restartButton);
    game = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gameScroll, gameOutput);
    game.setOneTouchExpandable(true);
    cards = new JPanel(new CardLayout());
    cards.add(settings, "settings");
    cards.add(game, "game");

    this.add(cards);
    this.pack();
    setResizable(true);
    setVisible(true);
  }

  @Override
  public void setCells(ImagePanel[][] cellImgs) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        cells[i][j] = cellImgs[i][j];
        gameGrid.add(cells[i][j]);
      }
    }
    this.repaint();
  }

  @Override
  public void setCellActions(Features f) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        int finalI = i;
        int finalJ = j;
        cells[i][j].addMouseListener(new MouseListener() {
          @Override
          public void mouseClicked(MouseEvent e) {
            // Method implemented for anonymous class
          }

          @Override
          public void mousePressed(MouseEvent e) {
            // Method implemented for anonymous class
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            f.processCellClick(finalI, finalJ);
          }

          @Override
          public void mouseEntered(MouseEvent e) {
            if (PlayerMoving.PLAYER3.shooting) {
              cells[finalI][finalJ].addImage(targetImage);
            }
          }

          @Override
          public void mouseExited(MouseEvent e) {
            cells[finalI][finalJ].removeImage(targetImage);
          }
        });
      }
    }
  }

  @Override
  public void setOutput(String msg) {
    output.setText(msg);
  }

  /**
   * Accept the set of callbacks from the controller, and hook up as needed to various things in
   * this view.
   *
   * @param f the set of feature callbacks as a Features object
   */
  @Override
  public void setFeatures(Features f) {

    menuButton.addActionListener(l -> {
      gameGrid.removeAll();
      cl.show(cards, "settings");
      this.pack();
      this.requestFocus();
    });

    restartButton.addActionListener(l -> {
      gameGrid.removeAll();
      f.generateModel(numPlayers, rows, cols, wrapping.isSelected(), perfect.isSelected(),
              walls, percentBats.getValue(), percentPits.getValue(),
              Integer.parseInt(arrows.getText()),  false);
      generateHelper(f);
    });

    generateButton.addActionListener(l -> {
      try {
        rowsSpinner.commitEdit();
        colsSpinner.commitEdit();
        wallsSpinner.commitEdit();
      } catch (java.text.ParseException e) {
        e.printStackTrace(); //Display that arguments are incorrect
      }
      numPlayers = Integer.parseInt((String) players.getSelectedItem());
      rows = (Integer) rowsSpinner.getValue();
      cols = (Integer) colsSpinner.getValue();
      walls = (Integer) wallsSpinner.getValue();
      f.generateModel(numPlayers, rows, cols, wrapping.isSelected(), perfect.isSelected(),
              walls, percentBats.getValue(), percentPits.getValue(),
              Integer.parseInt(arrows.getText()), true);
      generateHelper(f);
    });

    this.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        // Method implemented for anonymous class
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
          f.moveNorth(PlayerMoving.PLAYER1);
        } else if (e.getKeyChar() == 'a') {
          f.moveWest(PlayerMoving.PLAYER1);
        } else if (e.getKeyChar() == 's') {
          f.moveSouth(PlayerMoving.PLAYER1);
        } else if (e.getKeyChar() == 'd') {
          f.moveEast(PlayerMoving.PLAYER1);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
          f.moveNorth(PlayerMoving.PLAYER2);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
          f.moveWest(PlayerMoving.PLAYER2);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          f.moveSouth(PlayerMoving.PLAYER2);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
          f.moveEast(PlayerMoving.PLAYER2);
        } else if (e.getKeyChar() == ' ') {
          PlayerMoving.PLAYER3.shooting = true;
        } else if (e.getKeyChar() == 'q') {
          PlayerMoving.PLAYER1.shooting = true;
        } else if (e.getKeyChar() == '/') {
          PlayerMoving.PLAYER2.shooting = true;
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == ' ') {
          PlayerMoving.PLAYER3.shooting = false;
        } else if (e.getKeyChar() == 'q') {
          PlayerMoving.PLAYER1.shooting = false;
        } else if (e.getKeyChar() == '/') {
          PlayerMoving.PLAYER2.shooting = false;
        }
      }
    });
  }

  private void generateHelper(Features f) {
    output.setText("");
    clearState();
    cells = new ImagePanel[rows][cols];
    gameGrid.setLayout(new GridLayout(rows, cols));

    c.push();
    setCellActions(f);

    gameGrid.setPreferredSize(new Dimension(64 * cols, 64 * rows));
    //gameOutput.setPreferredSize(new Dimension(64 * cols, 300));
    //gameScroll.setPreferredSize(new Dimension(1000, 1000));
    game.setPreferredSize(new Dimension(64 * cols, 64 * rows + 300));
    game.setDividerLocation(64 * rows);
    cl = (CardLayout) cards.getLayout();
    cl.show(cards, "game");
    this.pack();
    this.requestFocus();
  }

  @Override
  public void win() {
    gameOutput.add(winLabel);
  }

  @Override
  public void lose() {
    gameOutput.add(loseLabel);
  }

  @Override
  public void clearState() {
    gameOutput.remove(winLabel);
    gameOutput.remove(loseLabel);
  }

}
