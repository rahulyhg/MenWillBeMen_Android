package sourabh.menwillbemen.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.data.LanguageData;
import sourabh.menwillbemen.data.SettingData;

import static android.R.attr.id;
import static android.provider.CallLog.Calls.NEW;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "IchiApp";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_USER_ID = "id";
	private static final String KEY_CITY_ID = "city_id";
	private static final String KEY_CARD_COLORS = "card_colors";
	private static final String KEY_BACKGROUND_IMAGE = "background_image";
	private static final String KEY_FIRST_RUN = "first_run";
	private static final String KEY_LANGUAGES = "languages";
	private static final String KEY_LANGUAGE_ID = "id";

	private static final String KEY_CITY_NAME = "city_name";

	private static final String KEY_FNAME = "fname";
	private static final String KEY_LNAME = "lname";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_GCMID = "GCMID";
	private static final String KEY_API_KEY = "API_KEY";
	private static final String KEY_MEMBERSHIP = "membership";
	private static final String KEY_IMEI = "imei";

	private static final String KEY_STATUS = "status";


	private static final String KEY_CREATED_AT= "created_at";

	private static final String KEY_IS_USER_INFO_SAVED = "is_user_info_saved";



	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();

	}



	public void    setUserInfo(String id, String fname, String lname,
							   String phone, String email,String API_KEY,
							   String GCMID , String imei, String membership, String status, String created_at) {





		editor.putString(KEY_USER_ID,id);
		editor.putString(KEY_FNAME,fname);
		editor.putString(KEY_LNAME, lname);
		editor.putString(KEY_PHONE, phone);

		editor.putString(KEY_EMAIL,email);
		editor.putString(KEY_API_KEY,API_KEY);
		editor.putString(KEY_GCMID, GCMID);
		editor.putString(KEY_STATUS, status);


		editor.putString(KEY_CREATED_AT, created_at);
		editor.putString(KEY_IMEI, imei);
		editor.putString(KEY_MEMBERSHIP, membership);

		editor.putBoolean(KEY_IS_USER_INFO_SAVED, true);



		// commit changes
		editor.commit();

		Log.d(TAG, "User info set!");
	}


	public void setDefaultCity(String city_id, String city_name)
	{
		editor.putString(KEY_CITY_ID,city_id);
		editor.putString(KEY_CITY_NAME,city_name);
		editor.commit();

	}

	public void setFirstRun()
	{
		editor.putBoolean(KEY_FIRST_RUN,true);
		editor.commit();

	}

	public void setLangauges(JSONObject jsonObject)
	{
		editor.putString(KEY_LANGUAGES,jsonObject.toString());
		editor.commit();

	}

	public void setSelectedLanguageId(int id)
	{
		editor.putInt(KEY_LANGUAGE_ID,id);
		editor.commit();

	}

	public int getSelectedLanguageId()
	{
		return pref.getInt(KEY_LANGUAGE_ID,1) ;
	}

	public List<LanguageData> getLangauges(){

		try {

//			JSONObject jsonObject = new JSONObject(pref.getString(KEY_LANGUAGES,""));
//
//
//			List<LanguageData> languageDataList = (List<LanguageData>)
//					CommonUtilities.getObjectFromJson(jsonObject.getJSONArray(AppConfig.API_KEY_GUEST))
//					,LanguageData.class);
			JSONObject jsonObject = new JSONObject(pref.getString(KEY_LANGUAGES,""));
			LanguageData languageData = CommonUtilities.getObjectFromJson(jsonObject,LanguageData.class);

			return languageData.getLanguages();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;

	}

	public int getThemeColor()
	{
		return PreferenceManager.
				getDefaultSharedPreferences(this._context)
				.getInt("theme_color", Color.YELLOW);

	}

	public boolean checkFirstRun(){
		return pref.getBoolean(KEY_FIRST_RUN,false) ;
	}

	public void setBackgroundImage(String path)
	{
		editor.putString(KEY_BACKGROUND_IMAGE,path);
		editor.commit();
	}

	public String getBackgroundImage()
	{
		return pref.getString(KEY_BACKGROUND_IMAGE,KEY_BACKGROUND_IMAGE) ;

	}

	public void setCardColors(List<SettingData> cardColorsDataList)
	{

//		HashMap<String,String> hashMapCardColors = new HashMap<>();
//		for (SettingData cardColor :
//				cardColorsDataList) {
//
//			hashMapCardColors.put(cardColor.getName(),cardColor.getValue())
//		}
//
		Set<String> color = new HashSet<>();

		for (SettingData cardColor:
			 cardColorsDataList) {
			color.add(cardColor.getValue());
		}
		editor.putStringSet(KEY_CARD_COLORS,color);

//		editor.putString(KEY_CARD_COLORS,new Gson().toJson(cardColorsDataList));
		editor.commit();

	}

	public Set<String> getCardColors(){
		//get from shared prefs
//		Gson gson = new Gson();
//
//		String storedCardColors = pref.getString(KEY_CARD_COLORS, "oopsDintWork");
//		java.lang.reflect.Type type = new TypeToken<List<SettingData>>(){}.getType();
////		HashMap<String, String> cardColors = gson.fromJson(storedHashMapString, type);
////		return cardColors;
//
//		return gson.fromJson(storedCardColors,type);
		Set<String> empty=null;
		return pref.getStringSet(KEY_CARD_COLORS,empty );

	}

	public  String getCityId() {
		return pref.getString(KEY_CITY_ID,KEY_CITY_ID) ;

	}

	public  String getCityName() {
		return pref.getString(KEY_CITY_NAME,KEY_CITY_NAME) ;

	}


	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}

//	public void updateCode(String code) {
//
//		editor.putString(KEY_CODE, code);
//
//		// commit changes
//		editor.commit();
//
//	}

	public void clearAll(){


		editor.clear().commit();
	}



	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
	public boolean isUserInfoSaved(){
		return pref.getBoolean(KEY_IS_USER_INFO_SAVED, false);
	}

	public boolean isMember(){
		if(pref.getString(KEY_MEMBERSHIP,KEY_MEMBERSHIP).equals("1"))
        {
			return true;
		}
        else
        {
			return false;
		}

	}


	public String getUserName(){
		return pref.getString(KEY_FNAME,"")+" "+pref.getString(KEY_LNAME,"");
	}
//	public String getOTPCode(){
//		return pref.getString(KEY_CODE,"");
//	}
	public String getAPIKEY(){
		return pref.getString(KEY_API_KEY, "guest");
	}


	public  String getFname() {
		return pref.getString(KEY_FNAME,KEY_FNAME) ;
	}

	public  String getPhone() {
		return pref.getString(KEY_PHONE,KEY_PHONE) ;
	}

	public  String getLname() {
		return pref.getString(KEY_LNAME,KEY_LNAME) ;
	}

	public  String getUserId() {
		return pref.getString(KEY_USER_ID,KEY_USER_ID) ;

	}

//	public  String getCityId() {
//		return pref.getString(KEY_CITY_ID,KEY_CITY_ID) ;
//
//	}
//
//	public  String getCityName() {
//		return pref.getString(KEY_CITY_NAME,KEY_CITY_NAME) ;
//
//	}
//
//	public  String getKeyPhn1() {
//		return pref.getString(KEY_PHN1,KEY_PHN1) ;
//
//	}
//
//	public  String getBloodGroupId() {
//		return pref.getString(KEY_BLOOD_GROUP_ID,KEY_BLOOD_GROUP_ID) ;
//
//	}
//
//	public  String getBloodGroupName() {
//		return pref.getString(KEY_BLOOD_GROUP_NAME,KEY_BLOOD_GROUP_NAME) ;
//
//	}
}
