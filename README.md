# README #

### What is this repository for? ###

* Welcome to JasMud (Working Title) a Java based MUD (multi-user dungeon) that lays emphasis on PVP,
	user interaction with the economy and each other.

### How do I get set up? ###

* To experience the game: Reach out to Jason (see contacts below) OR grab the code.
* If you just want a copy see below. If you want to contribute contact Jason for write access.

* Set Up: For Mercurial repository work I really like TortoiseHG @ https://tortoisehg.bitbucket.io/
* For cloning Mercurial repositories: https://confluence.atlassian.com/bitbucket/clone-a-repository-223217891.html
* Configure dependancies: Xstream, Mockito, and JUnit which are included in source.
* Database: Controlled via XStream data files. Currently we recommend running a fresh build without a save (will load defaults).
* Some tests can be ran via JUnit in "AllTests" (I'm sorry it is fairly limited).
* Once configured deploy by running "WorldServer", be aware of your local IP and the port in WorldServer (usually 2587)
* Connect via Telnet or a MUD client (we like Mudlet: https://www.mudlet.org/)
* For Telnet: open [Your IP] 2587
* Mud Client: Connect to you IP on the given port (2587).

### Getting Started on the Code ###
* WorldServer is the Main. Controls Socket connections to the game.
* WorldServer creates PlayerPrompts which is the main loop a player lives within.
* Otherwise: See Processes for most of the driving code and other folders for Game Implementation code (skills, items etc).

### Contribution guidelines ###

* This is a fairly personal project and is not generally open to contribution. However, that does not mean
	we will ignore you if you reach out to us with interest in the project.
* Additionally, Location design is an exception and would gladly accept any support.

### Who do I talk to? ###

* Jason Reed AKA Capaal: Lead Admin reachable @ jasreed.t@gmail.com