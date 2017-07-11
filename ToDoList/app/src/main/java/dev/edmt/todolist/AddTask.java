package dev.edmt.todolist;
//Hello
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DbHelper dbHelper;

    private EditText nameAdd;
    private EditText descAdd;
    private TextView dateAdd;

    private String fName;
    private String fDesc;
    private String fDate;

    private int x;
    private int z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    public void add(View view)
    {
        nameAdd = (EditText) findViewById(R.id.name);
        descAdd = (EditText) findViewById(R.id.desc);
        dateAdd = (TextView) findViewById(R.id.showDate);

        fName = String.valueOf(nameAdd.getText());


        fDesc = String.valueOf(descAdd.getText());

        if(fDesc == "")
        {
            fDesc = "No description";
        }

        if(x == 1) {
            fDate = String.valueOf(dateAdd.getText());
        }
        else
        {
            fDate = "No date";
        }

        dbHelper = new DbHelper(this);

        int count = 0;

        for(int x = 0; x < fName.length(); x++)
        {
            if(fName.charAt(x) == ' ')
            {
                count++;
            }
        }

        if(count == fName.length())
        {
            Toast.makeText(this, "Enter a proper name for task", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, AddTask.class));
            finish();
        }
        else {
            Toast.makeText(this, "Task successfully added", Toast.LENGTH_LONG).show();
            dbHelper.insertNewTask(fName, fDesc, fDate);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void onClick(View v) {
        DatePickerFragment fragment = new DatePickerFragment();

        fragment.show(getSupportFragmentManager(), "Date");
    }

     public void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        ((TextView)findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));

         x = 1;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
        setDate(cal);
    }

    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
    {
        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState)
        {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(),year, month, day);
        }
    }

}
