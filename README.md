# SBModMake
Mod maker for Starbound

Currently, the application has only been tested on Windows, but aspires to work on all platform that starbound supports.

## Purpose
* Reduce common modding tasks into a one-click or no-click operations
* Help modders organize mods
* Standardize starbound mods, using the conventions already in place by the community
* Give me something to do in my free time

## Features
* Automaticly finds the starbound folder [Windows]
* Makes packing mods easier (no CLI)
* Easy mod management
* Streamlines mod creation
* Default star bound folders for all OSs
* Save/load mod configurations
* Easy opening of json, text and png files

## Usage

### Creating a new mod
1. Input the name of the mod set the
2. Select a custom folder (or use the default one) and press create to create the mod folder.
3. Input the modinfo filename (or use the default one) and press create to create it.
4. Press the edit button besides the modinfo filename to edit the modinfo file.
5. Press Load to view the contents of the mod folder.
6. If you want to edit a text file within the mod, select it the file tree and press Edit.

### Settings
Open the settings window by clicking "Settings"

Set up the starbound folder (or let the program find it automatically on windows)
Set up a default text editor, like Notepad++
Select your operating system for the program to find the starbound tools.

## Compilation
Compile the code in [/src](src) and include the libraries in [/lib](lib).
Make sure to also reference these libraries:
* https://github.com/SilverFishCat/SBModMake-Core
* https://github.com/SilverFishCat/Swingon

In you use eclipse, you can import the entire content as an eclipse project.

## License
[LICENSE](LICENSE)
