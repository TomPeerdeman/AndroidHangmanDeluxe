Android Hangman Deluxe
====================

A rather simple Android application which lets the user play hangman.

Features:
- Normal gameplay as explained [Here](http://apps.mprog.nl/projects/hangman)
- Evil gameplay as explained [Here](http://apps.mprog.nl/projects/hangman)
- Time/num guesses based highscore
- Adjustable word length

Technologies:
- Android SDK
- SQLite


Installation instructions
====

1. Use the android SDK manager to install the following packages:
	- Android SDK platform API 19
	- Android SDK platform API 7
	- Android support library (under Extras)

2. Import the project into eclipse (eclipse should be the ADT version of course)
3. Import the android appcompat v7 project into eclipse (should be located in {your sdk path}/extras/android/support/v7/appcompat)
4. Add the appcompat project as library to the Hangman project (Hangman project->properties->Android->secton library->add)

Using the android emulator
====

Preferred settings:
- Screen: 320x480px
- Target/API: 19
- NO hardware keyboard!

If you are using an AVD i recommend using a x86 image. You can create an AVD with an x86 image by downloading a x86 atom image from the SDK manager and select Intel atom at the CPU/ABI selection.
