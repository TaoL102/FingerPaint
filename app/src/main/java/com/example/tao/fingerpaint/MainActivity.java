package com.example.tao.fingerpaint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.File;

/**
 * The MainActivity Class is the controller for this app.
 * It controls the lifecycle of this app (onStart,onStop,onDestroy...),
 * handles the click event of all buttons and implements the
 * basic functionality required.
 *
 * @author  Tao Liu
 * @version 1.0
 * @since   10/08/2016
 */
public class MainActivity extends AppCompatActivity {

    // Fields
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1111;
    DrawView paint_view;
    RelativeLayout layout_palette;
    RelativeLayout layout_shape;
    ImageView btnShapeCircle;
    ImageView btnShapeRectangle;
    ImageView btnShapeTriangle;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * When the system creates new instance of MainActivity,
     * this method is called. We use this method to declar
     * the user interface (defined in an XML layout file) and
     * define member variables.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Define member variables
        paint_view = (DrawView) this.findViewById(R.id.paint_view);
        layout_palette = (RelativeLayout) findViewById(R.id.layout_palette);
        layout_shape = (RelativeLayout) findViewById(R.id.layout_shape);
        btnShapeCircle = (ImageView) findViewById(R.id.btnShapeCircle);
        btnShapeRectangle = (ImageView) findViewById(R.id.btnShapeRectangle);
        btnShapeTriangle = (ImageView) findViewById(R.id.btnShapeTriangle);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * This method will be called when the system creates the options menu
     * on the actionbar.
     * @param menu
     * @return true to display menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * This method will be called when users click the menu items
     * @param item the item user clicks
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // User clicks the SAVE button
            case R.id.menu_action_save:
                saveDrawing();
                return true;

            // User clicks the RESET button
            case R.id.menu_action_reset:
                paint_view.resetCanvas();
                showToastMessage("Canvas Reset!");
                return true;

            case R.id.menu_action_exit:
                showToastMessage("App closing!");
                finish();
                System.exit(0);
                return true;

            case R.id.menu_action_palette:
                //Fade in Animation
                if(layout_palette.getVisibility()==View.INVISIBLE) {
                    layout_palette.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    layout_palette.setVisibility(View.VISIBLE);
                }
                return true;

            case R.id.menu_action_shape:
                //Fade in Animation
                if(layout_shape.getVisibility()==View.INVISIBLE) {
                    layout_shape.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
                    layout_shape.setVisibility(View.VISIBLE);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method will be called when users click the close button
     * on the toolbox panel(Shape selection panel and Palette panel).
     * @param view
     */
    public void btnCloseToolBox_OnClick(View view) {
        // Get the view id
        int id = view.getId();

        // Set the palette and shape selection panel invisible with fadeout animation
        switch (id) {
            case R.id.btnClosePalette:
                layout_palette.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
                layout_palette.setVisibility(View.INVISIBLE);
                break;

            case R.id.btnCloseShape:
                layout_shape.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
                layout_shape.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * This method will be called when users click the color in Palette panel.
     * It checks which color users choose and changes the paint color.
     * @param view
     */
    public void colorBtn_OnClick(View view) {
        int id = view.getId();
        int colorID;
        int color;

        switch (id) {
            case R.id.btnColorRed:
                colorID = R.color.colorRed;
                break;

            case R.id.btnColorViolet:
                colorID = R.color.colorViolet;
                break;

            case R.id.btnColorYellow:
                colorID = R.color.colorYellow;
                break;

            case R.id.btnColorGreen:
                colorID = R.color.colorGreen;
                break;

            case R.id.btnColorBlue:
                colorID = R.color.colorBlue;
                break;

            case R.id.btnColorDarkBlue:
                colorID = R.color.colorDarkBlue;
                break;

            case R.id.btnColorLightGreen:
                colorID = R.color.colorLightGreen;
                break;

            case R.id.btnColorLime:
                colorID = R.color.colorLime;
                break;

            case R.id.btnColorPurple:
                colorID = R.color.colorPurple;
                break;

            case R.id.btnColorPink:
                colorID = R.color.colorPink;
                break;
            case R.id.btnColorOrange:
                colorID = R.color.colorOrange;
                break;
            case R.id.btnColorCherry:
                colorID = R.color.colorCherry;
                break;
            default:
                colorID = R.color.colorBlack;
                break;
        }

        // Get the color value from res/values/color
        color = ContextCompat.getColor(this, colorID);

        // Change shape color in shape selection panel
        btnShapeCircle.setColorFilter(color);
        btnShapeRectangle.setColorFilter(color);
        btnShapeTriangle.setColorFilter(color);

        // Change paint color
        paint_view.setColor(color);

        // Show Toast Message
        showToastMessage("Color Changed!");
    }

    /**
     * This method will be called when users click the shape in shape selection panel.
     * It changes the brush shape according to the users' choice.
     * @param view
     */
    public void ShapeBtnOnClick(View view) {
        int id = view.getId();
        Shape shape;

        switch (id) {
            case R.id.btnShapeCircle:
                shape = Shape.CIRCLE;
                break;

            case R.id.btnShapeRectangle:
                shape = Shape.SQUARE;
                break;

            case R.id.btnShapeTriangle:
                shape = Shape.TRIANGLE;
                break;

            default:
                shape = Shape.CIRCLE;
                break;
        }

        // set the shape
        paint_view.setShape(shape);

        // Show toast message
        showToastMessage("Shape Changed!");
    }

    /**
     * This method is called when users click the Share button.
     * A Chooser menu will popup for users to select apps which
     * is predefined to support image type to share the drawing.
     * @param view
     */
    public void shareBtn_OnClick(View view) {
        // Call DrawView.createInternalCacheImage() method to create an image in the
        // internal storage. This method returns true if file is successfully created.
        boolean isFileCreated=paint_view.createInternalCacheImage();

        // If file is successfully created:
        if (isFileCreated) {
            // Create intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            // Clarify file path. To solve the Gmail "Permission denied for the attachment" issue,
            // I use FileProvider.getUriForFile() method to grant permissions to the receiving app temporaryly,
            // which would expire automatically when the receiving app's task stack is finished.
            // Reference： https://developer.android.com/training/secure-file-sharing/setup-sharing.html
            File imagePath = new File(getFilesDir(), "images");
            File newFile = new File(imagePath, "sharePic.jpg");
            Uri contentUri = FileProvider.getUriForFile(this, "com.example.tao.fingerpaint.fileprovider", newFile);

            // Set intent
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Call startActivity() method to invoke Chooser panel.
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }
    }

    /**
     * This method is used to save drawing.
     */
    private void saveDrawing(){
        // Check Permission
        boolean isPermissionGranted=checkWriteExternalStoragePermission();

        if (isPermissionGranted) {
            // Permission granted, call DrawView.saveBitmap() method to save drawing
            if (paint_view.saveBitmap()) {
                showToastMessage("Drawing Saved!");
            } else {
                showToastMessage("Saving Failed!");
            }
        } else {
            // Permission denied, call this method to request permission again.
            requestWriteExternalStoragePermission();
        }
    }

    /**
     * This method is used to show toast message
     * @param message text message shown
     */
    private void showToastMessage(String message) {

        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * This method is used to check WRITE_EXTERNAL_STORAGE permission.
     * When saving an image to the external storage, this method will
     * be called.
     * Reference: https://developer.android.com/training/permissions/requesting.html
     * @return whether WRITE_EXTERNAL_STORAGE permission is granted.
     */
    public boolean checkWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else return true;
    }

    /**
     * This method is used when Requesting WRITE_EXTERNAL_STORAGE permission.
     * When saving an image to the external storage, and WRITE_EXTERNAL_STORAGE
     * permission has not been granted yet, this method will be called.
     * Reference: https://developer.android.com/training/permissions/requesting.html
     */
    public void requestWriteExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            showToastMessage("Saving an image in the storage requires your permission.");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    /**
     *  When the user responds to requesting permission dialogue,
     *  the system invokes this method, passing it the user response.
     *  Override this method can find out whether the permission was granted.
     *  Reference: https://developer.android.com/training/permissions/requesting.html
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, we call DrawView.saveBitmap() method
                    // to save the bitmap
                    showToastMessage("Permission Granted");
                    DrawView paint_view = (DrawView) this.findViewById(R.id.paint_view);
                    if (paint_view.saveBitmap()) {
                        showToastMessage("Drawing Saved!");
                    } else {
                        showToastMessage("Saving Failed!");
                    }
                } else {

                    // permission denied.
                    showToastMessage("Permission Denied");
                }
                return;
            }
        }
    }

    /**
     * This method is used to manually handle the code when the configuration changed.
     * In this case, I use it to handle the drawing and layout when orientation changed.
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Save the drawing to the cache
        paint_view.createInternalCacheImage();

        // Create intent
        Intent intent = new Intent(this, DrawView.class);
        intent.putExtra("image", paint_view.customBitmap);

        // Handle the layout
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    /**
     * This method will be called when this activity is destroyed.
     * In this case, I use it to clear cache files.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clear Cache
        File imagePath = new File(getFilesDir(), "images");
        File newFile = new File(imagePath, "sharePic.png");
        newFile.delete();
    }
}
