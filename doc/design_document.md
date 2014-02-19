Design document
====================

Database
===
The database used is android's built in SQLite database.

#### Highscore's
| **field name** | id  | word | bad_guesses | time | game_type |
| :------------- | --- | :--: | ----------- | ---- | --------- |
| **field type** | INT | TEXT | INT         | INT  | INT       |

PRIMARY KEY on id.

The time is specified in seconds.

#### Word's
The words.xml file is read and all words are stored in this database.

| **field name** | word | word_length |
| :------------- | :--: | ----------- |
| **field type** | TEXT | INT         |

PRIMARY KEY on word.

word_length is used since the user can filter on word length. 
The built in LENGTH function is too slow for this.

Classes/Interfaces
===
- HangmanSettings<br />
	Stores the settings as configured in the settings activity such as word length range, max invalid guesses and if evil gameplay is enabled.
- HangmanStatus<br />
	Stores the current status of the hangman game such as characters visible to the user, the to be guessed word and the amount of guesses.
- GameplayDelegate<br />
	Interface that provides the methods that the good and evil gameplay should implement:<br />
	`public HangmanStatus initialize(HangmanSettings settings, WordsModel wordDatabase);`<br />
		Create a new game (and thus a new status) from the given settings.<br />
	`public HangmanStatus onGuess(HangmanSettings settings, HangmanStatus status, WordsModel wordDatabase, char guess);`<br />
		Handle a user triggered guess and adapt the current status to the guess.
- WordsModel<br />
	Interface that provides methods to get words from the word list using some constraints on the words. The constraints are length in range or a given equivalence class.
	Also provides methods to insert words into the word list.
	This will be implemented using SQLite.
- HighScoresModel<br />
	Simple list of highscore entry's.
- HighScoreEntry<br />
	Data structure that holds the word, the amount of bad guesses and the time.

#### Activities
MainActivity.java - the activity that shows and handles the game itself.<br />
SettingsActivity.java - the activity that shows all the settings and edit's the underlying structure so that the MainActivity can use them.<br />
HighScoreActivity.java - shows the highscore (10 games that have been won sorted by time to game completion ASC). Has 3 subcategories (tabs), overall, normal and evil. The overall tab shows the best 10 games, wich can be either normal or evil games. The normal tab shows the best 10 normal games. Etc.<br />

User interface
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
- Hide non alphabetic characters (example - )<br />
	This will not show the non alphabetic characters and instead replace them by the unknown letter sign (the underscore)

All number settings are set using a slider.
All settings are tiled horizontally.

Activity flow
===
#### MainActivity
The MainActivity is started when the game launches.
When the return buton is pressed in the main activity the game is paused and put into the background.
The main activity has a menu of 3 items:
- New game (Starts a new game)
- Settings (Launches the SettingsActivity)
- Highscore (Launches the HighScoreActivity)

#### SettingsActivity
When the return buton is pressed in the settings activity the app activates the MainActivity again.
The settings activity has a menu of 2 items:
- Resume game (Activate the MainActivity)
- Highscore (Launches the HighScoreActivity)

#### HighScoreActivity
When the return buton is pressed in the highscore activity the app activates the MainActivity again.
The highscore activity has a menu of 2 items:
- Resume game (Activate the MainActivity)
- Settings (Launches the SettingsActivity)

When the highscore is accessed by first activating the SettingsActivity (through the menu of the MainActivity), and in this SettingsActivity the HighScoreActivity is called (through the menu of the SettingsActivity), the return button should return to the MainActivity and **not** the SettingsActivity. 

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
