package link.harbon.work.flipperbird;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harbon on 16/1/3.
 */
public class ShopAdapter extends BaseAdapter {

    private Context mContext;

    private List<Production> mProductions;

    DisplayImageOptions mOptions;

    private ShopListener mShopListener;

    public ShopAdapter(Context context) {
        this.mContext = context;
        this.mProductions = new ArrayList<>();
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.calendar) // resource or drawable
                .showImageForEmptyUri(R.drawable.calendar) // resource or drawable
                .showImageOnFail(R.drawable.calendar) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .displayer(new RoundedBitmapDisplayer(25))
                .build();
    }



    public void addItems(List<Production> productions) {
        this.mProductions = productions;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mProductions.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_production, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.productionImage);
        TextView name = (TextView) view.findViewById(R.id.productionName);
        TextView price = (TextView) view.findViewById(R.id.productionPrice);
        TextView scorePrice = (TextView) view.findViewById(R.id.productionScorePrice);
        TextView buttonAdd = (TextView) view.findViewById(R.id.buttonAddToCart);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Production production = (Production) v.getTag();
                if (mShopListener != null) {
                    mShopListener.addToCart(production);
                }
            }
        });
        Production production = mProductions.get(position);
        buttonAdd.setTag(production);
        ImageLoader.getInstance().displayImage(production.mIcon_url, imageView, mOptions);

        name.setText(production.getName());
        price.setText("￥ " + production.getPrice()+"");
        scorePrice.setText("积分：" + production.getScorePrice()+"");
        return view;
    }
    public interface ShopListener {
        public void addToCart(Production production);
    }
    public void setShopListener(ShopListener l) {
        this.mShopListener = l;
    }

    public void clear() {
        mProductions.clear();
    }
}
