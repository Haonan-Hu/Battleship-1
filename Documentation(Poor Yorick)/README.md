
# Battleship

This is a two player game of battleship built in Java.

## Rules
-   Board size
    -   8x8
    -   The columns are denoted by letters (A-H)
    -   The rows are denoted by number (1-8)
-   Number of ships (per player)
    -   Given by user
    -   Minimum of 1 and a maximum of 5
-   Types of ships
    -   This will be based on the amount of ships chosen.
    -   If a total of 1 ship is chosen, then each player gets a single 1x1 ship
    -   If a total of 2 ships is chosen, then each player gets a 1x1 and a 1x2 ship
    -   This continues up to 5, where each player will a 1x1, 1x2, 1x3, 1x4, and a 1x5
-   Ship placement
    -   After the amount of ships is chosen, players need to be able to secretly orient and place the ships on their board.

#### Playing the Game

Taking turns, the user's pick a space on the opponent's board to "fire" at. They must then be informed if the shot was a "hit" or a "miss".

The player's view should be updated to reflect this (see Player's view).

After each shot, it is the other players turn.

#### Destroying a ship

Once a ship has been hit in every space it occupies, it is sunk. For example, if the 1x3 ship occupies the space B3, B4, and B5. Once the opponent has shot those three spaces, that ship is sunk.

#### Player's view

A player should have full view of (1) their board and where their ships are placed (show how many times each ship has been hit) and (2) a board to track all shots they've fired and whether they were misses or hits.

#### Game End

Once a player has sunk all of the opponent's ships, they immediately win.

## Code Standards
**Class names should be capitalized.** 
``` java
public class Player(){
	...
}
```
**For function declaration curly braces go same line**
``` java
if(...){
	...
}
```
**Function names are camel case.**
``` java
public void run(){
	...
}
```
or 
``` java
public String getName(){
	...
}
```
**For nested for loops use the variables row and col**
``` java
for(int row = 0; row < 10; row++){
	for(int col= 0; col < 10; col++){
		...
	}
}
```
## Authors
* Max Goad
* Jace Bayless
* Tri Pham
* Apurva Rai
* Meet Kapadia

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
