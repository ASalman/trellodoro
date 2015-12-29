package com.asalman.trellodoro.restapi;


import com.asalman.trellodoro.BuildConfig;
import com.asalman.trellodoro.restapi.service.BoardServices;
import com.asalman.trellodoro.restapi.service.ListServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by asalman on 12/30/15.
 * This class will provide us with multiple static function to get services calls
 */
public class RestClient
{
    private static final String ENTRY_POINT = " https://trello.com/1";
    private static final String APP_KEY_KEY = "key";
    private static final String USER_TOKEN_KEY = "token";
    private final static String APP_KEY = "f96a5b377b72a9818ecd86f6a5bfda25";
    private final static String USER_TOKEN = "368c59f10c5e0b47d6fd70d61b43b036309037bb2ad9d7b7e02e4b6fe18609fa";
    private final static String GOON_DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static BoardServices boardService;
    private static ListServices listService;
    private static RestAdapter restAdapter;

    private RestClient() { }

    /**
     * This function will return rest adapter needed to build services
     *
     * @return RestAdapter
     */
    public static RestAdapter getRestAdapter(){
        if (restAdapter == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat(RestClient.GOON_DEFAULT_DATETIME_FORMAT)
                    .create();

            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addQueryParam(RestClient.APP_KEY_KEY, RestClient.APP_KEY);
                    request.addQueryParam(RestClient.USER_TOKEN_KEY, RestClient.USER_TOKEN);
                }

            };

            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                    .setEndpoint(ENTRY_POINT)
                    .setConverter(new GsonConverter(gson))
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        }

        return restAdapter;
    }

    /**
     * Build BoardServices object which allow user to call all board services
     * @return BoardServices
     */
    public static BoardServices getBoardServices()
    {
        if (boardService == null){
            boardService = getRestAdapter().create(BoardServices.class);
        }
        return boardService;
    }

    /**
     * Build ListServices object which allow user to call all list services
     * @return ListServices
     */
    public static ListServices getListServices()
    {
        if (listService == null){
            listService = getRestAdapter().create(ListServices.class);
        }
        return listService;
    }
}
