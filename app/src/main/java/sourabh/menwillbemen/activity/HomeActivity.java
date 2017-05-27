package sourabh.menwillbemen.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.fabric.sdk.android.Fabric;
import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.AppController;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.CategoryData;
import sourabh.menwillbemen.data.LanguageData;
import sourabh.menwillbemen.data.PostItemData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.data.SettingData;
import sourabh.menwillbemen.data.TranslationData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.NotificationUtils;
import sourabh.menwillbemen.helper.SessionManager;

import static android.media.CamcorderProfile.get;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_APP_SHARED;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_CATEGORY;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_LANGUAGE_SELECTED;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_SHARED_TO_EXTERNAL;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_USER_ID;
import static sourabh.menwillbemen.app.AppConfig.KEY_SHARE;


public class HomeActivity extends BaseActivity
        implements
//        NavigationView.OnNavigationItemSelectedListener,
        LatestFragment.OnFragmentInteractionListener
{


    Context context;
    PostsData postsData;
    List<CategoryData> categoryDataList;
    private Drawer result = null;
    private Drawer resultAppended = null;
    Toolbar toolbar;
    Bundle savedInstanceState;
    List<SettingData> settingDataList;
    List<TranslationData> translationDataList;

    String activeCategoryId;
    HashMap<String, String> hashMapSettings = new HashMap<>();
    SessionManager sessionManager;
    SmartTabLayout viewPagerTab;
    ViewPager viewPager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private BroadcastReceiver mSettingsBroadcastReceiver;

    Boolean isSettingsChanged = false;
    InterstitialAd mInterstitialAd;

    int ad_counter = 0;
    private long mRequestStartTime;
    private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton fab;
    int selectedTempCategoryId;
    NiftyDialogBuilder addPostDialog;
    NiftyDialogBuilder feedbackDialog;
    int emojiSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.savedInstanceState = savedInstanceState;
        context = this;
        sessionManager = new SessionManager(context);
        Fabric.with(this, new Crashlytics());


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

//        themeColor = sessionManager.getThemeColor();
        Log.d(TAG,"Home");
        setSupportActionBar(toolbar);
//        final LayoutInflater factory = getLayoutInflater();
//        final View headerView = factory.inflate(R.layout.drawer_header, null);
//        imageViewHeader = (ImageView) headerView.findViewById(imageViewHeader);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        postsData = (PostsData) getIntent().getSerializableExtra(AppConfig.ARG_PARAM_POST_DATA);


        if(postsData == null){
            getDashBoard();
        }else{
            parseDashBoardData();
        }

        setBroadcastListener();


        String token = sessionManager.getFCMToken();

        if(!sessionManager.isLoggedIn() && sessionManager.getFCMToken() != ""){


        Log.d(TAG,"Not registered yet, fcm blank");
            RegisterUser();
        }



//        setTheme();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_add)
                .color(Color.WHITE)
                .sizeDp(16));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace  with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


                showOnPostDialog();



            }
        });


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getSupportFragmentManager(), FragmentPagerItems.with(this)
//                .add("Latest", LatestFragment.class)
//                .add("Top 50", LatestFragment.class)
//                .create());
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(adapter);
//
//        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
//        viewPagerTab.setViewPager(viewPager);



//        getDashBoard();

        initialiseInterstitialAd();






    }



    void showOnPostDialog(){



        addPostDialog=NiftyDialogBuilder.getInstance(context);

        addPostDialog
                .withTitle(null)
                .withDialogColor(getResources().getColor(R.color.colorPrimary))
                .withMessage(null)
                .setCustomView(R.layout.create_post_dialog,context)
                .isCancelableOnTouchOutside(true)
                .withDuration(700)
                .withEffect(Effectstype.Fliph).show();   //def Effectstype.Slidetop





        final TextView txtLanguage = (TextView) addPostDialog.findViewById(R.id.txtLanguage);
        final TextView txtCategory = (TextView) addPostDialog.findViewById(R.id.txtCategory);
        final TextView txtPost = (TextView) addPostDialog.findViewById(R.id.txtPost);
        Button btnSubmit = (Button) addPostDialog.findViewById(R.id.btnAddPost);



        txtLanguage.setText(sessionManager.getSelectedLanguageTitle());
        txtLanguage.setCompoundDrawablesWithIntrinsicBounds(

                null,null,
                new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_list)
                        .color(getResources().getColor(R.color.colorAccent))
                        .sizeDp(20), null);

//                txtLanguage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        final List<LanguageData> languageDataList = (sessionManager.getLangauges());
//                        String []languages = CommonUtilities.convertLangaugeListToLanguageArray
//                                (languageDataList);
//
//                        new MaterialDialog.Builder(context)
//                                .title("Choose Language")
//                                .cancelable(true )
//                                .items(languages)
//                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
//                                    @Override
//                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//
//
//                                        LanguageData selectedLanguageData = languageDataList.get(which);
//                                        txtLanguage.setText(selectedLanguageData.getLanguage_title());
//
//
//                                        return true;
//                                    }
//                                })
////                        .positiveText("Choose")
//                                .show();
//                    }
//                });

//                txtCategory.setText(sessionManager.getSelectedLanguageTitle());

        txtCategory.setCompoundDrawablesWithIntrinsicBounds(
                null,null,

                new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_list)
                        .color(getResources().getColor(R.color.colorAccent))
                        .sizeDp(20), null);

        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String []categories = CommonUtilities.convertCategoryListToCategoryArray(categoryDataList);

                new MaterialDialog.Builder(context)
                        .title("Choose Language")
                        .cancelable(true )
                        .items(categories)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {


                                CategoryData selectedCategoryData = categoryDataList.get(which);
                                txtCategory.setText(selectedCategoryData.getCategory_name());
                                selectedTempCategoryId = selectedCategoryData.getId_category();

                                return true;
                            }
                        })
//                        .positiveText("Choose")
                        .show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtCategory.getText().toString().equals("")){

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(txtCategory);

                }else if(txtPost.getText().toString().equals("")) {
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(txtPost);
                }
                else
                {
                    submitPost(sessionManager.getSelectedLanguageId()
                            ,selectedTempCategoryId,txtPost.getText().toString());
                }

            }
        });
    }

    void submitPost(int language_id,int category_id,String post)
    {
        Log.d(TAG,"submitPost");

        Map<String, String> params = new HashMap<>();

        params.put("language_id", String.valueOf(language_id));
        params.put("category_id", String.valueOf(category_id));
        params.put("post", String.valueOf(post));




        CustomRequest jsonObjReq = new CustomRequest(this,this,
                true, Request.Method.POST,
                AppConfig.URL_ADD_POST, false,
                params, CommonUtilities.buildHeaders(context),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        Log.d(TAG,"submitPost got responce");

                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){

                                new MaterialDialog.Builder(context)
                                        .title("Failed")
                                        .content("Your message failed to submitted, Please try again")
                                        .positiveText("Close")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                addPostDialog.dismiss();
                                            }
                                        })
                                        .show();
//                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }else{


                                new MaterialDialog.Builder(context)
                                        .title("Submitted")
                                        .content("Your message is submitted, it will be added soon.")
                                        .positiveText("Done")

                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                addPostDialog.dismiss();
                                            }
                                        })
                                        .show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

//        void    setTheme(){
//
//                try{
//                    toolbar.setBackgroundColor(themeColor);
//                    viewPagerTab.setBackgroundColor(themeColor);
//                    if (android.os.Build.VERSION.SDK_INT >= 21) {
//                        Window window = this.getWindow();
//                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                        window.setStatusBarColor(themeColor);
//                    }
//                }catch (Exception ex){
//
//                    String s = ";";
//                }
//            }



    void setupDrawer(Bundle savedInstanceState){



//initialize and create the image loader logic

        Log.d(TAG,"setupDrawer");



        //first create the main drawer (this one will be used to add the second drawer on the other side)
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.drawer_header)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home)
                                .withIcon(FontAwesome.Icon.faw_home)
//                                .withSelectedIconColor(themeColor)
                                .withIdentifier(1),


                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_change_language)
                                .withIcon(GoogleMaterial.Icon.gmd_language)
//                                .withSelectedIconColor(themeColor)
                                .withIdentifier(2),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_setting)
                                .withIcon(FontAwesome.Icon.faw_cog)
//                                .withSelectedIconColor(themeColor)
                                .withIdentifier(3),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_rating)
                                .withIcon(FontAwesome.Icon.faw_star )
//                                .withSelectedIconColor(themeColor)
                                .withIdentifier(4),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_feedback)
                                .withIcon(FontAwesome.Icon.faw_pencil)
//                                .withSelectedIconColor(themeColor)
                                .withIdentifier(5)
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withBadge("12").withIdentifier(3),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_contac  t).withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
//                        Toast.makeText(HomeActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
//                        Toast.makeText(HomeActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the sky
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {

                                switch ((int) drawerItem.getIdentifier()){

                                    case 1:
                                        break;
                                    case 2:
//                                        Toast.makeText(HomeActivity.this, "Change Language", Toast.LENGTH_SHORT).show();
                                        showChooseLanguageDalog();
                                        break;
                                    case 3:

                                        startActivity(new Intent(HomeActivity.this,SettingsActivity.class));

                                        break;
                                    case 4:
//                                        Toast.makeText(HomeActivity.this, "Rate", Toast.LENGTH_SHORT).show();
                                        rateApp();
                                        break;
                                    case 5:
//                                        Toast.makeText(HomeActivity.this, "Feedback", Toast.LENGTH_SHORT).show();

                                        openFeedbackDialog();

                                        break;


                                }

                            }

//                            if (drawerItem instanceof Badgeable) {
//                                Badgeable badgeable = (Badgeable) drawerItem;
//                                if (badgeable.getBadge() != null) {
//                                    //note don't do this if your badge contains a "+"
//                                    //only use toString() if you set the test as String
//                                    int badge = Integer.valueOf(badgeable.getBadge().toString());
//                                    if (badge > 0) {
//                                        badgeable.withBadge(String.valueOf(badge - 1));
//                                        result.updateItem(drawerItem);
//                                    }
//                                }
//                            }
                        }

                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof SecondaryDrawerItem) {
//                            Toast.makeText(HomeActivity.this, ((SecondaryDrawerItem) drawerItem).getName().getText(HomeActivity.this), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        if (drawerView == result.getSlider()) {
                            Log.e("sample", "left opened");
                        } else if (drawerView == resultAppended.getSlider()) {
                            Log.e("sample", "right opened");
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (drawerView == result.getSlider()) {
                            Log.e("sample", "left closed");
                        } else if (drawerView == resultAppended.getSlider()) {
                            Log.e("sample", "right closed");
                        }
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .build();

        //now we add the second drawer on the other site.
        //use the .append method to add this drawer to the first one










        resultAppended = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
//                .withSliderBackgroundColor(Util.getRandomColor(context))
                .withHasStableIds(true)
                .withHeader(R.layout.drawer_header) //set the AccountHead
//                .withFooter(R.layout.footer)
                .withDisplayBelowStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .withDrawerItems(
                        getCategoryDrawerItems()
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1)

//                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
//                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
//                        new DividerDrawerItem(),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
//                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
//                            Toast.makeText(HomeActivity.this, ((Nameable) drawerItem).getName().getText(HomeActivity.this), Toast.LENGTH_SHORT).show();

//
                            if(position==1){

                                activeCategoryId = null;
                            }else{
                                activeCategoryId = String.valueOf(categoryDataList.get(position-2).getId_category());
                            }

//

                                    ((LatestFragment) getSupportFragmentManager().getFragments().get(0))
                                            .categoryChanged(activeCategoryId);

                                    ((LatestFragment) getSupportFragmentManager().getFragments().get(1))
                                            .categoryChanged(activeCategoryId);



//                            }
//                            startActivity(new Intent(HomeActivity.this, PostsActivity.class));
                        }
                        return false;
                    }
                })
                .withDrawerGravity(Gravity.END)
                .append(result);

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);


        ImageView viewFooterRight = (ImageView) resultAppended.getHeader();
        ImageView viewHeaderLeft = (ImageView) result.getHeader();

        loadHeaderFooter(viewFooterRight,hashMapSettings.get(AppConfig.KEY_RIGHT_HEADER_IMAGE));
        loadHeaderFooter(viewHeaderLeft,hashMapSettings.get(AppConfig.KEY_LEFT_HEADER_IMAGE));






    }

    private void openFeedbackDialog() {

        feedbackDialog=NiftyDialogBuilder.getInstance(context);

        feedbackDialog
                .withTitle(null)
                .withDialogColor(getResources().getColor(R.color.colorPrimary))
                .withMessage(null)
                .setCustomView(R.layout.feedback_dialog,context)
                .isCancelableOnTouchOutside(true)
                .withDuration(700)
                .withEffect(Effectstype.Fliph).show();   //def Effectstype.Slidetop

        feedbackDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                emojiSelected = 0;
            }
        });

        final EditText txtFeedBack = (EditText) feedbackDialog.findViewById(R.id.txtFeedback);
        final ImageButton feedback_1 = (ImageButton) feedbackDialog.findViewById(R.id.feedback_1);
        final ImageButton feedback_2 = (ImageButton) feedbackDialog.findViewById(R.id.feedback_2);
        final ImageButton feedback_3 = (ImageButton) feedbackDialog.findViewById(R.id.feedback_3);
        final ImageButton feedback_4 = (ImageButton) feedbackDialog.findViewById(R.id.feedback_4);
        final ImageButton feedback_5 = (ImageButton) feedbackDialog.findViewById(R.id.feedback_5);
        final Button btnSubmit = (Button) feedbackDialog.findViewById(R.id.btnAddFeedback);
        final LinearLayout layout_emojies = (LinearLayout) feedbackDialog.findViewById(R.id.layout_emojies);


        feedback_1.setAlpha(128);
        feedback_2.setAlpha(128);
        feedback_3.setAlpha(128);
        feedback_4.setAlpha(128);
        feedback_5.setAlpha(128);

        feedback_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiSelected = 1;
                feedback_1.setAlpha(255);
                feedback_2.setAlpha(128);
                feedback_3.setAlpha(128);
                feedback_4.setAlpha(128);
                feedback_5.setAlpha(128);

            }
        });
        feedback_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiSelected = 2;

                feedback_1.setAlpha(128);
                feedback_2.setAlpha(255);
                feedback_3.setAlpha(128);
                feedback_4.setAlpha(128);
                feedback_5.setAlpha(128);

            }
        });
        feedback_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiSelected = 3;

                feedback_1.setAlpha(128);
                feedback_2.setAlpha(128);
                feedback_3.setAlpha(255);
                feedback_4.setAlpha(128);
                feedback_5.setAlpha(128);

            }
        });
        feedback_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiSelected = 4;

                feedback_1.setAlpha(128);
                feedback_2.setAlpha(128);
                feedback_3.setAlpha(128);
                feedback_4.setAlpha(255);
                feedback_5.setAlpha(128);

            }
        });
        feedback_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiSelected = 5;

                feedback_1.setAlpha(128);
                feedback_2.setAlpha(128);
                feedback_3.setAlpha(128);
                feedback_4.setAlpha(128);
                feedback_5.setAlpha(255);

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtFeedBack.getText().toString().equals("")){

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(txtFeedBack);
                }
                else if(emojiSelected == 0){

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(layout_emojies);

                }
                else{
                    submitFeedBack(emojiSelected,txtFeedBack.getText().toString());
                }
            }
        });


    }

    void loadHeaderFooter(ImageView imageViewHeader,String url){

        Log.d(TAG,"loadHeaderFooter "+url);

        Picasso.with(context)
                .load(url)
                .into(imageViewHeader, new  com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        Log.d(TAG,"headerfooter loaded");

//                        Toast.makeText(context,"Loaded",Toast.LENGTH_SHORT).show();;
                    }

                    @Override
                    public void onError() {
//                        Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show();;

                    }
                });


    }


    void submitFeedBack(int emojiSelected, String feedback){

        Log.d(TAG,"submitFeedBack");

        Map<String, String> params = new HashMap<>();

        params.put("emoji", String.valueOf(emojiSelected));
        params.put("feedback", String.valueOf(feedback));




        CustomRequest jsonObjReq = new CustomRequest(this,this,
                true, Request.Method.POST,
                AppConfig.URL_SUBMIT_FEEDBACK,false,
                params, CommonUtilities.buildHeaders(context),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        Log.d(TAG,"submitFeedBack got responce");

                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){

                                new MaterialDialog.Builder(context)
                                        .title(getString(R.string.failed))
                                        .content(getString(R.string.feedback_failed))
                                        .positiveText(getString(R.string.close))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                feedbackDialog.dismiss();
                                            }
                                        })
                                        .show();
//                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }else{


                                new MaterialDialog.Builder(context)
                                        .title(getString(R.string.thank_you))
                                        .content(getString(R.string.feedback_submitted))
                                        .positiveText(getString(R.string.done))

                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                feedbackDialog.dismiss();
                                            }
                                        })
                                        .show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    ArrayList<IDrawerItem> getCategoryDrawerItems() {


        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();


        drawerItems.add(
                new PrimaryDrawerItem()
//                        .withIcon(GoogleMaterial.Icon.gmd_keyboard_arrow_left)
//                            .withTextColor(getResources().getColor(R.color.colorWhite))
//                        .withSelectedIconColor(themeColor)
                        .withName("Fresh Stuff")
                        .withBadge(String.valueOf(getTotalPostsCount()))
                        .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.colorAccent))

        );

        for(CategoryData category : categoryDataList) {



//            CustomUrlPrimaryDrawerItem customPrimaryDrawerItem = new CustomUrlPrimaryDrawerItem();
//            customPrimaryDrawerItem.withIcon(FontAwesome.Icon.faw_arrow_circle_left );
//            customPrimaryDrawerItem.withName(category.getCategory_name());

            drawerItems.add(
//                    new CustomUrlPrimaryDrawerItem()
//                            .withName("demo")
//                            .withDescription("demo desc")
//                            .withIcon("http://192.168.1.101/menwillbemen_web/images/background/header.jpg")

                    new PrimaryDrawerItem()
//                            .withIcon(GoogleMaterial.Icon.gmd_keyboard_arrow_left )
//                            .withTextColor(getResources().getColor(R.color.colorWhite))
//                            .withSelectedIconColor(themeColor)
                            .withName(category.getCategory_name())
                    .withBadge(category.getCount().toString())
                            .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.colorPrimaryDark))

            );


            //if you have a id you can also do: .withIdentifier(category.getIdentifier());
            //depending on what you need to identify or to do the logic on click on one of those items you can also set a tag on the item: .withTag(category);
        }
        return drawerItems;
    }

    String getTotalPostsCount( ){
        int count = 0;
        for(CategoryData category : categoryDataList) {
            count = count + category.getCount();

        }
        return (CommonUtilities.format(count));
    }

    void   setFragments(String first_fragment_title, String second_fragment_title){

        Log.d(TAG,"setFragments");


        Bundle argsLatest = new Bundle();
        Bundle argsTop = new Bundle();

        Log.d("Home","setFragments");

        argsLatest.putBoolean(AppConfig.ARG_PARAM_IS_LATEST_POST_FRAGMENT,true);
        argsTop.putBoolean(AppConfig.ARG_PARAM_IS_LATEST_POST_FRAGMENT,false);


        argsLatest.putBoolean(AppConfig.ARG_PARAM_IS_HOME,true);
        argsTop.putBoolean(AppConfig.ARG_PARAM_IS_HOME,true);

//        argsLatest.putString(AppConfig.ARG_BACKGROUND_IMAGE,SettingsToHashMapSettings(settingDataList).get(AppConfig.KEY_BACKGROUND_IMAGE));
//        argsTop.putString(AppConfig.ARG_BACKGROUND_IMAGE,SettingsToHashMapSettings(settingDataList).get(AppConfig.KEY_BACKGROUND_IMAGE));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(first_fragment_title, LatestFragment.class,argsLatest)
                .add(second_fragment_title, LatestFragment.class,argsTop)

                .create());

        viewPager.setAdapter(adapter);
//        viewPagerTab.setSelectedIndicatorColors(Util.getRandomColor());
        viewPagerTab.setViewPager(viewPager);
    }


    public void getDashBoard()
    {

        Log.d(TAG,"getDashBoard");
        mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.

        CustomRequest jsonObjReq  = new CustomRequest(this,this,
                true, Request.Method.GET,
                AppConfig.URL_GET_DASHBOARD+sessionManager.getSelectedLanguageId(),
                false,
                CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),



                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                        Log.d(TAG,"getDashBoard got responce in : "+totalRequestTime);

                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){

                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }else{

                                //JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);
                                parseDashBoardJson(js.getData());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {


                long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                Log.d(TAG,"getDashBoard got error in : "+totalRequestTime);

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }







//    public void  getPosts(String category_id, boolean toShowLoading, int pageNo)
//    {
//
//        String URL = AppConfig.URL_GET_POSTS
//                +sessionManager.getSelectedLanguageId()+"/"
//                +category_id+"/"+String.valueOf(pageNo);
//
//        Volley.newRequestQueue(this).add(new CustomRequest(this,this,
//                toShowLoading, Request.Method.GET, URL,
//                CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),
//
//
//                new com.android.volley.Response.Listener() {
//
//                    @Override
//                    public void onResponse(Object response) {
//                        JSONObject jsonObject = (JSONObject) response;
//                        JsonSeparator js= new JsonSeparator(context,jsonObject);
//
//                        try {
//                            if(js.isError()){
//
//                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
//
//                                if(isLatestFragment){
//                                    ((LatestFragment) getSupportFragmentManager().getFragments().get(0))
//                                            .endOFPosts();
//                                }else{
//                                    ((LatestFragment) getSupportFragmentManager().getFragments().get(1))
//                                            .endOFPosts();
//                                }
//
//                            }else{
//
//                                //JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);
//                                parsePostsJson(js.getData());
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//
//                }, new com.android.volley.Response.ErrorListener() {
//
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
//
//            }
//        }));
//    }




    public void parseDashBoardJson(JSONObject jsonObject){
        Log.d(TAG,"parseDashBoardJson");

        postsData = CommonUtilities.getObjectFromJson(jsonObject, PostsData.class);
        parseDashBoardData();

    }

    public  void parseDashBoardData(){

        Log.d(TAG,"parseDashBoardData");


        categoryDataList = postsData.getCategories();
        settingDataList = postsData.getSettings();

//        translationDataList = postsData.getTranslations();
//        sessionManager.setTranslation(translationDataList);

        sessionManager.setCardColors(postsData.getCard_colors());
        try {

            String languageJson = "{\""+AppConfig.KEY_LANGUAGES+"\":"+new Gson().toJson(postsData.getLanguages())+"}";

            sessionManager.setLangauges(new JSONObject(languageJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        hashMapSettings = SettingsToHashMapSettings(settingDataList);


        sessionManager.setLoadingRefreshStyle(Integer.parseInt(hashMapSettings.get(AppConfig.KEY_REFRESH_STYLE)),
                Integer.parseInt(hashMapSettings.get(AppConfig.KEY_LOADING_MORE_STYLE)));

        sessionManager.setBackgroundImage(hashMapSettings.get(AppConfig.KEY_BACKGROUND_IMAGE));
        sessionManager.setIterations(
                Integer.parseInt(hashMapSettings.get(AppConfig.KEY_INTERSTITIAL_ITERATIONS)),
                Integer.parseInt(hashMapSettings.get(AppConfig.KEY_NATIVE_ITERATIONS))

                );



        setupDrawer(savedInstanceState);
        setFragments(AppConfig.KEY_TITLE_LATEST,AppConfig.KEY_TITLE_TOP50);

    }

//    public void   parsePostsJson(JSONObject jsonObject){
//        postsData = CommonUtilities.getObjectFromJson(jsonObject, PostsData.class);
//
//        if(toAppendOrRefresh){
//
//
//            if(isLatestFragment){
//                ((LatestFragment) getSupportFragmentManager().getFragments().get(0))
//                        .updateRecyclerViewData(postsData);
//            }else{
//                ((LatestFragment) getSupportFragmentManager().getFragments().get(1))
//                        .updateRecyclerViewData(postsData);
//            }
//
//
////            FragmentManager fragmentManager = getSupportFragmentManager();
////// (if you're not using the support library)
////// FragmentManager fragmentManager = getFragmentManager();
////            for (Fragment fragment : fragmentManager.getFragments()) {
////                if (fragment != null && fragment.isVisible() && fragment instanceof LatestFragment) {
////                    ((LatestFragment) fragment).updateRecyclerViewData(postsData);
////                }
////            }
//
//
//        }else{
//            setFragments(AppConfig.KEY_TITLE_LATEST,AppConfig.KEY_TITLE_TRENDING);
//
//        }
////        setupDrawer(savedInstanceState);
//
//
//    }

    void initialiseInterstitialAd(){

        Log.d(TAG,"initialiseInterstitialAd");

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BCDCAE3C6F97EF417DD1D5FFB4F86E3E")

                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
//                Toast.makeText(context,"onAdClosed",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                initialiseInterstitialAd();
//                Toast.makeText(context,"onAdFailedToLoad",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
//                Toast.makeText(context,"onAdLeftApplication",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
//                Toast.makeText(context,"onAdOpened",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

//                Toast.makeText(context,"onAdLoaded",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {

            mInterstitialAd.show();
        }
    }


    void rateApp(){


        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }

    }

    void showChooseLanguageDalog()
    {

        final List<LanguageData> languageDataList = (sessionManager.getLangauges());

        String []languages = CommonUtilities.convertLangaugeListToLanguageArray
                (languageDataList);

        new MaterialDialog.Builder(this)
                .title("Choose Language")
                .cancelable(true )
                .items(languages)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {


                        LanguageData selectedLanguageData = languageDataList.get(which);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(sessionManager.getSelectedLanguageCode());

                        Bundle bundle = new Bundle();
                        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, selectedLanguageData.getId_language());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, selectedLanguageData.getLanguage_title());
                        mFirebaseAnalytics.logEvent(FIREBASE_KEY_LANGUAGE_SELECTED, bundle);
                        mFirebaseAnalytics.setUserProperty(FIREBASE_KEY_LANGUAGE_SELECTED, selectedLanguageData.getLanguage_title());

                        FirebaseMessaging.getInstance().subscribeToTopic(languageDataList.get(which).getLanguage_code());
                        sessionManager.setSelectedLanguage(
                                selectedLanguageData.getId_language(),
                                selectedLanguageData.getLanguage_title(),
                                selectedLanguageData.getLanguage_code());

                        getDashBoard();

                        return true;
                    }
                })
//                        .positiveText("Choose")
                .show();
    }

    void setBroadcastListener(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(AppConfig.KEY_REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.KEY_TOPIC_GLOBAL);


                } else if (intent.getAction().equals(AppConfig.KEY_PUSH_NOTIFICATION)) {
                    // new push notification is received

//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };



        mSettingsBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("receiver", "Got message: " + "hii");

                isSettingsChanged = true;
//                if(postsData == null){
//                    getDashBoard();
//                }else{
//                    parseDashBoardData();
//                }

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"onResume");

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.KEY_REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.KEY_PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        LocalBroadcastManager.getInstance(this).registerReceiver(mSettingsBroadcastReceiver,
                new IntentFilter(AppConfig.KEY_SETTINGS_CHANGED));


        if(isSettingsChanged){

            isSettingsChanged = false;

            if(postsData == null){
                getDashBoard();
            }else{
                parseDashBoardData();
            }
        }



        ad_counter++;

        if(mInterstitialAd.isLoaded() && ad_counter == sessionManager.getInterstitialIterations())
        {

            ad_counter = 0;
            showInterstitial();

            initialiseInterstitialAd();
        }
        if(ad_counter >sessionManager.getInterstitialIterations()){
            ad_counter = sessionManager.getInterstitialIterations()-1;
        }


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }



    void RegisterUser()
    {


        Log.d(TAG,"RegisterUser");

        Map<String, String> params = new HashMap<>();
//        if(CommonUtilities.isPermissionGranted(context, Manifest.permission.READ_CONTACTS))
//        {
//            params.put("fname", CommonUtilities.getUserFnameLname(context));
//            params.put("lname", CommonUtilities.getUserFnameLname(context));
//
//        }
//        if(CommonUtilities.isPermissionGranted(context, Manifest.permission.GET_ACCOUNTS)){
//            params.put("email", CommonUtilities.getGmailAccount(context));
//        }

//        if(CommonUtilities.isPermissionGranted(context, Manifest.permission.READ_PHONE_STATE)){
//
//            String phone =CommonUtilities.getPhoneNumber(context);
//            String device_id = CommonUtilities.getDeviceID(context);
//
//            if(phone != null)
//                params.put("phone", phone);
//
//            if(device_id != null)
//                params.put("device_id", device_id);
//
//
//        }

        params.put("device_id", CommonUtilities.getUniqueAndroidDeviceId(context));

        params.put("firebase_reg_id", sessionManager.getFCMToken());



        CustomRequest jsonObjReq = new CustomRequest(this,this,
                false, Request.Method.POST,
                AppConfig.URL_REGISTER,false,
                params, CommonUtilities.buildGuestHeaders(),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        Log.d(TAG,"RegisterUser got responce");

                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){

                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }else{

//                                sessionManager.setUserJsonInfo(js.getData());

                                JSONObject user = (js.getData().getJSONArray(AppConfig.KEY_USER)
                                        .getJSONObject(0));
                                String key = user.getString(AppConfig.KEY_API_KEY);
                                sessionManager.setAPIKEY(key);
                                sessionManager.setLogin(true);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG,"onActivityResults");

        if (requestCode == 1) {

            if(data != null){

                ((LatestFragment) getSupportFragmentManager().getFragments().get(0))
                        .postDataUpdatedFromDetailedScreen((List<PostItemData>) data.getSerializableExtra(AppConfig.ARG_PARAM_POST_DATA));

                ((LatestFragment) getSupportFragmentManager().getFragments().get(1))
                        .postDataUpdatedFromDetailedScreen((List<PostItemData>) data.getSerializableExtra(AppConfig.ARG_PARAM_POST_DATA));;

            }




        }
    }



    public void hideFab() {
        fab.hide();
    }
    public void showFab() {
        fab.show();
    }


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; th1is adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    @Override
    public void onFragmentInteraction(PostsData postsData) {

    }


//    @Override
//    public void loadPosts(boolean isLatestFragment, int pageNo){
//        toAppendOrRefresh = true;
//        this.isLatestFragment = isLatestFragment;
//        getPosts(activeCategoryId, false, pageNo);
//    }


    public String getActiveCategoryId(){
        return  activeCategoryId;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home , menu);

        menu.findItem(R.id.action_openRight).setIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_left)
                .color(Color.WHITE)
                .sizeDp(20));

        menu.findItem(R.id.action_share).setIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_share)
                .color(Color.WHITE)
                .sizeDp(20));




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_openRight) {
            resultAppended.openDrawer(); /*Opens the Right Drawer*/
            return true;
        }

        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            String text = "Download App";
            sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
            sendIntent.setType("text/plain");

            startActivity(sendIntent);



            Bundle bundle = new Bundle();
            bundle.putString(FIREBASE_KEY_USER_ID, sessionManager.getUserId());
            mFirebaseAnalytics.logEvent(FIREBASE_KEY_APP_SHARED, bundle);


//                countShare.setText(String.valueOf(
//                        (postItemDataList.get(position).getPost_share_count()+1)));

            Log.d(TAG,"share clicked");

            return true;
        }





        return super.onOptionsItemSelected(item);
    }

    public HashMap<String, String> SettingsToHashMapSettings(List<SettingData> settingDataList){
        HashMap<String,String> hashMapSettings = new HashMap<>() ;


        for (SettingData settingData : settingDataList) {
            hashMapSettings.put(settingData.getName(), settingData.getValue());
        }

        return hashMapSettings;
    }


}
