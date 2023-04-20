package devesh.app.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class InstallSource {

    public final static String GOOGLE_PLAY_STORE="com.android.vending";
    public final static String AMAZON_STORE="com.amazon.venezia";
    public final static String SAMSUNG_APP_STORE="com.sec.android.app.samsungapps";
    public final static String CHROME="com.android.chrome";
    public final static String FIREFOX="org.mozilla.firefox";
    public final static String PACKAGE_INSTALLER="com.google.android.packageinstaller";
    public final static String WHATSAPP="com.whatsapp";


    public static String getInstallSource(Context mContext){
        final String TAG="InstallSource";
        String installerInfo="-";
        PackageManager packageManager = mContext.getPackageManager();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                installerInfo = packageManager.getInstallSourceInfo(mContext.getPackageName()).getInstallingPackageName();
            } else {
                installerInfo = packageManager.getInstallerPackageName(mContext.getPackageName());
            }

        } catch (Exception e) {
            installerInfo = "-";
            e.printStackTrace();
        }

        if(installerInfo==null){
            installerInfo="-";
        }
        Log.d(TAG, "getInstallSource: " + installerInfo);
        return installerInfo;

    }

    public static boolean isGalaxyStore(Context mContext){
        final String TAG="InstallSource";

        boolean a=false;
        String installerInfo=getInstallSource(mContext);

        if(installerInfo.equals(SAMSUNG_APP_STORE)){
            a=true;
        }
        Log.d(TAG, "getInstallSource: " + installerInfo);

        return a;
    }




}



  /*  public void getInstallSourceX() {
        String installerInfo;
        PackageManager packageManager = mContext.getPackageManager();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                installerInfo = packageManager.getInstallSourceInfo(mContext.getPackageName()).getInstallingPackageName();
            } else {
                installerInfo = packageManager.getInstallerPackageName(mContext.getPackageName());
            }

        } catch (Exception e) {
            installerInfo = "--";
            e.printStackTrace();
        }


        Log.d(TAG, "getInstallSource: " + installerInfo);


        if (installerInfo != null) {

            switch (installerInfo) {

                case "com.android.vending":
                    installerInfo = "Google PlayStore";
                    break;

                case "com.amazon.venezia":
                    installerInfo = "Amazon App Store";
                    break;

                case "com.android.chrome":
                    installerInfo = "Google Chrome";
                    break;

                case "com.google.android.packageinstaller":
                    installerInfo = "Package Installer";
                    break;

                case "com.whatsapp":
                    installerInfo = "Whatsapp";
                    break;

                case "org.mozilla.firefox":
                    installerInfo = "Firefox";
                    break;

            }
        } else {
            installerInfo = "--";
        }
    }
*/
