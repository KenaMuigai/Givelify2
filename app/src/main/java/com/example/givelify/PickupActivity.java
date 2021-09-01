package com.example.givelify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PickupActivity extends AppCompatActivity {
    private final static String TAG = "Pickup Activity";
    private Spinner countySpinner;
    private Spinner areaSpinner;
    private EditText etAddress;
    private String mCounty,mArea,mAddress="Default";
    private Button btnStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        countySpinner= findViewById(R.id.county_spinner);
        areaSpinner=findViewById(R.id.area_spinner);
        etAddress=findViewById(R.id.et_address);
        btnStep2=findViewById(R.id.btn_step2);
        populateSpinner();
    }
    private void populateSpinner() {
        ArrayAdapter<CharSequence> adapter= android.widget.ArrayAdapter.createFromResource(PickupActivity.this,R.array.cities_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countySpinner.setAdapter(adapter);
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(parent.getId()) {
                    case R.id.county_spinner:
                        mCounty = parent.getItemAtPosition(position).toString();
                        switch (mCounty){
                            case "Nairobi":
                                ArrayAdapter<CharSequence> adapter= android.widget.ArrayAdapter.createFromResource(PickupActivity.this,R.array.nairobi_spinner, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                areaSpinner.setAdapter(adapter);
                                areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mArea= parent.getItemAtPosition(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                break;
                            case "Nakuru":
                                ArrayAdapter<CharSequence> adapter1= android.widget.ArrayAdapter.createFromResource(PickupActivity.this,R.array.nakuru_spinner, android.R.layout.simple_spinner_item);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                areaSpinner.setAdapter(adapter1);
                                areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mArea= parent.getItemAtPosition(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                break;
                            case "Mombasa":
                                ArrayAdapter<CharSequence> adapter2= android.widget.ArrayAdapter.createFromResource(PickupActivity.this,R.array.mombasa_spinner, android.R.layout.simple_spinner_item);
                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                areaSpinner.setAdapter(adapter2);
                                areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mArea= parent.getItemAtPosition(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                break;
                            case "Kisumu":
                                ArrayAdapter<CharSequence> adapter3= android.widget.ArrayAdapter.createFromResource(PickupActivity.this,R.array.kisumu_spinner, android.R.layout.simple_spinner_item);
                                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                areaSpinner.setAdapter(adapter3);
                                areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mArea= parent.getItemAtPosition(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                break;
                            case "Kiambu":
                                ArrayAdapter<CharSequence> adapter4= android.widget.ArrayAdapter.createFromResource(PickupActivity.this,R.array.kiambu_spinner, android.R.layout.simple_spinner_item);
                                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                areaSpinner.setAdapter(adapter4);
                                areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mArea= parent.getItemAtPosition(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                btnStep2.setOnClickListener(v -> {
                    mAddress=etAddress.getText().toString().trim();
                    if (countySpinner.getSelectedItem().toString().equals("City")){
                        Toast.makeText(PickupActivity.this, "Please Complete Fields", Toast.LENGTH_SHORT).show();

                    }else if(mAddress.equals("")){
                        Toast.makeText(PickupActivity.this, "Please Complete Fields", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    Intent intent = new Intent(PickupActivity.this, PickupActivity2.class);
                        intent.putExtra("COUNTY", mCounty);
                        intent.putExtra("AREA", mArea);
                        intent.putExtra("ADDRESS", mAddress);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}