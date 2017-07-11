package dev.edmt.todolist;
//fdfdsfsdfs
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Update extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DbHelper dbHelper;

    private EditText nameUpdate;
    private EditText descUpdate;

    private TextView dateUpdate;
    private TextView tCurDesc;
    private TextView tCurDate;

    private String fName;
    private String fDesc;
    private String fDate;

    private int x;
    private int y = 0;
    private int z = 0;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        descUpdate = (EditText) findViewById(R.id.desc);
        descUpdate.setText("");
        nameUpdate = (EditText) findViewById(R.id.name);
        nameUpdate.setText("");

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");

        setTitle(name);
    }

    public void update(View view)
    {
        nameUpdate = (EditText) findViewById(R.id.name);
        descUpdate = (EditText) findViewById(R.id.desc);
        dateUpdate = (TextView) findViewById(R.id.showDate);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id", -1);

        if(String.valueOf(nameUpdate.getText()).length() > 0) {

            for(int x = 0; x < String.valueOf(nameUpdate.getText()).length(); x++) {
                {
                    if (String.valueOf(nameUpdate.getText()).charAt(x) == ' ') {
                        count++;
                    }
                }
            }

            if(count != String.valueOf(nameUpdate.getText()).length()) {
                fName = String.valueOf(nameUpdate.getText());
                z++;
            }
            else {
                y = 1;
                Toast.makeText(this, "Enter a proper name for task", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, Update.class));
                finish();
            }
        }

        if(descUpdate.getText().toString().trim().length() > 0) {
            fDesc = String.valueOf(descUpdate.getText());
            z++;
        }

        if(x == 1) {
            fDate = String.valueOf(dateUpdate.getText());
            z++;
        }

        dbHelper = new DbHelper(this);

        if(y != 1 && z != 0) {
            dbHelper.updateTask(id, fName, fDesc, fDate);

            Toast.makeText(this, "Task successfully updated", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(this, "No change occurs", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void info(View view)
    {
        Intent intent = getIntent();

        String desc = intent.getStringExtra("desc");
        String date = intent.getStringExtra("date");

        AlertDialog dialog = new AlertDialog.Builder(Update.this)

                .setTitle(date)
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
    public void onClick(View v) {
        DatePickerFragment fragment = new DatePickerFragment();

        fragment.show(getSupportFragmentManager(), "Date");
    }

    public void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView)findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));
        x = 1;
    }

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
