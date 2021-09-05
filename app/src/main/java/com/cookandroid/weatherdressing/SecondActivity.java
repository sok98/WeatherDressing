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


    //날씨1 탭
    TextView tv_temp, tv_sky, tv_rain, tv_wet, tv_wind, tv_date;
    ImageView iv_icon;
    EditText et_addr;
    String data;


    // 오늘의 년도날짜 받아오는 것
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
    Date date = new Date();
    String weather_date = formatter.format(date);

    // 위도/경도 서울시 도봉구 쌍문1동으로 고정
    String srvUrl = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?serviceKey=";
    String srvKey = "ykhQDbZN0NLbGGQDviiiYh%2Fyd4aosYSsTmHRMjbc82zJ1lNtYrHGHv7IHgBeygcc9yVSIAT2COyn2vgKou3jOw%3D%3D";
    String basetime = getNowDateTime();
    String weather_url = srvUrl + srvKey + "&base_date=" + weather_date + "&base_time=" + basetime +"&nx=61&ny=126&numOfRows=10&_type=xml";


    //옷차림 탭
    int temp=27;   // 기온 저장
    ImageView i1, i2, i3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSweather = tabHost.newTabSpec("WEATHER").setIndicator("날씨");
        tabSweather.setContent(R.id.tabWeather);
        tabHost.addTab(tabSweather);

        TabHost.TabSpec tabSclothes = tabHost.newTabSpec("CLOTHES").setIndicator("옷추천");
        tabSclothes.setContent(R.id.tabClothes);
        tabHost.addTab(tabSclothes);

        TabHost.TabSpec tabSdiary = tabHost.newTabSpec("DIARY").setIndicator("다이어리");
        tabSdiary.setContent(R.id.tabDiary);
        tabHost.addTab(tabSdiary);

        TabHost.TabSpec tablogin = tabHost.newTabSpec("LOGIN").setIndicator("내 정보");
        tablogin.setContent(R.id.tabLogin);
        tabHost.addTab(tablogin);

        tabHost.setCurrentTab(3);

        // 옷차림 탭
        i1=(ImageView)findViewById(R.id.i1);
        i2=(ImageView)findViewById(R.id.i2);
        i3=(ImageView)findViewById(R.id.i3);

        if(temp < 9){
            //긴팔 긴바지 패딩
            i1.setImageResource(R.drawable.icon_lt);
            i2.setImageResource(R.drawable.icon_lp);
            i3.setImageResource(R.drawable.icon_lo);

        }else if(temp < 19){
            //긴팔 긴바지 가디건
            i1.setImageResource(R.drawable.icon_lt);
            i2.setImageResource(R.drawable.icon_lp);
            i3.setImageResource(R.drawable.icon_so);

        }else if(temp < 25){
            //반팔 반바지 긴바지
            i1.setImageResource(R.drawable.icon_st);
            i2.setImageResource(R.drawable.icon_sp);
            i3.setImageResource(R.drawable.icon_lp);

        }else{
            //반팔 반바지 민소매
            i1.setImageResource(R.drawable.icon_st);
            i2.setImageResource(R.drawable.icon_sp);
            i3.setImageResource(R.drawable.icon_s);

        }

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp < 9){
                    //긴팔
                    Intent intent = new Intent(getApplicationContext(), longT.class);
                    startActivity(intent);

                }else if(temp < 19){
                    //긴팔
                    Intent intent = new Intent(getApplicationContext(), longT.class);
                    startActivity(intent);

                }else if(temp < 25){
                    //반팔
                    Intent intent = new Intent(getApplicationContext(), shortT.class);
                    startActivity(intent);

                }else{
                    //반팔
                    Intent intent = new Intent(getApplicationContext(), shortT.class);
                    startActivity(intent);

                }
            }
        });


        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp < 9){
                    //긴바지
                    Intent intent = new Intent(getApplicationContext(), longP.class);
                    startActivity(intent);

                }else if(temp < 19){
                    //긴바지
                    Intent intent = new Intent(getApplicationContext(), longP.class);
                    startActivity(intent);

                }else if(temp < 25){
                    //반바지
                    Intent intent = new Intent(getApplicationContext(), shortP.class);
                    startActivity(intent);

                }else{
                    //반바지
                    Intent intent = new Intent(getApplicationContext(), shortP.class);
                    startActivity(intent);

                }
            }
        });


        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp < 9){
                    //패딩
                    Intent intent = new Intent(getApplicationContext(), longO.class);
                    startActivity(intent);

                }else if(temp < 19){
                    //가디건
                    Intent intent = new Intent(getApplicationContext(), shortO.class);
                    startActivity(intent);

                }else if(temp < 25){
                    //긴바지
                    Intent intent = new Intent(getApplicationContext(), longP.class);
                    startActivity(intent);

                }else{
                    //민소매
                    Intent intent = new Intent(getApplicationContext(), shortS.class);
                    startActivity(intent);

                }
            }
        });


        // 다이어리 탭
        etDiary = (EditText) findViewById(R.id.etDiary);
        btnSave = (Button) findViewById(R.id.btnSave);

        // 날짜 설정하기
        dPicker = (DatePicker) findViewById(R.id.dateP);
        int cYear = dPicker.getYear();
        int cMonth = dPicker.getMonth();
        int cDay = dPicker.getDayOfMonth();

        // 파일이름 정하기
        int dMonth = cMonth + 1;
        String fileName = cYear + "_" + dMonth + "_" + cDay + ".txt";
        String msg = readDiary(fileName);   // 파일이름에 해당하는 파일 내용 msg에 저장
        etDiary.setText(msg);   // 파일 내용 띄우기

        // 데이트피커 설정하기
        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int cy, int cm, int cd) {
                // 날짜가 바뀔때마다
                int dmonth = cm + 1;
                String fileName = cy + "_" + dmonth + "_" + cd + ".txt";
                String msg = readDiary(fileName);
                etDiary.setText(msg);
            }
        });

        // 저장하기
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
                    Toast.makeText(getApplicationContext(), fileName + "이 저장되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //날씨 탭

        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_sky = (TextView) findViewById(R.id.tv_sky);
        tv_rain = (TextView) findViewById(R.id.tv_rain);
        tv_wet = (TextView) findViewById(R.id.tv_wet);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        et_addr = (EditText) findViewById(R.id.et_addr);

        new SecondActivity.GetXMLTask().execute();


        // 내 정보 탭
        btnAdd = (Button) findViewById(R.id.btnAdd);
        dialogView = (View) View.inflate(SecondActivity.this,R.layout.dialog, null);
        dlgEdtName = (EditText) dialogView.findViewById(R.id.dlgEdt1);
        dlgEdtUrl = (EditText) dialogView.findViewById(R.id.dlgEdt2);
        list = (ListView)findViewById(R.id.listView);

        myHelper = new myDBHelper(this);

        final ArrayList<String> midList1 = new ArrayList<String>(); // 쇼핑몰 이름 저장
        final ArrayList<String> midList2 = new ArrayList<String>(); // 쇼핑몰 주소 저장

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



        // 쇼핑몰을 눌렀을 때
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                // 주소로 이동
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(midList2.get(p)));
                startActivity(intent);
            }
        });


        // 쇼핑몰 추가 버튼 눌렀을 떄
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SecondActivity.this);
                dlg.setTitle("쇼핑몰 정보 입력");
                dlg.setView(dialogView);
                // 확인버튼 누르면
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlDB = myHelper.getWritableDatabase();
                        try{
                            sqlDB.execSQL("insert into shopTBL values('"
                                    + dlgEdtName.getText().toString()+"', '"
                                    + dlgEdtUrl.getText().toString()+"');");
                            Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e){
                            Log.d("SQLite", "데이터 추가할 때 예외 : "+e.getMessage()+"\n");
                            e.printStackTrace();
                        } finally {
                            sqlDB.close();
                        }
                        // 리스트뷰에 추가
                        midList1.add(dlgEdtName.getText().toString());
                        midList2.add(dlgEdtUrl.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();

            }
        });

        // 항목을 꾹 눌렀을 때 (삭제)
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SecondActivity.this);
                dlg.setTitle("정말 삭제하시겠습니까?");
                // 확인버튼 누르면
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 삭제
                        sqlDB = myHelper.getWritableDatabase();
                        try {
                            sqlDB.execSQL("delete from shopTBL where gName='"
                                    + midList1.get(position) + "';");
                            Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                        }catch (Exception e) {
                            Log.d("SQLite", "데이터 삭제할 때 예외 : "+e.getMessage()+"\n");
                            e.printStackTrace();
                        }
                        midList1.remove(position);
                        midList2.remove(position);
                        adapter.notifyDataSetChanged();

                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"삭제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();

                return false;
            }
        });

    }   //onCreate



    // 다이어리 읽어오기
    String readDiary(String fName) {
        String diaryStr = null;
        FileInputStream inFs;
        try {
            inFs = openFileInput(fName);   // 파일 이름에 해당하는 파일 열기
            byte[] txt = new byte[500];
            inFs.read(txt); // 파일 내용 읽어 txt에 저장
            inFs.close();
            diaryStr = (new String(txt)).trim();    // txt 내용 diaryStr에 저장
            btnSave.setText("수정");
        } catch (IOException e) {
            etDiary.setHint("일기 없음");
            btnSave.setText("저장");
        }
        return diaryStr;    // 다이어리 내용 반환
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "shopDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {   //테이블 생성
            try {
                db.execSQL("CREATE TABLE  shopTBL ( gName CHAR(20) PRIMARY KEY, gUrl CHAR(20));");
            } catch (Exception e){}
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS shopTBL");    //테이블 삭제 (앱 열때마다 실행)
            onCreate(db);   //삭제 후 다시 생성

        }
    }

    //날씨
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

        //날씨
        @Override
        protected void onPostExecute(Document doc) {
            String s = "";
            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;
                NodeList idx = fstElmnt.getElementsByTagName("category");

                // 모든 category 값들을 출력 위한
                // s += "category = "+  idx.item(0).getChildNodes().item(0).getNodeValue() +"\n";
                // 강수확률 PDP, fcstValue 강수확률에 해당하는 값

                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("POP")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "% \n";
                    tv_rain.setText(s);
                    s = "";
                }

                // 습도 REH, fcstValue 습도에 해당하는 값
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("REH")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "% \n";
                    tv_wet.setText(s);
                    s = "";
                }

                // 온도 T3H, fcstValue 온도에 해당하는 값
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("T3H")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "°\n";
                    tv_temp.setText(s);
                    s = "";
                }

                // 풍속 WSD, fcstValue 풍속에 해당하는 값
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("WSD")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "m/s \n";
                    tv_wind.setText(s);
                    s = "";
                }

                // 구름상태 SKY, fcstValue 구름상태에 해당하는 값
                // 0~2 : 맑음, 3~5 : 구름조금, 6~8 : 구름많음, 9~10 : 흐림
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("SKY")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    int cloud_num = parseInt(gugun.item(0).getChildNodes().item(0).getNodeValue());
                    if (cloud_num == 0 || cloud_num == 1 || cloud_num == 2) {
                        s += "맑음\n";
                        iv_icon.setImageResource(R.drawable.sun);

                    } else if (cloud_num == 3 || cloud_num == 4 || cloud_num == 5) {
                        s += "구름 조금\n";
                        iv_icon.setImageResource(R.drawable.cloudy);
                    } else if (cloud_num == 6 || cloud_num == 7 || cloud_num == 8) {
                        s += "구름 많음\n";
                        iv_icon.setImageResource(R.drawable.cloudy2);
                    } else if (cloud_num == 9 || cloud_num == 10) {
                        s += "흐림\n";
                        iv_icon.setImageResource(R.drawable.cloudy3);
                    }
                    tv_sky.setText(s);
                    s = "";
                }

                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("PTY")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    int fall_num = parseInt(gugun.item(0).getChildNodes().item(0).getNodeValue());
                    if (fall_num == 1) {
                        s += "비\n";
                        iv_icon.setImageResource(R.drawable.rain);
                    } else if (fall_num == 4) {
                        s += "소나기\n";
                        iv_icon.setImageResource(R.drawable.rain);
                    } else if (fall_num == 3) {
                        s += "눈\n";
                        iv_icon.setImageResource(R.drawable.snowy);
                    }else if (fall_num == 2) {
                        s += "진눈깨비\n";
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
                        dow = "(일)";
                        break;
                    case 2:
                        dow = "(월)";
                        break;
                    case 3:
                        dow = "(화)";
                        break;
                    case 4:
                        dow = "(수)";
                        break;
                    case 5:
                        dow = "(목)";
                        break;
                    case 6:
                        dow = "(금)";
                        break;
                    case 7:
                        dow = "(토)";
                        break;
                }tv_date.setText(month+"/"+date+" "+dow);


                super.onPostExecute(doc);
            }
        }
    }


}

