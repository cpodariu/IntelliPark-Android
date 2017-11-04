package norbert.matrix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public class MyView extends View {

        Paint paint;
        Path path;

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

        private void init(){
            paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            int i,j;
            int x = 20;
            int y = 20;
            for(i=0;i<10;i++){
                x=20;
                canvas.drawRect(x, y, 10, 10, paint);
                for(j=0;j<10;j++)
                {
                    x+=50;
                    canvas.drawRect(x, y, 10, 10, paint);


                }
                y+=50;
            }


            //drawRect(left, top, right, bottom, paint)

        }

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }


}
