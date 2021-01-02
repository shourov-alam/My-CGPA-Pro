package com.sh.mycgpapro.Activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sh.mycgpapro.Adapter.CourseAdapter;
import com.sh.mycgpapro.Database.DataBaseHelper;
import com.sh.mycgpapro.Database.DatabaseSource;
import com.sh.mycgpapro.ModelClass.Model_class;
import com.sh.mycgpapro.R;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;

public class Course_Record extends AppCompatActivity {

    int p,s;
    SharedPreferences sharedPreferences;
    FloatingActionButton fab;
    ArrayList<Model_class> arrayList;
    DatabaseSource databaseSource;
    CourseAdapter courseAdapter;
    SwipeMenuListView listView;
    TextView tx;
    Toolbar toolbar;
    String orderBySemester = DataBaseHelper.COL_SEMESTER + " ASC";
    String orderByGradeHigh = DataBaseHelper.COL_GRADE + " DESC";
    String orderByAlphabet = DataBaseHelper.COL_NAME + " COLLATE NOCASE";
    String currentOrder=orderBySemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__record);
        tx=findViewById(R.id.empty_view_text);
        toolbar =  findViewById(R.id.toolbar);
        listView = findViewById(R.id.list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Course Records");

        listView.setTextFilterEnabled(true);
        fab = findViewById(R.id.fab);


        sharedPreferences = getSharedPreferences("course_order", MODE_PRIVATE);
        currentOrder = sharedPreferences.getString("requestCodeValue2", orderBySemester);

        databaseSource = new DatabaseSource(getApplicationContext());
        arrayList = databaseSource.getAllCourse(currentOrder);

        loadData(currentOrder);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater layoutInflater = LayoutInflater.from(Course_Record.this);

                final View view = layoutInflater.inflate(R.layout.course_entry, null);
                final EditText course_name = view.findViewById(R.id.course_name);
                final EditText course_grade = view.findViewById(R.id.course_grade);
                final EditText course_credit = view.findViewById(R.id.course_credit);
                final EditText course_remarks = view.findViewById(R.id.course_remarks);
                final ImageView im= view.findViewById(R.id.refreshID);
                final Spinner semester = view.findViewById(R.id.semester);

                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        course_name.setText("");
                        course_grade.setText("");
                        course_credit.setText("");
                        course_remarks.setText("");
                        semester.setSelection(0);
                    }
                });


          new MaterialAlertDialogBuilder(Course_Record.this, R.style.AlertDialogTheme)

                     .setPositiveButton("Save", null)

                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                            }
                        })

                  .setCancelable(false)
                  .setView(view)
                  .show()

                  .getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if(!TextUtils.isEmpty(course_name.getText().toString()) && !TextUtils.isEmpty(course_credit.getText().toString())&& !TextUtils.isEmpty(course_grade.getText().toString())) {

                      p = semester.getSelectedItemPosition();
                      final int semester_count = spinner_result(p);

                      Model_class model = new Model_class(course_name.getText().toString(), course_remarks.getText().toString(), semester_count,
                              Float.parseFloat(course_credit.getText().toString()), Float.parseFloat(course_grade.getText().toString()));

                      databaseSource = new DatabaseSource(getApplicationContext());

                      Boolean status = databaseSource.addCourse(model);
                      // loadData(currentOrder);

                      if (status) {

                          toastSuccess("Successfully Added");

                          loadData(currentOrder);

                          for(int j=0;j<arrayList.size();j++) {

                              if (arrayList.get(j).getId() == databaseSource.getLastRow()) {
                                  listView.clearFocus();
                                  listView.requestFocusFromTouch();
                                  listView.setSelection(j);

                                  break;

                              }
                          }


                      } else {

                          toastError("Not Added!! Try Again");
                      }

                  }else {
                      if(TextUtils.isEmpty(course_name.getText().toString())){

                          toastError("Enter course name");

                      }
                      if(TextUtils.isEmpty(course_grade.getText().toString())){

                          toastError("Enter course grade");
                      }

                      if(TextUtils.isEmpty(course_credit.getText().toString())){

                          toastError("Enter course credit");
                      }

                  }
              }
          });


            }
        });


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(R.color.list);
                // set item width
                openItem.setWidth(200);
                // set item title
                openItem.setIcon(R.drawable.ic_autorenew_black_24dp);
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(R.color.list);
                // set item width
                deleteItem.setWidth(200);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                final Model_class model = arrayList.get(position);
                switch (index) {
                    case 0:


                        LayoutInflater layoutInflater = LayoutInflater.from(Course_Record.this);

                        final View view = layoutInflater.inflate(R.layout.course_update_row, null);
                        final EditText course_name = view.findViewById(R.id.course_name);
                        final EditText course_grade = view.findViewById(R.id.course_grade);
                        final EditText course_credit = view.findViewById(R.id.course_credit);
                        final EditText course_remarks = view.findViewById(R.id.course_remarks);
                        final Spinner semester = view.findViewById(R.id.semester);


                        if (model != null) {

                            course_name.setText(model.getCourse_name());
                            course_grade.setText(String.valueOf(model.getGrade()));
                            course_credit.setText(String.valueOf(model.getCredit()));
                            course_remarks.setText(model.getCourse_remarks());
                            semester.setSelection(model.getSemester() - 1);

                        }

                        new MaterialAlertDialogBuilder(Course_Record.this, R.style.AlertDialogTheme)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (model != null) {
                                            int id = model.getId();


                                            p = semester.getSelectedItemPosition();

                                            final int semester_count = spinner_result(p);

                                            Model_class model = new Model_class(course_name.getText().toString(), course_remarks.getText().toString(), semester_count, id,
                                                    Float.parseFloat(course_credit.getText().toString()), Float.parseFloat(course_grade.getText().toString()));

                                            databaseSource = new DatabaseSource(getApplicationContext());

                                            Boolean updatedStatus = databaseSource.updateCourse(model);


                                            if (updatedStatus) {

                                                toastSuccess("Successfully Updated");
                                                loadData(currentOrder);
                                                fab.setVisibility(View.VISIBLE);

                                                for(int i=0;i<arrayList.size();i++){
                                                    if(arrayList.get(i).getId()==id){
                                                       // listView.requestFocusFromTouch();
                                                       // listView.setSelection(i);
                                                        listView.clearFocus();
                                                        listView.requestFocusFromTouch();
                                                        listView.setSelection(i);
                                                        break;

                                                    }

                                                }

                                            } else {

                                               toastError("Not Updated!! Try Again");

                                            }

                                        }
                                    }
                                })

                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .setView(view)
                                .show();

                        break;
                    case 1:

                        boolean status = databaseSource.deleteCourse(model);

                        // MainActivity mainActivity=MainActivity.getInstance();

                        //  mainActivity.clear();

                        if (status) {

                            toastSuccess("Deleted");
                            loadData(currentOrder);

                            fab.setVisibility(View.VISIBLE);

                            listView.clearFocus();
                            listView.requestFocusFromTouch();
                            listView.setSelection(position-1);

                        } else {

                           toastError("Not Deleted!! Try Again");
                        }

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                if(mLastFirstVisibleItem < firstVisibleItem){
                    // Scrolling down
                    fab.setVisibility(View.GONE);
                }
                if(mLastFirstVisibleItem > firstVisibleItem){
                    // scrolling up

                    fab.setVisibility(View.VISIBLE);
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });

    }

    private void loadData(String orderBy) {

        currentOrder=orderBy;

        arrayList = databaseSource.getAllCourse(orderBy);

        if(arrayList.size()==0){
            tx.setVisibility(View.VISIBLE);
        }else {
            tx.setVisibility(View.GONE);
        }
        courseAdapter = new CourseAdapter(this, arrayList);
        listView.setAdapter(courseAdapter);

    }
    int spinner_result(int position){

        if(position==0){
            s=1;
        }
        else if(position==1){
            s=2;
        }
        else if(position==2){
            s=3;
        }
        else if(position==3){
            s=4;
        }
        else if(position==4){
            s=5;
        }
        else if(position==5){
            s=6;
        }
        else if(position==6){
            s=7;
        }
        else if(position==7){
            s=8;
        }
        else if(position==8){
            s=9;
        }
        else if(position==9){
            s=10;
        }
        else if(position==10){
            s=11;
        }
        else if(position==11){
            s=12;
        }

        return s;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setQueryHint("Enter course name");

        searchView.setIconifiedByDefault(true);
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v =  searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search_black_24dp);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){

                    try {
                        fab.setVisibility(View.VISIBLE);
                        courseAdapter.filter("");
                        listView.clearTextFilter();
                    }catch (Exception e){

                    }
                }
                else {

                    try {
                        courseAdapter.filter(s);
                        fab.setVisibility(View.GONE);
                    }catch (Exception e){

                    }

                }
                return true;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

       if (id==R.id.action_delete){
            //do your functionality here

           new AlertDialog.Builder(Course_Record.this)

                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           DataBaseHelper dataBaseHelper = new DataBaseHelper(Course_Record.this);

                           SQLiteDatabase sqLiteDatabase=dataBaseHelper.getWritableDatabase();

                           sqLiteDatabase.delete(DataBaseHelper.COURSE_TABLE, null, null);

                           loadData(currentOrder);

                           fab.setVisibility(View.VISIBLE);

                       }
                   })

                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   })

                   .setTitle("Delete All")
                   .setMessage("Are you sure to delete all the records?")
                   .setCancelable(false)
                   .show();


           return true;
        }

       else if (id==R.id.action_refresh) {

           loadData(currentOrder);

        }

      else if (id==R.id.action_sort) {

    show_dialog();

        }

       else if (id==android.R.id.home) {

          onBackPressed();

       }

        return super.onOptionsItemSelected(item);
    }

        private void show_dialog() {


        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater =LayoutInflater.from(Course_Record.this);

        final View view = layoutInflater.inflate(R.layout.sort_layout, null);

        final ImageView img1 =  view.findViewById(R.id.img1);
        final ImageView img2 =  view.findViewById(R.id.img2);
        final ImageView img3 =  view.findViewById(R.id.img3);

        if (currentOrder.equals(orderBySemester)){

            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.INVISIBLE);
        }
        else if (currentOrder.equals(orderByGradeHigh)){

            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.INVISIBLE);
        }
        else if (currentOrder.equals(orderByAlphabet)){

            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.VISIBLE);
        }

        alertBuilder.setView(view);

        final AlertDialog show = alertBuilder.show();
        final LinearLayout semester_row =  view.findViewById(R.id.lin1);
        final LinearLayout grade_row =  view.findViewById(R.id.lin2);
        final LinearLayout alphabet_row =  view.findViewById(R.id.lin3);

        semester_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadData(orderBySemester);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("requestCodeValue2", orderBySemester);
                editor.apply();
                show.dismiss();

            }
        });
        grade_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(orderByGradeHigh);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("requestCodeValue2", orderByGradeHigh);
                editor.apply();
                show.dismiss();

            }
        });
        alphabet_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(orderByAlphabet);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("requestCodeValue2", orderByAlphabet);
                editor.apply();
                show.dismiss();

            }
        });


    }

    void toastSuccess(String message){
        Toasty.success(getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();

    }
    void toastError(String message){
        Toasty.error(getApplicationContext(), message, Toast.LENGTH_LONG, true).show();

    }

}
