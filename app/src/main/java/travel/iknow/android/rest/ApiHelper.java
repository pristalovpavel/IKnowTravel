package travel.iknow.android.rest;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import travel.iknow.android.data.DataSource;

/**
 * Created by Pristalov Pavel on 12.02.2015 for IKnowTravel.
 */
public interface ApiHelper
{
    public static final String API_VERSION = "4";

    @POST("/auth/local")
    void registration(@Body Local local, Callback<Object> cb);

    @POST("/auth/local")
    void auth(@Body Local local, Callback<Object> cb);

    @POST("/auth_token")
    void requestToken(Callback<Token> cb);

    @GET("/contents?region=Russia_Moscow")
    void loadContent(Callback<List<Content>> cb);

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
