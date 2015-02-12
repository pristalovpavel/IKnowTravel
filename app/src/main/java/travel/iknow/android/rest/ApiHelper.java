package travel.iknow.android.rest;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Pristalov Pavel on 12.02.2015 for IKnowTravel.
 */
public interface ApiHelper
{
    public static final String API_VERSION = "4";

    @POST("/auth/local")
    void registration(@Body Local local, Callback<Object> cb);

    RequestInterceptor requestInterceptor = new RequestInterceptor()
    {
        @Override
        public void intercept(RequestFacade request)
        {
            request.addHeader("X-Api-Version", API_VERSION);
            request.addHeader("Accept", "application/json");
            request.addHeader("X-User-Agent", "cherry-moscow/1");
        }
    };
}
