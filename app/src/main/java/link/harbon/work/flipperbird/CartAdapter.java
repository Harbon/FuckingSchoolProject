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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harbon on 16/1/5.
 */
public class CartAdapter extends BaseAdapter {

    List<Production> mCartProductions;

    Context mContext;

    CartListener mCartListener;

    public CartAdapter (Context context) {
        mCartProductions = new ArrayList<>();
        mContext = context;
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
        imageView.setImageDrawable(mContext.getResources().getDrawable(production.getPicture()));
        name.setText(production.getName());
        price.setText(production.getPrice()+"");
        scorePrice.setText(production.getScorePrice()+"");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mCartProductions.remove(position);
                CartAdapter.this.notifyDataSetChanged();
                Snackbar.make(null, "删除了商品："+mCartProductions.get(position).getName(), Snackbar.LENGTH_SHORT);
                if (mCartListener != null) {
                    mCartListener.deleteItem(position);
                }
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
}
