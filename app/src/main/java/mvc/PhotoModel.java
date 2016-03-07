package mvc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Bradshaw on 2016-03-06.
 */
public class PhotoModel {

    final int MAX_PHOTO_SIZE = 65536;
    final int FIXED_DIMENSION = 1000;

    private Bitmap photo;

    /**
     * Creates a photo object out of a bitmap. Resizes the bitmap to meet a set fixed size.
     * @param photo Bitmap that represents the photo
     */
    public PhotoModel(Bitmap photo)
    {
        this.photo = compressBitmap(photo);
    }

    private Bitmap compressBitmap(Bitmap photo)
    {
        int quality = 100;
        double scale = 0.8;
        //Resize image based on a fixed largest dimension
        double scaleFactor = (double)FIXED_DIMENSION / Math.max(photo.getHeight(), photo.getWidth());
        int newWidth = (int)(photo.getWidth() * scaleFactor);
        int newHeight = (int)(photo.getHeight() * scaleFactor);
        photo = Bitmap.createScaledBitmap(photo, newWidth, newHeight, false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //Use PNG compression to get maximum quality to check encoded byte size
        photo.compress(Bitmap.CompressFormat.PNG, quality, stream);
        int byteSize = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP).length();
        while(byteSize > MAX_PHOTO_SIZE)
        {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stream = new ByteArrayOutputStream();
            //Resize photo, then recheck size (hugely inefficient, might want to change)
            photo = Bitmap.createScaledBitmap(photo, (int)(photo.getWidth()*scale),
                    (int)(photo.getHeight()*scale), false);
            photo.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            byteSize = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP).length();
            quality = quality - 10;
            if (quality == 0)
            {
                //Shouldn't reach here, if it does just return an image that can't be too large
                photo = Bitmap.createScaledBitmap(photo, 50, 50, false);
                break;
            }
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    public Bitmap getPhoto()
    {
        return photo;
    }
}
