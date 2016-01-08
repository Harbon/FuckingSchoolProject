package link.harbon.work.flipperbird;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by harbon on 16/1/3.
 */
public class ShoppingCart extends Dialog {

    ListView mListView;
    TextView mTotalPrice;
    TextView mTotalScore;
    TextView mChargeButton;
    CartAdapter mAdapter;
    CheckBox isScorePayCheckBox;
    float totalPrice = 0;
    float totalScore = 0;
    Context mContext;

    public ShoppingCart(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setContentView(R.layout.shopping_cart);
        mListView = (ListView) findViewById(R.id.cartListView);
        mTotalPrice = (TextView) findViewById(R.id.totalPrice);
        mTotalScore = (TextView) findViewById(R.id.totalScore);
        mChargeButton = (TextView) findViewById(R.id.buyButton);
        isScorePayCheckBox = (CheckBox) findViewById(R.id.checkboxScorePay);
        isScorePayCheckBox.setChecked(false);
        mAdapter = new CartAdapter(context);
        mAdapter.setCartListener(new CartAdapter.CartListener() {
            @Override
            public void deleteItem(int position) {
                calculatePrice();
            }
        });
        mListView.setAdapter(mAdapter);
        mChargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.mCurrentUser == null) {
                    Toast.makeText(mContext,"用户未登陆，请先登陆", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mAdapter.getCount() > 0) {
                    boolean isScorePay = isScorePayCheckBox.isChecked();
                    if (isScorePay) {
                        if (User.mCurrentUser.getScore() < totalScore) {
                            Toast.makeText(mContext,"积分不够，无法完成支付", Toast.LENGTH_LONG).show();
                        }else {
                            User.mCurrentUser.setScore(User.mCurrentUser.getScore() - totalScore);
                            User.mCurrentUser.update(mContext);
                            Toast.makeText(mContext, "购买成功", Toast.LENGTH_LONG).show();
                            ShoppingCart.this.dismiss();
                        }
                    }else {
                        if (User.mCurrentUser.getMoney() < totalPrice) {
                            Toast.makeText(mContext, "余额不足，无法完成支付", Toast.LENGTH_LONG).show();
                        }else {
                            User.mCurrentUser.setMoney(User.mCurrentUser.getMoney() - totalPrice);
                            User.mCurrentUser.setScore(User.mCurrentUser.getScore() + totalPrice * 0.01f);
                            User.mCurrentUser.update(mContext);
                            Toast.makeText(mContext, "购买成功，获得积分:"+(totalPrice * 0.01f), Toast.LENGTH_LONG).show();
                            ShoppingCart.this.dismiss();
                        }
                    }

                }else {
                    Toast.makeText(mContext, "请先选择商品", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void addProduction(Production production) {
        mAdapter.addItem(production);
        calculatePrice();
    }

    public void calculatePrice() {
        totalPrice = 0;
        totalScore = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            Production production = (Production) mAdapter.getItem(i);
            totalPrice += production.getPrice();
            totalScore += production.getScorePrice();
        }
        mTotalPrice.setText(totalPrice+"");
        mTotalScore.setText(totalScore+"");
    }

}
