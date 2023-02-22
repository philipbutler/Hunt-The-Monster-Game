# Hunt The Monster (1-3 Players)

## Gameplay Screenshot
![gameplay](https://github.com/philipbutler/Hunt-The-Monster-Game/blob/main/screenshots/win.png)

## How to Run
- You'll need Java (11 or later) installed. You can grab that [here](https://www.oracle.com/java/technologies/downloads/#jdk19-mac)
- In your command line, cd (navigate) to .jar file location:
```sh
cd res
```

#### GUI: The real way to play
```sh
java -jar play.jar --gui
```
- Change any settings and hit "Generate" to start the game. (Try 3 Players!)
- Attempt to find and kill the wumpus, while avoiding death
- Go back to the menu to modify settings or restart the same maze at anytime

#### Text: Game logic only, or to use your imagination
```sh
java -jar HW6Swing.jar --text 5 5 true false 30 20 20 10 0
```
where you can substitute the values after --text for: rows(int) cols(int) wrapping(true/false) perfect(true/false) wallsRemaining(int) numArrows(int) seed(int)

## Controls

| Move  |      Player 1 (Red)       |           Player 2 (Green) | Player 3 (Blue)                 |
|:------|:-------------------------:|---------------------------:|---------------------------------|
| Up    |             W             |               Up Arrow Key | Click Adjacent Cell to move     |
| Down  |             A             |             Down Arrow Key |                                 |
| Left  |             S             |             Left Arrow Key |                                 |
| Right |             D             |            Right Arrow Key |                                 |
| Shoot | Q (Hold, press direction) | /? (Hold, press direction) | Spacebar (Hold, Click Location) |

### View gameplay pictures in `Hunt-The-Monster-Game/screenshots`
## Resources
- [Zelda Sprites](https://zelda.gamepedia.com/Category:Four_Swords_Sprites)
- [Swing GridLayout](https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html)
- [Swing CardLayout](https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html)
- [Swing Spinners](http://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html)
- [Swing Split Panes](https://docs.oracle.com/javase/tutorial/uiswing/components/splitpane.html)
- [SO CardLayout](https://stackoverflow.com/questions/20922913/how-to-make-swing-windows-switch-between-content/20922950)
- [SO Key Events](https://stackoverflow.com/questions/8961938/java-keylistener-not-registering-arrow-keys)
- [SO Read JSlider](https://stackoverflow.com/questions/16586867/read-the-value-of-a-jslider)
- [SO KeyListener](https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx)
- [SO Get Spinner Value](https://stackoverflow.com/questions/15400781/how-to-get-int-value-from-spinner)
- [SO Image to JPanel](https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel)
- [SO Image not showing](https://stackoverflow.com/questions/50456806/jlayeredpane-image-not-showing-up)
- [SO Fit JFrame to CardLayout](https://stackoverflow.com/questions/8277834/how-to-set-a-jframe-size-to-fit-the-cardlayout-displayed-jpanel/8279991#8279991)
- [SO Remove Space in GridLayout](https://stackoverflow.com/questions/31259373/how-to-remove-space-between-imageicons-in-gridlayout-java)
- [Image Rotation](https://blog.idrsolutions.com/2019/05/image-rotation-in-java/)
- [Change JLabel Font](https://www.tutorialspoint.com/how-to-change-jlabel-font-in-java)
