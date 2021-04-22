package com.vijaya.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployerBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployerActivity extends AppCompatActivity {

    private static final String TAG = "EmployerActivity";
    private ActivityEmployerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employer);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

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
        values.put(SampleDBContract.Employer.COLUMN_NAME, binding.nameEditText.getText().toString());
        values.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, binding.descEditText.getText().toString());

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employer.TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
        resetViews();
    }

    private void readFromDB() {
        String name = binding.nameEditText.getText().toString();
        String desc = binding.descEditText.getText().toString();
        long date = 0;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
        } catch (Exception e) {
        }

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = {
                SampleDBContract.Employer._ID,
                SampleDBContract.Employer.COLUMN_NAME,
                SampleDBContract.Employer.COLUMN_DESCRIPTION,
                SampleDBContract.Employer.COLUMN_FOUNDED_DATE
        };

        String selection =
                SampleDBContract.Employer.COLUMN_NAME + " like ? and " +
                        SampleDBContract.Employer.COLUMN_FOUNDED_DATE + " > ? and " +
                        SampleDBContract.Employer.COLUMN_DESCRIPTION + " like ?";

        String[] selectionArgs = {"%" + name + "%", date + "", "%" + desc + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        binding.recycleView.setAdapter(new SampleRecyclerViewCursorAdapter(this, cursor));
    }

    private void deleteFromDB() {
        String companyName = binding.nameEditText.getText().toString();

        if (companyName.isEmpty()) {
            Toast.makeText(this, "Company Name is mandatory to delete", Toast.LENGTH_LONG).show();
        } else {
            SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
            String table = SampleDBContract.Employer.TABLE_NAME;
            String whereClause = SampleDBContract.Employer.COLUMN_NAME + "=?";
            String[] whereArgs = new String[]{String.valueOf(companyName)};
            database.delete(table, whereClause, whereArgs);
            Toast.makeText(this, "Deleted company: " + companyName, Toast.LENGTH_LONG).show();
            resetViews();
            readFromDB();
        }
    }

    private void updateFromDB() {
        ContentValues cv = new ContentValues();
        String companyName = binding.nameEditText.getText().toString();
        String companyDescription = binding.descEditText.getText().toString();
        String companyFounded = binding.foundedEditText.getText().toString();
        if (companyName.isEmpty()) {
            Toast.makeText(this, "Company Name is mandatory to update its content.", Toast.LENGTH_LONG).show();
        } else {
            if (!companyDescription.isEmpty()) {
                cv.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, companyDescription);
            }
            if (!companyFounded.isEmpty()) {
                cv.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, companyFounded);
            }

            SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();

            String whereClause = SampleDBContract.Employer.COLUMN_NAME + "=?";
            String[] whereArgs = new String[]{String.valueOf(companyName)};

            boolean isUpdate = database.update(SampleDBContract.Employer.TABLE_NAME, cv, whereClause, whereArgs) > 0;
            Toast.makeText(this, "Updated company description: " + companyName, Toast.LENGTH_LONG).show();
            resetViews();
            readFromDB();
        }

    }

    private void resetViews() {
        binding.nameEditText.setText("");
        binding.descEditText.setText("");
        binding.foundedEditText.setText("");
    }
}