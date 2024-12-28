package devesh.app.ocr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;

import devesh.app.ocr.billing.BillingActivity;
import devesh.app.ocr.billing.GPlayBilling;
import devesh.app.ocr.camera.CameraFragment;
import devesh.app.ocr.database.DatabaseTool;
import devesh.app.ocr.databinding.ActivityMainBinding;
import devesh.app.ocr.mlkit_ocr.OCRTool;
import devesh.app.ocr.utils.CachePref;


public class MainActivity extends BaseActivity {

    String TAG = "APP: ";
    FragmentManager fragmentManager;
    Fragment fragmentScreen;
    Fragment oldFrag;
    OCRTool ocrTool;
    DatabaseTool databaseTool;
    AppBarConfiguration appBarConfiguration;
    ActivityMainBinding binding;
    GPlayBilling gPlayBilling;
    CachePref cachePref;
    boolean isSubscribed;
    boolean isBuyProBanner;
    ActivityResultLauncher<Intent> openGalleryApp = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Bundle extras = result.getData().getExtras();
                    Log.d(TAG, "onActivityResult: " + result);
                    //Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //   imageView1.setImageBitmap(imageBitmap);
                    //     Bundle extras = result.getData().getExtras();
                    //Bundle extras = result.getData().getExtras();
                    if (result.getData() != null) {
                        Intent i = result.getData();
                        Uri uri = i.getData();
                        Log.d(TAG, "onActivityResult: " + i.getData());
                        Bitmap bitmap = null;
                        ShowLoader(false);

                        CropImage.activity(uri)
                                .setAutoZoomEnabled(true)
                                .setMultiTouchEnabled(true)
                                .start(MainActivity.this);

                       /* try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            AnalyzeImage(bitmap);
                   *//*     Intent act2 = new Intent(MainActivity.this, EditScreenActivity.class);
                        act2.putExtra("uri", uri);
                        act2.putExtra("source", "gallery");

                        startActivity(act2);
*//*
                        } catch (IOException e) {
                            e.printStackTrace();
                            ShowLoader(false);

                        }*/
                    } else {
                        ShowLoader(false);

                    }



/*
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                    }

                    if (result.getResultCode() == REQUEST_IMAGE_CAPTURE && result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        // imageView.setImageBitmap(imageBitmap);
                    }
                    */
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cachePref = new CachePref(this);
        isSubscribed = false;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        isBuyProBanner = false;
        databaseTool = new DatabaseTool(this);
        gPlayBilling = new GPlayBilling(this, (billingResult, list) -> {

        });

        String d = cachePref.getString("ocrlang");
        if (d != null) {
            int i = Integer.parseInt(d);
            int m = OCRTool.LANGUAGE_DEFAULT;
            switch (i) {
                case 0:
                    m = OCRTool.LANGUAGE_DEFAULT;
                    break;
                case 1:
                    m = OCRTool.LANGUAGE_Devanagari;

                    break;
                case 2:
                    m = OCRTool.LANGUAGE_Japanese;

                    break;
                case 3:
                    m = OCRTool.LANGUAGE_Korean;

                case 4:
                    m = OCRTool.LANGUAGE_Chinese;

                    break;
            }

            ocrTool = new OCRTool(m);
        } else {
            ocrTool = new OCRTool(OCRTool.LANGUAGE_DEFAULT);

        }
        ReceiveShareIntent();

        if (savedInstanceState == null) {
            setFragment(new CameraFragment(), null, "camera");
        }

        //  setSupportActionBar(binding.toolbar);

/*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
*/

     /*   binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        RequestPermission();

        Log.d(TAG, "onCreate:Database");
        Log.d(TAG, databaseTool.getAll().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  fragmentScreen.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (fragmentManager != null) {
            if (fragmentManager.findFragmentByTag("camera") != null) {
                if (fragmentManager.findFragmentByTag("camera").isVisible()) {
                    //fragmentScreen = fragmentManager.findFragmentByTag("camera");

                    if (!isSubscribed) {
                        if (!isBuyProBanner) {
                            binding.BuyProBanner.getRoot().setVisibility(View.VISIBLE);
                            isBuyProBanner = true;
                        } else {
                            isBuyProBanner = false;
                            binding.BuyProBanner.getRoot().setVisibility(View.GONE);
                        }


                    } else {
                        super.onBackPressed();
                    }


                }
            }
        }


        //    super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.BuyProBanner.ExitButton.setOnClickListener(view -> {
            MainActivity.this.finish();
        });
        binding.BuyProBanner.LearnMoreButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BillingActivity.class);
            startActivity(intent);
        });
        binding.BuyProBanner.getRoot().setVisibility(View.GONE);
        isBuyProBanner = false;

        gPlayBilling.init(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                gPlayBilling.fetchPayments((billingResult1, list) -> {
                    Log.d(TAG, "onBillingSetupFinished: " + list);
                    if (list.isEmpty()) {
                        isSubscribed = false;
                    }

                    for (Purchase p : list) {
                        int state = p.getPurchaseState();
                        Log.d(TAG, "fetchOwnedPlans: " + p.getOrderId() + "\nState: " + p.getPurchaseState());
                        if (state == Purchase.PurchaseState.PURCHASED) {
                            Log.d(TAG, "fetchOwnedPlans: PURCHASED");
                            isSubscribed = true;
                        } else if (state == Purchase.PurchaseState.PENDING) {
                            Log.d(TAG, "fetchOwnedPlans: PENDING");
                            isSubscribed = true;

                        } else if (state == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                            Log.d(TAG, "fetchOwnedPlans: UNSPECIFIED_STATE");
                            isSubscribed = false;
                        } else {
                            isSubscribed = false;
                            Log.d(TAG, "fetchOwnedPlans: UNSPECIFIED_STATE unknown");
                        }

                        if (!p.isAcknowledged()) {
                            AcknowledgePurchaseParams acknowledgePurchaseParams =
                                    AcknowledgePurchaseParams.newBuilder()
                                            .setPurchaseToken(p.getPurchaseToken())
                                            .build();
                            gPlayBilling.billingClient.acknowledgePurchase(acknowledgePurchaseParams, response -> {
                                Log.d(TAG, "onAcknowledgePurchaseResponse: " + response);
                                if (response.getResponseCode() == RESULT_OK) {
                                    isSubscribed = true;
                                    Log.d(TAG, "onBillingSetupFinished: RESULT OK");
                                }
                            });

                        }

                    }
                    cachePref.setBoolean(getString(R.string.Pref_isSubscribed), isSubscribed);
                    PremiumUserUI();

                });
            }
        });

        PremiumUserUI();


    }

    void ReceiveShareIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {


                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    // Update UI to reflect image being shared
                    //   ShowLoader(true);

                    CropImage.activity(imageUri)
                            .setAutoZoomEnabled(true)
                            .setMultiTouchEnabled(true)
                            .start(MainActivity.this);

                }

            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void OpenSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void OpenHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    /*  public void openResult(String text){
          Log.d(TAG, "openResult: ");
          Bundle b= new Bundle();
          b.putString("text",text);
  *//*        fragmentScreen =new ResultFragment();
        fragmentScreen.setArguments(b);
        fragmentManager.beginTransaction()
                //  .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(binding.fragmentContainerFrame.getId(), fragmentScreen, "result")
                //.setReorderingAllowed(true)
               // .addToBackStack("app")
                .commit();*//*
     *//*
binding.ResultView.getRoot().setVisibility(View.VISIBLE);
binding.ResultView.editTextScanResult.setText(text);
*//*
    }
*/
    public void ShowLoader(boolean show) {
        runOnUiThread(() -> {

            if (show) {
                binding.LoadingView.getRoot().setVisibility(View.VISIBLE);
            } else {
                binding.LoadingView.getRoot().setVisibility(View.GONE);
            }
        });
    }

    public void OpenResult() {
        File file = new File(getFilesDir(), "img_cache.png");
        ShowLoader(false);

        CropImage.activity(Uri.fromFile(file))
                .setAutoZoomEnabled(true)
                .setMultiTouchEnabled(true)
                .start(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //File file=new File(resultUri.toString());
                //   Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    ShowLoader(true);
                    AnalyzeImage(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    ShowLoader(false);

                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ShowLoader(false);

            }
        } else {
            ShowLoader(false);

        }
    }

    public void openGallery() {


        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        openGalleryApp.launch(Intent.createChooser(i, "Select Picture"));


    }

    public void openChooseLanguageDialogue(int i) {
        ocrTool = new OCRTool(i);

    }

    void AnalyzeImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
/*
        InputImage image=null;
        try {

             image = InputImage.fromFilePath(this, Uri.fromFile(file));

        }catch (Exception e){
            Log.e(TAG, "OpenResult: ",e );
        }*/
        ocrTool.ProcessImage(image, new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text visionText) {
                // Task completed successfully

                Log.d(TAG, "onSuccess: OCR:\n" + visionText.getText());
                ShowLoader(false);
                Intent resultActivity = new Intent(MainActivity.this, ResultActivity.class);
                resultActivity.putExtra("text", visionText.getText());
                resultActivity.putExtra("ad2db", "yes");
                startActivity(resultActivity);
                /*
                Bundle b= new Bundle();
                b.putString("text",visionText.getText());*/
                //   setFragment(new ResultFragment(),b,"result");

                String resultText = visionText.getText();
                for (Text.TextBlock block : visionText.getTextBlocks()) {
                    String blockText = block.getText();
                    Point[] blockCornerPoints = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    Log.d(TAG, blockText);
                    for (Text.Line line : block.getLines()) {
                        String lineText = line.getText();
                        Point[] lineCornerPoints = line.getCornerPoints();
                        Rect lineFrame = line.getBoundingBox();
                        Log.d(TAG, lineText);
                        for (Text.Element element : line.getElements()) {
                            String elementText = element.getText();
                            Point[] elementCornerPoints = element.getCornerPoints();
                            Rect elementFrame = element.getBoundingBox();
                            Log.d(TAG, elementText);
                        }
                    }
                }

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Task failed with an exception
                ShowLoader(false);

                Log.e(TAG, "onFailure: OCR: ", e);


            }
        });

    }

    void PremiumUserUI() {
        runOnUiThread(() -> {

            isSubscribed = cachePref.getBoolean(getString(R.string.Pref_isSubscribed));
            if (isSubscribed) {
                binding.PremiumUserLL.setVisibility(View.VISIBLE);
                binding.JoinProLL.setVisibility(View.GONE);

            } else {
                binding.PremiumUserLL.setVisibility(View.GONE);
                binding.JoinProLL.setVisibility(View.VISIBLE);

                binding.JoinProCardView.setOnClickListener(view -> {
                    Intent intent = new Intent(this, BillingActivity.class);
                    startActivity(intent);

                });
            }


        });
    }

    void setFragment(Fragment fragment, Bundle bundle, String tag) {
        if (fragmentScreen != null) {
            oldFrag = fragmentScreen;
        }

        fragmentScreen = fragment;
        if (bundle != null) {

            fragmentScreen.setArguments(bundle);

        }

        if (oldFrag != null) {

            fragmentManager.beginTransaction()
                    .hide(oldFrag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(binding.fragmentContainerFrame.getId(), fragmentScreen, tag)
                    // .setReorderingAllowed(true)
                    // .addToBackStack("app")
                    .commit();

        } else {

            fragmentManager.beginTransaction()
                    //  .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(binding.fragmentContainerFrame.getId(), fragmentScreen, tag)
                    // .setReorderingAllowed(true)
                    // .addToBackStack("app")
                    .commit();

        }


        /*
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mBinding.fragmentContainerFrame.getId(), fragment, null)
                .commit();*/
    }


    void analyzeIMG(ImageProxy imageProxy) {
        @SuppressLint("UnsafeOptInUsageError")
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            // Pass image to an ML Kit Vision API

            ocrTool.ProcessImage(image, new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text visionText) {
                    // Task completed successfully

                    Log.d(TAG, "onSuccess: OCR:\n" + visionText.getText());
                    imageProxy.close();
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Task failed with an exception

                    Log.e(TAG, "onFailure: OCR: ", e);
                    imageProxy.close();

                }
            });


        }
    }


    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}