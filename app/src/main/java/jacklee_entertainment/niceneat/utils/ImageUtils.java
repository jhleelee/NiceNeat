package jacklee_entertainment.niceneat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class ImageUtils {
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }

    public static Bitmap tintWithColor(int drawableId, int colorId, Context context) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                drawableId);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), conf);
        bmp.eraseColor(context.getResources().getColor(colorId));
        Bitmap res = overlay(bmp, icon);
        return res;
    }
}
