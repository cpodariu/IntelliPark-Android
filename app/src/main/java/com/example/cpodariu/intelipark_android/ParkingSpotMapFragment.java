package com.example.cpodariu.intelipark_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.cpodariu.intelipark_android.Utils.SharedPreferencesHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpodariu on 05.11.2017.
 */

public class ParkingSpotMapFragment extends Fragment {
    public class MyView extends View {

        Long mySpot;
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
            mySpot = Long.valueOf(SharedPreferencesHelper.getMySpot(getContext()));
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            paint.setTextSize(25);

            String contents = "";
            contents = SharedPreferencesHelper.getParkingMap(this.getContext());

            String lines[] = contents.split("\\r?\\n");
            int rows;
            int columns;
            int shiftRow = 0;
            int shiftColumn = 0;
            Long k = Long.valueOf(1);
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
                            paint.setColor(Color.WHITE);
                            canvas.drawText(String.valueOf(k), ((x + 9 + shiftRow) * 4) - 55, ((y + 12 + shiftColumn) * 4) + 100, paint);
                            if (k.equals(mySpot)) {
                                paint.setColor(Color.GREEN);
                            }
                            else {
                                paint.setColor(Color.RED);
                            }
                            k++;
                            break;
                        case "OCCUPIEDPARKING":
                            paint.setColor(Color.WHITE);
                            canvas.drawText(String.valueOf(k), ((x + 9 + shiftRow) * 4) - 55, ((y + 12 + shiftColumn) * 4) + 100, paint);
                            if (k == mySpot) {
                                paint.setColor(Color.GREEN);
                            }
                            else {
                                paint.setColor(Color.RED);
                            }
                            k++;
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

                    switch (state) {
                        case "FREEPARKING":
                            paint.setColor(Color.WHITE);
                            canvas.drawText(String.valueOf(k - 1), ((x + 9 + shiftRow) * 4) - 55, ((y + 12 + shiftColumn) * 4) + 100, paint);
                            break;
                        case "OCCUPIEDPARKING":
                            paint.setColor(Color.WHITE);
                            canvas.drawText(String.valueOf(k - 1), ((x + 9 + shiftRow) * 4) - 55, ((y + 12 + shiftColumn) * 4) + 100, paint);
                            break;
                    }
                    ++shiftRow;
                }
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new MyView(getContext());
    }

}
