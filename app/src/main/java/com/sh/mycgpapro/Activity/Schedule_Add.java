package com.sh.mycgpapro.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.sh.mycgpapro.BroadCast.AlarmReceiver;
import com.sh.mycgpapro.Database.DatabaseSource_Schedule;
import com.sh.mycgpapro.ModelClass.Schedule_Model;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.sh.mycgpapro.R;
import es.dmoral.toasty.Toasty;

public class Schedule_Add extends AppCompatActivity {

    ImageView imageView,bt1,bt, bt2;
    TextView title, des, date;
    CheckBox checkBox,checkBox1;
    Spinner spinner;
    Button button;
    boolean cleared=false;
    private String[] cameraPermissions;
    AlarmManager alarmManager;
    DatePicker simpleDatePicker;
    PendingIntent pendingIntent;
    TimePicker simpleTimePicker;
    SharedPreferences sharedPreferences;
    String s="";
    String e1,image1,spinner_position;
    boolean d,pk,flag1;
    boolean flag = false;
    int requestCode = 1;
    private Uri imageUri;
    Intent intent;
    Toolbar toolbar;
    int p,final_position,id;
    Calendar ca,c,cale;
    DatabaseSource_Schedule databaseSource;
    long milliseconds;
    int k;
    String strDate="";

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int IMAGE_PICK_CAMERA_CROPLESS_CODE = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule__add);
        imageView = findViewById(R.id.imageID);
        title = findViewById(R.id.titleID);
        des = findViewById(R.id.desID);
        date = findViewById(R.id.textID);
        checkBox = findViewById(R.id.checkBox);
        checkBox1 = findViewById(R.id.checkBox1);
        spinner = findViewById(R.id.spinnerID);
        button = findViewById(R.id.btnID);
        toolbar = findViewById(R.id.toolbar);
        ca= c = Calendar.getInstance();
        c = Calendar.getInstance();
        cale =Calendar.getInstance();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                                                    if(isChecked){

                                                        spinner.setVisibility(View.VISIBLE);
                                                        checkBox1.setVisibility(View.VISIBLE);
                                                    }else {
                                                        spinner.setVisibility(View.GONE);
                                                        checkBox1.setVisibility(View.GONE);
                                                        checkBox1.setChecked(false);

                                                    }


                                                }
                                            }

        );

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            String topic = bundle.getString("key");

            if(topic.equals("update")){
                String title1=bundle.getString("title");
                String des1=bundle.getString("des");
                String date1=bundle.getString("date");
                String check1=bundle.getString("check");
                String check2=bundle.getString("ring");
                image1=bundle.getString("image");
                k=bundle.getInt("position");
                String before_check=bundle.getString("before");

                if(!image1.equals("null")) {

                    Glide.with(getApplicationContext()).load(image1).into(imageView);

                }
                title.setText(title1);
                des.setText(des1);
                date.setText(date1);


                if(check1.equals("true")){
                    checkBox.setChecked(true);

                    if(Integer.parseInt(before_check)<=5){
                        spinner.setSelection(Integer.parseInt(before_check));

                    }else if(Integer.parseInt(before_check) == 7){
                        spinner.setSelection(6);

                    }
                    else if(Integer.parseInt(before_check) == 10){
                        spinner.setSelection(7);

                    }

                    else if(Integer.parseInt(before_check) == 14){
                        spinner.setSelection(8);

                    }


                    spinner.setVisibility(View.VISIBLE);
                }
                else {

                    checkBox.setChecked(false);
                }

                if(check2.equals("true")){
                    checkBox1.setChecked(true);
                    checkBox1.setVisibility(View.VISIBLE);

                }

                else {
                    checkBox1.setVisibility(View.VISIBLE);
                    checkBox1.setChecked(false);

                }

                id=bundle.getInt("id");
                button.setText("Update");

            }

        }
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(Schedule_Add.this);

                final View view = layoutInflater.inflate(R.layout.date_time, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(Schedule_Add.this).create();

                alertDialog.setTitle("Select Date & Time");
                // alertDialog.setIcon("Icon id here");
                alertDialog.setCancelable(true);
                //  Constant.alertDialog.setMessage("Your Message Here");

                simpleDatePicker =  view.findViewById(R.id.simpleDatePicker);
                simpleTimePicker =  view.findViewById(R.id.simpleTimePicker);
                bt = view.findViewById(R.id.refresh);
                bt1 = view.findViewById(R.id.dtp);
                bt2 = view.findViewById(R.id.tmp);
                bt1.setImageDrawable(getResources().getDrawable(R.drawable.ic_today_green_24dp));

                if(date.getText().toString().equals("Select Date and Time")){

                    flag = true;
                    simpleDatePicker.init(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), cale.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            flag1 = true;
                            cale.set(simpleDatePicker.getYear(), simpleDatePicker.getMonth(), simpleDatePicker.getDayOfMonth());

                            SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                            strDate = format1.format(cale.getTime());


                        }
                    });

                    cale.set(simpleDatePicker.getYear(), simpleDatePicker.getMonth(), simpleDatePicker.getDayOfMonth());

                    SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                    strDate = format1.format(cale.getTime());


                    simpleTimePicker.setCurrentHour(cale.get(Calendar.HOUR_OF_DAY));
                    simpleTimePicker.setCurrentMinute(cale.get(Calendar.MINUTE));

                }else {

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                    Date date2 = null;
                    try {

                        date2 = sdf1.parse(date.getText().toString());
                    }
                    catch (Exception e) {
                    }

                    c.setTime(date2);


                            simpleDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            flag = true;
                            c.set(simpleDatePicker.getYear(), simpleDatePicker.getMonth(), simpleDatePicker.getDayOfMonth());
                            SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                            strDate = format1.format(c.getTime());

                        }
                    });

                    c.set(simpleDatePicker.getYear(), simpleDatePicker.getMonth(), simpleDatePicker.getDayOfMonth());
                    SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                    strDate = format1.format(c.getTime());
                    simpleTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                    simpleTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));

                }

                simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                        e1 = simpleTimePicker.getCurrentHour() + ":" +  simpleTimePicker.getCurrentMinute();
                        try {
                            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            final Date dateObj = sdf.parse(e1);

                            // System.out.println(dateObj);
                            s = new SimpleDateFormat("hh:mm aaa").format(dateObj);

                        }
                        catch (final ParseException e) {
                            e.printStackTrace();
                        }
                    }

                });


                e1 = simpleTimePicker.getCurrentHour() + ":" +  simpleTimePicker.getCurrentMinute();

                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    final Date dateObj = sdf.parse(e1);
                    s = new SimpleDateFormat("hh:mm aaa").format(dateObj);

                }
                catch (final ParseException e) {
                    e.printStackTrace();
                }

                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bt1.setImageDrawable(getResources().getDrawable(R.drawable.ic_today_green_24dp));
                        bt2.setImageDrawable(getResources().getDrawable(R.drawable.ic_access_time_black_24dp));


                        simpleDatePicker.setVisibility(View.VISIBLE);
                        simpleTimePicker.setVisibility(View.INVISIBLE);

                    }
                });
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        bt1.setImageDrawable(getResources().getDrawable(R.drawable.ic_today_black_24dp));
                        bt2.setImageDrawable(getResources().getDrawable(R.drawable.ic_access_time_green_24dp));


                        simpleDatePicker.setVisibility(View.INVISIBLE);
                        simpleTimePicker.setVisibility(View.VISIBLE);

                    }
                });
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (simpleDatePicker.getVisibility() == View.VISIBLE) {

                            if(date.getText().toString().equals("Select Date and Time")){

                                simpleDatePicker.init(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                                    @Override
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    }
                                });

                                ca.set(simpleDatePicker.getYear(), simpleDatePicker.getMonth(), simpleDatePicker.getDayOfMonth());

                                SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                                strDate = format1.format(ca.getTime());


                            }

                            else {

                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                                Date date3 = null;
                                try {
                                    date3 = sdf1.parse(date.getText().toString());
                                }
                                catch (Exception e) {
                                }



                                ca.setTime(date3);
                                simpleDatePicker.init(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                                    @Override
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    }
                                });

                                ca.set(simpleDatePicker.getYear(), simpleDatePicker.getMonth(), simpleDatePicker.getDayOfMonth());

                                SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                                strDate = format1.format(ca.getTime());


                            }

                        }


                        if (simpleTimePicker.getVisibility() == View.VISIBLE) {

                            if(date.getText().toString().equals("Select Date and Time")) {

                                simpleTimePicker.setCurrentHour(ca.get(Calendar.HOUR_OF_DAY));
                                simpleTimePicker.setCurrentMinute(ca.get(Calendar.MINUTE));

                            }
                            else {
                                simpleTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                                simpleTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));


                            }

                        }

                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String n = new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime());
                        String g = new SimpleDateFormat("dd MMM yyyy").format(Calendar.getInstance().getTime());


                      if(!strDate.equals("")&& !s.equals("")){

                          date.setText(strDate+ " " +s );


                        }else if(!strDate.equals("")&& s.equals("")) {
                            date.setText(strDate + " " + n);
                        }
                      else if(strDate.equals("")&& !s.equals("")) {
                          date.setText(g+ " " + s);
                      }


                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();

                    }
                });

                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });


                alertDialog.setView(view);
                alertDialog.show();

            }

        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_picker();
            }
        });

        sharedPreferences = getSharedPreferences("alarm_code", MODE_PRIVATE);
        requestCode = sharedPreferences.getInt("requestCodeValue", 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(title.getText().toString())) {


                    if (spinner.getVisibility() == View.VISIBLE) {
                        p = spinner.getSelectedItemPosition();


                        spinner_position = String.valueOf(spinner_selection(p));

                    } else {
                        spinner_position = "null";

                    }


                    long j;
                    if (spinner_position.equals("null")) {
                        j = 0;

                    } else {

                        j = 86400000 * Integer.parseInt(spinner_position);
                    }


                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                    Calendar calendar = Calendar.getInstance();
                    Calendar cal = Calendar.getInstance();


                    try {


                        calendar.setTime(sdf.parse(date.getText().toString()));

                        int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
                        int monthForAlarm = calendar.get(Calendar.MONTH);
                        int yearForAlarm = calendar.get(Calendar.YEAR);
                        int hourForAlarm = calendar.get(Calendar.HOUR_OF_DAY);
                        int minuteForAlarm = calendar.get(Calendar.MINUTE);
                        int secondForAlarm = 0;

                        cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (checkBox.isChecked()) {

                        if (date.getText().toString().equals("Select Date and Time")) {

                            toastError("Select Date and Time");

                        }
                        else {
                            if (cal.getTimeInMillis() - j >= Calendar.getInstance().getTimeInMillis()) {

                                call();
                            } else {
                                toastError("Schedule time must greater than current time!!");
                            }
                        }


                    } else {

                        if (date.getText().toString().equals("Select Date and Time")) {

                            call();

                        } else {
                            if (cal.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis()) {
                                call();
                            } else {
                                toastError("Schedule time must greater than current time!!");
                            }
                        }

                    }


                }else {


                    toastError("Enter a Title of Schedule");

                }

            }

        });

    }

    void call(){

        if (button.getText().toString().equals("Update")) {
                String flags, final_image,flags1;
                if (checkBox.isChecked()) {

                    flags = "true";
                } else {

                    flags = "false";
                }


                if(checkBox1.getVisibility()==View.VISIBLE){

                    if (checkBox1.isChecked()) {

                        flags1 = "true";
                    } else {

                        flags1 = "false";
                    }

                }else {
                    flags1 = "false";
                }



                if (String.valueOf(imageUri).equals("null") ) {

                    if(!cleared){
                        final_image = image1;
                    }else {
                        final_image=String.valueOf(imageUri);
                        cleared=false;
                    }
                }
                else {

                    final_image = String.valueOf(imageUri);
                }

                try {
                    DateFormat f = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                    Date d = f.parse(date.getText().toString());
                    milliseconds= d.getTime();

                } catch (Exception e) {

                }


                Schedule_Model model = new Schedule_Model(flags1,title.getText().toString(), des.getText().toString(), date.getText().toString(),
                        flags, final_image, id, spinner_position,String.valueOf(milliseconds));


                databaseSource = new DatabaseSource_Schedule(getApplicationContext());
                Boolean updatedStatus = databaseSource.updateSchedule(model);


                if (updatedStatus) {

                    d=true;


                    toastSuccess("Successfully Updated");


                } else {

                    Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_LONG).show();
                    toastError("Not Updated!! Try Again");
                }


                clear();


            } else {

                String flag,flags1;
                if (checkBox.isChecked()) {

                    flag = "true";
                } else {

                    flag = "false";
                }
                if(checkBox1.getVisibility()==View.VISIBLE){

                    if (checkBox1.isChecked()) {

                        flags1 = "true";
                    } else {

                        flags1 = "false";
                    }

                }else {
                    flags1 = "false";
                }
                try {
                    DateFormat f = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                    Date d = f.parse(date.getText().toString());
                    milliseconds= d.getTime();

                } catch (Exception e) {

                }


                Schedule_Model model = new Schedule_Model(flags1,title.getText().toString(), des.getText().toString(),
                        date.getText().toString(), flag, String.valueOf(imageUri), spinner_position,String.valueOf(milliseconds));

                databaseSource = new DatabaseSource_Schedule(getApplicationContext());

                Boolean status = databaseSource.addSchedule(model);



                if (status) {
                     pk=true;

                    toastSuccess("Successfully Added");


                } else {


                    toastError("Not Added!! Try Again");

                }

                clear();

        }

    }

    private int spinner_selection(int position) {
        if(position==0){

            final_position=0;
        }
        if(position==1){

            final_position=1;

        }
        if(position==2){

            final_position=2;
        }
        if(position==3){

            final_position=3;
        }
        if(position==4){
            final_position=4;

        }
        if(position==5){
            final_position=5;

        }
        if(position==6){

            final_position=7;
        }
        if(position==7){
            final_position=10;

        }
        if(position==8){
            final_position=14;

        }
  return final_position;

    }


    private boolean checkCameraPermission() {


        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);

    }

    void image_picker() {

        new AlertDialog.Builder(Schedule_Add.this)
                .setTitle("Image Capture")
                .setMessage("Do you want to capture image related with your Schedule?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (!checkCameraPermission()) {

                                    requestCameraPermission();


                            } else {

                                pickCamCropLess();
                            }
                        }else {

                            pickCamCropLess();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


    }

    private void pickCamCropLess() {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CROPLESS_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (resultCode == RESULT_OK) {

            if(requestCode==IMAGE_PICK_CAMERA_CROPLESS_CODE){

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 900, 1000, true);

                    imageView.setImageBitmap(resized);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void setAlarm2() {

        sharedPreferences = getSharedPreferences("alarm_code", MODE_PRIVATE);
        requestCode = sharedPreferences.getInt("requestCodeValue", 1);

        long l;
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        try {

            for (int j = 1; j < requestCode; j++) {
                //   intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), j, intent, 0);

                if(pendingIntent!=null){

                    alarmManager.cancel(pendingIntent);
                }

            }

        }catch (Exception e){

        }

        ArrayList<Schedule_Model> list;
        list = databaseSource.getAllSchedule();

        requestCode=1;
        for (int i = 0; i < list.size(); i++) {

            Schedule_Model model = list.get(i);
            String date = model.getDate();
            String checking=model.getIs_notification();
            String a=model.getBefore_day();
            if(date.equals("Select Date and Time")){
                continue;
            }
            if(a.equals("null") || a.equals("0")){
                l=0;
            }
           else {

               l=Integer.valueOf(a) * 86400000;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();


            try {
                calendar.setTime(sdf.parse(date));

                int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
                int monthForAlarm = calendar.get(Calendar.MONTH);
                int yearForAlarm = calendar.get(Calendar.YEAR);
                int hourForAlarm = calendar.get(Calendar.HOUR_OF_DAY);
                int minuteForAlarm = calendar.get(Calendar.MINUTE);
                int secondForAlarm = 0;

                cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ( cal.getTimeInMillis() - l >= Calendar.getInstance().getTimeInMillis() && checking.equals("true")) {

                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("titleName", model.getTitle());
                intent.putExtra("beforeDate", model.getBefore_day());
                intent.putExtra("id", model.getId());
                intent.putExtra("flag", "true");
                intent.putExtra("ring", model.getRing());

                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-l, pendingIntent);
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-l, pendingIntent);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-l, pendingIntent);

                requestCode++;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("requestCodeValue", requestCode);
                editor.apply();

            }

        }

    }
    public void clear() {

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {

                setAlarm2();

            }
        });
        myThread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId() == R.id.action_refresh1) {

           imageView.setImageResource(0);
           imageUri=null;
           cleared=true;
           title.setText("");
           des.setText("");
           date.setText("Select Date and Time");
           checkBox.setChecked(false);
           checkBox1.setChecked(false);
           spinner.setSelection(0);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(d){

    Intent intent = new Intent(getApplicationContext(),Schedule_Record.class);
            intent.putExtra(Schedule_Record.TAG_ACTIVITY_FROM,"update");
            intent.putExtra("idu",id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    startActivity(intent);


    d=false;

        }else if(pk){

            Intent intent1 = new Intent(getApplicationContext(),Schedule_Record.class);
            intent1.putExtra(Schedule_Record.TAG_ACTIVITY_FROM,"add");

            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent1);


            pk=false;

        }else {

    super.onBackPressed();

        }

    }

    void toastSuccess(String message){
        Toasty.success(getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();

    }
    void toastError(String message){
        Toasty.error(getApplicationContext(), message, Toast.LENGTH_LONG, true).show();

    }

}


