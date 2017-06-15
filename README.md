# Java TETRIS GAME

![TetrisShot](https://stalk-calvin.github.io/img/tetris.gif)

[![CI](https://api.travis-ci.org/stalk-calvin/tetris-game.svg?branch=master)](https://travis-ci.org/stalk-calvin/tetris-game)
[![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

Algorithms gathered from other websites and sound files are gathered from web as well.

## Game Controls

↑ - Up

↓ - Down (faster down)

← - Left

→ - Right

p - Pause game

r - Change rotation (left/right)

space - Drop the block down

x - Restart Game

## Hidden sound

| Points over | Makes Sound        |
|-------------|--------------------|
| 500         | Holy Alphabet      |
| 1000        | Holy Caffeine      |
| 3000        | Holy Fruit Salad   |
| 5000        | Holy Heart Failure |
| 10000       | Holy Mashed Potato |
| 20000       | Holy Nightmare     |
| 30000       | Bitchin            |

## Scoring

| Action           | Awarded points     |
|------------------|--------------------|
| Down (Soft drop) | 1                  |
| Drop             | rows * 2           |
| 1 Row cleared    | 100                |
| 2 Row cleared    | 300                |
| 3 Row cleared    | 600                |
| 4 Row cleared    | 900                |

## Leveling

Levels up every minute regardless of scores (excluding paused time). 

600ms is initial speed and every level gets faster by 50ms. 

## Contributors

Calvin Lee, @stalk.calvin

<a href="https://www.instagram.com/stalk.calvin/"><img alt="Add me to Instagram" src="http://icons.iconarchive.com/icons/uiconstock/socialmedia/128/Instagram-icon.png" height="20px" width="20px"/></a> <span><a href="https://www.instagram.com/stalk.calvin/">Instagram</a></span>
<br/>
<a href="https://www.linkedin.com/in/stalkme"><img alt="Add me to Linkedin" src="https://image.freepik.com/free-icon/linkedin-logo_318-50643.jpg" height="20px" width="20px"/></a> <span><a href="https://www.linkedin.com/in/stalkme">LinkedIn</a></span>

## Ways to play the game:

1. clone repo: https://github.com/stalk-calvin/tetris-game.git
2. cd tetris-game
3. run: `mvn install exec:java -Dexec.mainClass="tetris.Main"`
