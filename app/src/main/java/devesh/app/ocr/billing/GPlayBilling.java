package devesh.app.ocr.billing;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

public class GPlayBilling  {
    public BillingClient billingClient;
    public List<ProductDetails> mProductDetailsList;
    String TAG = "PlayBilling";
    ProductDetails productDetails;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    private final PurchasesUpdatedListener purchasesUpdatedListenerX = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult,
                                       List<Purchase> purchases) {
            // To be implemented in a later section.
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }
    };
    BillingClientStateListener billingClientStateListenerX=new BillingClientStateListener() {
        @Override
        public void onBillingSetupFinished(BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                // The BillingClient is ready. You can query purchases here.
                Log.d(TAG, "onBillingSetupFinished: ");
               // getProducts();
            }
        }

        @Override
        public void onBillingServiceDisconnected() {
            // Try to restart the connection on the next request to
            // Google Play by calling the startConnection() method.
            Log.d(TAG, "onBillingServiceDisconnected: ");
        }
    };
    ProductDetailsResponseListener productDetailsResponseListenerX=new ProductDetailsResponseListener() {
        @Override
        public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {

        }
    };
    ConsumeResponseListener consumeResponseListenerX=new ConsumeResponseListener() {
        @Override
        public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {

        }
    };
    PurchasesResponseListener purchasesResponseListenerX=new PurchasesResponseListener() {
        @Override
        public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {

        }
    };


  Activity mActivity=null;
    public GPlayBilling(Activity activity, PurchasesUpdatedListener purchasesUpdatedListener){
        mActivity=activity;
        billingClient = BillingClient.newBuilder(mActivity)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

    }


    public void init(BillingClientStateListener billingClientStateListener){
        Log.d(TAG, "init: BillingClientStateListener");
        billingClient.startConnection(billingClientStateListener);
/*

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d(TAG, "onBillingSetupFinished: ");
                    getProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d(TAG, "onBillingServiceDisconnected: ");
            }
        });

*/

    }

    public void getProducts(ProductDetailsResponseListener productDetailsResponseListener) {
        Log.d(TAG, "getProducts: ");
        List<QueryProductDetailsParams.Product> itemsList = new ArrayList<>();


        QueryProductDetailsParams.Product qp = QueryProductDetailsParams.Product.newBuilder()
                .setProductId("subs")
                .setProductType(BillingClient.ProductType.SUBS)
                .build();

        itemsList.add(qp);

        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(itemsList)
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                productDetailsResponseListener
        );

     /*   billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                (billingResult, productDetailsList) -> {
                    // check billingResult
                    // process returned productDetailsList
                    Log.d(TAG, "onProductDetailsResponse: ");
                    mProductDetailsList = productDetailsList;
                    for (ProductDetails p : mProductDetailsList) {
                        Log.d(TAG, "onProductDetailsResponse: " + p.toString() + "\n" + p.getProductId() + "\n" + p.getName() + "\n" + p.getTitle());
                    }


                }
        );*/
    }

    void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchasesAsync or your PurchasesUpdatedListener.
        //Purchase purchase = ...;

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                }
            }
        };

        billingClient.consumeAsync(consumeParams, listener);

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        }
    }

    public void handlePurchase(Purchase purchase, ConsumeResponseListener consumeResponseListener) {
        // Purchase retrieved from BillingClient#queryPurchasesAsync or your PurchasesUpdatedListener.
        //Purchase purchase = ...;

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = consumeResponseListener;

        billingClient.consumeAsync(consumeParams, listener);

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        }
    }

    public BillingResult StatPay(String token) {

        List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = new ArrayList<>();

        BillingFlowParams.ProductDetailsParams bp =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                        // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                        .setProductDetails(productDetails)
                        // to get an offer token, call ProductDetails.getSubscriptionOfferDetails()
                        // for a list of offers that are available to the user
                        .setOfferToken(token)
                        .build();
        productDetailsParamsList.add(bp);

    /*    ImmutableList productDetailsParamsList =
                ImmutableList.from(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(productDetails)
                                // to get an offer token, call ProductDetails.getSubscriptionOfferDetails()
                                // for a list of offers that are available to the user
                                .setOfferToken("selectedOfferToken")
                                .build()
                );*/

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

// Launch the billing flow
        BillingResult billingResult = billingClient.launchBillingFlow(mActivity, billingFlowParams);
return billingResult;
    }



    public void fetchPayments(PurchasesResponseListener purchasesResponseListener) {
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                purchasesResponseListener
        );

       /* new PurchasesResponseListener() {
            public void onQueryPurchasesResponse(BillingResult billingResult, List purchases) {
                // check billingResult
                // process returned purchase list, e.g. display the plans user owns

            }
        }*/

    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(mActivity);
        return resultCode == ConnectionResult.SUCCESS;
    }


}
