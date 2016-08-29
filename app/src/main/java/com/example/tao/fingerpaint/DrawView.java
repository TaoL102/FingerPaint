package com.example.tao.fingerpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The DrawView Class is used to draw an image on a canvas.
 * It defines and changes the brush shape, color and thickness.
 *
 * @author Tao Liu
 * @version 1.0
 * @since 10/08/2016
 */
public class DrawView extends View {

    // Fields
    private Shape shape;
    private Paint paint;
    private int colorBackground;
    Bitmap customBitmap;
    Canvas customCanvas;

    // Constructor
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Define the member variables
        // Initialise paint color, brush shape, and background color
        paint = new Paint();
        paint.setColor(Color.RED);
        colorBackground = Color.WHITE;
        setShape(Shape.CIRCLE);
    }

    // Set Methods
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    public void setColor(int color) {
        paint.setColor(color);
    }

    /**
     * This method implements to do drawing on a specified canvas.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(customBitmap, 0, 0, paint);
    }

    /**
     * This method will be called once the size has changed.
     * In this case, it is used to create a bitmap according to the actual render size.
     * It is also used to recreate the bitmap when orientation has changed.
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // To solve the issue that canvas is lost when changing orientation
        // Reference: http://stackoverflow.com/questions/23571139/canvas-orientation-change-scaling-android
        // Check if cache pic exists, if exists, recreate the bitmap( just changing the orientation)
        File imagePath = new File(getContext().getFilesDir(), "images");
        File newFile = new File(imagePath, "sharePic.png");
        if (newFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            customBitmap = BitmapFactory.decodeFile(newFile.getPath(), options);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            customBitmap = Bitmap.createBitmap(customBitmap, 0, 0, customBitmap.getWidth(), customBitmap.getHeight(), matrix, true);
            customCanvas = new Canvas(customBitmap);
            newFile.delete();
        }

        // If bitmap is null, crate a new bitmap according to the current size
        if (customBitmap == null) {
            customBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            customCanvas = new Canvas(customBitmap);
            customCanvas.drawColor(colorBackground);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * This method is used to handle the touch event.
     * In this case, it is used to draw a shape according to the color
     * and shape user selected. The thickness is based on the touch pressure.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        // Define X and Y position of the current touch point
        float x = event.getX();
        float y = event.getY();

        // Define the touch pressure and thickness
        float pressure = event.getPressure();
        float thickness = (pressure * 50);

        // Check what shape user selected
        switch (shape) {
            case CIRCLE:
                customCanvas.drawCircle(x, y, thickness / 2, paint);
                break;

            case TRIANGLE:
                Point point = new Point((int) x, (int) y);
                Path path = getEquilateralTrianglePath(point, (int) thickness);
                customCanvas.drawPath(path, paint);
                break;

            case SQUARE:
                customCanvas.drawRect(x, y, x + thickness, y + thickness, paint);
                break;
        }
        invalidate();
        return true;
    }

    /**
     * This method is used to get the equilateral triangle path
     *
     * @param point
     * @param thickness
     * @return
     */
    public static Path getEquilateralTrianglePath(Point point, int thickness) {

        Point p2 = new Point(point.x + thickness, point.y);
        Point p3 = new Point(point.x + (thickness / 2), point.y - thickness);

        Path path = new Path();
        path.moveTo(point.x, point.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);

        return path;
    }

    /**
     * This method is used to reset the canvas
     */
    public void resetCanvas() {
        // Draw the background color to the canvas
        customCanvas.drawColor(colorBackground);
        invalidate();
    }

    /**
     * This method is used to save a bitmap to the external storage
     * @return if bitmap is successfully saved in the external storage
     */
    public boolean saveBitmap() {
        // Check if external storage is writable
        if (this.isExternalStorageWritable()) {
            // If writable, create and save the picture
            if (this.createExternalStoragePicture()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if external storage is available for read and write
     * Reference: https://developer.android.com/training/basics/data-storage/files.html
     * @return if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to create picture in external storage
     * Reference: https://developer.android.com/reference/android/os/Environment.html#getExternalStoragePublicDirectory(java.lang.String)
     * @return if image is created in the External Picture directory
     */
    private boolean createExternalStoragePicture() {

        // Create a path where we will place our picture in the user's
        // public pictures directory.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        // Filename
        String currentDateandTime = new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
        String fileName= "FingerPaint" + currentDateandTime + ".png";
        File file = new File(path,fileName);

        // If image successfully created,
        if(createImage(path,file)){
            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(getContext(),
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
            return true;
        }
        return  false;

    }

    /**
     * This method is used to create an image in the cache file
     * for internal use.In this app, it is used when sharing drawing.
     * Reference： https://developer.android.com/training/secure-file-sharing/setup-sharing.html
     * @return if image is created in the cache
     */
    public boolean createInternalCacheImage() {

        // File path.
        // To fix the Gmail "Permission denied for the attachment" issue,
        // I use getFilesDir() method instead of getCacheDir() method.
        File imagePath = new File(getContext().getFilesDir(), "images");
        File file = new File(imagePath, "sharePic.jpg");

        if(createImage(imagePath,file)){
            return true;
        }
        return  false;
    }


    /**
     * This method is used to create an image in the storage
     * Reference： https://developer.android.com/training/secure-file-sharing/setup-sharing.html
     * @return if image is created in the assigned directory
     */
    public boolean createImage(File imagePath,File file) {

        // Make sure the directory exists.
        imagePath.mkdirs();

        // Save stream to file
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            customBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // PNG is a lossless format, the compression factor (100) is ignored
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

