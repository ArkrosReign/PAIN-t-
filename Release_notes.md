My lovely PAIN(t) - 09/10/26

First update - 09/08/26, 12:24 AM CST

Some features I added
- Added the main class files for ImageManager.java and JoesPaint with JP (shortened) acting as the main class
- Added the override for the main class and development would begin! (please ignore the comments of me crashing out)

Update #2 - 09/10/26, 10:52 AM CST

- Added the needed Javafx imports for Stage, Menubar, Menu, and Menuitem
- Created the menubar and filemenu
- added the open, save, save as, and exit menu options
- added the menu options when u press the file button 
> Open
> Save
> Save As
> Exit 
- Added the file menu to the menubar
- Added the scene and Vbox layout 

Note: I will admit, I was fighting NetBeans for 10 minutes just to realize was trying to run what I had for lab 1

Update #3 09/10/26, 2:06 PM CST

- During a test run, the project was opening what I had set for lab 1 instead of the current project, this was fixed by changing the run configs in project settings and using javafx:run
- Updated the exit button where if you press it the window will close
- Added the open functionality using Oracle's filechooser documentation as a reference
- Open now allows the user to select an image file and display through ImageView
- Encountered a issue where ImageView was declared in a different block which it couldn't be accessed by the open function. Fixed this by declaring ImageView before the open action
- Added the needed image imports and filechooser settings to allow image files to be opened
- Added the functionality (this took SO LONG), allowing the displayed image to be saved as a png file
- Added the necessary image conversion and saving by using Writableimage, SwingFXUtils, BufferedImage, and ImageIO
- Encountered another issue where BufferedImage was not recognized and despite already having the import. This was fixed by adjusting the dependencies to the Maven project folder 
- Finished implementing both Save and Save As

Some issues ive taken note of: (only 1, the issues ive noted before were fixed)
- Save doesn't do anything without no image loaded
- When loading some images parts of an image can be cropped out like you see with the cat brick or one where it only decided to show another cats ear only 😭


Note: For reference I used Oracle documentation like a few was SwingFXUtils, Writableimage, and ImageView to name a few
