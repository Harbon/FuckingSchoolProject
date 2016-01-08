package link.harbon.work.flipperbird;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

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
    int totalScore = 0;
    Context mContext;
    ProgressBar mProgressBar;

    ShoppingCartListener mShoppingCarListener;

    public ShoppingCart(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setContentView(R.layout.shopping_cart);
        mListView = (ListView) findViewById(R.id.cartListView);
        mTotalPrice = (TextView) findViewById(R.id.totalPrice);
        mTotalScore = (TextView) findViewById(R.id.totalScore);
        mChargeButton = (TextView) findViewById(R.id.buyButton);
        isScorePayCheckBox = (CheckBox) findViewById(R.id.checkboxScorePay);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                    final boolean isScorePay = isScorePayCheckBox.isChecked();
                    if (isScorePay) {
                        if (User.mCurrentUser.getScore() < totalScore) {
                            Toast.makeText(mContext,"积分不够，无法完成支付", Toast.LENGTH_LONG).show();
                            return;
                        }
//                            User.mCurrentUser.setScore(User.mCurrentUser.getScore() - totalScore);
//                            User.mCurrentUser.update(mContext);
//                            Toast.makeText(mContext, "购买成功", Toast.LENGTH_LONG).show();
//                            ShoppingCart.this.dismiss();
                    }else {
                        if (User.mCurrentUser.getMoney() < totalPrice) {
                            Toast.makeText(mContext, "余额不足，无法完成支付", Toast.LENGTH_LONG).show();
                            return;
                        }

//                            User.mCurrentUser.setMoney(User.mCurrentUser.getMoney() - totalPrice);
//                            User.mCurrentUser.setScore(User.mCurrentUser.getScore() + totalPrice * 0.01f);
//                            User.mCurrentUser.update(mContext);
//                            Toast.makeText(mContext, "购买成功，获得积分:"+(totalPrice * 0.01f), Toast.LENGTH_LONG).show();
//                            ShoppingCart.this.dismiss();
                    }
                    mProgressBar.setVisibility(View.VISIBLE);
                    StringEntity stringEntity = null;
                    if (isScorePay) {
                        try {
                            JSONObject jsonObject = new JSONObject().put("score", -totalScore);
                            stringEntity = new StringEntity(jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }else {
                        try {
                            JSONObject jsonObject = new JSONObject().put("score", totalPrice / 10f).put("money", -totalPrice);
                            stringEntity = new StringEntity(jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

                    API.asyncHttpClient.post(mContext,API.BASE_URL+API.POST_UPDATE_USER_MONEY_OR_SCORE+User.mCurrentUser.getUserId(),stringEntity,"application/json", new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Log.i("aaaaaa", "response:"+response);
                            try {
                                int status = response.getInt("status");
                                Log.i("aaaaaa", "status:"+status);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mProgressBar.setVisibility(View.GONE);
                            if (isScorePay) {
                                Toast.makeText(mContext, "使用积分购买成功", Toast.LENGTH_LONG).show();
                                if (mShoppingCarListener != null) {
                                    mShoppingCarListener.charge(0f, -totalScore);
                                }
                            }else {
                                int getScore = (int) (totalPrice * 0.1);
                                Toast.makeText(mContext, "购买成功，获得积分:"+getScore, Toast.LENGTH_LONG).show();
                                if (mShoppingCarListener != null) {
                                    mShoppingCarListener.charge(-totalPrice, getScore);
                                }
                            }
                            mAdapter.clear();
                            calculatePrice();
                            ShoppingCart.this.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "支付失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    });

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
    public interface ShoppingCartListener {
        public void charge(float moneyChange, int scoreChange);
    }
    public void setShoppingCarListener (ShoppingCartListener l) {
        mShoppingCarListener = l;
    }

    public void clear() {

    }
}
