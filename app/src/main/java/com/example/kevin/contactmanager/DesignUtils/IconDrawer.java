package com.example.kevin.contactmanager.DesignUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.util.Random;

public class IconDrawer {

    //Por sugerencia de Google, se usan los colores 500 como colores primarios
    public final static int MATERIAL_PRIMARY_COLORS[] = {
            0xFFF44336, //rojo
            0xFFE91E63, //rosado
            0xFF9C27B0, //purple
            0xFF673AB7, //deep purple
            0xFF3F51B5, //indigo
            0xFF2196F3, //blue
            0xFF03A9F4, //light blue
            0xFF00BCD4, //cyan
            0xFF009688, //teal
            0xFF4CAF50, //green
            0xFF8BC34A, //light green
            0xFFCDDC39, //lime
            0xFFFFEB3B, //yellow
            0xFFFFC107, //amber
            0xFFFF9800, //orange
            0xFFFF5722, //deep orange
            0xFF795548, //brown
            0xFF9E9E9E, //grey
            0xFF607D8B //blue grey
    };

    private static final Random mGenerator = new Random(); //El generador tiene que tener historia

    public IconDrawer() {
    }

    public static int getRandomColor(){
        int n = mGenerator.nextInt(MATERIAL_PRIMARY_COLORS.length);
        return MATERIAL_PRIMARY_COLORS[n];
    }


    public static Bitmap generateCircleBitmap(Context context, int circleColor, float diameterDP, String text){
        final int textColor = 0xffffffff;

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float diameterPixels = diameterDP * (metrics.densityDpi / 160f);
        float radiusPixels = diameterPixels/2;

        // Create the bitmap
        Bitmap output = Bitmap.createBitmap((int) diameterPixels, (int) diameterPixels,
                Bitmap.Config.ARGB_8888);

        // Create the canvas to draw on
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);

        // Draw the circle
        final Paint paintC = new Paint();
        paintC.setAntiAlias(true);
        paintC.setColor(circleColor);
        canvas.drawCircle(radiusPixels, radiusPixels, radiusPixels, paintC);

        // Draw the text
        if (text != null && text.length() > 0) {
            final Paint paintT = new Paint();
            paintT.setColor(textColor);
            paintT.setAntiAlias(true);
            paintT.setTextSize(radiusPixels * 2);
            //Typeface typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Thin.ttf");
            //paintT.setTypeface(typeFace);
            final Rect textBounds = new Rect();
            paintT.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, radiusPixels - textBounds.exactCenterX(), radiusPixels - textBounds.exactCenterY(), paintT);
        }

        return output;
    }
}
