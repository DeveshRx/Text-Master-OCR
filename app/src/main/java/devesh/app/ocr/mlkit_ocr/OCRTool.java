package devesh.app.ocr.mlkit_ocr;

import static android.content.Context.CAMERA_SERVICE;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class OCRTool {
    TextRecognizer recognizer;
public static final int LANGUAGE_DEFAULT=0;
    public static final int LANGUAGE_Devanagari=1;
    public static final int LANGUAGE_Japanese =2;
    public static final int LANGUAGE_Korean =3;
    public static final int LANGUAGE_Chinese =4;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }
    public OCRTool(int language){
        switch (language){
            case LANGUAGE_DEFAULT:
                recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                break;
            case LANGUAGE_Devanagari:

// When using Devanagari script library
                recognizer =
                        TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());

                break;
            case LANGUAGE_Japanese:

// When using Japanese script library
                recognizer =
                        TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());

                break;
            case LANGUAGE_Korean:

// When using Korean script library
                recognizer =
                        TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

                break;
            case LANGUAGE_Chinese:
                // When using Chinese script library
                recognizer =
                        TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());

                break;
            default:
                recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                break;
        }


    }
    public void ProcessImage(InputImage image, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        recognizer.process(image)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);


    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getRotationCompensation(String cameraId, Activity activity, boolean isFrontFacing)
            throws CameraAccessException {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // Get the device's sensor orientation.
        CameraManager cameraManager = (CameraManager) activity.getSystemService(CAMERA_SERVICE);
        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360;
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360;
        }
        return rotationCompensation;
    }
}
