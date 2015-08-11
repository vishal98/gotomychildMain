package com.mychild.view.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushbots.push.Pushbots;
import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.CustomAdapter;
import com.mychild.adapters.SubjectSpinnerAdapter;
import com.mychild.model.StudentDTO;
import com.mychild.model.TeacherModel;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.sharedPreference.TeacherDetailsPref;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.view.CommonToApp.BaseActivity;
import com.mychild.view.CommonToApp.GalleryActivity;
import com.mychild.view.CommonToApp.LoginActivity;

import com.mychild.view.CommonToApp.NoticeActivity;
import com.mychild.view.Parent.ExamsActivity;
import com.mychild.view.Parent.ParentInboxActivity;
import com.mychild.view.R;
import com.mychild.webserviceparser.TeacherHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sandeep on 20-03-2015.
 */
public class TeacherHomeActivity extends BaseActivity implements View.OnClickListener, RequestCompletion {
    String teacherName = "test_teacher";
    private TeacherModel teacherModel = null;
    private int selectedGrade = 0, SelectedSubject = 0;
    private CustomAdapter classNamesAdapter = null;
    private SubjectSpinnerAdapter subjectSpinnerAdapter = null;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<StudentDTO> selectedStudents = null;
    private TeacherDetailsPref teacherDetailsPref;

    enum RequestType {
        TYPE_TEACHER_DETAILS, TYPE_SUBJECTS, TYPE_POST_DATA;
    }
    RequestType type = RequestType.TYPE_TEACHER_DETAILS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        TopBar topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        teacherDetailWebservice();
        topBar.titleTV.setText(getString(R.string.app_name));
        topBar.backArrowIV.setImageResource(R.drawable.icon_home);
        topBar.logoutIV.setOnClickListener(this);
        ((ImageView) findViewById(R.id.assign_task_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.time_table_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.exams_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.mail_box_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.notice_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.calender_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.attendance)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.transport)).setOnClickListener(this);
      //  ((ImageView) findViewById(R.id.changepwd_teacher_imgview)).setOnClickListener(this);
        ((TextView) findViewById(R.id.teacher_name_tv)).setText(StorageManager.readString(this, getString(R.string.pref_username), ""));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {

            case R.id.assign_task_iv:
                startActivity(new Intent(this, AssignTaskActivity.class));
                break;
            case R.id.time_table_iv:
                //CommonUtils.getToastMessage(this, "Under Developent");
                startActivity(new Intent(this, TeacherTimeTabelActivity.class));
                break;
            case R.id.exams_iv:
                intent = new Intent(this, ExamsActivity.class);
                intent.putExtra(getString(R.string.key_from), getString(R.string.key_from_teacher));
                startActivity(intent);
                intent = null;
                break;

            case R.id.mail_box_iv:
                startActivity(new Intent(this, ParentInboxActivity.class));
                break;

            case R.id.notice_iv:
                intent = new Intent(this, NoticeActivity.class);
                intent.putExtra(getString(R.string.key_from), getString(R.string.key_from_teacher));
                startActivity(intent);
                intent = null;
                break;
            case R.id.calender_iv:
                intent = new Intent(this, TeacherCalenderEventsActivity.class);
                startActivity(intent);
                intent = null;
                break;

            case R.id.attendance:
                intent = new Intent(this, AttendenceUpdateActivity.class);
                startActivity(intent);
                intent = null;
                break;

            case R.id.transport:
                startActivity(new Intent(this, GalleryActivity.class));
                break;

        //    case R.id.changepwd_teacher_imgview:
          //     startActivity(new Intent(this,ChangePasswordActivity.class));
            //    break;

            case R.id.logoutIV:
                SharedPreferences clearSharedPreferenceForLogout;
                clearSharedPreferenceForLogout = getSharedPreferences("MyChild_Preferences", 0);
//                String regId = Pushbots.sharedInstance().regID();
//                WebServiceCall webServiceCall = new WebServiceCall(TeacherHomeActivity.this);
//                webServiceCall.unRegisterDevice(regId);
                SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
                editor.clear();
                editor.commit();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

           /* case R.id.message_btn:
                if (CommonUtils.isNetworkAvailable(this)) {
                    Constants.showProgress(this);
                    WebServiceCall call = new WebServiceCall(TeacherHomeActivity.this);
                    call.getCallRequest(getString(R.string.url_teacher_deatils));
                    CommonUtils.getLogs("URL is : " + getString(R.string.url_teacher_deatils));

                } else {
                    CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
                }
                break;
            case R.id.profile_btn:
                if (CommonUtils.isNetworkAvailable(this)) {
                    Constants.showProgress(this);
                    WebServiceCall call = new WebServiceCall(TeacherHomeActivity.this);
                    call.getCallRequest(getString(R.string.base_url) + getString(R.string.url_teachers_list) + teacherName);
                    CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teachers_list) + teacherName);
                } else {
                    CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
                }
                break;*/
            default:
        }

    }

    @Override
    public void onRequestCompletion(JSONObject obj, JSONArray responseArray) {
        CommonUtils.getLogs("Response :" + responseArray);
        {
            CommonUtils.getLogs("Response:::" + obj);
            if (obj != null && !obj.equals("")) {
                switch (type) {
                    case TYPE_TEACHER_DETAILS:
                        try {
                            teacherModel = TeacherHomeJsonParser.getInstance().getTeacherDetails(obj);
                            teacherDetailsPref=new TeacherDetailsPref(getApplicationContext());
                            teacherDetailsPref.SaveTeacherDetailsToPreference(obj, StorageManager.readString(this, getString(R.string.pref_username), ""));
                            Constants.stopProgress(this);
                            //    setClassSpinner();
                            obj = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }else{
                Constants.stopProgress(this);
                Constants.showMessage(this, "Retry Again ","NetWork Error");
            }
        }

        //ArrayList<StudentDTO> studentsList = TeacherHomeJsonParser.getInstance().getStudentsList(responseArray);
    }

    @Override
    public void onRequestCompletionError(String error) {

        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }


    public void setAsyncTaskCompletionListener(String object) {
        CommonUtils.getLogs("Response:::" + object);
        if (object != null && !object.equals("")) {
            switch (type) {
                case TYPE_TEACHER_DETAILS:
                    try {
                        JSONObject obj = new JSONObject(object);
                        teacherModel = TeacherHomeJsonParser.getInstance().getTeacherDetails(obj);
                         teacherDetailsPref=new TeacherDetailsPref(getApplicationContext());
                        teacherDetailsPref.SaveTeacherDetailsToPreference(obj, StorageManager.readString(this, getString(R.string.pref_username), ""));
                      
                    //    setClassSpinner();
                        obj = null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public  void teacherDetailWebservice(){
        teacherName = "/" + StorageManager.readString(this, getString(R.string.pref_username), "");
        if (CommonUtils.isNetworkAvailable(this)) {
            RequestType type = RequestType.TYPE_TEACHER_DETAILS;

            String teacherName1=StorageManager.readString(this, getString(R.string.pref_username), "");
         //   teacherDetailsPref=new TeacherDetailsPref(getApplicationContext());
           // teacherModel =teacherDetailsPref.getChildrenListFromPreference(teacherName1);
           Constants.showProgress(this);
            WebServiceCall call = new WebServiceCall(TeacherHomeActivity.this);

         call.getJsonObjectResponse(getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);

            CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
}
