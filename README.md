# Hunt The Monster (1-3 Players)

## How to Run
- You'll need Java 11 installed. You can grab that [here](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
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


## Asumptions, Design Changes, and Justifications
- I was unsure the best way to input the distance for each player, and thought of a way that involved creating crosshairs for each player like player 3 has, but for time's sake decided to only allow players to shoot a distance of 1. 
- The biggest change from the norm is that there is no turns in this version of the game, each player can move freely regardless of turns.
- I created the ImagePanes in the controller, which felt kind of wrong since it feels that images should only exist in the view, but they then get passed to the View to avoid the view from having access to any objects in the model

### View gameplay pictures in `Hunt-The-Monster-Game/screenshots`

## Resources
https://zelda.gamepedia.com/Category:Four_Swords_Sprites
Demo code from Module 11
https://piazza.com/class/keonixf87hw4bm?cid=694
https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
http://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html
https://docs.oracle.com/javase/tutorial/uiswing/components/splitpane.html
https://stackoverflow.com/questions/20922913/how-to-make-swing-windows-switch-between-content/20922950
https://stackoverflow.com/questions/8961938/java-keylistener-not-registering-arrow-keys
https://stackoverflow.com/questions/16586867/read-the-value-of-a-jslider
https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
https://stackoverflow.com/questions/15400781/how-to-get-int-value-from-spinner
https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
https://stackoverflow.com/questions/50456806/jlayeredpane-image-not-showing-up
https://blog.idrsolutions.com/2019/05/image-rotation-in-java/
https://stackoverflow.com/questions/8277834/how-to-set-a-jframe-size-to-fit-the-cardlayout-displayed-jpanel/8279991#8279991
https://stackoverflow.com/questions/31259373/how-to-remove-space-between-imageicons-in-gridlayout-java
https://www.tutorialspoint.com/how-to-change-jlabel-font-in-java
