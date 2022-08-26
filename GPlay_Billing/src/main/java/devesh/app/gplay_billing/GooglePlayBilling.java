package devesh.app.gplay_billing;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

public class GooglePlayBilling {

    final static String TAG = "GPlay_Lib";
    public boolean isConnected = false;
    public List<String> SubscriptionsList = new ArrayList<>();
    public List<SkuDetails> SKUSubscriptionsList = new ArrayList<>();

    public List<String> ProductsList = new ArrayList<>();
    public List<Purchase> MyPurchases = new ArrayList<>();

    Context mContext;


    private BillingClient billingClient;

    public GooglePlayBilling(Context context) {
        mContext = context;
        SubscriptionsList = new ArrayList<>();
        ProductsList = new ArrayList<>();
        MyPurchases = new ArrayList<>();


    }

    public void Connect(BillingClientStateListener BCSL, PurchasesUpdatedListener purchasesUpdatedListener) {

        billingClient = BillingClient.newBuilder(mContext)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();


        billingClient.startConnection(BCSL);

        /** DEMO EXAMPLE
         * BillingClientStateListener:

         new BillingClientStateListener() {
        @Override public void onBillingSetupFinished(BillingResult billingResult) {
        Log.d(TAG, "onBillingSetupFinished: ");
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
        // The BillingClient is ready. You can query purchases here.
        Log.d(TAG, "onBillingSetupFinished: READY");
        isConnected=true;
        }
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) {
        // The BillingClient is ready. You can query purchases here.
        Log.d(TAG, "onBillingSetupFinished: ERROR");
        isConnected=false;

        }

        }

        @Override public void onBillingServiceDisconnected() {
        // Try to restart the connection on the next request to
        // Google Play by calling the startConnection() method.
        Log.d(TAG, "onBillingServiceDisconnected: ");
        }
        }
         **/

        /** EXAMPLE DEMO
         * PurchasesUpdatedListener:

         PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        // To be implemented in a later section.
        Log.d(TAG, "onPurchasesUpdated: ");



        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
        && purchases != null) {
        if (!purchases.isEmpty()) {
        MyPurchases = purchases;
        }
        //for (Purchase purchase : purchases) { }

        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
        // Handle an error caused by a user cancelling the purchase flow.
        } else {
        // Handle any other error codes.
        }
        }

        }
         */


    }


    public void getSubscriptionList(SkuDetailsResponseListener SDRL) {
        List<String> skuList = SubscriptionsList;

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(), SDRL);


        /** DEMO EXAMPLE

         new SkuDetailsResponseListener() {
        @Override public void onSkuDetailsResponse(BillingResult billingResult,
        List<SkuDetails> skuDetailsList) {
        // Process the result.

        if(!skuDetailsList.isEmpty()){
        for (SkuDetails sku:skuDetailsList
        ) {
        Log.d(TAG, "onSkuDetailsResponse: "+sku.getTitle());
        }
        }else{
        Log.d(TAG, "onSkuDetailsResponse: EMPTY SKU");
        }

        }
        }

         */

    }

    public void getProductList(SkuDetailsResponseListener SDRL) {
        List<String> skuList = ProductsList;

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(), SDRL);


        /** DEMO EXAMPLE

         new SkuDetailsResponseListener() {
        @Override public void onSkuDetailsResponse(BillingResult billingResult,
        List<SkuDetails> skuDetailsList) {
        // Process the result.

        if(!skuDetailsList.isEmpty()){
        for (SkuDetails sku:skuDetailsList
        ) {
        Log.d(TAG, "onSkuDetailsResponse: "+sku.getTitle());
        }
        }else{
        Log.d(TAG, "onSkuDetailsResponse: EMPTY SKU");
        }

        }
        }

         */

    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(mContext);
        return resultCode == ConnectionResult.SUCCESS;
    }

    public int Purchase(SkuDetails skuDetails, Activity activity) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        int responseCode = billingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
        Log.d(TAG, "Purchase: responseCode: " + responseCode);

        return responseCode;
    }

    public void handlePurchase(Purchase purchase, ConsumeResponseListener listener) {
        // Purchase retrieved from BillingClient#queryPurchasesAsync or your PurchasesUpdatedListener.

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        /** DEMO EXAMPLE
         *
         ConsumeResponseListener listener = new ConsumeResponseListener() {
        @Override public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
        // Handle the success of the consume operation.
        }
        }
        };
         */
        billingClient.consumeAsync(consumeParams, listener);
    }


    /** DEMO EXAMPLE*/
    /**
     * PurchasesResponseListener purchasesResponseListener=new PurchasesResponseListener() {
     *
     * @Override public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
     *
     * }
     * };
     */
    public void FetchSubsPurchases(PurchasesResponseListener purchasesResponseListener) {
        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, purchasesResponseListener);
    }

    public void FetchProductPurchases(PurchasesResponseListener purchasesResponseListener) {

        billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, purchasesResponseListener);
    }

//=====

    /**
     PurchaseHistoryResponseListener purchaseHistoryResponseListener=new PurchaseHistoryResponseListener() {
    @Override
    public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {

    }
    };
     */
    public void FetchSubsPurchaseHistory(PurchaseHistoryResponseListener purchaseHistoryResponseListener){
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS,purchaseHistoryResponseListener);
    }
    public void FetchProductPurchaseHistory(PurchaseHistoryResponseListener purchaseHistoryResponseListener){
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,purchaseHistoryResponseListener);
    }

}



   /* private final PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
            Log.d(TAG, "onPurchasesUpdated: ");



            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                if (!purchases.isEmpty()) {
                    MyPurchases = purchases;
                }
                //for (Purchase purchase : purchases) { }

            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }

    };
   */

