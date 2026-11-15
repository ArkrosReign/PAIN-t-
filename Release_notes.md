My Lovely PAIN(t) — Release Notes
---

Update #1 — September 8, 2026 | 12:24 AM CST

- Added the main class files for `ImageManager.java` and `JoesPaint`, with JP (shortened) acting as the main class.
- Added the override for the main class and development would begin!

> Please ignore the comments of me crashing out.

Update #2 — September 10, 2026 | 10:52 AM CST

- Added the needed JavaFX imports for `Stage`, `MenuBar`, `Menu`, and `MenuItem`.
- Created the MenuBar and File menu.
- Added the following File menu options:
  - Open
  - Save
  - Save As
  - Exit
- Added the File menu to the MenuBar.
- Added the Scene and VBox layout.

**Note:** I will admit, I was fighting NetBeans for 10 minutes just to realize I was trying to run what I had for Lab 1.

Update #3 — September 10, 2026 | 2:06 PM CST

- Fixed the project run configuration, which was opening the Lab 1 project instead of the current project. Resolved by changing the run configurations and using `javafx:run`.
- Updated the Exit button so the application window closes when selected.
- Implemented Open functionality using Oracle's FileChooser documentation as a reference.
- Users can now select an image file and display it through `ImageView`.
- Fixed an accessibility issue by declaring `ImageView` before the Open action.
- Added the necessary image imports and FileChooser settings.
- Implemented image saving as PNG.
- Added image conversion and saving using:
  - `WritableImage`
  - `SwingFXUtils`
  - `BufferedImage`
  - `ImageIO`
- Fixed a `BufferedImage` dependency recognition issue by adjusting the Maven project dependencies.
- Finished implementing Save and Save As.

**Known Issues at the Time:**

- Save did not do anything when no image was loaded.
- Some images displayed incorrectly or had portions cropped out, including the infamous cat brick and the image that decided to show only another cat's ear. 😭

**References:** Oracle documentation, including `SwingFXUtils`, `WritableImage`, and `ImageView`.

Update #4 — September 13, 2026 | 9:38 PM CST

**Save Confirmation**

- Added a popup notification when a file is saved.
- The notification displays the file name for confirmation.

### Update #5 — September 18, 2026 | 11:03 PM EST

**Interface Improvements**

- Added a Help button to the MenuBar.
- Added Help and About options.
- Added a ColorPicker.

### Update #6 — September 20, 2026 | 11:29 AM EST

**Drawing Functionality**

- Added SmartSave, warning users if they attempt to exit without saving.
- Added the ability to draw lines.

### Update #7 — September 21, 2026

**Image Format Support**

