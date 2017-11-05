package norbert.matrix;

import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity {

    public class MyView extends View {

        Paint paint;
        private ScaleGestureDetector mScaleDetector;
        private float mScaleFactor = 1.f;
        private float dX, dY;

        public MyView(Context context) {
            super(context);
            init();
            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        }

        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public MyView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1.5f);
            paint.setStyle(Paint.Style.STROKE);
        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleFactor *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

                invalidate();
                return true;
            }
        }

        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:

                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            onTouch(this, ev);
            // Let the ScaleGestureDetector inspect all events.
            mScaleDetector.onTouchEvent(ev);
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.save();
            canvas.scale(mScaleFactor, mScaleFactor);

            paint.setTextSize(25);

            String contents = "";
            try {
                InputStream stream = getAssets().open("data.txt");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                contents = new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String lines[] = contents.split("\\r?\\n");
            int rows;
            int columns;
            int shiftRow = 0;
            int shiftColumn = 0;
            for (String line : lines) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    rows = Integer.parseInt(parts[0]);
                    columns = Integer.parseInt(parts[1]);
                } else {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    String state = parts[2];
                    if (shiftRow == 10) {
                        shiftRow = 0;
                        shiftColumn++;
                    }

                    switch (state) {
                        case "FREEPARKING":
                            paint.setColor(Color.GREEN);
                            break;
                        case "OCCUPIEDPARKING":
                            paint.setColor(Color.RED);
                            break;
                        default:
                            paint.setColor(Color.BLACK);
                            break;
                    }
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(((x + shiftRow) * 4) - 55, ((y + shiftColumn) * 4) + 100, (((x  + shiftRow) + 21) * 4) - 55, (((y + shiftColumn) + 21) * 4) + 100, paint);
                    /* canvas.drawPaint(paint);
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(1.5f);
                    paint.setStyle(Paint.Style.STROKE); */
                    paint.setColor(Color.WHITE);
                    canvas.drawText(String.valueOf(7), ((x + 9 + shiftRow) * 4) - 55, ((y + 12 + shiftColumn) * 4) + 100, paint);
                    ++shiftRow;
                }
            }

            canvas.restore();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
}