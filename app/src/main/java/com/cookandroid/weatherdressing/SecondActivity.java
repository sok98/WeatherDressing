package com.cookandroid.weatherdressing;

import android.app.TabActivity;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.cookandroid.weatherdressing.MainActivity.getNowDateTime;
import static java.lang.Integer.parseInt;

public class SecondActivity extends TabActivity {

    DatePicker dPicker;
    EditText etDiary;
    EditText dlgEdtName, dlgEdtUrl;
    Button btnSave, btnAdd;
    View dialogView;
    ListView list;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;


    //??????1 ???
    TextView tv_temp, tv_sky, tv_rain, tv_wet, tv_wind, tv_date;
    ImageView iv_icon;
    EditText et_addr;
    String data;


    // ????????? ???????????? ???????????? ???
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
    Date date = new Date();
    String weather_date = formatter.format(date);

    // ??????/?????? ????????? ????????? ??????1????????? ??????
    String srvUrl = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?serviceKey=";
    String srvKey = "ykhQDbZN0NLbGGQDviiiYh%2Fyd4aosYSsTmHRMjbc82zJ1lNtYrHGHv7IHgBeygcc9yVSIAT2COyn2vgKou3jOw%3D%3D";
    String basetime = getNowDateTime();
    String weather_url = srvUrl + srvKey + "&base_date=" + weather_date + "&base_time=" + basetime +"&nx=61&ny=126&numOfRows=10&_type=xml";


    //????????? ???
    int temp=27;   // ?????? ??????
    ImageView i1, i2, i3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSweather = tabHost.newTabSpec("WEATHER").setIndicator("??????");
        tabSweather.setContent(R.id.tabWeather);
        tabHost.addTab(tabSweather);

        TabHost.TabSpec tabSclothes = tabHost.newTabSpec("CLOTHES").setIndicator("?????????");
        tabSclothes.setContent(R.id.tabClothes);
        tabHost.addTab(tabSclothes);

        TabHost.TabSpec tabSdiary = tabHost.newTabSpec("DIARY").setIndicator("????????????");
        tabSdiary.setContent(R.id.tabDiary);
        tabHost.addTab(tabSdiary);

        TabHost.TabSpec tablogin = tabHost.newTabSpec("LOGIN").setIndicator("??? ??????");
        tablogin.setContent(R.id.tabLogin);
        tabHost.addTab(tablogin);

        tabHost.setCurrentTab(3);

        // ????????? ???
        i1=(ImageView)findViewById(R.id.i1);
        i2=(ImageView)findViewById(R.id.i2);
        i3=(ImageView)findViewById(R.id.i3);

        if(temp < 9){
            //?????? ????????? ??????
            i1.setImageResource(R.drawable.icon_lt);
            i2.setImageResource(R.drawable.icon_lp);
            i3.setImageResource(R.drawable.icon_lo);

        }else if(temp < 19){
            //?????? ????????? ?????????
            i1.setImageResource(R.drawable.icon_lt);
            i2.setImageResource(R.drawable.icon_lp);
            i3.setImageResource(R.drawable.icon_so);

        }else if(temp < 25){
            //?????? ????????? ?????????
            i1.setImageResource(R.drawable.icon_st);
            i2.setImageResource(R.drawable.icon_sp);
            i3.setImageResource(R.drawable.icon_lp);

        }else{
            //?????? ????????? ?????????
            i1.setImageResource(R.drawable.icon_st);
            i2.setImageResource(R.drawable.icon_sp);
            i3.setImageResource(R.drawable.icon_s);

        }

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp < 9){
                    //??????
                    Intent intent = new Intent(getApplicationContext(), longT.class);
                    startActivity(intent);

                }else if(temp < 19){
                    //??????
                    Intent intent = new Intent(getApplicationContext(), longT.class);
                    startActivity(intent);

                }else if(temp < 25){
                    //??????
                    Intent intent = new Intent(getApplicationContext(), shortT.class);
                    startActivity(intent);

                }else{
                    //??????
                    Intent intent = new Intent(getApplicationContext(), shortT.class);
                    startActivity(intent);

                }
            }
        });


        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp < 9){
                    //?????????
                    Intent intent = new Intent(getApplicationContext(), longP.class);
                    startActivity(intent);

                }else if(temp < 19){
                    //?????????
                    Intent intent = new Intent(getApplicationContext(), longP.class);
                    startActivity(intent);

                }else if(temp < 25){
                    //?????????
                    Intent intent = new Intent(getApplicationContext(), shortP.class);
                    startActivity(intent);

                }else{
                    //?????????
                    Intent intent = new Intent(getApplicationContext(), shortP.class);
                    startActivity(intent);

                }
            }
        });


        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp < 9){
                    //??????
                    Intent intent = new Intent(getApplicationContext(), longO.class);
                    startActivity(intent);

                }else if(temp < 19){
                    //?????????
                    Intent intent = new Intent(getApplicationContext(), shortO.class);
                    startActivity(intent);

                }else if(temp < 25){
                    //?????????
                    Intent intent = new Intent(getApplicationContext(), longP.class);
                    startActivity(intent);

                }else{
                    //?????????
                    Intent intent = new Intent(getApplicationContext(), shortS.class);
                    startActivity(intent);

                }
            }
        });


        // ???????????? ???
        etDiary = (EditText) findViewById(R.id.etDiary);
        btnSave = (Button) findViewById(R.id.btnSave);

        // ?????? ????????????
        dPicker = (DatePicker) findViewById(R.id.dateP);
        int cYear = dPicker.getYear();
        int cMonth = dPicker.getMonth();
        int cDay = dPicker.getDayOfMonth();

        // ???????????? ?????????
        int dMonth = cMonth + 1;
        String fileName = cYear + "_" + dMonth + "_" + cDay + ".txt";
        String msg = readDiary(fileName);   // ??????????????? ???????????? ?????? ?????? msg??? ??????
        etDiary.setText(msg);   // ?????? ?????? ?????????

        // ??????????????? ????????????
        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int cy, int cm, int cd) {
                // ????????? ???????????????
                int dmonth = cm + 1;
                String fileName = cy + "_" + dmonth + "_" + cd + ".txt";
                String msg = readDiary(fileName);
                etDiary.setText(msg);
            }
        });

        // ????????????
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // DatePicker //TextView
                    String year = Integer.toString(dPicker.getYear());
                    String month = Integer.toString(dPicker.getMonth() + 1);
                    String day = Integer.toString(dPicker.getDayOfMonth());
                    dPicker.getDayOfMonth();
                    String fileName = year + "_" + month + "_" + day + ".txt";
                    FileOutputStream outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                    String msg = etDiary.getText().toString();
                    outFs.write(msg.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName + "??? ?????????????????????.",
                            Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //?????? ???

        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_sky = (TextView) findViewById(R.id.tv_sky);
        tv_rain = (TextView) findViewById(R.id.tv_rain);
        tv_wet = (TextView) findViewById(R.id.tv_wet);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        et_addr = (EditText) findViewById(R.id.et_addr);

        new SecondActivity.GetXMLTask().execute();


        // ??? ?????? ???
        btnAdd = (Button) findViewById(R.id.btnAdd);
        dialogView = (View) View.inflate(SecondActivity.this,R.layout.dialog, null);
        dlgEdtName = (EditText) dialogView.findViewById(R.id.dlgEdt1);
        dlgEdtUrl = (EditText) dialogView.findViewById(R.id.dlgEdt2);
        list = (ListView)findViewById(R.id.listView);

        myHelper = new myDBHelper(this);

        final ArrayList<String> midList1 = new ArrayList<String>(); // ????????? ?????? ??????
        final ArrayList<String> midList2 = new ArrayList<String>(); // ????????? ?????? ??????

        Cursor cursor;
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("select * from shopTBL;", null);
        while (cursor.moveToNext()){
            midList1.add(cursor.getString(0));
            midList2.add(cursor.getString(1));
        }
        cursor.close();
        sqlDB.close();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, midList1);
        list.setAdapter(adapter);



        // ???????????? ????????? ???
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                // ????????? ??????
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(midList2.get(p)));
                startActivity(intent);
            }
        });


        // ????????? ?????? ?????? ????????? ???
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SecondActivity.this);
                dlg.setTitle("????????? ?????? ??????");
                dlg.setView(dialogView);
                // ???????????? ?????????
                dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlDB = myHelper.getWritableDatabase();
                        try{
                            sqlDB.execSQL("insert into shopTBL values('"
                                    + dlgEdtName.getText().toString()+"', '"
                                    + dlgEdtUrl.getText().toString()+"');");
                            Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e){
                            Log.d("SQLite", "????????? ????????? ??? ?????? : "+e.getMessage()+"\n");
                            e.printStackTrace();
                        } finally {
                            sqlDB.close();
                        }
                        // ??????????????? ??????
                        midList1.add(dlgEdtName.getText().toString());
                        midList2.add(dlgEdtUrl.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
                dlg.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"?????????????????????.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();

            }
        });

        // ????????? ??? ????????? ??? (??????)
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SecondActivity.this);
                dlg.setTitle("?????? ?????????????????????????");
                // ???????????? ?????????
                dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // ??????
                        sqlDB = myHelper.getWritableDatabase();
                        try {
                            sqlDB.execSQL("delete from shopTBL where gName='"
                                    + midList1.get(position) + "';");
                            Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();

                        }catch (Exception e) {
                            Log.d("SQLite", "????????? ????????? ??? ?????? : "+e.getMessage()+"\n");
                            e.printStackTrace();
                        }
                        midList1.remove(position);
                        midList2.remove(position);
                        adapter.notifyDataSetChanged();

                    }
                });
                dlg.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();

                return false;
            }
        });

    }   //onCreate



    // ???????????? ????????????
    String readDiary(String fName) {
        String diaryStr = null;
        FileInputStream inFs;
        try {
            inFs = openFileInput(fName);   // ?????? ????????? ???????????? ?????? ??????
            byte[] txt = new byte[500];
            inFs.read(txt); // ?????? ?????? ?????? txt??? ??????
            inFs.close();
            diaryStr = (new String(txt)).trim();    // txt ?????? diaryStr??? ??????
            btnSave.setText("??????");
        } catch (IOException e) {
            etDiary.setHint("?????? ??????");
            btnSave.setText("??????");
        }
        return diaryStr;    // ???????????? ?????? ??????
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "shopDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {   //????????? ??????
            try {
                db.execSQL("CREATE TABLE  shopTBL ( gName CHAR(20) PRIMARY KEY, gUrl CHAR(20));");
            } catch (Exception e){}
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS shopTBL");    //????????? ?????? (??? ???????????? ??????)
            onCreate(db);   //?????? ??? ?????? ??????

        }
    }

    //??????
    private class GetXMLTask extends AsyncTask<String, Void, Document> {

        @Override

        protected Document doInBackground(String... urls) {
            URL url;
            Document doc = null;
            try {
                url = new URL(weather_url);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        //??????
        @Override
        protected void onPostExecute(Document doc) {
            String s = "";
            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;
                NodeList idx = fstElmnt.getElementsByTagName("category");

                // ?????? category ????????? ?????? ??????
                // s += "category = "+  idx.item(0).getChildNodes().item(0).getNodeValue() +"\n";
                // ???????????? PDP, fcstValue ??????????????? ???????????? ???

                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("POP")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "% \n";
                    tv_rain.setText(s);
                    s = "";
                }

                // ?????? REH, fcstValue ????????? ???????????? ???
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("REH")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "% \n";
                    tv_wet.setText(s);
                    s = "";
                }

                // ?????? T3H, fcstValue ????????? ???????????? ???
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("T3H")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "??\n";
                    tv_temp.setText(s);
                    s = "";
                }

                // ?????? WSD, fcstValue ????????? ???????????? ???
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("WSD")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "m/s \n";
                    tv_wind.setText(s);
                    s = "";
                }

                // ???????????? SKY, fcstValue ??????????????? ???????????? ???
                // 0~2 : ??????, 3~5 : ????????????, 6~8 : ????????????, 9~10 : ??????
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("SKY")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    int cloud_num = parseInt(gugun.item(0).getChildNodes().item(0).getNodeValue());
                    if (cloud_num == 0 || cloud_num == 1 || cloud_num == 2) {
                        s += "??????\n";
                        iv_icon.setImageResource(R.drawable.sun);

                    } else if (cloud_num == 3 || cloud_num == 4 || cloud_num == 5) {
                        s += "?????? ??????\n";
                        iv_icon.setImageResource(R.drawable.cloudy);
                    } else if (cloud_num == 6 || cloud_num == 7 || cloud_num == 8) {
                        s += "?????? ??????\n";
                        iv_icon.setImageResource(R.drawable.cloudy2);
                    } else if (cloud_num == 9 || cloud_num == 10) {
                        s += "??????\n";
                        iv_icon.setImageResource(R.drawable.cloudy3);
                    }
                    tv_sky.setText(s);
                    s = "";
                }

                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("PTY")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    int fall_num = parseInt(gugun.item(0).getChildNodes().item(0).getNodeValue());
                    if (fall_num == 1) {
                        s += "???\n";
                        iv_icon.setImageResource(R.drawable.rain);
                    } else if (fall_num == 4) {
                        s += "?????????\n";
                        iv_icon.setImageResource(R.drawable.rain);
                    } else if (fall_num == 3) {
                        s += "???\n";
                        iv_icon.setImageResource(R.drawable.snowy);
                    }else if (fall_num == 2) {
                        s += "????????????\n";
                        iv_icon.setImageResource(R.drawable.mixed);
                    }
                    tv_sky.setText(s);
                    s = "";
                }

                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH) + 1;
                int date = cal.get(Calendar.DATE);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                String dow = "";
                switch (dayOfWeek){
                    case 1:
                        dow = "(???)";
                        break;
                    case 2:
                        dow = "(???)";
                        break;
                    case 3:
                        dow = "(???)";
                        break;
                    case 4:
                        dow = "(???)";
                        break;
                    case 5:
                        dow = "(???)";
                        break;
                    case 6:
                        dow = "(???)";
                        break;
                    case 7:
                        dow = "(???)";
                        break;
                }tv_date.setText(month+"/"+date+" "+dow);


                super.onPostExecute(doc);
            }
        }
    }


}

