package travel.iknow.android;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import travel.iknow.android.data.DataSource;
import travel.iknow.android.data.model.AddressCover;
import travel.iknow.android.data.model.ArticleCover;
import travel.iknow.android.data.model.Category;
import travel.iknow.android.data.model.Content;
import travel.iknow.android.data.model.Local;
import travel.iknow.android.data.model.Token;
import travel.iknow.android.db.ContentAddressCoverRelation;
import travel.iknow.android.db.ContentArticleCoverRelation;
import travel.iknow.android.db.ContentCategoriesRelation;
import travel.iknow.android.db.DbHelper;
import travel.iknow.android.rest.ApiHelper;

/**
 * Created by Pristalov Pavel on 12.02.2015 for IKnowTravel.
 */
public class MainActivity extends ActionBarActivity
{
    private String name;
    private String email;
    private String password;

    private ProgressBar loadingBar;
    private RecyclerView recyclerView;
    private ContentAdapter adapter;
    private LinearLayoutManager layoutManager;

    private Cursor currentCursor;

    private int currentPage = 0;
    public static final int CONTENT_DB_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        TextView text = (TextView) findViewById(R.id.title);
        text.setText(R.string.app_name);

        loadingBar = (ProgressBar) findViewById(R.id.loading_progress);

        performAuth();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContentAdapter(MainActivity.this, currentCursor);
        recyclerView.setAdapter(adapter);

        refreshAdapter();

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager)
        {
            @Override
            public void onLoadMore(int currentPage)
            {
                MainActivity.this.currentPage = currentPage;
                getContent(currentPage);
            }
        });
    }

    private void auth()
    {
        SharedPreferences prefs = getSharedPreferences(DataSource.PREFERENCES_NAME, 0);
        name = prefs.getString(DataSource.NAME_PREFERENCES_NAME, "");
        email = prefs.getString(DataSource.EMAIL_PREFERENCES_NAME, "");
        password = prefs.getString(DataSource.PASSWORD_PREFERENCES_NAME, "");

        if(name.isEmpty() || email.isEmpty() || password.isEmpty()) return;

        Callback<Object> cb = new Callback<Object>()
        {
            /**
             * Successful HTTP response.
             *
             * @param o
             * @param response
             */
            @Override
            public void success(Object o, Response response)
            {
                Toast.makeText(MainActivity.this, "Successfully authed!", Toast.LENGTH_SHORT)
                        .show();

                getContent(currentPage);
            }

            /**
             * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
             * exception.
             *
             * @param error
             */
            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(MainActivity.this, "cannot load", Toast.LENGTH_SHORT).show();
                loadingBar.setVisibility(View.GONE);
            }
        };

        ((IKnowTravelApplication) getApplication()).apiHelper
                .auth(new Local(name, email, password, false), cb);
    }

    private void performAuth()
    {
        final IKnowTravelApplication application = ((IKnowTravelApplication) getApplication());

        Callback<Token> cb = new Callback<Token>()
        {
            /**
             * Successful HTTP response.
             *
             * @param newToken
             * @param response
             */
            @Override
            public void success(Token newToken, Response response)
            {
                Toast.makeText(MainActivity.this, "token: " + newToken.getToken(), Toast.LENGTH_SHORT)
                        .show();
                DataSource.currentToken = newToken.getToken();

                application.updateHeaders(newToken.getToken());
                auth();
            }

            /**
             * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
             * exception.
             *
             * @param error
             */
            @Override
            public void failure(RetrofitError error)
            {
                loadingBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "cannot get token", Toast.LENGTH_SHORT).show();
            }
        };

        application.apiHelper.requestToken(cb);
    }

    private void onContentLoaded(List<Content> contents)
    {
        loadingBar.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this, "Content loaded", Toast.LENGTH_SHORT)
                .show();

        for (Content content : contents)
        {
            if(!DbHelper.isIdInDB(Content.class, content.getContentId()))
            {
                ContentCategoriesRelation categoriesRelation =
                        new ContentCategoriesRelation(content);
                if(!DbHelper.isRelationInDB(ContentCategoriesRelation.class,
                        categoriesRelation.getContentId()))
                    categoriesRelation.save();

                if(content.getType().equals(DataSource.TYPE_ADDRESS))
                {
                    ContentAddressCoverRelation addressCoverRelation =
                            new ContentAddressCoverRelation(content);

                    if(!DbHelper.isRelationInDB(ContentAddressCoverRelation.class,
                            addressCoverRelation.getContentId()))
                        addressCoverRelation.save();

                    if(!DbHelper.isIdInDB(AddressCover.class, content.getAddressCover().getCoverId()))
                        content.getAddressCover().save();
                }

                if(content.getType().equals(DataSource.TYPE_ARTICLE))
                {
                    ContentArticleCoverRelation articleCoverRelation =
                            new ContentArticleCoverRelation(content);

                    if(!DbHelper.isRelationInDB(ContentArticleCoverRelation.class,
                            articleCoverRelation.getContentId()))
                        articleCoverRelation.save();

                    if(!DbHelper.isIdInDB(ArticleCover.class, content.getArticleCover().getCoverId()))
                        content.getArticleCover().save();
                }

                for(Category cat : content.getCategories())
                {
                    if(!DbHelper.isIdInDB(Category.class, cat.getCategoryId())) cat.save();
                }
                content.save();
            }
        }

        refreshAdapter();
    }

    private void getContent(int currentPage)
    {
        final IKnowTravelApplication application = ((IKnowTravelApplication) getApplication());

        Callback<List<Content>> cb = new Callback<List<Content>>()
        {
            /**
             * Successful HTTP response.
             *
             * @param contents
             * @param response
             */
            @Override
            public void success(List<Content> contents, Response response)
            {
                onContentLoaded(contents);
            }

            /**
             * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
             * exception.
             *
             * @param error
             */
            @Override
            public void failure(RetrofitError error)
            {
                loadingBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "cannot load content", Toast.LENGTH_SHORT).show();
            }
        };

        loadingBar.setVisibility(View.VISIBLE);
        application.apiHelper.loadContent(currentPage * ApiHelper.DEFAULT_CONTENT_LIMIT,
                ApiHelper.DEFAULT_CONTENT_LIMIT,
                cb);
    }

    private void refreshAdapter()
    {
        getSupportLoaderManager().restartLoader(CONTENT_DB_LOADER_ID, null, loader);
    }

    ContentLoader loader = new ContentLoader(this)
    {
        @Override
        public void onDataLoaded(Cursor cursor)
        {
            adapter.swapCursor(cursor);
        }
    };
}
