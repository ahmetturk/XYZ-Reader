package com.example.xyzreader.ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mCursor;
    private long mStartId;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = findViewById(R.id.activity_article_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStartId = ItemsContract.Items.getItemId(getIntent().getData());

        imageView = findViewById(R.id.activity_article_detail_image_view);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;

        // Select the start ID
        if (mStartId > 0) {
            mCursor.moveToFirst();
            // TODO: optimize
            while (!mCursor.isAfterLast()) {
                if (mCursor.getLong(ArticleLoader.Query._ID) == mStartId) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.activity_article_detail_frame_layout_fragment_container,
                                    ArticleDetailFragment.newInstance(mCursor.getLong(ArticleLoader.Query._ID)))
                            .commit();
                    break;
                }
                mCursor.moveToNext();
            }
            mStartId = 0;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
    }

    public void setBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
