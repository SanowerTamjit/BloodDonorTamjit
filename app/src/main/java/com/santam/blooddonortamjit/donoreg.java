package com.santam.blooddonortamjit;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class donoreg extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 1;
    private LinearLayout linearLayout;
    private static final String TAG = donoreg.class.getSimpleName();
    Spinner country, phoneCode,BloodGroupSpinner;
    EditText Name,Birthdate,Age,PhoneNo,City,State,ZipCode,Email,Address,TotalDonate,LastDonate,Password,RePassword,imgPthText;
    Button Register,Close,BrowsePhoto;
    private ProgressDialog progressDialog;

    DatePickerDialog d = null;
    public  static  String setCountryString =  "";
    public  static  String setPhoneString =  "";
    public  static  String setBloodGroup =  "";
    final Calendar myCalendar = Calendar.getInstance();
    protected static List<String> countryData = new ArrayList<String>();
    protected static List<String> phonecodeData = new ArrayList<String>();

    private static Uri filePath;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static String[] BloodGroup = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donoreg);

        linearLayout = (LinearLayout) findViewById(R.id
                .linearLayout);

        Name = (EditText) findViewById(R.id.txtName);
        Birthdate = (EditText) findViewById(R.id.txtBirthDate);
        Age = (EditText) findViewById(R.id.txtAge);
        PhoneNo = (EditText) findViewById(R.id.txtphoneNo);
        Email = (EditText) findViewById(R.id.txtEmail);
        City = (EditText) findViewById(R.id.txtCity);
        State = (EditText) findViewById(R.id.txtState);
        ZipCode = (EditText) findViewById(R.id.txtZip);
        Address = (EditText) findViewById(R.id.txt_Address);
        TotalDonate = (EditText) findViewById(R.id.txtTDonate);
        LastDonate = (EditText) findViewById(R.id.txtlastDateDonate);
        Password = (EditText) findViewById(R.id.txtPassword);
        RePassword = (EditText) findViewById(R.id.txtRePassword);
        imgPthText = (EditText) findViewById(R.id.txtImgView);
        country = (Spinner) findViewById(R.id.countryspinner);
        phoneCode = (Spinner) findViewById(R.id.phnCodeSpinner);
        BloodGroupSpinner = (Spinner) findViewById(R.id.bloodSpinner);
        Register = (Button) findViewById(R.id.btnReg);
        Close = (Button) findViewById(R.id.btncls);
        BrowsePhoto = (Button) findViewById(R.id.browse);


        countryData.add("Select One");
        phonecodeData.add("Select");
        fillCountry_Code();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        Close.setOnClickListener(this);
        BrowsePhoto.setOnClickListener(this);
        LastDonate.setOnClickListener(this);
        Birthdate.setOnClickListener(this);
        Register.setOnClickListener(this);
    }

    public String loadJSONData(){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = getAssets().open("country.json");
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferReader.readLine()) != null){
                stringBuilder.append(line);
            }
            bufferReader.close();
            Log.d("Log E-", "Response Ready "+stringBuilder.toString() );
            return stringBuilder.toString();
        }
        catch (IOException e){
            e.printStackTrace();

        }
        return null;
    }

    private void fillCountry_Code() {
        ArrayAdapter<String> dt = new ArrayAdapter<String>(this, R.layout.countryspinner,R.id.txt, BloodGroup);
        BloodGroupSpinner.setAdapter(dt);
        BloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected1 = BloodGroupSpinner.getItemAtPosition(BloodGroupSpinner.getSelectedItemPosition()).toString();
                setBloodGroup = selected1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            JSONArray countr_code = new JSONArray(loadJSONData());
            for (int i = 0; i < countr_code.length(); i++) {
                JSONObject jobj = countr_code.getJSONObject(i);
                String name = jobj.getString("name").toString();
                String code = jobj.getString("dial_code").toString();
                countryData.add(name);
                phonecodeData.add(code);
                // Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();
            }
            ArrayAdapter<String> spin2 = new ArrayAdapter<String>(this, R.layout.countryspinner,R.id.txt, phonecodeData);
            phoneCode.setAdapter(spin2);
            phoneCode.setEnabled(false);
            ArrayAdapter<String> spin = new ArrayAdapter<String>(this, R.layout.countryspinner,R.id.txt, countryData);
            country.setAdapter(spin);
            country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected1 = country.getItemAtPosition(country.getSelectedItemPosition()).toString();
//                    Toast.makeText(getApplicationContext(), selected1,
//                            Toast.LENGTH_SHORT).show();
                    phoneCode.setSelection(position);
                    setCountryString = selected1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


//            phoneCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String selected1 = phoneCode.getItemAtPosition(phoneCode.getSelectedItemPosition()).toString();
//                    Toast.makeText(getApplicationContext(), selected1,
//                            Toast.LENGTH_SHORT).show();
//                    setPhoneString = selected1;
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == Close) {
            finish();
        }
        else if(view == Register){
            if(Name.getText().length() == 0 || Birthdate.getText().length() == 0 ||
                    Age.getText().length() == 0 || PhoneNo.getText().length() == 0 ||
                    Address.getText().length() == 0 || Password.getText().length() == 0 || RePassword.getText().length() == 0){
//                Toast.makeText(getApplicationContext(),"Please Fill Out All *Mark Field",Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Please Fill Out All *Mark Field", Snackbar.LENGTH_LONG);

                snackbar.show();
            }else{
                if (Password.getText().toString().trim().equals(RePassword.getText().toString().trim())){
                    register();
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Password Don't Match. Please Try Again", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }

            }

        }
        else if (view == Birthdate){
            showDatePicker(Birthdate);
        }
        else if (view ==LastDonate){
            showDatePicker(LastDonate);
        }
        else if(view == country){

        }
        else if (view == BrowsePhoto){
            showFileChooser();
        }

    }

    private void register() {
        progressDialog.show();
        final String cellno = setPhoneString+""+PhoneNo.getText();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.DONOR_CONTROLLER, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Registration Response: " + response.toString());
                progressDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        uploadMultipart();

                        AlertDialog.Builder alert=new AlertDialog.Builder(donoreg.this);
                        alert.setItems(new String[]{"Thank you for successfully registerd. \nGo to Login Page?"},new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface arg0,
                                    int position) {
                                switch (position) {
                                    case 0:
                                        startActivity(new Intent(getApplicationContext(), login.class));
                                        finish();
                                        break;
                                    default: finish();
                                             break;
                                }
                            }
                        });
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "Error From Online", Snackbar.LENGTH_LONG);

                        snackbar.show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error);
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar
                        .make(linearLayout,
                                "Error--> Not Response", Snackbar.LENGTH_LONG);

                snackbar.show();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "insert");
                params.put("name", Name.getText().toString());
                params.put("birthdate", Birthdate.getText().toString());
                params.put("age", Age.getText().toString());
                params.put("cellno", cellno);
                params.put("bloodgroup", setBloodGroup);
                params.put("country", setCountryString);
                params.put("city", City.getText().toString());
                params.put("state", State.getText().toString());
                params.put("zip", ZipCode.getText().toString());
                params.put("address", Address.getText().toString());
                params.put("email", Email.getText().toString());
                params.put("totaldonate", TotalDonate.getText().toString());
                params.put("l_datedonate", LastDonate.getText().toString());
                params.put("password", Password.getText().toString().trim());
//                params.put("image", filePath.toString());

                return params;
            }

        };

        // Adding request to request queue
        RequestHandler.getInstance(this).addToRequestQueue(strReq);

    }

    public void showDatePicker(final TextView textView){
        DatePickerDialog.OnDateSetListener dpd= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel()
            {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                textView.setText(sdf.format(myCalendar.getTime()));
            }
        };
        //  Time now = new Time();
        DatePickerDialog d = new DatePickerDialog(donoreg.this, dpd, myCalendar.get(Calendar.YEAR) ,myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        d.show();
    }


    public void uploadMultipart() {
        //getting name for the image
        String name = Name.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);
        Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, constants.DONOR_CONTROLLER)
                    .addFileToUpload(filePath.getPath(), "image") //Adding file
                    .addParameter("name",name)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Toast.makeText(getApplicationContext(),data.getData().toString(),Toast.LENGTH_LONG).show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageView imageView = (ImageView) findViewById(R.id.img);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//grantUriPermission(getCallingPackage(),uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        cursor = getContentResolver().query(
//                Images.Media.EXTERNAL_CONTENT_URI,
//                null, Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
