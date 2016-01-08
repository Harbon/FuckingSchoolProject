package link.harbon.work.flipperbird;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harbon on 16/1/5.
 */
public class CartAdapter extends BaseAdapter {

    List<Production> mCartProductions;

    Context mContext;

    CartListener mCartListener;
    DisplayImageOptions mOptions;

    public CartAdapter (Context context) {
        mCartProductions = new ArrayList<>();
        mContext = context;
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.calendar) // resource or drawable
                .showImageForEmptyUri(R.drawable.calendar) // resource or drawable
                .showImageOnFail(R.drawable.calendar) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .displayer(new RoundedBitmapDisplayer(25))
                .build();
    }
    public void addItem(Production production) {
        mCartProductions.add(production);
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mCartProductions.size();
    }

    @Override
    public Object getItem(int position) {
        return mCartProductions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.productionImage);
        TextView name = (TextView) view.findViewById(R.id.productionName);
        TextView price = (TextView) view.findViewById(R.id.productionPrice);
        TextView scorePrice = (TextView) view.findViewById(R.id.productionScorePrice);
        TextView deleteButton = (TextView) view.findViewById(R.id.deleteProductionButton);
        final Production production = mCartProductions.get(position);
//        imageView.setImageDrawable(mContext.getResources().getDrawable(production.getPicture()));
        ImageLoader.getInstance().displayImage(production.mIcon_url, imageView, mOptions);
        name.setText(production.getName());
        price.setText(production.getPrice()+"");
        scorePrice.setText(production.getScorePrice()+"");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Toast.makeText(mContext,"删除了商品："+mCartProductions.get(position).getName(), Toast.LENGTH_SHORT).show();
                mCartProductions.remove(position);
                if (mCartListener != null) {
                    mCartListener.deleteItem(position);
                }
                CartAdapter.this.notifyDataSetChanged();
            }
        });
        deleteButton.setTag(position);
        return view;
    }

    public interface CartListener{
        public void deleteItem(int position);
    }
    public void setCartListener(CartListener l) {
        this.mCartListener = l;
    }

    public void clear() {
        mCartProductions.clear();
    }
}
