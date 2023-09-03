package devesh.app.ocr.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import devesh.app.common.AdMobAPI;
import devesh.app.common.utils.CachePref;
import devesh.app.mlkit_ocr.OCRTool;
import devesh.app.ocr.MainActivity;
import devesh.app.ocr.R;
import devesh.app.ocr.databinding.FragmentCameraBinding;


public class CameraFragment extends Fragment {
    final float Overlay_Rotation_Portrate = 360;
    final float Overlay_Rotation_Landscape = 90;
    final String[] LanguageOptionsFull = {"Default (English)", "Devanagari देवनागरी", "Japanese 日本", "Korean 한국인", "Chinese 中國人"};
    final String[] LanguageOptions = {"English", "Devanagari", "Japanese", "Korean", "Chinese"};
    FragmentCameraBinding mBinding;
    String TAG = "CameraFragment";
    //    OCRTool ocrTool;
    Preview preview;
    boolean isLandscape;
    ProcessCameraProvider cameraProvider;
    AdMobAPI adMobAPI;
    int DefaultLanguageMode = 0;
    Bitmap previewBitmap = null;
    ImageCapture imageCapture;
    ExecutorService cameraExecutor = Executors.newFixedThreadPool(2);
    Camera camera;
    CachePref cachePref;
    boolean isSubscribed;
boolean isFlashAvailable;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCameraBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            adMobAPI.DestroyAds();

        }catch (Exception e){

        }
        mBinding = null;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //      mParam1 = getArguments().getString(ARG_PARAM1);
            //    mParam2 = getArguments().getString(ARG_PARAM2);
        }
        isLandscape = false;
        isFlashAvailable=true;
        cachePref = new CachePref(getActivity());

        adMobAPI = new AdMobAPI(getActivity());
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        //   ocrTool=new OCRTool(getActivity());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
                Log.e(TAG, "onStart: ", e);
            }
        }, ContextCompat.getMainExecutor(getActivity()));

    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.imageCaptureButton.setOnClickListener(view -> {
            onClick();
        });




        mBinding.FlashButton.setOnClickListener(view -> {
            ToggleFlash();

        });

        mBinding.SettingsButton.setOnClickListener(view -> {
            ((MainActivity) getActivity()).OpenSettings();
        });
        mBinding.HistoryButton.setOnClickListener(view -> {
            ((MainActivity) getActivity()).OpenHistory();
        });
        mBinding.GalleryButton.setOnClickListener(view -> {
            ((MainActivity) getActivity()).openGallery();

        });

   /*     mBinding.PicModeButton.setOnClickListener(view -> {
            if(isLandscape){
                mBinding.TxOverlay.setRotation(Overlay_Rotation_Portrate);
           //     mBinding.PicModeButton.setText("Portrate");
                isLandscape=false;
                imageCapture.setTargetRotation(Surface.ROTATION_270);

            }else{
                mBinding.TxOverlay.setRotation(Overlay_Rotation_Landscape);
             //   mBinding.PicModeButton.setText("Landscape");
                isLandscape=true;
                imageCapture.setTargetRotation(Surface.ROTATION_180);
            }

        });
*/

        mBinding.ModeLanguageButton.setOnClickListener(view -> {


            MaterialAlertDialogBuilder materialAlertDialogBuilder =
                    new MaterialAlertDialogBuilder(getActivity())
                            // Add customization options here
                            .setTitle("Choose Language")
                            .setSingleChoiceItems(LanguageOptionsFull, DefaultLanguageMode, (dialogInterface, i) -> {
                                Log.d(TAG, "onStart: Selected: " + i);
                                mBinding.ModeLanguageButton.setText(LanguageOptions[i]);
                                DefaultLanguageMode = i;
                                switch (i) {
                                    case 0:
                                        ((MainActivity) getActivity()).openChooseLanguageDialogue(OCRTool.LANGUAGE_DEFAULT);
                                        break;
                                    case 1:
                                        ((MainActivity) getActivity()).openChooseLanguageDialogue(OCRTool.LANGUAGE_Devanagari);

                                        break;
                                    case 2:
                                        ((MainActivity) getActivity()).openChooseLanguageDialogue(OCRTool.LANGUAGE_Japanese);

                                        break;
                                    case 3:
                                        ((MainActivity) getActivity()).openChooseLanguageDialogue(OCRTool.LANGUAGE_Korean);

                                    case 4:
                                        ((MainActivity) getActivity()).openChooseLanguageDialogue(OCRTool.LANGUAGE_Chinese);

                                        break;
                                }
                                cachePref.setString("ocrlang",String.valueOf(DefaultLanguageMode));
                                dialogInterface.dismiss();
                            })
                            .setIcon(R.drawable.ic_baseline_translate_24)
                            .setNegativeButton("close", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            });

            materialAlertDialogBuilder.show();

        });
String d=cachePref.getString("ocrlang");
if(d!=null){
int i=Integer.parseInt(d);
    DefaultLanguageMode=i;
    mBinding.ModeLanguageButton.setText(LanguageOptions[i]);
}
//mBinding.imagePreview.setImageBitmap(null);

        setFlashButton();
    }

    @Override
    public void onResume() {
        super.onResume();

        adMobAPI.setAdaptiveBanner(mBinding.AdFrame, getActivity());


    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            adMobAPI.DestroyAds();

            mBinding.AdFrame.removeAllViewsInLayout();

        }catch (Exception e){

        }
    }

    void ToggleFlash() {
        if (camera.getCameraInfo().getTorchState().getValue().equals(TorchState.ON)) {
            camera.getCameraControl().enableTorch(false);
        } else {
            camera.getCameraControl().enableTorch(true);
        }
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        preview = new Preview.Builder()

                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            imageCapture =
                   new ImageCapture.Builder()
                           //.setTargetRotation(getActivity().getDisplay().getRotation())
                           .setTargetRotation(Surface.ROTATION_0)
                           .build();
        }else{
            imageCapture =
                    new ImageCapture.Builder()
                            //.setTargetRotation(getActivity().getDisplay().getRotation())
                            .build();
        }
*/


/*
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        // enable the following line if RGBA output is needed.
                        //.setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setTargetRotation(Surface.ROTATION_90)
                        .build();*/


     /*  imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
//                int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                // insert your code here.
            //    analyzeIMG(imageProxy);


                // after done, release the ImageProxy object
                @SuppressLint("UnsafeOptInUsageError")
                Image image=imageProxy.getImage();
                if(image!=null){

                    previewBitmap=toBitmap(image);
                }
                imageProxy.close();
            }
        });
*/
        imageCapture =
                new ImageCapture.Builder()
                        //  .setTargetRotation(Surface.ROTATION_90)
                        //   .setTargetResolution(new Size(1920, 1080))
                        .setJpegQuality(100)

                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)

                        .build();


        try {
            preview.setSurfaceProvider(mBinding.viewFinder.getSurfaceProvider());

            //  camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,imageAnalysis,imageCapture, preview);
            camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageCapture, preview);

            isFlashAvailable=camera.getCameraInfo().hasFlashUnit();
            setFlashButton();
        } catch (Exception e) {
            Log.e(TAG, "bindPreview: " + e);
        }




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraProviderFuture.cancel(true);
        try {
            adMobAPI.DestroyAds();

        }catch (Exception e){

        }
    }

    public void onClick() {
        //   Toast.makeText(getActivity(), "Capturing Image...", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).ShowLoader(true);
/*if(previewBitmap!=null){

    mBinding.imagePreview.setImageBitmap(previewBitmap);
}*/
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(new File(getActivity().getFilesDir(), "img_cache.png")).build();
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        // insert your code here.
                        Log.d(TAG, "onImageSaved: ");

                        ((MainActivity) getActivity()).OpenResult();
                        //         Intent view = new Intent(getActivity(), OCRReadActivity.class);
//view.putExtra("source","camera");
                        //  startActivity(view);
                    }

                    @Override
                    public void onError(ImageCaptureException error) {
                        // insert your code here.
                        Log.e(TAG, "onError: " + error);
                    }
                }
        );


    }

    private Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    void setFlashButton(){
        if(isFlashAvailable){
            mBinding.FlashButton.setVisibility(View.VISIBLE);
        }else{
            mBinding.FlashButton.setVisibility(View.GONE);
        }
    }
/*
    void analyzeIMG(ImageProxy imageProxy) {
        @SuppressLint("UnsafeOptInUsageError")
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            // Pass image to an ML Kit Vision API


            ocrTool.ProcessImage(image,new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text visionText) {
                    // Task completed successfully

                    Log.d(TAG, "onSuccess: OCR:\n"+visionText.getText());
            //        ((MainActivity) getActivity()).openResult(visionText.getText());

                    imageProxy.close();
                }
            },new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Task failed with an exception

                    Log.e(TAG, "onFailure: OCR: ", e);
                    imageProxy.close();

                }
            });




        }
    }
*/

}