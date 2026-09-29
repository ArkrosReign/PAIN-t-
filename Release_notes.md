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


Note: For reference I used Oracle documentation like a few was SwingFXUtils, Writableimage, and ImageView to name a few to help me with this assignment

Update #4 9/13/26, 9:38 PM CST
- Added a feature where when you save, you will receive a popup saying the file has been saved
- Also said popup displays file name for confirmation

Update #5 9/18/26, 11:03 PM EST
- added a help button in menubar which includes the help and about button
- added a colorpicker

Update #6 9/20/26, 11:29 AM EST (airport coding time)
- added smartsave where the app will warn the user if they try to exit without saving
- added the ability to draw a line

Update #7 9/21/26, 12:34 PM EST
- added jpeg, png, and bmp support

Update #8 9/21/26, 10:23 AM CST 
- Added the ability to draw lines on a image that is open
- Now you can open a image and freely draw lines on, save it and open it again and it will remain there
- When you open a image larger than 800x500 it will cover the colorpicker on the top

SPRINT #2 KNOWN ISSUES
- When I open certain images it can block the color picker + scale (FIXED)
- Drawing lines then opening the image will not show up while on a blank canvas i can (FIXED, had to rewrite the stackpane function cus it was extend only drawing lines on the bottom)
- colorpicker gets blocked when opening a larger image

Update #9 9/23/26, 5:43 PM CST
- Added Javadoc documentation
- Added a adjuster where you can adjust the line to 1-20px
- Added a line width label that displays the current width
- Added a color picker for well picking colors
- Added a label for what color you choose in a hexadecimal format
- Added a color grabber tool for a selecting a color directly from the canvas
- Added a pencil for freehand drawing 

Update #10 9/23/26, 11:01 PM CST
- Added shapes for drawing
- Added the option to pick solid or dashed lines
- Added support for multiple img tabs
- Added a pun in the java about
- Added more a file for drawing and shape for organization
- Added a "imagetab" to help organize the workshop

SPRINT #3 Known Issues (Fixed)
- the screen shaking when you draw
- Opening a image and it blocks colorpicker + scale

ISSUES HAVE YET TO BE FIXED
- Additional img tabs can be created but drawing only works on the main tab
- Drawing does not work on a additional drawing tab

Update #11 9/28/26, 10:01 PM CST
-Added a clear confirmation when you wish to clear a canvas
- Added the ability to add text to canvas
- Added more shapes
