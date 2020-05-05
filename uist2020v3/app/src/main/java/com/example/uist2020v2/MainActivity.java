package com.example.uist2020v2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.size.Size;
import com.otaliastudios.cameraview.size.SizeSelector;

import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Dictionary;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.aruco.Aruco;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CameraView mCamera;
    private Handler snapHandler;
    private Button calibration;
    private Button ambient_tips;
    private ImageView overlay;
    private ImageView autofritz;
    private String app_num = "06";
    private String SERVERIP = "104.196.101.18";
//    String SERVERIP = "192.168.42.3";
    private String testInputForCal = "calib270-196,290-195,311-195,331-195,352-195,394-194,415-194,435-193,456-193,477-193,518-192,540-192,560-191,581-191,602-191,644-191,665-190,686-189,707-189,728-189,772-188,793-187,814-187,835-186,857-186,270-216,290-216,311-216,332-216,352-216,394-215,415-215,435-215,456-214,477-214,519-213,540-213,560-213,581-213,602-212,645-211,666-211,686-211,708-210,729-210,772-209,793-208,814-208,836-208,857-207,262-276,282-276,302-276,323-275,343-275,364-275,385-274,406-274,426-274,447-274,468-274,488-273,509-273,530-273,551-273,571-272,592-272,613-272,634-271,655-271,676-271,697-271,718-270,740-270,761-269,782-269,803-269,825-268,846-268,868-268,262-297,282-296,302-296,323-296,343-296,364-296,385-295,406-295,426-295,447-294,468-294,489-294,509-294,530-294,551-293,572-293,592-293,613-293,634-292,655-292,676-292,697-291,718-291,740-291,761-290,782-290,804-290,825-289,847-289,868-288,261-318,282-317,302-317,323-317,344-316,364-316,385-316,406-316,426-316,447-315,468-315,489-315,509-315,530-315,551-314,572-314,593-314,613-314,635-313,655-313,677-313,697-313,719-312,740-312,761-312,782-311,804-311,825-311,847-310,868-310,261-338,282-338,303-338,323-337,344-337,364-337,385-337,406-337,426-337,447-337,468-336,489-336,509-336,530-336,551-335,572-335,593-335,614-335,635-334,656-334,676-334,698-333,719-333,740-333,761-333,783-333,804-332,825-332,847-331,868-331,261-359,282-359,303-358,324-359,343-358,364-358,385-358,407-358,426-358,447-357,468-357,489-357,509-357,530-357,551-356,572-356,593-356,614-356,635-356,656-356,677-355,698-355,719-354,740-354,761-354,783-354,804-354,825-353,847-353,868-353,261-419,281-418,302-418,324-418,343-418,364-418,385-418,405-417,427-417,447-417,468-417,489-417,509-417,530-417,551-416,574-416,593-416,614-416,635-415,656-415,677-415,698-415,719-415,740-415,762-414,783-414,804-414,826-414,847-414,869-414,260-440,281-439,302-439,323-439,344-439,364-439,385-438,406-438,427-438,447-438,468-437,489-437,509-437,530-437,551-437,574-437,593-437,614-437,635-437,656-436,677-436,698-436,719-436,741-436,762-436,783-436,805-435,827-435,847-435,869-435,260-461,281-460,302-460,323-460,344-459,364-459,385-459,405-459,427-459,447-459,468-459,489-459,509-458,530-458,551-458,574-458,593-458,614-458,635-458,656-458,677-457,698-457,719-457,741-457,762-457,783-456,805-456,827-457,848-456,869-456,260-482,281-481,302-481,323-481,344-481,364-480,385-480,405-480,427-480,447-480,468-480,489-479,510-479,530-479,551-479,573-479,593-479,614-479,635-478,656-478,677-478,698-478,720-478,741-478,762-478,783-478,805-478,826-478,848-478,869-478,260-502,281-502,302-502,323-502,343-502,365-501,386-501,405-501,427-501,447-501,469-501,490-501,509-500,531-500,551-500,573-500,593-500,614-500,635-500,656-500,677-500,698-500,720-500,741-500,762-499,783-499,805-499,827-499,848-499,870-499,268-564,289-563,309-563,331-563,351-563,394-563,415-563,436-562,456-562,477-562,520-562,541-562,562-561,583-561,604-561,646-561,667-561,689-561,710-561,731-561,774-560,795-560,817-560,838-560,860-560,267-584,288-584,309-584,330-583,351-583,394-583,414-583,435-583,456-583,477-583,519-582,541-582,562-582,583-582,604-582,646-582,667-582,689-582,710-582,731-582,774-582,795-582,817-582,839-582,861-582";
    private String testInput = "newcptransistor01:359-476,494-526@10-7,10-8,10-9";
//    private String testInput = "newcpbutton01:464-350,520-428@5-9,8-9,5-11,8-11";
    private boolean calibrating = false;
    private boolean buildtree = true;
    private boolean isholesVisualize= false;

    private int preview_frame_height = 0;
    private int preview_frame_width = 0;

    private List<Electronics> currentComponents = new ArrayList<Electronics>();
    private List<Point>[] holes_position= new List[14];
    private List<Boolean> [] holes_Visual_check = new List[14];
    private Bitmap currentOverlay;

    private String currentComponentName = "start00";

    final Runnable snap_repeat = new Runnable() {
        public void run() {
            mCamera.takePictureSnapshot();
            snapHandler.postDelayed(snap_repeat, 500);
            Log.d("sanp_repeat", "sanp_repeat");
        }
    };

    private String [] components = { "LED",
            "button",
            "resistor",
            "capacitor",
            "thread",
            "ceramic",
            "IC",
            "trimpot",
            "photocell",
            "potentiometer",
            "reed_switch",
            "diode_rectifier",
            "transistor",
            "slide_switch",
            "temperature_sensor",
            "7_segment_display",
            "LCD"};
    private int [] pinsnumber =      {3,3,3,3,2,3,8,3,2,3,2,2,3,3,3,10,16};
    private int [] hole_lines_size = {25, 25, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 25, 25};
    private String commandToServer;
    private KDTree tree  = new KDTree(400);
    private int numOfElectronics = 0;
    private Dictionary dictionary;
    private int pre_code_num = 4;
    private Size serverResolution, clientResolution;
    private int updates = 0;

    private static final int POS = 1;
    private static final int NEG = 0;

    private TextView top_text;
    private TextView mid_text;
    private TextView bot_text;
    private ImageView top_image;
    private ImageView mid_image;
    private ImageView bot_image;
    private Button ambient_top_button;
    private Button ambient_bot_button;
    private TextView componentTitleTextView;
    JSONObject qaJson = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mCamera = findViewById(R.id.camera);
        mCamera.setLifecycleOwner(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if(OpenCVLoader.initDebug())
            dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50);

        Util.checkPermission(this);
        snapHandler = new Handler();

        top_text = findViewById(R.id.top);
        mid_text = findViewById(R.id.middle);
        bot_text = findViewById(R.id.bottom);
        top_image = findViewById(R.id.top_pic);
        mid_image = findViewById(R.id.mid_pic);
        bot_image = findViewById(R.id.bot_pic);
        ambient_top_button = findViewById(R.id.ambient_top_text);
        ambient_bot_button = findViewById(R.id.ambient_bot_text);
        componentTitleTextView = findViewById(R.id.componentTitleTextView);


        mCamera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                final Size size = result.getSize();
                Log.d("mCamera", size + "");

                result.toBitmap(size.getWidth(), size.getHeight(), new BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
//                        Bitmap bmp = RotateBitmap(bitmap, -90);
                        if (!isholesVisualize) commandToServer = "calibration__";
                        Bitmap bmp = RotateBitmap(bitmap, 0);
                        Log.d("checkhands", "size.getWidth():" + size.getWidth());
                        Log.d("checkhands", "size.getHeight():" + size.getHeight());
                        Log.d("checkhands", "mCamera.getPictureSize().getWidth():" + mCamera.getPictureSize().getWidth());
                        Log.d("checkhands", "mCamera.getPictureSize().getHeight():" + mCamera.getPictureSize().getHeight());
                        if(size.getWidth() == mCamera.getPictureSize().getWidth() && !commandToServer.isEmpty()) {
                            Log.d("checkhands", "take picture2");
                            MyTaskParams myTaskParams = new MyTaskParams(bmp, commandToServer, null);
                            SendToServer sendToServer = new SendToServer();
                            sendToServer.execute(myTaskParams);
                            commandToServer = "";

                        } else {
                            Log.d("onBitmapReady", "checking hands");
                            if (isholesVisualize)
                                checkHands(bmp);
                        }
                    }
                });
            }
        });

        mCamera.setPreviewStreamSize(new SizeSelector() {
            @Override
            public List<Size> select (List<Size> source) {
                Log.d("SizeSelectorPreview", source+"");

                List<Size> lll = new ArrayList<Size>();
                for (Size size: source) {
                    if(size.getWidth() == 1280 && size.getHeight() == 960){
                        lll.add(size);
                    }
                }

                serverResolution = new Size(mCamera.getPictureSize().getHeight(),mCamera.getPictureSize().getWidth());
//                clientResolution = new Size(mCamera.getPictureSize().getWidth(), mCamera.getPictureSize().getHeight());
                clientResolution = serverResolution;
                Log.d("SizeSelector", serverResolution + "serverResolution");
                Log.d("SizeSelector", clientResolution + "clientResolution");

                return lll;
            }
        });

        mCamera.setPictureSize(new SizeSelector() {
            @Override
            public List<Size> select (List<Size> source) {
                Log.d("SizeSelectorPic", source+"");
                List<Size> lll = new ArrayList<Size>();
                for (Size size: source) {
                    if(size.getWidth() == 1280 && size.getHeight() == 960){
                        lll.add(size);
                    }
                }
                return lll;
            }
        });

        calibration = (Button)findViewById(R.id.calibration);
        calibration.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                commandToServer = "ButtonPressed";
                commandToServer = "calibration__";
                mCamera.takePicture();
                calibration.setBackgroundColor(0x80ffc857);
                calibration.setText("CAPTURING.....");
//                Log.d("calibration", "button pressed")


//                parseComponentsFromServer(testInputForCal);
//                parseComponentsFromServer(testInput);;
            }
        });




        overlay = findViewById(R.id.overlay);
        Log.d("overlay", overlay.getHeight() +","+overlay.getWidth());
        autofritz = findViewById(R.id.autofritz);
        holes_check_initialize();

        final FrameLayout layout = (FrameLayout) findViewById(R.id.preview_framelayout);
        final ViewTreeObserver observer= layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        preview_frame_height = 580;
                        preview_frame_width = 780;
                        Log.d("preview_frame_height", preview_frame_height+"");
                        Log.d("preview_frame_width", preview_frame_width+"");
                    }
                });

        top_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);// add here your class name
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.imagedialog);//add your own xml with defied with and height of videoview
                dialog.show();
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                final ImageView mImageView = (ImageView) dialog.findViewById(R.id.imagedialog);

                if (currentComponentName.equals("button"))mImageView.setImageResource(R.drawable.button_top);
                else if (currentComponentName.equals("trimpot")) mImageView.setImageResource(R.drawable.trimpot_top);
                else if (currentComponentName.equals("potentiometer"))mImageView.setImageResource(R.drawable.potentiometer_top);
                else if (currentComponentName.equals("IC")) mImageView.setImageResource(R.drawable.chip_top);
                else if (currentComponentName.equals("transistor")) mImageView.setImageResource(R.drawable.transistor_top);
                else if (currentComponentName.equals("motor")) mImageView.setImageResource(R.drawable.motor_top);
                else if (currentComponentName.equals("LCD")) mImageView.setImageResource(R.drawable.lcd_datasheet);
            }
        });


        mid_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);// add here your class name
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.imagedialog);//add your own xml with defied with and height of videoview
                dialog.show();
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                final ImageView mImageView = (ImageView) dialog.findViewById(R.id.imagedialog);
                if (currentComponentName.equals("button")) mImageView.setImageResource(R.drawable.button_mid);
                 else if (currentComponentName.equals("trimpot")) mImageView.setImageResource(R.drawable.trimpot_mid);
                 else if (currentComponentName.equals("potentiometer")) mImageView.setImageResource(R.drawable.potentiometer_mid);
                 else if (currentComponentName.equals("IC")) mImageView.setImageResource(R.drawable.chip_mid);
                 else if (currentComponentName.equals("transistor")) mImageView.setImageResource(R.drawable.transistor_new_mid);
                 else if (currentComponentName.equals("motor")) mImageView.setImageResource(R.drawable.motor_mid);
                 else if (currentComponentName.equals("LCD")) mImageView.setImageResource(R.drawable.lcd_datasheet);
            }
        });

        bot_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);// add here your class name
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.videodialog);//add your own xml with defied with and height of videoview
                dialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                lp.copyFrom(dialog.getWindow().getAttributes());
                dialog.getWindow().setAttributes(lp);
                int file = 0;
                if (currentComponentName.equals("button")) file = R.raw.button_demo;
                else if (currentComponentName.equals("trimpot")) file = R.raw.trimpot_demo;
                else if (currentComponentName.equals("potentiometer")) file = R.raw.potentiometer_clip;
                else if (currentComponentName.equals("transistor")) file = R.raw.transistor_lecture2;

                String uriPath= "android.resource://" + getPackageName() + "/" + file;
                final VideoView mVideoView = (VideoView) dialog.findViewById(R.id.fullscreen_videoview);
                Log.v("Vidoe-URI", uriPath+ "");
                mVideoView.setVideoURI(Uri.parse(uriPath));
                mVideoView.start();
            }
        });

        Button qaButton = findViewById(R.id.QAbutton);
        qaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final QADialog dialog = new QADialog(MainActivity.this);// add here your class name

                dialog.show();
            }
        });


        top_text.setText("Component Details");
        mid_text.setText("Some tips will be here");
        bot_text.setText("Example video will be here");

        ambient_top_button.setText("I will put some hints here");
        ambient_bot_button.setText("I will put some hints here");

        top_image.setImageBitmap(null);
        mid_image.setImageBitmap(null);
        bot_image.setImageBitmap(null);

    }

    private Bitmap style_PosNeg(Bitmap input_bmp, Electronics none){
        Bitmap temp;
        if (input_bmp != null) temp = input_bmp;
        else temp = Bitmap.createBitmap(overlay.getWidth(), overlay.getHeight(), Bitmap.Config.ARGB_8888);
        DashPathEffect effect = new DashPathEffect(new float[] { 5, 5 }, 0);
        Canvas canvas = new Canvas(temp);
        Paint black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.BLACK);
        black.setTextSize(30);
        black.setFakeBoldText(true);
        black.setStrokeWidth(8);
        black.setPathEffect(effect);

        Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setColor(Color.RED);
        red.setTextSize(30);
        red.setFakeBoldText(true);
        red.setStrokeWidth(8);
        red.setPathEffect(effect);

        Point pos1 = holes_position[1].get(0);
        Point pos2 = holes_position[13].get(0);
//        String pos_str = "Connect power first";


        Point neg1 = holes_position[0].get(1);
        Point neg2 = holes_position[12].get(1);
//        String neg_str = "Connect ground first ";

        float x_start = 10;
//        float pos_len = red.measureText(pos_str);
//        float neg_len = black.measureText(neg_str);
        if (!checkTwoPins(new Point(0,0), new Point(12,0))) {
//            canvas.drawText(pos_str, x_start, 400, red);
//            canvas.drawText("by red wires", x_start, 430, red);
            canvas.drawLine((float) pos1.x, (float) pos1.y, (float) pos2.x, (float) pos2.y, red);
//            canvas.drawLine((float) pos2.x, (float) pos2.y, x_start + pos_len, 400, red);
        }
        if (!checkTwoPins(new Point(1,1), new Point(13,1))) {
//            canvas.drawText(neg_str, x_start, 500, black);
//            canvas.drawText("by black wires", x_start, 530, black);
            canvas.drawLine((float) neg1.x, (float) neg1.y, (float) neg2.x, (float) neg2.y, black);
//            canvas.drawLine((float) neg2.x, (float) neg2.y, x_start + neg_len, 500, black);
        }
        return temp;

    }

    private Bitmap getICtooltips(Bitmap input_bmp, Electronics IC){
        Bitmap temp;
        if (input_bmp != null) temp = input_bmp;
        else temp = Bitmap.createBitmap(clientResolution.getWidth(), clientResolution.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);

        String datasheet_up [] = {"POWER 5V", "4A", "4Y", "GROUND", "GROUND", "3Y", "3A","3,4EN"};
        String datasheet_down [] = {"1,2EN", "1A", "1Y", "GROUND", "GROUND", "2Y", "2A", "MOTOR POWER 5V"};
        Paint blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue.setColor(Color.BLUE);
        blue.setTextSize(30);
        blue.setFakeBoldText(true);
        blue.setStrokeWidth(5);

        Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setColor(Color.RED);
        red.setTextSize(30);
        red.setFakeBoldText(true);
        red.setStrokeWidth(5);

        float x_start = 250;
        float y_start = 300;

        float x_start2 = 250;
        float y_start2 = 1100;

        for (int i=0; i< datasheet_up.length; i++) {

            Point pin_up = IC.pins.get(i*2);
            Point hole_up = holes_position[(int)pin_up.x].get((int)pin_up.y);

            Point pin_down = IC.pins.get(1+i*2);
            Point hole_down = holes_position[(int)pin_down.x].get((int)pin_down.y);

            canvas.drawText(datasheet_up[i], x_start, y_start, blue);
            float temp1 = blue.measureText(datasheet_up[i]);
            canvas.drawLine((float)hole_up.x + 10, (float)hole_up.y, x_start + temp1/2 , y_start + 20,blue);
            x_start += temp1 + 50;

            canvas.drawText(datasheet_down[i], x_start2, y_start2, red);
            float temp2 = red.measureText(datasheet_down[i]);
            canvas.drawLine((float)hole_down.x + 10, (float)hole_down.y, x_start2 + temp2/2 , y_start2 - 50,red);
            x_start2 += temp2 + 50;
        }
        return temp;
    }
    private Bitmap getLCDTips(Bitmap input_bmp, Electronics LCD){
        Bitmap temp;
        if (input_bmp != null) temp = input_bmp;
        else temp = Bitmap.createBitmap(clientResolution.getWidth(), clientResolution.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(temp);
        String datasheet [] = {"GROUND", "POWER", "ADJUST", "RS", "R/W", "EN", "d4","d5","d6","d7", "POWER", "GROUND"};
        Paint yellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellow.setColor(Color.YELLOW);
        yellow.setTextSize(30);
        yellow.setFakeBoldText(true);
        yellow.setStrokeWidth(5);

        float x_start = 250;
        float y_start = 300;

        for (int i=0; i< LCD.pins.size(); i++) {

            if (i == 6 || i == 7 ||i == 8 ||i == 9) continue;
            Point pin_up = LCD.pins.get(i);
            Point hole_up = holes_position[(int)pin_up.x].get((int)pin_up.y);

            canvas.drawText(datasheet[i], x_start, y_start, yellow);
            float temp1 = yellow.measureText(datasheet[i]);
            canvas.drawLine((float)hole_up.x + 10, (float)hole_up.y, x_start + temp1/2 , y_start + 20, yellow);
            x_start += temp1 + 50;

        }

        return temp;
    }

    private Bitmap updateBasic(Bitmap input_bmp){
        Bitmap temp;
        if (input_bmp != null) temp = input_bmp;
        else temp = Bitmap.createBitmap(overlay.getWidth(), overlay.getHeight(), Bitmap.Config.ARGB_8888);

        overlay.setImageBitmap(temp);
        overlay.setImageAlpha(180);
        Log.d("updates", updates+ "");
        return temp;
    }

    private Bitmap drawResistorValue(Bitmap input_bmp){
        Bitmap temp;
        if (input_bmp != null) temp = input_bmp;
        else temp = Bitmap.createBitmap(clientResolution.getWidth(), clientResolution.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(temp);

        Paint green = new Paint(Paint.ANTI_ALIAS_FLAG);
        green.setColor(Color.RED);
        green.setStrokeWidth(15);
        green.setTextSize(30);
        green.setFakeBoldText(true);

        if (currentComponents.size()>0){
            for (Electronics electronic: currentComponents){
                String name = electronic.name.substring(0,electronic.name.length()-2);
                if (name.equals("resistor")){
                    Point pin1 = electronic.pins.get(0);
                    Point hole1 = holes_position[(int)pin1.x].get((int)pin1.y);
                    Point pin2 = electronic.pins.get(1);
                    Point hole2 = holes_position[(int)pin2.x].get((int)pin2.y);

                    canvas.drawText(electronic.resistor_value +"", (float) (hole1.x + hole2.x)/2, (float)(hole1.y + hole2.y)/2, green);
//                    Point first = electronic.boxes.get(0);
//                    Point second = electronic.boxes.get(1);
//                    canvas.drawText(electronic.resistor_value +"", (float) (first.x + second.x)/2, (float)(first.y + second.y)/2, green);

                }
            }
        }

        return temp;
    }
    private void blinkButton(Button button){
        Animation anim = new AlphaAnimation(0.2f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        button.startAnimation(anim);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.clearAnimation();
                //also your extra work here
            }
        });

    }

    private Bitmap AutoFritz (Bitmap input_bmp, Electronics electronic){

        Bitmap temp;
        if (input_bmp != null) temp = input_bmp;
        else
            temp = Bitmap.createBitmap(autofritz.getWidth(), autofritz.getHeight(), Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(temp);
            Bitmap resistor = BitmapFactory.decodeResource(getResources(), R.drawable.resistor_new);
            DashPathEffect effect = new DashPathEffect(new float[] { 5, 5 }, 0);
            Paint white = new Paint(Paint.ANTI_ALIAS_FLAG);
            white.setColor(Color.WHITE);

//            white.setColor(getResources().getColor(Color.WHITE);
            white.setTextSize(25);
            white.setFakeBoldText(true);
            white.setStrokeWidth(8);
            white.setPathEffect(effect);

            Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
//            red.setColor(getResources().getColor(R.color.opred));
            red.setColor(Color.RED);
            red.setStrokeWidth(8);
            red.setTextSize(25);
            red.setFakeBoldText(true);
            red.setPathEffect(effect);
            Paint black = new Paint(Paint.ANTI_ALIAS_FLAG);
//            black.setColor(getResources().getColor(R.color.opblack));
            black.setColor(Color.BLACK);
            black.setStrokeWidth(8);
            black.setTextSize(25);
            black.setFakeBoldText(true);
            black.setPathEffect(effect);

            Paint yellow = new Paint(Paint.ANTI_ALIAS_FLAG);
            yellow.setColor(Color.YELLOW);
//            yellow.setColor(getResources().getColor(R.color.opyellow));
            yellow.setTextSize(25);
            yellow.setFakeBoldText(true);
            yellow.setStrokeWidth(8);
            yellow.setPathEffect(effect);
            Paint blue = new Paint(Paint.ANTI_ALIAS_FLAG);
            blue.setColor(getResources().getColor(R.color.opblue));
            blue.setTextSize(20);
            blue.setFakeBoldText(true);
            blue.setStrokeWidth(8);
            blue.setPathEffect(effect);


            String name = electronic.name.substring(0, electronic.name.length() - 2);

            if (name.equals("IC")) {
                String datasheet_up[] = {"IC POWER 5V", "", "", "", "", "", "", ""};
                String datasheet_down[] = {"Enable", "Digital", "Motor", "GND", "GND", "Motor", "Digital", "Motor Power 5V"};

                float x_start = 50;
                float y_start = 100;

                float x_start2 = 50;
                float y_start2 = 650;

                for (int i = 0; i < datasheet_up.length; i++) {

                    Point pin_up = electronic.pins.get(i * 2);
                    Point hole_up = holes_position[(int) pin_up.x].get((int) pin_up.y);

                    Point pin_down = electronic.pins.get(1 + i * 2);
                    Point hole_down = holes_position[(int) pin_down.x].get((int) pin_down.y);

                    canvas.drawText(datasheet_up[i], x_start, y_start, blue);
                    float temp1 = blue.measureText(datasheet_up[i]);
                    if(!datasheet_up[i].equals(""))
                        canvas.drawLine((float) hole_up.x + 10, (float) hole_up.y, x_start + temp1 / 2, y_start + 20, blue);
                    x_start += temp1 + 40;

                    canvas.drawText(datasheet_down[i], x_start2, y_start2, red);
                    float temp2 = red.measureText(datasheet_down[i]);
                    canvas.drawLine((float) hole_down.x + 10, (float) hole_down.y, x_start2 + temp2 / 2, y_start2 - 50, red);
                    x_start2 += temp2 + 40;
                }

            } else if (name.equals("button")) {
                Point pin = getOtherPoint(electronic.pins.get(0));
                Point hole = holes_position[(int) pin.x].get((int) pin.y+1);
                Point tmp = getPosNegPoint(pin, POS);
                Point pos = holes_position[(int) tmp.x].get((int) tmp.y);

                Point pin1 = getOtherPoint(electronic.pins.get(2));
                Point hole1 = holes_position[(int) pin1.x].get((int) pin1.y+1);
                Point tmp1 = getPosNegPoint(pin1, NEG);
                Point neg = holes_position[(int) tmp1.x].get((int) tmp1.y);

                Point pin2 = getOtherPoint(electronic.pins.get(3));
                Point hole2 = holes_position[(int) pin2.x].get((int) pin2.y+1);

//                groundHole.x -= 5;
//                neg.y -= 10;
//                powerHole.x -= 5;
//                pos.y -= 10;

                float left = (float) Math.min(hole1.x+5, neg.x);
                float right = (float) Math.max(hole1.x+5, neg.x);
                float top = (float) Math.min(hole1.y+5, neg.y);
                float bottom = (float) Math.max(hole1.y+5, neg.y);

                Matrix matrix = new Matrix();
                matrix.postRotate((float)Math.toDegrees(-Math.atan((right-left)/(bottom-top))));
                Bitmap rotatedImage =  Bitmap.createBitmap(resistor, 0, 0, resistor.getWidth(), resistor.getHeight(), matrix, false);
                //autofritz the resistor to the ground
//                canvas.drawLine((float) hole1.x + 10, (float) hole1.y + 10, (float) neg.x + 10, (float) neg.y, white);
                canvas.drawBitmap(rotatedImage, null, new RectF((float)left, (float)top, (float)right, (float)bottom), new Paint());

                //draw the line connects to 5V
                canvas.drawLine((float) hole.x+5, (float) hole.y+5, (float) pos.x, (float) pos.y, red);

                //draw the point for digitalRead
                canvas.drawRect((float) hole2.x, (float) hole2.y, (float) hole2.x + 15, (float) hole2.y + 15, yellow);
                canvas.drawText("digitalRead", (float) hole2.x - 100, (float) hole2.y + 0, yellow);
                electronic.button.setBackground(ContextCompat.getDrawable(this, R.drawable.button));

            } else if (name.equals("trimpot") || name.equals("potentiometer")) {
                Point pin = getOtherPoint(electronic.pins.get(0));
                Point hole = holes_position[(int) pin.x].get((int) pin.y);
                Point tmp = getPosNegPoint(pin, POS);
                Point pos = holes_position[(int) tmp.x].get((int) tmp.y);

                Point pin1 = getOtherPoint(electronic.pins.get(2));
                Point hole1 = holes_position[(int) pin1.x].get((int) pin1.y);
                Point tmp1 = getPosNegPoint(pin1, NEG);
                Point neg = holes_position[(int) tmp1.x].get((int) tmp1.y);

                Point pin2 = getOtherPoint(electronic.pins.get(1));
                Point hole2 = holes_position[(int) pin2.x].get((int) pin2.y);

                int gap = 0;
                if (electronic.pins.get(1).x >=2 && electronic.pins.get(1).x <= 6) gap = 45;
                else gap = -45;

                //autofritz the resistor to the 5V
                canvas.drawLine((float) hole.x + 10, (float) hole.y + 10, (float) pos.x + 10, (float) pos.y, red);
                //draw the line connects to the ground
                canvas.drawLine((float) hole1.x + 10, (float) hole1.y + 10, (float) neg.x + 10, (float) neg.y, black);
                //draw the point for digitalRead
                canvas.drawRect((float) hole2.x, (float) hole2.y, (float) hole2.x + 15, (float) hole2.y + 15, yellow);
                canvas.drawText("analogRead", (float) hole2.x - 100, (float) hole2.y + gap, yellow);
            } else if (name.equals("LCD")) {
                String datasheet[] = {"GROUND", "POWER", "analog", "RS", "GROUND", "EN", "", "", "", "", "d4", "d5", "d6", "d7", "POWER", "GROUND"};
                int tmp = 0;

                float x_start = 10;
                float y_start = 150;

                for (Point pin : electronic.pins) {
                    Point pinpin = getOtherPointReverse(pin);
                    Point hole = holes_position[(int) pinpin.x].get((int) pinpin.y);
                    Point tmpPos = getPosNegPoint(pin, POS);
                    Point pos = holes_position[(int) tmpPos.x].get((int) tmpPos.y);
                    Point tmpNeg = getPosNegPoint(pin, POS);
                    Point neg = holes_position[(int) tmpNeg.x].get((int) tmpNeg.y);

                    if (tmp == 0 || tmp == 15 || tmp == 4) {
                        float space = black.measureText(datasheet[tmp]);
                        canvas.drawLine((float) hole.x, (float) hole.y, x_start + space / 2, y_start + 10, black);
                        canvas.drawText(datasheet[tmp], x_start, y_start, black);
                        x_start += space + 30;
                    } else if (tmp == 1 || tmp == 14) {
                        float space = red.measureText(datasheet[tmp]);
                        canvas.drawLine((float) hole.x, (float) hole.y, x_start + space / 2, y_start + 10, red);
                        canvas.drawText(datasheet[tmp], x_start, y_start, red);
                        x_start += space + 30;

                    } else if (tmp == 2) {
                        //if there is trimpot in the circuit
                        Point trimpotPin = null;
                        for (Electronics electronics : currentComponents) {
                            String nametmp = electronics.name.substring(0, electronic.name.length() - 2);
                            if (nametmp.equals("trimpot")) trimpotPin = electronics.pins.get(1);
                        }
                        if (trimpotPin == null) {
                            float space = yellow.measureText(datasheet[tmp]);
                            canvas.drawLine((float) hole.x, (float) hole.y, x_start + space / 2, y_start + 10, yellow);
                            canvas.drawText(datasheet[tmp], x_start, y_start, yellow);
                            x_start += space + 30;
                        } else {
                            canvas.drawLine((float) hole.x, (float) hole.y, (float) trimpotPin.x + 10, (float) trimpotPin.y, red);
                        }
                    } else if (tmp == 3 || tmp == 5 || tmp == 10 || tmp == 11 || tmp == 12 || tmp == 13) {
                        float space = yellow.measureText(datasheet[tmp]);
                        canvas.drawLine((float) hole.x + 10, (float) hole.y, x_start + space / 2, y_start + 10, yellow);
                        canvas.drawText(datasheet[tmp], x_start, y_start, yellow);
                        x_start += space + 30;
                    }


                    tmp++;
                }
            }
            else if (name.equals("transistor")) {
                Point pin = getOtherPoint(electronic.pins.get(0));
                Point hole = holes_position[(int) pin.x].get((int) pin.y+1);

                Point pin1 = getOtherPoint(electronic.pins.get(1));
                Point hole1 = holes_position[(int) pin1.x].get((int) pin1.y+1);
                Point tmp = getPosNegPoint(pin1, POS);
                Point pos = holes_position[(int) tmp.x].get((int) tmp.y);

                Point pin2 = getOtherPoint(electronic.pins.get(2));
                Point hole2 = holes_position[(int) pin2.x].get((int) pin2.y+1);
                Point tmp1 = getPosNegPoint(pin2, NEG);
                Point neg = holes_position[(int) tmp1.x].get((int) tmp1.y);

                Bitmap motor = BitmapFactory.decodeResource(getResources(), R.drawable.dc_motor_leg);
//                Bitmap transistor = BitmapFactory.decodeResource(getResources(), R.drawable.transistor_npn);
                float x_start = (float) hole.x - 80;
                float y_start = 100;
//                float left = hole2.x;
//                float right = (float) Math.max(hole2.x, 400) + 10;
                float top = (float) Math.min(hole2.y-100, neg.y+50);
                float bottom = (float) Math.max(hole2.y - 100, neg.y+50);
                canvas.drawBitmap(motor, null, new RectF((float)hole2.x, (float)top, (float)(hole2.x+2.2f*(bottom-top)), (float)bottom), new Paint());
//                canvas.drawBitmap(transistor, null, new RectF((float)electronic.boxes[0], (float)electronic.boxes[0], (float)(hole2.x+2.2f*(bottom-top)), (float)bottom), new Paint());
                canvas.drawLine((float)hole2.x, (float)(hole2.y+20), (float)neg.x, (float)neg.y, yellow);

                canvas.drawText("Base", x_start, y_start, yellow);
                float temp1 = blue.measureText("Base");
                canvas.drawLine((float) hole.x + 10, (float) hole.y, x_start + temp1 / 2, y_start + 20, yellow);
                x_start += blue.measureText("Base") + 20;
                canvas.drawText("analogWrite", (float) hole.x - 140, (float) hole.y , yellow);
                canvas.drawRect((float) hole.x, (float) hole.y, (float) hole.x + 15, (float) hole.y + 15, yellow);

                canvas.drawText("Controller", x_start, y_start, red);
                float temp2 = blue.measureText("Controller");
                canvas.drawLine((float) hole1.x + 10, (float) hole1.y, x_start + temp2 / 2, y_start + 20, red);
                x_start += blue.measureText("Controller") + 20;

                //canvas.drawRect((float)electronic.boxes.get(0).x,(float)electronic.boxes.get(0).y,(float)electronic.boxes.get(1).x,(float)electronic.boxes.get(1).y, yellow);
                canvas.drawText("Emitter", x_start, y_start, black);
                float temp3 = blue.measureText("Emitter");
                canvas.drawLine((float) hole2.x + 10, (float) hole2.y, x_start + temp3 / 2, y_start + 20, black);
//                FrameLayout.LayoutParams rel_btn = (FrameLayout.LayoutParams) electronic.button.getLayoutParams();
//                rel_btn.topMargin = rel_btn.topMargin-50;
//                rel_btn.height = rel_btn.height+50;
//                electronic.button.setLayoutParams(rel_btn);
                electronic.button.setBackground(ContextCompat.getDrawable(this, R.drawable.transistor_npn));
            }
            autofritz.setImageBitmap(temp);
            autofritz.setImageAlpha(185);

        }catch(Exception e){}
        return temp;
    }

    private Bitmap updateOverlay(Bitmap input_bmp, Electronics electronic){
        Bitmap temp;
        if (input_bmp != null) temp = input_bmp;
        else temp = Bitmap.createBitmap(clientResolution.getWidth(), clientResolution.getHeight(), Bitmap.Config.ARGB_8888);

        updateBasic(null);
        AutoFritz(null,electronic);

        return temp;
    }
    private Point getOtherPoint (Point input){
        int x = (int) input.x;
        int y = (int) input.y;

        if (x <= 6 && x >=2){
            for (int i=2; i<7; i++)
                if (!holes_Visual_check[i].get(y))
                    return new Point(i,y);
        }else if (x <= 11 && x >=7){
            for (int i=11 ; i > 6  ; i--)
                if (!holes_Visual_check[i].get(y))
                    return new Point(i,y);
        }

        return new Point(x,y);
    }
    private Point getOtherPointReverse (Point input){
        int x = (int) input.x;
        int y = (int) input.y;

        if (x <= 6 && x >=2){
            for (int i=6; i>1; i--)
                if (!holes_Visual_check[i].get(y))
                    return new Point(i,y);
        }else if (x <= 11 && x >=7){
            for (int i=7 ; i < 12  ; i++)
                if (!holes_Visual_check[i].get(y))
                    return new Point(i,y);
        }

        return new Point(x,y);
    }
    private Point getPosNegPoint(Point input, int KEY){
        int x = (int) input.x;
        int y = (int) input.y;
        int result_x = 1;
        int result_y = 12;

        if (KEY == NEG) {
            if (x >= 2 && x <= 6) result_x = 0;
            else result_x = 12;
        } else if (KEY == POS) {
            if (x >= 2 && x <= 6) result_x = 1;
            else result_x = 13;
        }

        visualizeAllPosition();
        if (x < 2) { }
        else if (x > 11) { }
        else {
            result_y = (int) input.y;
            result_y = result_y * 25 / 30;
            if (!holes_Visual_check[result_x].get(result_y))
                return new Point(result_x,result_y);
            else {
                for (int i = 1; i < 25; i++){
                    int tmp = result_y - i;
                    if (tmp >=0 && tmp <= 24)
                        if (!holes_Visual_check[result_x].get(tmp)) {
                            return new Point(result_x, tmp);
                        }
                    int tmp2 = result_y + i;
                    if (tmp2 >=0 && tmp2 <= 24)
                        if (!holes_Visual_check[result_x].get(tmp2)) {
                            return new Point(result_x, tmp2);
                        }
                }
            }
        }
        return new Point(result_x,result_y);
    }

    private void checkHands(Bitmap bitmap){
        try {
            if(OpenCVLoader.initDebug()) {
                Mat mat = new Mat();
                Utils.bitmapToMat(bitmap, mat);
                Mat ids = new Mat();
                List<Mat> corners = new ArrayList<>();
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
                Aruco.detectMarkers(mat, dictionary, corners, ids);

                int count = 0;
                for (int i=0 ;i < ids.height(); i++){
                    int tmp = (int)ids.get(i,0)[0];
                    if (tmp == 1 ||
                        tmp == 2 ||
                        tmp == 4 ||
                        tmp == 5 ||
                        tmp == 6 ||
                        tmp == 8 ||
                        tmp == 9 ||
                        tmp == 11) count++;
                }
                if (count - pre_code_num >= 5){
//                if (pre_code_num != 15 && count == 15) {
                    commandToServer = "Object_Detect";
                    mCamera.takePicture();
                    Toast.makeText(getApplicationContext(), "update changes", Toast.LENGTH_SHORT).show();
                }
                pre_code_num = count;
                Log.d("checkhands", count+"");
                Log.d("checkhands", ids.height()+"");

            }
        }catch(CvException e){}
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    private Bitmap VisualizeCompoPins(Bitmap input_bmp){
        visualizeAllPosition();
        Log.d("VisualizeHoles", "visualizing");
        Bitmap temp_bmp;
        if (input_bmp != null) temp_bmp = input_bmp;
        else temp_bmp = Bitmap.createBitmap(clientResolution.getWidth(), clientResolution.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(temp_bmp);
        Paint paintCompo = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCompo.setColor(Color.GREEN);

        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < holes_position[i].size(); j++) {
                Point temp = holes_position[i].get(j);
                Point temp2 = new Point(temp.x + 10, temp.y + 10);
                if(holes_Visual_check[i].get(j)) {
                    canvas.drawRect((float) temp.x, (float) temp.y, (float) temp2.x, (float) temp2.y, paintCompo);
                    Log.d("drawRect", "Compo");
                }
            }
        }
        return temp_bmp;
    }
    private Bitmap VisualizeHoles(Bitmap input_bmp){
        visualizeAllPosition();
        Bitmap temp_bmp;
        if (input_bmp != null) temp_bmp = input_bmp;
        else temp_bmp = Bitmap.createBitmap(overlay.getWidth(), overlay.getHeight(), Bitmap.Config.ARGB_8888);

        if(isholesVisualize) {
            Log.d("VisualizeHoles", "visualizing1234");
            Canvas canvas = new Canvas(temp_bmp);

            Paint paintHoles = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintHoles.setColor(Color.YELLOW);
            paintHoles.setAlpha(100);


            Paint paintCompo = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintCompo.setColor(Color.GREEN);

            for (int i = 0; i < 14; i++) {
                for (int j = 0; j < holes_position[i].size(); j++) {
                    Point temp = holes_position[i].get(j);
                    Point temp2 = new Point(temp.x + 10, temp.y + 10);
                    Log.d("VisualizeHoles", temp+"");
                    if(holes_Visual_check[i].get(j)) {
                        canvas.drawRect((float) temp.x, (float) temp.y, (float) temp2.x, (float) temp2.y, paintCompo);
                        Log.d("drawRect", "Compo");
                    }
                    else {
                        canvas.drawRect((float)temp.x, (float)temp.y, (float)temp2.x,(float)temp2.y, paintHoles);
                        Log.d("drawRect", "holes");
                    }

                }
            }
            temp_bmp = style_PosNeg(temp_bmp, null);
            overlay.setImageBitmap(temp_bmp);
            overlay.setImageAlpha(180);
        }
        return temp_bmp;
    }
    private void holes_check_reset(){
        for (int i =0 ; i < hole_lines_size.length; i++)
            for (int j=0; j < hole_lines_size[i]; j++)
                holes_Visual_check[i].set(j,false);
    }
    private void visualizeAllPosition(){
        holes_check_reset();
        Log.d("visualizeAllPosition", "visualizing");
        if(!currentComponents.isEmpty()){
            for (Electronics electronic: currentComponents){
                Log.d("visualizeAllPosition", "visualizing1234");
                for (Point pins : electronic.pins){
                    int i = (int)pins.x;
                    int j = (int)pins.y;
                    holes_Visual_check[i].set(j,true);
                }
            }
        }
    }
    private boolean checkTwoPins(Point first, Point second){
        if (currentComponents.size() <=0) return false;
        visualizeAllPosition();
        int first_1 = (int) first.x;
        int first_2 = (int) first.y;

        int second_1 = (int) second.x;
        int second_2 = (int) second.y;

        if (holes_Visual_check[first_1].get(first_2) &&
            holes_Visual_check[second_1].get(second_2))
            return true;
        else return false;
    }
    private void holes_check_initialize(){
        for (int i =0 ; i < hole_lines_size.length; i++){
            List<Boolean> temp = new ArrayList<Boolean>();
            for (int j=0; j < hole_lines_size[i]; j++) temp.add(false);
            holes_Visual_check[i] = temp;
        }
    }
    private Point ServerToClient(Point point){
        if (serverResolution != null && clientResolution != null){
            int serverWidth = 1280;
            int serverHeight = 960;
            int clientWidth = autofritz.getWidth();
            int clientHeight = autofritz.getHeight();

            Point temp = new Point(point.x/serverWidth * clientWidth, point.y/serverHeight * clientHeight);
            return temp;
        }else return new Point(0,0);

    }
    private class Electronics {
        String name;
        List<Point> boxes;
        List<Point> pins;
        Bitmap myOverlay;
        int resistor_value;
        Button button;

        Electronics(final String name, final List<Point> boxes, final List<Point> pins, final Button button) {
            this.name = name;
            this.boxes = boxes;


            this.pins = pins;
            String nametmp = name.substring(0,name.length()-2);

            if(button != null && (nametmp.equals("IC") || nametmp.equals("potentiometer") || nametmp.equals("trimpot") ||
                    nametmp.equals("motor") || nametmp.equals("button")) || nametmp.equals("transistor")) {

                final String name_tmp = this.name;
                FrameLayout frameLayout = findViewById(R.id.buttonFrame);
                button.setTag(name);

                button.setBackgroundColor(Color.TRANSPARENT);
                FrameLayout.LayoutParams rel_btn = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                rel_btn.leftMargin = (int) boxes.get(0).x;
                rel_btn.topMargin = (int) boxes.get(0).y;

                rel_btn.width = Math.abs((int)(boxes.get(0).x - boxes.get(1).x));
                rel_btn.height = Math.abs((int)(boxes.get(0).y - boxes.get(1).y));
                button.setLayoutParams(rel_btn);
//            button.getBackground().setAlpha(64);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyTaskParams myTaskParams = new MyTaskParams(null, "ButtonPressed", name_tmp);
                        SendToServer sendToServer = new SendToServer();
                        sendToServer.execute(myTaskParams);
                        Log.d("ButtonPressed", "ButtonPressed");

                        updateall(name_tmp);
                        updateOverlay(null, Electronics.this);
                    }
                });
                this.button = button;
                if (frameLayout != null) frameLayout.addView(this.button);
                else Log.d("buttonCreated", "layout is null");
            }

        }
    }

    private void updateall(String input){
        String name = input.substring(0,input.length()-2);
        currentComponentName = name;
        componentTitleTextView.setText(currentComponentName);
        if (name.equals("button")){
            top_text.setText(Html.fromHtml(
                            "<b> Schematic: </b> <br /> A & B are connected, C & D are connected" + "<br />" +
                            "<b>" + "Arduino Pin:</b><br /> Digital Input"));
            mid_text.setText(Html.fromHtml(
                    "<b>" + "Tips:" + "</b>" + "<br />" +
                            "1. Use a pull-up or pull-down resistor"  + "<br />" +
                            "2. The resistance must be bigger than 220 ohms"));
            bot_text.setText(Html.fromHtml(
                    "<b>" + "Lecture Video:" + "</b>" ));

            ambient_top_button.setText("Push the button firmly until it cannot go any further.");
            ambient_bot_button.setText("Please test the button's functionality with Arduino");


            top_image.setImageResource(R.drawable.button_top);
            mid_image.setImageResource(R.drawable.button_mid);
            bot_image.setImageResource(R.drawable.button_video);

        }
        else if (name.equals("trimpot")) {
            top_text.setText(Html.fromHtml(
                            "<b> I/O Type: </b> Input" + "<br />" +
                            "<b>" + "Pin Type:</b> Analog" +
                            "<b>" + "Note:</b> 5V和GND相反，讀取的值會相反"));
            mid_text.setText(Html.fromHtml(
                    "<b>" + "Tips:" + "</b>" + "<br />" +
                            "1. 記得利用analog pin來讀取值\n"  + "<br />" +
                            "2. 正負可以相反"));
            bot_text.setText(Html.fromHtml(
                    "<b>" + "Example video:" + "</b>" +
                            "<br />" + "可以利用trimpot調整LED亮度." + "<br />"));

            ambient_top_button.setText("注意trimpot在麵包版上的空間分配.");
            ambient_bot_button.setText("利用自動生成的程式碼，確保轉動時，analog值會改變.");

            top_image.setImageResource(R.drawable.trimpot_top);
            mid_image.setImageResource(R.drawable.trimpot_mid);
            bot_image.setImageResource(R.drawable.trimpot_video);

        }

        else if (name.equals("transistor")){
            top_text.setText(Html.fromHtml(
                            "<b> Schematic: </b>" + "<br />" +
                                    "As a voltage applied to the base, current flow through collector to emitter" + "<br />" +
                            "<b>" + "Arduino Pin:</b> analogWrite"));
            mid_text.setText(Html.fromHtml(
                    "<b>" + "Tips:" + "</b>" + "<br />" +
                            "1. Use a transistor as a switch."  + "<br />" +
                            "2. Motor should be on the collector-emitter path"));
            bot_text.setText(Html.fromHtml(
                            "<a href=\"https://itp.nyu.edu/physcomp/labs/motors-and-transistors/using-a-transistor-to-control-high-current-loads-with-an-arduino/\">Lecture web and video</a>"));
            bot_text.setMovementMethod(LinkMovementMethod.getInstance());


            ambient_top_button.setText("The black / flat side should face to you");
            ambient_bot_button.setText("Check if the transistor works wit a motor");

            top_image.setImageResource(R.drawable.transistor_top);
            mid_image.setImageResource(R.drawable.transistor_new_mid);
            bot_image.setImageResource(R.drawable.transistor_video_new);

        }

        else if (name.equals("IC")){
            top_text.setText(Html.fromHtml(
                            "<b> 記得IC頭上的半圓形朝左邊。 </b>  <br />" +
                            "<b> 利用Arduino控制1A/2A。</b>  <br />" +
                            "<b> 控制EN大小來調整轉速。 </b> "));
            mid_text.setText(Html.fromHtml(
                    "<b>" + "Tips:" + "</b>" + "<br />" +
                            "1. 控制EN大小來調整轉速"  + "<br />" +
                            "2. 1Y and 2Y connect to motor’s two legs."));
            bot_text.setText(Html.fromHtml(
                    "<b>" + "Speed:" + "</b>" +
                            "<br />" + "你可以對EN pin寫analogWrite。"));

            ambient_top_button.setText("記得8和18都要接上電壓(5V)。");
            ambient_bot_button.setText("確定能利用Aruidno與IC控制馬達方向與轉速，才進下一步");

            top_image.setImageResource(R.drawable.chip_top);
            mid_image.setImageResource(R.drawable.chip_mid);
            bot_image.setImageBitmap(null);
        }

        else if (name.equals("motor")){
            top_text.setText(Html.fromHtml(""));
            mid_text.setText(Html.fromHtml(
                    "<b>" + "Tips:" + "</b>" +
                            "<br />" + "Can be controlled by a transistor or a h-bridge chip" +
                            "<br />" + "Reverse current to reverse rotation"));

            ambient_top_button.setText("Ensure to use an additional power supply to drive a motor");
            ambient_bot_button.setText("Please test it before connecting other components");

            top_image.setImageResource(R.drawable.motor_top);
            mid_image.setImageResource(R.drawable.motor_mid);
//            bot_image.setImageResource(R.drawable.transistor_video);
        }

        else if (name.equals("potentiometer")){
            top_text.setText(Html.fromHtml(
                            "<b> I/O Type: </b> Input" + "<br />" +
                            "<b>" + "Pin Type:</b> Analog"));
            mid_text.setText(Html.fromHtml(
                    "<b>" + "Tips:" + "</b>" + "<br />" +
                            "1. 記得利用analog pin來讀取值\n"  + "<br />" +
                            "2. 正負可以相反"));
            bot_text.setText(Html.fromHtml(
                    "<b>" + "Example video:" + "</b>" +
                            "<br />" + "可以利用trimpot調整LED亮度."));

            ambient_top_button.setText("注意可變電阻在麵包版上的空間分配.");
            ambient_bot_button.setText("利用自動生成的程式碼，確保轉動時，analog值會改變.");

            top_image.setImageResource(R.drawable.potentiometer_top);
            mid_image.setImageResource(R.drawable.potentiometer_mid);
            bot_image.setImageResource(R.drawable.potentiometer_video);
        }

        else if (name.equals("LCD")){
            top_text.setText(Html.fromHtml(
                    "<b>" + "LCD:" + "</b>" + "Please read the datasheet carefully!"+
                            "<br />" +
                            "<b> I/O Type: </b> Output" +
                            "<br />" +
                            "<b>" + "Pin Type:</b> 1 Analog pin and 6 Digital pins"));
            mid_text.setText(Html.fromHtml(
                    "<b>" + "Recommend:" + "</b>" +
                            "<br />" + "Connect LCD in this way! "));
            bot_text.setText(Html.fromHtml(
                    "<b>" + "Example video:" + "</b>" +
                            "<br />" + "Rotate trimpot to adjust LCD contrast!"));

            ambient_top_button.setText("* Carefully follow the overlayed datasheet. Pin4 also goes to digital pin");
            ambient_bot_button.setText("* Remember to Connect trimpot to Pin 3. ex: Trimpot or Potentiometer");

            top_image.setImageResource(R.drawable.lcd_datasheet);
            mid_image.setImageResource(R.drawable.lcd_datasheet);
            bot_image.setImageResource(R.drawable.lcd_video);

        }
        else{ }
        blinkButton(ambient_top_button);
        blinkButton(ambient_bot_button);
    }
    private static class MyTaskParams {
        String command;
        String information;
        Bitmap bitmap;

        MyTaskParams(Bitmap bitmap, String command, String information) {
            this.bitmap = bitmap;
            this.command = command;
            this.information = information;
        }
    }

    private class SendToServer extends AsyncTask<MyTaskParams,Integer,String> {
        Socket socket;
        String getCommand;
        int PORT = 8000;
        PrintWriter printWriter;
        BufferedReader bufferedReader;
        byte [] byteArray;
        String output;

        @Override
        protected String doInBackground(MyTaskParams...params){
            try {
                if(params[0].bitmap != null) {
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        params[0].bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        Log.d("ClientActivity", "change to byte success");
                    }
                    catch (Exception e) { Log.d("ClientActivity", e.getMessage()); }
                }
                socket = new Socket(SERVERIP, PORT);

                if (socket == null)Log.d("ClientActivity", "socket is null");
                if (socket != null) {
                    int cont = 1;
                    while (cont == 1) {
                        try {
//                            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                            if(params[0].bitmap != null) output = Base64.encodeToString(byteArray,Base64.DEFAULT);
                            else if (params[0].information != null) output = params[0].information;
                            String appendix = params[0].command;
                            Log.d("ClientActivity", output + app_num + appendix);
                            printWriter.write(output + app_num + appendix);
                            printWriter.flush();

                        } catch (Exception e) { Log.e("ClientAcivtity: Ex", String.valueOf(e)); }
                        try {
                            Log.d("ClientActivity", "receiving message.....");
                            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                final String msg;
                                msg = (line);
                                getCommand = msg;
//                                parseComponentsFromServer(getCommand);
                                Log.d("DeviceActivity", msg);
                            }
                        } catch (Exception e) { e.printStackTrace(); }
                        cont--;
                    }
                    Log.d("ClientActivity", "C: Closed.");
                }
            } catch (Exception e) { Log.e("ClientAcivtity: Ex", String.valueOf(e)); }
            return getCommand;
        }

        @Override
        protected void onProgressUpdate(Integer ...Values){ }

        @Override
        protected void onPostExecute(String fromserver){
            super.onPostExecute(fromserver);
            try {
                parseComponentsFromServer(fromserver);
            }catch (Exception e){}
        }
    }

    private Point bitmapToFrameLayout(Point input){
        Log.d("trans", input.toString());
        if (preview_frame_height != 0 && preview_frame_width != 0){
            int serverWidth = 1280;
            int serverHeight = 960;
            Log.d("transWidth", serverWidth+"");
            Log.d("transHeight", serverHeight+"");
            Point temp = new Point(input.x * preview_frame_width/serverWidth, input.y * preview_frame_height/serverHeight);
            return temp;
        }else return new Point(0,0);
    }

    private void build_tree(List<Point>[] holes_pos){
        if(holes_pos.length == 14) {
            Log.d("addingtree","holes_pos size:"+Integer.toString(holes_pos.length));
            for (int i=0; i < hole_lines_size.length; i++){
                Log.d("addingtree","holes_pos size:"+i);
                for (int j=0; j < hole_lines_size[i]; j++){
                    Log.d("addingtree","i:"+i + "/j:" + j);
                    Point temp = holes_pos[i].get(j);
                    double [] node = new double [2];
                    node[0] = temp.x;
                    node[1] = temp.y;
                    tree.add(node);
                }
            }
            buildtree = false;
            Log.d("addingtree","build tree success");
        }
    }

    private void parseComponentsFromServer(String input) {
        try {
            Log.d("parseComponentsFromServer", input);
        }catch(Exception e){
            return;
        }
            if (input.equals("none"))return;
        if(input.equals("caliNotSuccess")){
            commandToServer = "calibration__";
            mCamera.takePicture();
            Log.d("calibration", "send again");
            return;
        }

        if (input.length() > 5) {
            if (input.substring(0, 5).equals("calib")) {
                if (buildtree) {
                    Log.d("calibration", input.substring(5));
                    String[] holesString = input.substring(5).split(",");
                    if (holesString.length != 400) return;
                    int count_temp = 0;
                    try {
                        for (int i = 0; i < hole_lines_size.length; i++) {
                            List<Point> temp = new ArrayList<Point>();
                            for (int j = 0; j < hole_lines_size[i]; j++) {
                                String[] x_y = holesString[count_temp].split("-");
                                Point trans = ServerToClient(new Point(Double.valueOf(x_y[0]), Double.valueOf(x_y[1])));

                                //trans.x*= ((float)autofritz.getWidth())/clientResolution.getWidth();
                                //trans.y*= ((float)autofritz.getHeight())/clientResolution.getHeight();
//                                Log.d("autofritz.getWidth()", autofritz.getWidth() + "");
//                                Log.d("autofritz.getHeight()", autofritz.getHeight() + "");
                                Log.d("calibration", trans + "");
                                temp.add(trans);
                                count_temp++;
                            }
                            holes_position[i] = temp;
                        }
//                        parseComponentsFromServer(testInput);;
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                    if (count_temp != 400) {
                        calibrating = true;
                        isholesVisualize = false;
                    } else {
                        calibrating = false;
                        isholesVisualize = true;
                        commandToServer = "";
                        if (buildtree) build_tree(holes_position);
                        Toast.makeText(getApplicationContext(), "calibration success", Toast.LENGTH_SHORT).show();

                        Button temp = findViewById(R.id.calibration);
                        temp.setBackgroundResource(android.R.drawable.btn_default);
                        temp.setVisibility(View.INVISIBLE);
                        snapHandler.post(snap_repeat);
                        componentTitleTextView.setText("Circuit Style");
                        top_text.setText(Html.fromHtml(
                                "<b> 1. </b>Ensure component's polarity is correct before insertion <br />" ));
                        mid_text.setText(Html.fromHtml(
                                "<b> 2. </b>Ensure wires/components are trimmed to lay flat against the breadboard. <br />"));
                        bot_text.setText(Html.fromHtml(
                                "<b> 3. </b> Begin by placing the ICs first and keep pin 1 of all IC's pointing the same direction. <br />" ));

                        ambient_top_button.setText("Avoid crossing wires !");

                        ambient_bot_button.setText("Begin by connecting power and ground rails.");

                        top_image.setImageResource(R.drawable.style_bot);
                        mid_image.setImageResource(R.drawable.style_mid);
                        bot_image.setImageResource(R.drawable.style_top);

                        VisualizeHoles(null);
                        //parseComponentsFromServer(testInput);;
                    }
                    return;
                }
            }
            else if(input.substring(0,5).equals("newcp")){
                String [] input_splitted =  input.substring(5).split("#");
                for (String component_info:input_splitted){
                    String [] name_points = component_info.split(":");
                    String [] points_pins = name_points[1].split("@");
                    String [] pins = points_pins[1].split(",");
                    String [] points = points_pins[0].split(",");
                    ///receive bounding box points
                    List<Point> input_points_list = new ArrayList<Point>();
                    for (String point : points){
                        String [] x_y = point.split("-");

                        Point trans = ServerToClient(new Point(Double.valueOf(x_y[0]), Double.valueOf(x_y[1])));
                        Log.d("boxes_trans", trans.toString());
                        //trans.x*= ((float)autofritz.getWidth())/clientResolution.getWidth();
                        //trans.y*= ((float)autofritz.getHeight())/clientResolution.getHeight();

//                        Log.d("boxes_trans2", bitmapToFrameLayout(trans).toString());
                        input_points_list.add(trans);
                    }
                    ///receive pins points
                    List<Point> input_pins_list = new ArrayList<Point>();
                    for (String pin : pins){
                        String [] x_y = pin.split("-");
                        input_pins_list.add(new Point (Double.valueOf(x_y[0]),Double.valueOf(x_y[1])));
                    }
                    final String tagName = name_points[0];
//                    if(numOfElectronics < 10) tagName = name_points[0] + 0 + numOfElectronics;
//                    else tagName = name_points[0] + numOfElectronics;
                    //=========add new electronic to list ============//
                    Button button = new Button(this);
                    Electronics electronic = new Electronics(tagName, input_points_list, input_pins_list,button);

                    String name = tagName.substring(0,tagName.length()-2);
                    int resistor_value = 0;
                    try {
                        if (name.equals("resistor") && name_points.length == 3) {

                            if (!name_points[2].equals("None")) {
                                resistor_value = Integer.parseInt(name_points[2]);
                                electronic.resistor_value = resistor_value;
                            }
                        }
                    }catch(Exception e){}

                    currentComponents.add(electronic);
//                    currentComponents.add(new Electronics(tagName, input_points_list, input_pins_list));
                    numOfElectronics++;

                    updateOverlay(null,electronic);
                    updateall(tagName);
                    updates++;
                }

                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            else if(input.substring(0,5).equals("movcp")){
                String[] name_points = input.substring(5).split(":");

                int count =0;
                if(currentComponents.size()>=1)
                    for (Electronics electronic : currentComponents)
                        if (!electronic.name.equals(name_points[0]))
                            count+=1;

                if (count == currentComponents.size() || currentComponents.size() ==0){
                    parseComponentsFromServer("newcp" + input.substring(5));
                    return;
                }

                String[] points_pins = name_points[1].split("@");
                final String[] pins = points_pins[1].split(",");
                String[] points = points_pins[0].split(",");
                ///receive bounding box points
                List<Point> input_points_list = new ArrayList<Point>();
                for (String point : points) {
                    String [] x_y = point.split("-");
                    Point trans = bitmapToFrameLayout(new Point (Double.valueOf(x_y[0]), Double.valueOf(x_y[1])));
                    trans.x*= ((float)autofritz.getWidth())/clientResolution.getWidth();
                    trans.y*= ((float)autofritz.getHeight())/clientResolution.getHeight();
                    input_points_list.add(trans);
                }
                ///receive pins points
                List<Point> input_pins_list = new ArrayList<Point>();
                for (String pin : pins) {
                    Log.d("objectdetection", pin);
                    String[] x_y = pin.split("-");
                    input_pins_list.add(new Point(Double.valueOf(x_y[0]), Double.valueOf(x_y[1])));
                }
                for (final Electronics electronic : currentComponents) {
                    Log.d("movcp", "update1");
                    if (electronic.name.equals(name_points[0])) {
                        electronic.pins = input_pins_list;
                        electronic.boxes = input_points_list;

                        ViewGroup layout = (ViewGroup) electronic.button.getParent();
                        if(null!=layout) //for safety only  as you are doing onClick
                            layout.removeView(electronic.button);

                        Button button = new Button (this);
                        final String name_tmp = electronic.name;
                        FrameLayout frameLayout = findViewById(R.id.buttonFrame);
                        button.setTag(electronic.name);
                        button.setBackgroundResource(android.R.drawable.btn_star);
                        button.setBackgroundColor(getResources().getColor(R.color.opwhite));

                        FrameLayout.LayoutParams rel_btn = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        rel_btn.leftMargin = (int) electronic.boxes.get(0).x;
                        rel_btn.topMargin = (int) electronic.boxes.get(0).y;

                        rel_btn.width = Math.abs((int)(electronic.boxes.get(0).x - electronic.boxes.get(1).x));
                        rel_btn.height = Math.abs((int)(electronic.boxes.get(0).y - electronic.boxes.get(1).y));


                        button.setLayoutParams(rel_btn);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyTaskParams myTaskParams = new MyTaskParams(null, "ButtonPressed", name_tmp);
                                SendToServer sendToServer = new SendToServer();
                                sendToServer.execute(myTaskParams);
                                Log.d("ButtonPressed", "ButtonPressed");

                                updateall(name_tmp);
                                updateOverlay(null, electronic);
                            }
                        });
                        electronic.button = button;
                        if (frameLayout != null) frameLayout.addView(button);
                        else Log.d("buttonCreated", "layout is null");

                        Log.d("movcp", "update2");
                        updateOverlay(null, electronic);
                    }
                }
                updateall(name_points[0]);
                updates++;
            }
            else if(input.substring(0,5).equals("oldcp")){
                try {
                    String name = input.substring(5).split(":")[0];

                    int count =0;
                    if(currentComponents.size()>=1)
                        for (Electronics electronic : currentComponents)
                            if (!electronic.name.equals(name))
                                count+=1;
                    if (count == currentComponents.size() || currentComponents.size() ==0){
                        parseComponentsFromServer("newcp" + input.substring(5));
                        return;
                    }

                    int i = 0;
                    for (Electronics electronic : currentComponents) {
                        if (electronic.name.equals(name)) {
                            currentComponents.remove(i);
                            autofritz.setImageBitmap(null);
                            overlay.setImageBitmap(null);
                            ViewGroup layout = (ViewGroup) electronic.button.getParent();
                            if(null!=layout) //for safety only  as you are doing onClick
                                layout.removeView(electronic.button);
                        }
                        i += 1;
                    }
                    numOfElectronics--;
                    updates++;
                }catch(Exception e){}
            }
            else return;
        }else return;
    }


    private int dp(int input){
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                input,
                r.getDisplayMetrics()
        );
        return px;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCamera.open();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mCamera.close();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.destroy();
    }


}
