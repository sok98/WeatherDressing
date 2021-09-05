package com.cookandroid.weatherdressing;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.Integer.parseInt;

public class MainActivity extends TabActivity {

    private static final String TAG = "";
    SessionCallback callback;
    Button btLogOut;

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

    //현재 시간 반환
    public static String getNowDateTime() {
        String[] standard_time = {"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};


        Calendar cal = Calendar.getInstance();
        int time = cal.get(Calendar.HOUR_OF_DAY);

        String tt = Integer.toString(time) + "00";
        int t = Integer.parseInt(tt);

        for (int i = standard_time.length - 1; i >= 0; i--) {
            int st = Integer.parseInt(standard_time[i]);
            if (t > st) {
                if (st == 800) {
                    String str = String.format("%04d", 800);
                    return str;
                } else if (st == 500) {
                    String str = String.format("%04d", 500);
                    return str;
                } else if (st == 200) {
                    String str = String.format("%04d", 200);
                    return str;
                } else {
                    String str = Integer.toString(st);
                    return str;
                }
            }
        }
        return "";
    }


    //옷차림 탭
    int temp=23;   // 기온 저장
    ImageView i1, i2, i3 , i_icon;
    TextView t_temp, t_sky, t_date, tv1, tv2, tv3;

    //다이어리 탭
    DatePicker dPicker;
    EditText etDiary;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        TabHost.TabSpec tablogin = tabHost.newTabSpec("LOGIN").setIndicator("로그인");
        tablogin.setContent(R.id.tabLogin);
        tabHost.addTab(tablogin);

        tabHost.setCurrentTab(0);

        //날씨 탭

        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_sky = (TextView) findViewById(R.id.tv_sky);
        tv_rain = (TextView) findViewById(R.id.tv_rain);
        tv_wet = (TextView) findViewById(R.id.tv_wet);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        et_addr = (EditText) findViewById(R.id.et_addr);

        new GetXMLTask().execute();


        /**카카오톡 로그아웃 요청**/
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출하므로
        // 매번 로그아웃을 요청함
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        // 옷차림 탭
        i1=(ImageView)findViewById(R.id.i1);
        i2=(ImageView)findViewById(R.id.i2);
        i3=(ImageView)findViewById(R.id.i3);

        i_icon = (ImageView)findViewById(R.id.i_icon);
        t_temp = (TextView) findViewById(R.id.t_temp);
        t_sky = (TextView) findViewById(R.id.t_sky);
        t_date = (TextView) findViewById(R.id.t_date);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);


        //temp = parseInt((String)tv_temp.getText());

        if(temp < 9){
            //긴팔 긴바지 패딩
            i1.setImageResource(R.drawable.icon_lt);
            tv1.setText("긴팔");
            i2.setImageResource(R.drawable.icon_lp);
            tv2.setText("긴바지");
            i3.setImageResource(R.drawable.icon_lo);
            tv3.setText("패딩");

        }else if(temp < 19){
            //긴팔 긴바지 가디건
            i1.setImageResource(R.drawable.icon_lt);
            tv1.setText("긴팔");
            i2.setImageResource(R.drawable.icon_lp);
            tv2.setText("긴바지");
            i3.setImageResource(R.drawable.icon_so);
            tv3.setText("가디건");

        }else if(temp < 25){
            //반팔 반바지 긴바지
            i1.setImageResource(R.drawable.icon_st);
            tv1.setText("반팔");
            i2.setImageResource(R.drawable.icon_sp);
            tv2.setText("반바지");
            i3.setImageResource(R.drawable.icon_lp);
            tv3.setText("긴바지");

        }else{
            //반팔 반바지 민소매
            i1.setImageResource(R.drawable.icon_st);
            tv1.setText("반팔");
            i2.setImageResource(R.drawable.icon_sp);
            tv2.setText("반바지");
            i3.setImageResource(R.drawable.icon_s);
            tv3.setText("민소매");

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
        etDiary = (EditText)findViewById(R.id.etDiary);
        btnSave = (Button)findViewById(R.id.btnSave);

        // 날짜 설정하기
        dPicker = (DatePicker)findViewById(R.id.dateP);
        int cYear = dPicker.getYear();
        int cMonth = dPicker.getMonth();
        int cDay = dPicker.getDayOfMonth();

        // 파일이름 정하기
        int dMonth = cMonth +1;
        String fileName = cYear+"_"+dMonth+"_"+cDay+".txt";
        String msg = readDiary(fileName);   // 파일이름에 해당하는 파일 내용 msg에 저장
        etDiary.setText(msg);   // 파일 내용 띄우기

        // 데이트피커 설정하기
        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int cy, int cm, int cd) {
                // 날짜가 바뀔때마다
                int dmonth = cm +1;
                String fileName = cy+"_"+dmonth+"_"+cd+".txt";
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
                    String month = Integer.toString(dPicker.getMonth()+1);
                    String day = Integer.toString(dPicker.getDayOfMonth());
                    dPicker.getDayOfMonth();
                    String fileName = year+"_"+month+"_"+day+".txt";
                    FileOutputStream outFs = openFileOutput(fileName, Context.MODE_PRIVATE);
                    String msg = etDiary.getText().toString();
                    outFs.write(msg.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName+"이 저장되었습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                catch(IOException e) {
                    Toast.makeText(getApplicationContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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


    }   //onCreate

    //로그인
    class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        //에러로 인한 로그인 실패
                        // finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                    Log.e("UserProfile", userProfile.toString());
                    Log.e("UserProfile", userProfile.getId() + "");
                    Log.e("UserProfile", userProfile.getProfileImagePath() + "");
                    Log.e("UserProfile", String.valueOf(userProfile.getId()) + "");
                    Log.e("UserProfile", userProfile.getNickname() + "");

                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        // 세션 실패시
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
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
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "%";
                    tv_rain.setText(s);
                    s = "";
                }

                // 습도 REH, fcstValue 습도에 해당하는 값
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("REH")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "%";
                    tv_wet.setText(s);
                    s = "";
                }

                // 온도 T3H, fcstValue 온도에 해당하는 값
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("T3H")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "°";
                    tv_temp.setText(s);
                    t_temp.setText(s);
                    s = "";
                }

                // 풍속 WSD, fcstValue 풍속에 해당하는 값
                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("WSD")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    s += gugun.item(0).getChildNodes().item(0).getNodeValue() + "m/s";
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
                        i_icon.setImageResource(R.drawable.sun);
                    } else if (cloud_num == 3 || cloud_num == 4 || cloud_num == 5) {
                        s += "구름 조금\n";
                        iv_icon.setImageResource(R.drawable.cloudy);
                        i_icon.setImageResource(R.drawable.cloudy);
                    } else if (cloud_num == 6 || cloud_num == 7 || cloud_num == 8) {
                        s += "구름 많음\n";
                        iv_icon.setImageResource(R.drawable.cloudy2);
                        i_icon.setImageResource(R.drawable.cloudy2);
                    } else if (cloud_num == 9 || cloud_num == 10) {
                        s += "흐림\n";
                        iv_icon.setImageResource(R.drawable.cloudy3);
                        i_icon.setImageResource(R.drawable.cloudy3);
                    }
                    tv_sky.setText(s);
                    t_sky.setText(s);
                    s = "";
                }

                if (idx.item(0).getChildNodes().item(0).getNodeValue().equals("PTY")) {
                    NodeList gugun = fstElmnt.getElementsByTagName("fcstValue");
                    int fall_num = parseInt(gugun.item(0).getChildNodes().item(0).getNodeValue());
                    if (fall_num == 1) {
                        s += "비\n";
                        iv_icon.setImageResource(R.drawable.rain);
                        i_icon.setImageResource(R.drawable.rain);
                    } else if (fall_num == 4) {
                        s += "소나기\n";
                        iv_icon.setImageResource(R.drawable.rain);
                        i_icon.setImageResource(R.drawable.rain);
                    } else if (fall_num == 3) {
                        s += "눈\n";
                        iv_icon.setImageResource(R.drawable.snowy);
                        i_icon.setImageResource(R.drawable.snowy);
                    }else if (fall_num == 2) {
                        s += "진눈깨비\n";
                        iv_icon.setImageResource(R.drawable.mixed);
                        i_icon.setImageResource(R.drawable.mixed);
                    }
                    tv_sky.setText(s);
                    t_sky.setText(s);
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
                t_date.setText(month+"/"+date+" "+dow);


                super.onPostExecute(doc);
            }
        }
    }

}