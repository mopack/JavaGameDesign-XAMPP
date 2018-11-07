# "Super Monster Smash" Game Project

## Origin

A couples of friends of mine and I decided to make an online webpage card game in a summer vacation.  

Most of them are from 淡江資傳系(Tamkang University, Department of Information and Communication), and they are good at 2D drawing.  

The original plan was using Java + XAMPP + 2D drawing.

* Java Applet: Core game design. (with Eclipse IDE)

* XAMPP: Support a well website to play the game, and MySQL can be the database of players' accounts.

* 2D drawing: Includes character cards, event cards, user interface, animation motions, etc.

## Game Rules

* Pick a character you like, each character has two abilities and an amount of HP. Then 6 players will seperate into 3 groups (called group A/B/C) unknowingly. If there aren't enough players online, computer players will play instead. After this stage, you will know what group you are in secretly.

* Six players seat beside a round table. Everyone will get a event card at the beginning of each round. Then every player can make there decision when it's his or her turn. It start from a player, then goes counterclockwise until every player has played his or her turn.

* When it's your turn, you can use your character's abilities, using event cards in your hands, summon the Adjudicator or doing nothing. 

* There are 10 kinds of event cards, they all have a unique value, you can choose to skip some event cards to summon the Adjudicator (deal 2 damage to a target player). If you want to summon the Adjudicator, you have to discard event cards with total value greater or equal to 4. Other character's ability might have to discard event cards, too. 

* The winning condition is to guess which player is your partner. And together defeat other groups. The last player with HP more than one and his/her partner win the game.

## Game Project Execution

I wrote the Java Applet's code and setup XAMPP. Others tried to draw those 2d drawing.

We couldn't finish this big task. I made the choose character state, the playing card state in Java code. I also made it became a website, so everyone can play it.

But I have no time making the MySQL Database and the animation motions codes, there isn't multi-player mode, and I only done the first 2 character's abilities.

Others tried to draw 20 character cards, but no enough time for event cards, user interface, and animation motions.

Summer vacation went so fast. When school began, we decided to stop this project.

## Current Version

* Builds: You can just download Builds/game.jar to play this unfinished version.

* htdocs: If you put htdocs on a web space and open htdocs/index.php, you can play the game online.

* Source: All the Java source codes are in there, with some 2d card arts. We used lots of graphics from other games. Please let me know if you don't want them to be used there.
