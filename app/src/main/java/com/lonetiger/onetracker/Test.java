package com.lonetiger.onetracker;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lonetiger.onetracker.View.MainActivity;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Test extends AppCompatActivity {

    TextView dob;
    final Calendar myCalendar = Calendar.getInstance();
    RadioGroup rb;

    EditText currencyTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dob = (TextView)findViewById(R.id.birth_date);
        rb = (RadioGroup) findViewById(R.id.rdg);
        currencyTxt = (EditText)findViewById(R.id.currency);

      //--

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();

                //----
                String myFormat = "dd/MM/YYYY"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()));
                dob.setTextColor(Color.BLACK);


                //----------
            }

        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new DatePickerDialog(Test.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //Toast.makeText(Test.this,String.valueOf(checkedId),Toast.LENGTH_LONG).show();
                switch (checkedId) {

                    case R.id.male_radio:
                        Toast.makeText(Test.this,"Male",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.female_radio:
                        Toast.makeText(Test.this,"Female",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.other_radio:
                        Toast.makeText(Test.this,"Other",Toast.LENGTH_LONG).show();
                        break;

                }
            }

        });


        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
        picker.getTheme();
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                // Implement your code here

                currencyTxt.setText(name);
                picker.dismiss();
            }
        });




        currencyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
    }
});


        //-----------


    }
}
