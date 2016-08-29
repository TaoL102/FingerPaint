# FingerPaint
This is an assignment for Mobile Software Development course.The app is simply for drawing with save,share and other simple funtions.It's quite basic,the functions required are all implemented. Due to limited time for this assgnment,I didn't extend some fancy functions.

![alt tag](https://github.com/TaoL102/FingerPaint/blob/master/Screenshots/Screenshot_20160829-214654.png?raw=true)
![alt tag](https://github.com/TaoL102/FingerPaint/blob/master/Screenshots/Screenshot_20160829-225124.png?raw=true)

These are the 3 extensions required for this assignment.
• Save:
      Save button is in the menu at the top right corner.
      After referencing the Android Developer docs, the drawing, as a type of picture, should be stored in the system predefined DIRECTORY_PICTURES, which is as public files in the external storage and will be scanned by system media scanner, so that once we save the image, users will see it in the gallery app.
      I implemented the necessary steps, including requesting and checking WRITE_EXTERNAL_STORAGE permission, checking if the storage is writable, checking if the folder is available and notifying the media scanner about the new file according to the developer documents.
      For devices running Android 6.0 (API level 23) or higher, the app checks whether it has the permissions every time it runs. If permission not granted, it will pop up a dialogue for users to grant or deny the permission.
      For devices running Android 5.1 (API level 22) or lower, the system asks the user to grant the permissions when the user installs the app. In the simulator for API 22, it grants the permission by default, so saving function works as well.

      Reference: Saving Files,https://developer.android.com/training/basics/data-storage/files.html 

• Email:
      Instead of just implementing an email button, I put a Floating Action Button at the right bottom of the screen to enable the SHARE function. When clicking this button, a CHOOSER will pop up with all apps that support Type Image (including Email app), letting user to choose which app to share with.
      Considering handling the image file securely and avoiding creating necessary image files in the external storage which would be user-unfriendly, I implemented the FileProvider class to store image in a predefined internal folder and grant permissions to the receiving app temporarily, which would expire automatically when the receiving app's task stack is finished.
      In the simulator, the email app would not appear in the CHOOSER dialogue until an email account is configured correctly. The sharing function works.

      Reference：https://developer.android.com/training/secure-file-sharing/setup-sharing.html   

• Handling different screen sizes and orientations. 
      According to the developer documentation, I used dp or sp as unit rather than px.
      I created different xml files as layout files for small screens, larger screens(In this case, 240dpi or more) and land orientation.

      Reference: Supporting Different Screen Sizes, https://developer.android.com/training/multiscreen/screensizes.html
      Reference：Supporting Different Densities,https://developer.android.com/training/multiscreen/screendensities.html
