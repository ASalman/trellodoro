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


public class OAuthFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    private RelativeLayout layout;
    private WebView webView;
    private ProgressDialog pd;
    private String authorizationToken;

    private String redirectUri="https://api.souq.com/oauth/authorize/";
    private String scopes="customer_profile,cart_management,customer_demographics,customer_profile,cart_management,customer_demographics";



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
        position = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_oauth,
                container, false);
        layout = (RelativeLayout) rootView
                .findViewById(R.id.fragment_wizard_oauth);

        webView = (WebView) rootView.findViewById(R.id.webView);



        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }




    void OAuth(View rootView){
        //connection=new SouqAPIConnection(MyApplication.CLIENT_ID,MyApplication.API_KEY_SOUQ,this);

        webView.requestFocus(View.FOCUS_DOWN);
        webView.getSettings().setJavaScriptEnabled(true);
        pd = ProgressDialog.show(this.getContext(), "", "Loading...",true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (url.startsWith("http://178.62.240.44/returnurl")) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(url);

                    String path = uri.toString();
                    Log.i("token",path);
                    authorizationToken = path.substring(path.indexOf("token=")+6);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);




                } else {
                    Log.i("Authorize", "Redirecting to: " + url);
                    webView.loadUrl(url);
                }
                return true;
            }
        });

        String authUrl = "https://trello.com/1/authorize?callback_method=fragment&return_url=http://178.62.240.44/returnurl&scope=read%2Cwrite&expiration=never&name=PomodoroTrelloApp&key=f96a5b377b72a9818ecd86f6a5bfda25";
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OAuth(view);
    }
}