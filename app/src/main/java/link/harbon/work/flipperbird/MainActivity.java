package link.harbon.work.flipperbird;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ShoppingCart mShoppingCart;

    CircleImageView userAvatar;
    TextView userName;
    TextView userEmail;
    TextView remainScoreTextView;
    TextView remainMoneyTextView;
    FloatingActionButton mFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageLoaderConfiguration configs = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(configs);//初始化ImageLoader组件
        mShoppingCart = new ShoppingCart(this, R.style.customDialog);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShoppingCart.show();

            }
        });
        mShoppingCart.setShoppingCarListener(new ShoppingCart.ShoppingCartListener() {
            @Override
            public void charge(float moneyChange, int scoreChange) {
                Log.i("aaaaaa", "moneychange:"+moneyChange+"  scoreChage:"+scoreChange);
                float currentMoney = User.mCurrentUser.getMoney() + moneyChange;
                int currentScore = User.mCurrentUser.getScore() + scoreChange;
                Log.i("aaaaaa", "currentMoney:"+currentMoney+"  currentScore:"+currentScore);
                remainMoneyTextView.setText(currentMoney+"元");
                remainScoreTextView.setText(currentScore +"");
                User.mCurrentUser.setMoney(currentMoney);
                User.mCurrentUser.setScore(currentScore);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                userAvatar = (CircleImageView) navigationView.findViewById(R.id.user_avatar);
                if (userAvatar == null) {
                    Log.i("aaaaaa","user avatar nulllll");
                }
                userName = (TextView) navigationView.findViewById(R.id.user_name);
                userEmail = (TextView) navigationView.findViewById(R.id.user_email);
                remainMoneyTextView = (TextView) navigationView.findViewById(R.id.remainMoney);
                remainScoreTextView = (TextView) navigationView.findViewById(R.id.remainScore);
                if (userName == null) {
                    Log.i("aaaaaa", "userName null");
                }
            }
        });


        final StaggeredGridView staggeredGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        final ShopAdapter shopAdapter = new ShopAdapter(this);
//        shopAdapter.addItems(MonitorData.getMonitorProductions());
        shopAdapter.setShopListener(new ShopAdapter.ShopListener() {
            @Override
            public void addToCart(Production production) {
                mShoppingCart.addProduction(production);
                Toast.makeText(MainActivity.this, "已添加：" + production.getName() + "到购物车", Toast.LENGTH_SHORT).show();
            }
        });
        staggeredGridView.setAdapter(shopAdapter);
        staggeredGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Production production = (Production) shopAdapter.getItem(position);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProductDetail.class);
                intent.putExtra("production", production);
                MainActivity.this.startActivity(intent);
            }
        });
        API.asyncHttpClient.get(API.BASE_URL + API.GET_ALL_PRODUCTION, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int status = response.getInt("status");
                    if (status == 0) {
                        JSONArray jsonArray = response.getJSONArray("productions");
                        List<Production> productions = new ArrayList<Production>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject productionJson = (JSONObject) jsonArray.get(i);
                            String name = productionJson.getString("name");
                            float price = (float) productionJson.getDouble("price");
                            float scorePrice = (float) productionJson.getDouble("scorePrice");
                            String icon_url = productionJson.getString("picture");
                            String description = productionJson.getString("description");
                            Production production = new Production(name, price, scorePrice, icon_url);
                            production.setDescription(description);
                            productions.add(production);
                        }
                        shopAdapter.addItems(productions);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getParcelableExtra("buyProduction") != null) {
            Production production = intent.getParcelableExtra("buyProduction");
            mShoppingCart.addProduction(production);
            Toast.makeText(this, "已添加："+production.getName()+"到购物车", Toast.LENGTH_SHORT).show();
        }

        if (intent.getParcelableExtra("userLogin") != null) {
            User user = intent.getParcelableExtra("userLogin");
            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
            Log.i("aaaaaa", "user score:"+user.getScore());
            remainScoreTextView.setText(user.getScore()+"");
            remainMoneyTextView.setText(user.getMoney()+"元");
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            User.mCurrentUser = user;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.login_register) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.change_email) {

        }else if (item.getItemId() == R.id.change_name) {

        }else if (item.getItemId() == R.id.change_password) {

        }else if (item.getItemId() == R.id.logout) {
            User.mCurrentUser = null;
            userName.setText("");
            userEmail.setText("");
            remainMoneyTextView.setText("");
            remainScoreTextView.setText("");
            Toast.makeText(this, "当前账户已经注销", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
