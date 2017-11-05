package norbert.matrix;

import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
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
        Path path;
        List<String> matrix = new ArrayList<>();

        public MyView(Context context) {
            super(context);
            init();
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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

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
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
}
