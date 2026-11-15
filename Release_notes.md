# My lovely PAIN(t) — Release Notes

### Update #1 — 09/08/26, 12:24 AM CST

Some features I added:
- Added the main class files for `ImageManager.java` and `JoesPaint`, with JP (shortened) acting as the main class.
- Added the override for the main class and development would begin! (Please ignore the comments of me crashing out.)

### Update #2 — 09/10/26, 10:52 AM CST

- Added the needed JavaFX imports for Stage, MenuBar, Menu, and MenuItem.
- Created the MenuBar and File menu.
- Added the Open, Save, Save As, and Exit menu options.
- Added the menu options when you press the File button:
  - Open
  - Save
  - Save As
  - Exit
- Added the File menu to the MenuBar.
- Added the Scene and VBox layout.

**Note:** I will admit, I was fighting NetBeans for 10 minutes just to realize I was trying to run what I had for Lab 1.

### Update #3 — 09/10/26, 2:06 PM CST

- During a test run, the project was opening what I had set for Lab 1 instead of the current project. This was fixed by changing the run configurations in project settings and using `javafx:run`.
- Updated the Exit button so that when you press it, the window will close.
- Added the Open functionality using Oracle's FileChooser documentation as a reference.
- Open now allows the user to select an image file and display it through ImageView.
- Encountered an issue where ImageView was declared in a different block, which meant it couldn't be accessed by the Open function. Fixed this by declaring ImageView before the Open action.
- Added the needed image imports and FileChooser settings to allow image files to be opened.
- Added the functionality (this took SO LONG), allowing the displayed image to be saved as a PNG file.
- Added the necessary image conversion and saving by using WritableImage, SwingFXUtils, BufferedImage, and ImageIO.
- Encountered another issue where BufferedImage was not recognized despite already having the import. This was fixed by adjusting the dependencies to the Maven project folder.
- Finished implementing both Save and Save As.

**Some issues I've taken note of:**
- Save doesn't do anything without an image loaded.
- When loading some images, parts of an image can be cropped out, like you see with the cat brick or one where it only decided to show another cat's ear. 😭

**Note:** For reference, I used Oracle documentation like SwingFXUtils, WritableImage, and ImageView to help me with this assignment.

### Update #4 — 09/13/26, 9:38 PM CST

- Added a feature where when you save, you will receive a popup saying the file has been saved.
- The popup also displays the file name for confirmation.

### Update #5 — 09/18/26, 11:03 PM EST

- Added a Help button in the MenuBar, which includes the Help and About buttons.
- Added a ColorPicker.

### Update #6 — 09/20/26, 11:29 AM EST (airport coding time)

- Added SmartSave, where the app will warn the user if they try to exit without saving.
- Added the ability to draw a line.

### Update #7 — 09/21/26, 11:29 AM EST

- Added JPEG, PNG, and BMP support.

### Update #8 — 09/21/26, 10:23 AM CST

- Added the ability to draw lines on an image that is open.
- Now you can open an image and freely draw lines on it, save it, and open it again, and the drawing will remain there.
- When you open an image larger than 800x500, it can cover the ColorPicker at the top.

### SPRINT #2 KNOWN ISSUES

- When I open certain images, they can block the ColorPicker and scale controls. **(FIXED)**
- Drawing lines and then opening an image would not show the lines, while on a blank canvas they would. **(FIXED — had to rewrite the StackPane function because it was only extending the drawing lines on the bottom.)**
- ColorPicker gets blocked when opening a larger image.

### Update #9 — 09/23/26, 5:43 PM CST

- Added Javadoc documentation.
- Added an adjuster where you can adjust the line width from 1–20px.
- Added a line width label that displays the current width.
- Added a ColorPicker for, well, picking colors.
- Added a label for the chosen color in hexadecimal format.
- Added a Color Grabber tool for selecting a color directly from the canvas.
- Added a Pencil tool for freehand drawing.

### Update #10 — 09/23/26, 11:01 PM CST

- Added shapes for drawing.
- Added the option to pick solid or dashed lines.
- Added support for multiple image tabs.
- Added a pun in the Java About section.
- Added more files for drawing and shapes for better organization.
- Added an ImageTab class to help organize the workshop.

### SPRINT #3 KNOWN ISSUES (FIXED)

- Screen shaking when drawing. **(FIXED)**
- Opening an image and having it block the ColorPicker and scale controls. **(FIXED)**

**ISSUES THAT WERE YET TO BE FIXED AT THE TIME:**
- Additional image tabs could be created, but drawing only worked on the main tab.
- Drawing did not work on an additional drawing tab.

### Update #11 — 09/28/26, 10:01 PM CST

- Added a clear confirmation when you wish to clear a canvas.
- Added the ability to add text to the canvas.
- Added more shapes, including the Right Triangle.

### Update #12 — 10/01/26, 12:47 AM CST

- Added the Polygon shape, but JavaFX asks how many sides you wish to draw.
- Added the Undo/Redo buttons.

### Update #13 — 10/11/26

- Successfully ran `mvn clean install`.
- Confirmed that the project builds successfully through Maven.
- Ran three JUnit tests successfully.
- Successfully generated the JAR file.
- Updated the project resource configuration so the CSS file is copied correctly during the build.

### Update #14 — 10/25/26–10/26/26

- Fixed the Copy/Paste keyboard shortcut functionality.
- Added rotation options for images and canvas content:
  - Rotate 90°
  - Rotate 180°
  - Rotate 270°
- Added the ability to preview the application through a local webserver running at `localhost:8080`.
- Added AutoSave functionality, which saves workspace images to the user's local JoesPaint autosave directory.
- Added a UserLogger class to record application activity.
- Connected the activity logger to JoesPaint, with log entries being written to the local activity log.

### FINAL RELEASE NOTES

Joes' Pain(t) has grown from a basic image viewer into a JavaFX drawing application with multiple drawing tools, image manipulation, file management, and additional functionality.

The final application includes:

- Image opening and saving.
- PNG, JPEG, and BMP support.
- Multiple image tabs.
- Pencil, Line, and Eraser tools.
- Shape drawing tools, including Rectangle, Square, Ellipse, Circle, Triangle, Right Triangle, Polygon, and Star.
- Text insertion.
- Color selection and color grabbing.
- Adjustable line width and solid/dashed line styles.
- Undo and Redo functionality.
- Copy and Paste functionality.
- Image rotation.
- Clear canvas functionality with confirmation.
- Save notifications.
- AutoSave functionality.
- Local browser preview through the webserver.
- Application activity logging.
- Javadoc documentation.

**Final Notes:** This project was developed using JavaFX and Maven. Oracle documentation was used as a reference during development, particularly for JavaFX image handling and related functionality.

