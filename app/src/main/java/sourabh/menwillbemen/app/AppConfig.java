package sourabh.menwillbemen.app;


import android.os.Build;

import static android.os.Build.VERSION_CODES.BASE;

public class AppConfig {
	// Server user login url
//	public static String URL_LOGIN = "http://192.168.0.102/android_login_api/login.php";

	// Server user register url
//	public static String BASE = "http://mindwingstech.com/LifeLine/services/v1/";
//	public static String BASE = "http://192.168.1.100/ichiapp_web/v1/";


	//public static String IMAGES_BASE = "http://aws-bitnami-server.bitnamiapp.com/discountmart_web/images";
	public static String IMAGES_BASE = "http://192.168.0.101/discountmart_web/admin_web/images/";

//	public static String BASE = "http://aws-bitnami-server.bitnamiapp.com/discountmart_web/v1/";

	static String VERSION = "/v1/";



//	public static final String BASE = "http://192.168.42.164/menwillbemen_web" + VERSION;
//	public static final String BASE = "http://192.168.1.101/menwillbemen_web" + VERSION;
	public static final String BASE = "http://aws-bitnami-server.bitnamiapp.com/menwillbemen_web" + VERSION;


	public static String URL_GET_CITIES = BASE + "get_cities/";
	public static String URL_REGISTER = BASE + "register";
	public static String URL_LOGIN = BASE + "login";
	public static String URL_GET_ADSLIDERS = BASE + "get_adsliders";
	public static String URL_NO_IMAGE = IMAGES_BASE + "no_image_icon.png";

	public static String URL_GET_POSTS = BASE + "get_posts/";
	public static String URL_ADD_POST = BASE + "add_post";
	public static String URL_SUBMIT_FEEDBACK = BASE + "submit_feedback";


	public static String URL_GET_LANUGAGES = BASE + "get_languages";

	public static String URL_GET_DASHBOARD = BASE + "get_dashboard/";
	public static String URL_UPDATE_WHATSAPP_COUNT= BASE + "update_whatsapp_count";
	public static String URL_UPDATE_SHARE_COUNT= BASE + "update_share_count";
	public static String URL_LIKE_POST= BASE + "like_post";
	public static String URL_UNLIKE_POST= BASE + "unlike_post";

	public static String URL_CREATE_RETAILER_CATEGORY = BASE + "create_retailer_category";
	public static String URL_DELETE_RETAILER_CATEGORY = BASE + "delete_retailer_category/";
	public static String URL_UPDATE_RETAILER_CATEGORY = BASE + "update_retailer_category/";
	public static String URL_CREATE_OFFER = BASE + "create_offer";

	public static String URL_CREATE_SERVICE_CATEGORY = BASE + "create_service_category";
	public static String URL_DELETE_SERVICE_CATEGORY = BASE + "delete_service_category/";
	public static String URL_UPDATE_SERVICE_CATEGORY = BASE + "update_service_category/";

	public static String URL_DELETE_ADSLIDER = BASE + "delete_adslider/";
	public static String URL_ADD_ADSLIDERS = BASE + "add_adslider";

	public static String URL_CREATE_RETAILER = BASE + "create_retailer";
	public static String URL_DELETE_RETAILER = BASE + "delete_retailer/";

	public static String URL_CREATE_CITY = BASE + "create_city";
	public static String URL_DELETE_CITY = BASE + "delete_city/";
	public static String URL_UPDATE_CITY = BASE + "update_city/";
	public static String URL_UPDATE_OFFER = BASE + "update_offer/";
	public static String URL_DELETE_OFFER = BASE + "delete_offer/";

	public static String URL_GET_SERVICE_CATEGORIES = BASE + "get_service_categories";
	public static String URL_GET_OFFER_CATEGORIES = BASE + "get_offer_categories";
	public static String URL_GET_SHOPPING_CATEGORIES = BASE + "get_shopping_categories";

	public static String URL_GET_SERVICEPROVIDERS = BASE + "get_service_providers/";
	public static String URL_GET_RETAILERS = BASE + "get_retailers/";
	public static String URL_CREATE_COUPON_REQUEST = BASE + "create_coupon_request";
	public static String URL_VALIDATE_COUPON = BASE + "validate_coupon/";
	public static String URL_GET_PRODUCTS = BASE + "get_products/";
	public static String URL_GET_ADDRESSES = BASE + "get_addresses/";



	public static String URL_REQUEST_BLOOD = BASE + "request_blood";
	public static String SMS_ORIGIN = "LYFLYN";
	public static String URL_VERIFY_OTP = BASE + "activate_user_status";
	public static final String OTP_DELIMITER = ":";
	public static String URL_REQUEST_OTP = BASE + "request_OTP";

	public static String URL_UPDATE_USER = BASE + "update_user";
	public static String URL_UPDATE_PASSWORD = BASE + "update_password";
	public static String URL_UPDATE_PASSWORD_BY_PHN = BASE + "update_password_by_phn";


	public static String URL_REQUEST_OTP_TO_CHANGE_PHN = BASE + "request_OTP_to_update_phn";


	public static String URL_GET_NEWS = BASE + "get_news";

	public static String URL_GET_BLOOD_REQUESTS = BASE + "get_blood_requests/";
	public static String URL_CLOSE_REQUEST = BASE + "close_request/";
	public static String URL_ADD_ADDRESS = BASE + "add_address";
	public static String URL_PLACE_ORDER = BASE + "place_order";

	// Google project id
//	public static final String SENDER_ID = "mindwings-lifeline";
	public static final String SENDER_ID = "182991462265";

	public static final String API_KEY_GUEST= "guest";
	public static final String ARG_PARAM_POST_DATA= "POST_DATA";
	public static final String ARG_PARAM_TOTAL_ITEMS= "total_items";
	public static final String ARG_PARAM_CURRENT_ITEM_INDEX= "current_item_index";

	public static final String ARG_PARAM_POSITION= "POSITION";
	public static final String ARG_PARAM_IS_LATEST_POST_FRAGMENT= "IS_LATEST_POST_FRAGMENT";
	public static final String ARG_PARAM_IS_HOME= "IS_HOME";
	public static final String ARG_BACKGROUND_IMAGE = "background_image";
	public static final String ARG_PARAM_CATEGORY_ID= "category_id";
	public static final String ARG_PARAM_TYPE= "type";

	public static final String KEY_LEFT_HEADER_IMAGE = "left_header_image";
	public static final String KEY_RIGHT_HEADER_IMAGE = "right_header_image";


	public static final String KEY_TITLE_LATEST = "LATEST";
	public static final String KEY_TITLE_TOP50 = "TOP 50";
	public static final String KEY_TITLE_TRENDING = "TRENDING";
	public static final String KEY_REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";
	public static final String KEY_PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
	// id to handle the notification in the notification tray
	public static final int KEY_NOTIFICATION_ID = 100;
	public static final int KEY_NOTIFICATION_ID_BIG_IMAGE = 101;
	public static final String KEY_CACHE_HIT = "cache-hit";

	public static final String KEY_SETTINGS_CHANGED = "settings_changed";


	public static final String TAG = "LifeLine";
	public static final String DISPLAY_MESSAGE_ACTION =
			"lifeline.mindwings.lifeline.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";
	public static final String KEY_LANGUAGES= "languages";
	public static final String KEY_TOPIC_GLOBAL = "global";


	public static final String KEY_API_KEY= "api_key";
	public static final String KEY_USER= "user";
	public static final String KEY_LOADING_MORE_STYLE = "loading_more_style";
	public static final String KEY_REFRESH_STYLE = "refresh_style";

	public static final String KEY_SHARE= "share";
	public static final String KEY_WHATSAPP= "whatsapp";
	public static final String KEY_LIKE= "like";
	public static final String KEY_UNLIKE= "unlike";

	public static final String KEY_BACKGROUND_IMAGE = "background_image";

	public static final String KEY_INTERSTITIAL_ITERATIONS = "interstitial_iterations";
	public static final String KEY_NATIVE_ITERATIONS = "native_iterations";
	public static final String KEY_TYPE_LATEST= "latest";
	public static final String KEY_TYPE_TOP= "top";


	public static String FIREBASE_KEY_LANGUAGE_SELECTED = "language_selected";
	public static String FIREBASE_KEY_TAPPED_ON_DETAILED_POST = "tapped_on_detailed_post";
	public static String FIREBASE_KEY_CATEGORY = "category";
	public static String FIREBASE_KEY_POST_LIKED = "post_liked";
	public static String FIREBASE_KEY_POST_UNLIKED = "post_unliked";
	public static String FIREBASE_KEY_POST_SHARED_TO_WHATSAPP = "post_shared_to_whatsapp";
	public static String FIREBASE_KEY_POST_SHARED_TO_EXTERNAL = "post_shared_to_external";
	public static String FIREBASE_KEY_APP_SHARED = "app_shared";
	public static String FIREBASE_KEY_USER_ID = "user_id";

	public static String CRASHLYTICS_KEY_ERROR_CODE = "error_code";
	public static String CRASHLYTICS_KEY_ERROR = "error";


	public static int TIMER = 8000;
	public static int MAX_LINES = 6;
	public static int LARGE_NUMBER = 99999;


}
