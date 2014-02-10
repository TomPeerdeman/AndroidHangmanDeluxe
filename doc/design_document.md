Design document
====================

Database
===
The database used is android's built in SQLite database.

#### Highscore's
| **field name** | id  | word | word_length | bad_guesses | time | game_type |
| :------------- | --- | :--: | ----------- | ----------- | ---- | --------- |
| **field type** | INT | TEXT | INT         | INT         | INT  | INT       |

PRIMARY KEY on id.

word_length is used since the user might filter on word length (not yet planned). 
The built in LENGTH function is too slow for this.

The time is specified in seconds.

#### Word's
The words.xml file is read and all words are stored in this database.

| **field name** | word | word_length |
| :------------- | :--: | ----------- |
| **field type** | TEXT | INT         |

PRIMARY KEY on word.

Classes/Interfaces
===
- HangmanSettings<br />
	Stores the settings as configured in the settings activity such as word length range, max invalid guesses and if evil gameplay is enabled.
- HangmanStatus<br />
	Stores the current status of the hangman game such as characters visible to the user, the to be guessed word and the amount of guesses.
- GameplayDelegate<br />
	Interface that provides the methods that the good and evil gameplay should implement:<br />
	`public HangmanStatus initialize(HangmanSettings settings);`<br />
		Create a new game (and thus a new status) from the given settings.<br />
	`public HangmanStatus onGuess(HangmanSettings settings, HangmanStatus status, char guess);`<br />
		Handle a user triggered guess and adapt the current status to the guess.
- WordDatabase<br />
	Interface that provides methods to get words from the word list using some constraints on the words. The constraints are length in range or a given equivalence class.
	Also provides methods to insert words into the word list.
	This will be implemented using SQLite.
- WordListReader<br />
	Class that will read the words.xml file and store all the elements using a given WordDatabase (probably SQLite).
- HighScore<br />
	Simple list of highscore entry's
- HighScoreEntry<br />
	Combination of a highscore game's settings and final status.
- HighScoreDataSource<br />
	Interface that provides methods to load and save the highscore's.
	This will be implemented using SQLite.

Usert interface
===
#### Main game
See [game UI](game_ui.png). This UI should always display the onboard keyboard. The time counts up and pauses if the app is paused or the settings or highhscore activity is opened. The guesses left shows the amount of guesses left vs the maximum amount of bad guesses.
The background of the app will react on the amount af guesses left. With all guesses left the background will be green. If all guesses are used the background is red. 
The menu contains 3 items:
- New game
- Settings
- Highscore

#### Highscore
The highscore UI will have 3 tabs:
- Overall
- Evil
- Normal

on each of the 3 tabs a list is shown of the highscore's where each highscore contains:
- The word to be guessed
- The number of bad guesses
- The time to guess the word

Only the 10 best (least time) score's are shown.

#### Settings
The settings UI consists of the following settings:
- Max number of guesses
- Min word length
- Max word length
- Evil/normal gameplay
- Hide non alphabetic characters (example - )

All number settings are set using a slider.
All settings are tiled horizontally.

Style guide
===

The code style used is an adapted form of Oracle's Code Conventions for the Java Programming Language.
Oracle's Code Conventions for the Java Programming Language are described [here](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

### Adaptations
- Indentation is done by using tabs. It is preferred that one tab represents 4 spaces.
- Comments after a piece of code on the same line are not allowed. For example:
```
// This comment is correct.
int[] arr; // This comment is not allowed here.
```
- A keyword followed by a parenthesis should **NOT** be separated. Example:
```
	while(true) {
		if(false) {
			...
		}
	}
```
Instead of
```
	while (true) {
		if (false) {
			...
		}
	}
```
