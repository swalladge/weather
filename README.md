

# Weather

A project for HIT237 Assignment 2, by Samuel Walladge (student number: s265679).

Copyright © 2016 Samuel Walladge

Repository hosted at [https://bitbucket.org/swalladge/weather](https://bitbucket.org/swalladge/weather).


## Info

- This is an IntelliJ IDE project, so the build system is whatever its default is (gradle I think).
- Main class to run for the GUI app is `Weather`
- Depends on [jsoup](https://jsoup.org/). (just download the jsoup jar and dump in the `lib` subdirectory of the project root directory)


## Features

- Loads all weather data from the html file at the official link.
- Can search for weather events by date (in either dd/mm/yyyy or yyyy-mm-dd format)
- Press enter to search or click search button.
- Fancy animated banner. Two animations are included (rain and clear day/night), which are chosen based loosely on the currently selected weather event.
- Ability to pause/play the animation.
- Buttons for loading and displaying the data.
- Info pane with short help
- Friendly error messages displayed on info pane where appropriate.
- Expanded information on selected weather event shown on info pane.

