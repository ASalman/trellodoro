package com.asalman.trellodoro.ui.fragments;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.WizardPageFinishedEvent;
import com.squareup.otto.Bus;


public class OAuthFragment extends Fragment {

    private static final String ARG_POSITION = "Position";

    private int mPosition;
    private RelativeLayout mLayout;
    private WebView mWebView;
    private ProgressDialog mProgressDialog;
    private String mAuthorizationToken;
    private Bus mBus = BusProvider.getInstance();



    public static OAuthFragment newInstance(int position) {
        OAuthFragment f = new OAuthFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_oauth,
                container, false);
        mLayout = (RelativeLayout) rootView
                .findViewById(R.id.fragment_wizard_oauth);

        mWebView = (WebView) rootView.findViewById(R.id.webView);


        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }



    void OAuth(View rootView){
        //connection=new SouqAPIConnection(MyApplication.CLIENT_ID,MyApplication.API_KEY_SOUQ,this);

        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mProgressDialog = ProgressDialog.show(this.getContext(), "", "Loading...",true);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (url.startsWith("http://178.62.240.44/returnurl")) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(url);

                    String path = uri.toString();
                    Log.i("token",path);
                    mAuthorizationToken = path.substring(path.indexOf("token=")+6);
                    if (mAuthorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + mAuthorizationToken);
                    MyApplication.getAccessToken().setValue(mAuthorizationToken);
                    mBus.post(new WizardPageFinishedEvent(mPosition, OAuthFragment.this));

                } else {
                    Log.i("Authorize", "Redirecting to: " + url);
                    mWebView.loadUrl(url);
                }
                return true;
            }
        });

        String authUrl = "https://trello.com/1/authorize?callback_method=fragment&return_url=http://178.62.240.44/returnurl&scope=read%2Cwrite&expiration=never&name=PomodoroTrelloApp&key=f96a5b377b72a9818ecd86f6a5bfda25";
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        mWebView.loadUrl(authUrl);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OAuth(view);
    }
}