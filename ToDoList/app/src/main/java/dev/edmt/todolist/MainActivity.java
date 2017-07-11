package dev.edmt.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.jar.JarEntry;

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstTask;

    private int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emptymain);

        dbHelper = new DbHelper(this);

        if(dbHelper.checkIfEmpty() > 0)
        {
            setContentView(R.layout.activity_main);

            lstTask = (ListView) findViewById(R.id.lstTask);

            loadTaskList();
        }
    }

    private void loadTaskList() {

        if(dbHelper.checkIfEmpty() <= 0)
        {
            setContentView(R.layout.emptymain);
        }
        final ArrayList<String> taskList = dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter = new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,taskList);
            lstTask.setAdapter(mAdapter);

        }
        else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        lstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long l) {
                final int id = dbHelper.getID(position);
                final String task = dbHelper.getName(id);
                final String desc = dbHelper.getDesc(id);
                final String date = dbHelper.getDate(id);

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(task)
                        .setMessage(date + "\n\n" + desc)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        lstTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                final int id = dbHelper.getID(position);
                final String task = dbHelper.getName(id);
                final String desc = dbHelper.getDesc(id);
                final String date = dbHelper.getDate(id);

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(task)
                        .setMessage("What do you want to do next?")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, Update.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", task);
                                intent.putExtra("desc", desc);
                                intent.putExtra("date", date);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteTask(id);
                                Toast.makeText(MainActivity.this, "Task successfully deleted", Toast.LENGTH_LONG).show();
                                loadTaskList();
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });
    }

    public void add(View view)
    {
        startActivity(new Intent(this, AddTask.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_task:
                startActivity(new Intent(this, AddTask.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
