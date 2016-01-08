package link.harbon.work.flipperbird;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by harbon on 16/1/3.
 */
public class ProductDetail extends AppCompatActivity {

    ImageView mProductionImage;
    FloatingActionButton mBuyButton;
    TextView mProductionPriceTextView;
    TextView mProductionScoreTextView;
    TextView mProductionDescription;
    CollapsingToolbarLayout mCollapsingToolBarLayout;
    DisplayImageOptions mOptions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.calendar) // resource or drawable
                .showImageForEmptyUri(R.drawable.calendar) // resource or drawable
                .showImageOnFail(R.drawable.calendar) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();
        Intent intent = getIntent();
        final Production production = intent.getParcelableExtra("production");
        mProductionImage = (ImageView) findViewById(R.id.productionImage);
        mBuyButton = (FloatingActionButton) findViewById(R.id.buyButton);
        mProductionPriceTextView = (TextView) findViewById(R.id.productionPriceText);
        mProductionScoreTextView = (TextView) findViewById(R.id.productionScoreText);
        mProductionDescription = (TextView) findViewById(R.id.productionDescription);
        mCollapsingToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageLoader.getInstance().displayImage(production.mIcon_url, mProductionImage, mOptions);
        mCollapsingToolBarLayout.setTitle(production.getName());
        if (production != null) {
            mBuyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentFor = new Intent();
                    intentFor.setClass(ProductDetail.this, MainActivity.class);
                    intentFor.putExtra("buyProduction", production);
                    ProductDetail.this.startActivity(intentFor);
                }
            });
        }
        mProductionPriceTextView.setText("价格：" + production.getPrice() + " 元");
        mProductionScoreTextView.setText("积分：" + production.getScorePrice());
        mProductionDescription.setText(production.getDescription());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
