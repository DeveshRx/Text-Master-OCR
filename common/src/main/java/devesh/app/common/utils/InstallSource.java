package devesh.app.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class InstallSource {
    Context mContext;
    String TAG = "InstallSource";

    public final String GOOGLE_PLAY_STORE="com.android.vending";
    public final String AMAZON_STORE="com.amazon.venezia";
    public final String SAMSUNG_APP_STORE="com.sec.android.app.samsungapps";
    public final String CHROME="com.android.chrome";
    public final String FIREFOX="org.mozilla.firefox";
    public final String PACKAGE_INSTALLER="com.google.android.packageinstaller";
    public final String WHATSAPP="com.whatsapp";


    public InstallSource(Context context) {
        mContext = context;
    }

    public String getInstallSource(){
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

    public void getInstallSourceX() {
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
}
