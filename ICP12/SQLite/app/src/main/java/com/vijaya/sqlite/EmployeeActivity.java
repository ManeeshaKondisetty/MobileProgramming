package com.vijaya.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployeeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployeeActivity extends AppCompatActivity {

    private ActivityEmployeeBinding binding;
    private static final String TAG = "EmployeeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        String[] queryCols = new String[]{"_id", SampleDBContract.Employer.COLUMN_NAME};
        String[] adapterCols = new String[]{SampleDBContract.Employer.COLUMN_NAME};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                queryCols,                                // The columns to return
                null,                             // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.employerSpinner.setAdapter(cursorAdapter);

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFromDB();
            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFromDB();
            }
        });


    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, binding.firstnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_LASTNAME, binding.lastnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, binding.jobDescEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0));

        Log.d("getINT", ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0) + "");
        Log.d("getColumnName", ((Cursor) binding.employerSpinner.getSelectedItem()).getColumnName(0));

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.dobEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, date);

            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.employedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employee.TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
        resetViews();
    }

    private void readFromDB() {
        String firstname = binding.firstnameEditText.getText().toString();
        String lastname = binding.lastnameEditText.getText().toString();

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] selectionArgs = {"%" + firstname + "%", "%" + lastname + "%"};

        Cursor cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE_WITH_EMPLOYER, selectionArgs);
        binding.recycleView.setAdapter(new SampleJoinRecyclerViewCursorAdapter(this, cursor));
    }

    private void deleteFromDB() {
        String firstname = binding.firstnameEditText.getText().toString();
        String lastname = binding.lastnameEditText.getText().toString();

        if (firstname.isEmpty() || lastname.isEmpty()) {
            Toast.makeText(this, "First Name and Last Name are mandatory to delete.", Toast.LENGTH_LONG).show();

        } else {
            SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
            String table = SampleDBContract.Employee.TABLE_NAME;
            String whereClause = SampleDBContract.Employee.COLUMN_FIRSTNAME + "=? and " + SampleDBContract.Employee.COLUMN_LASTNAME + "=?";
            String[] whereArgs = new String[]{firstname, lastname};
            database.delete(table, whereClause, whereArgs);

            readFromDB();
            Toast.makeText(this, "Deleted user: " + firstname + " " + lastname, Toast.LENGTH_LONG).show();
            resetViews();
        }
    }

    private void updateFromDB() {

        ContentValues cv = new ContentValues();
        String employeeFirstName = binding.firstnameEditText.getText().toString();
        String employeeLastName = binding.lastnameEditText.getText().toString();
        String employeeJobDesc = binding.jobDescEditText.getText().toString();
        String employeeDOB = binding.dobEditText.getText().toString();
        String employeeEmployed = binding.employedEditText.getText().toString();
        if (employeeFirstName.isEmpty()) {
            Toast.makeText(this, "First Name is mandatory to update its content.", Toast.LENGTH_LONG).show();
        } else {
            if (!employeeLastName.isEmpty()) {
                cv.put(SampleDBContract.Employee.COLUMN_LASTNAME, employeeLastName);
            }
            if (!employeeJobDesc.isEmpty()) {
                cv.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, employeeJobDesc);
            }
            if (!employeeDOB.isEmpty()) {
                cv.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, employeeDOB);
            }
            if (!employeeEmployed.isEmpty()) {
                cv.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, employeeEmployed);
            }

            SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();

            String whereClause = SampleDBContract.Employee.COLUMN_FIRSTNAME + "=?";
            String[] whereArgs = new String[]{String.valueOf(employeeFirstName)};

            boolean isUpdate = database.update(SampleDBContract.Employee.TABLE_NAME, cv, whereClause, whereArgs) > 0;
            Toast.makeText(this, "Updated Employee details for: " + employeeFirstName, Toast.LENGTH_LONG).show();
            resetViews();
            readFromDB();
        }
    }

    private void resetViews() {
        binding.firstnameEditText.setText("");
        binding.lastnameEditText.setText("");
        binding.jobDescEditText.setText("");
        binding.dobEditText.setText("");
        binding.employedEditText.setText("");


    }
}