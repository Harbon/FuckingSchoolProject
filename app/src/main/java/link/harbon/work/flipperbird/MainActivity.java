package link.harbon.work.flipperbird;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ShoppingCart mShoppingCart;

    CircleImageView userAvatar;
    TextView userName;
    TextView userEmail;
    FloatingActionButton mFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mShoppingCart = new ShoppingCart(this, R.style.customDialog);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                mShoppingCart.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userAvatar = (CircleImageView) navigationView.findViewById(R.id.user_avatar);
        userName = (TextView) navigationView.findViewById(R.id.user_name);
        userEmail = (TextView) navigationView.findViewById(R.id.user_email);


        StaggeredGridView staggeredGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        final ShopAdapter shopAdapter = new ShopAdapter(this);
        shopAdapter.addItems(MonitorData.getMonitorProductions());
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

        }
        return true;
    }
}
