package travel.iknow.android;

import android.app.Application;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import travel.iknow.android.data.DataSource;
import travel.iknow.android.rest.ApiHelper;

/**
 * Created by Pristalov Pavel on 13.02.2015 for IKnowTravel.
 */
public class IKnowTravelApplication extends Application
{
    public static RestAdapter restAdapter = null;
    public static ApiHelper apiHelper = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(DataSource.API_DOMAIN_ADDRESS)
                .setRequestInterceptor(ApiHelper.requestInterceptor)
                .build();

        apiHelper = restAdapter.create(ApiHelper.class);
    }

    public void updateHeaders(final String newToken)
    {
        RequestInterceptor requestInterceptor = new RequestInterceptor()
        {
            @Override
            public void intercept(RequestFacade request)
            {
                request.addHeader("X-Api-Version", ApiHelper.API_VERSION);
                request.addHeader("Accept", "application/json");
                request.addHeader("X-User-Agent", "cherry-moscow/1");
                request.addHeader("Authorization",
                        new StringBuilder(DataSource.TOKEN_PREFIX)
                                .append("\"")
                                .append(newToken)
                                .append("\"")
                                .toString());
            }
        };

        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(DataSource.API_DOMAIN_ADDRESS)
                .setRequestInterceptor(requestInterceptor)
                .build();

        apiHelper = restAdapter.create(ApiHelper.class);
    }
}
