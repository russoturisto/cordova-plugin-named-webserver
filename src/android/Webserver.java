package org.apache.cordova.plugin;

import android.util.Log;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public interface Webserver {


    HashMap<String, Object> getResponses();
    CallbackContext getOnRequestCallbackContext();

    /**
     * Called after plugin construction and fields have been initialized.
     * Prefer to use pluginInitialize instead since there is no value in
     * having parameters on the initialize() function.
     */
    void initialize(CordovaInterface cordova, CordovaWebView webView);

    /**
     * Returns the plugin's service name (what you'd use when calling pluginManger.getPlugin())
     */
    String getServiceName();

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param rawArgs         The exec() arguments in JSON form.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     */
    boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException;

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     */
    boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException;

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments, wrapped with some Cordova helpers.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     */
    boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException;

    /**
     * Called when the system is about to start resuming a previous activity.
     *
     * @param multitasking		Flag indicating if multitasking is turned on for app
     */
    void onPause(boolean multitasking);

    /**
     * Called when the activity will start interacting with the user.
     *
     * @param multitasking		Flag indicating if multitasking is turned on for app
     */
    void onResume(boolean multitasking);

    /**
     * Called when the activity is becoming visible to the user.
     */
    void onStart();

    /**
     * Called when the activity is no longer visible to the user.
     */
    void onStop();

    /**
     * Called when the activity receives a new intent.
     */
    void onNewIntent(Intent intent);

    /**
     * The final call you receive before your activity is destroyed.
     */
    void onDestroy();

    /**
     * Called when the Activity is being destroyed (e.g. if a plugin calls out to an external
     * Activity and the OS kills the CordovaActivity in the background). The plugin should save its
     * state in this method only if it is awaiting the result of an external Activity and needs
     * to preserve some information so as to handle that result; onRestoreStateForActivityResult()
     * will only be called if the plugin is the recipient of an Activity result
     *
     * @return  Bundle containing the state of the plugin or null if state does not need to be saved
     */
    Bundle onSaveInstanceState();

    /**
     * Called when a plugin is the recipient of an Activity result after the CordovaActivity has
     * been destroyed. The Bundle will be the same as the one the plugin returned in
     * onSaveInstanceState()
     *
     * @param state             Bundle containing the state of the plugin
     * @param callbackContext   Replacement Context to return the plugin result to
     */
    void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext);

    /**
     * Called when a message is sent to plugin.
     *
     * @param id            The message id
     * @param data          The message data
     * @return              Object to stop propagation or null
     */
    Object onMessage(String id, Object data);

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode   The request code originally supplied to startActivityForResult(),
     *                      allowing you to identify who this result came from.
     * @param resultCode    The integer result code returned by the child activity through its setResult().
     * @param intent        An Intent, which can return result data to the caller (various data can be
     *                      attached to Intent "extras").
     */
    void onActivityResult(int requestCode, int resultCode, Intent intent);

    /**
     * Hook for blocking the loading of external resources.
     *
     * This will be called when the WebView's shouldInterceptRequest wants to
     * know whether to open a connection to an external resource. Return false
     * to block the request: if any plugin returns false, Cordova will block
     * the request. If all plugins return null, the default policy will be
     * enforced. If at least one plugin returns true, and no plugins return
     * false, then the request will proceed.
     *
     * Note that this only affects resource requests which are routed through
     * WebViewClient.shouldInterceptRequest, such as XMLHttpRequest requests and
     * img tag loads. WebSockets and media requests (such as <video> and <audio>
     * tags) are not affected by this method. Use CSP headers to control access
     * to such resources.
     */
    Boolean shouldAllowRequest(String url);

    /**
     * Hook for blocking navigation by the Cordova WebView. This applies both to top-level and
     * iframe navigations.
     *
     * This will be called when the WebView's needs to know whether to navigate
     * to a new page. Return false to block the navigation: if any plugin
     * returns false, Cordova will block the navigation. If all plugins return
     * null, the default policy will be enforced. It at least one plugin returns
     * true, and no plugins return false, then the navigation will proceed.
     */
    Boolean shouldAllowNavigation(String url);

    /**
     * Hook for allowing page to call exec(). By default, this returns the result of
     * shouldAllowNavigation(). It's generally unsafe to allow untrusted content to be loaded
     * into a CordovaWebView, even within an iframe, so it's best not to touch this.
     */
    Boolean shouldAllowBridgeAccess(String url);

    /**
     * Hook for blocking the launching of Intents by the Cordova application.
     *
     * This will be called when the WebView will not navigate to a page, but
     * could launch an intent to handle the URL. Return false to block this: if
     * any plugin returns false, Cordova will block the navigation. If all
     * plugins return null, the default policy will be enforced. If at least one
     * plugin returns true, and no plugins return false, then the URL will be
     * opened.
     */
    Boolean shouldOpenExternalUrl(String url);

    /**
     * Allows plugins to handle a link being clicked. Return true here to cancel the navigation.
     *
     * @param url           The URL that is trying to be loaded in the Cordova webview.
     * @return              Return true to prevent the URL from loading. Default is false.
     */
    boolean onOverrideUrlLoading(String url);

    /**
     * Hook for redirecting requests. Applies to WebView requests as well as requests made by plugins.
     * To handle the request directly, return a URI in the form:
     *
     *    cdvplugin://pluginId/...
     *
     * And implement handleOpenForRead().
     * To make this easier, use the toPluginUri() and fromPluginUri() helpers:
     *
     *     public Uri remapUri(Uri uri) { return toPluginUri(uri); }
     *
     *     public CordovaResourceApi.OpenForReadResult handleOpenForRead(Uri uri) throws IOException {
     *         Uri origUri = fromPluginUri(uri);
     *         ...
     *     }
     */
    Uri remapUri(Uri uri);

    /**
     * Called to handle CordovaResourceApi.openForRead() calls for a cdvplugin://pluginId/ URL.
     * Should never return null.
     * Added in cordova-android@4.0.0
     */
    CordovaResourceApi.OpenForReadResult handleOpenForRead(Uri uri) throws IOException;

}
