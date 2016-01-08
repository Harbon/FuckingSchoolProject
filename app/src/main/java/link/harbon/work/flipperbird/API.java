package link.harbon.work.flipperbird;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by harbon on 16/1/8.
 */
public class API {
    public static String BASE_URL = "http://192.168.1.10:3000";
    public static String USER_REGISTER = "/users/register";
    public static String USER_LOGIN = "/users/login";
    public static String USER_LOGOUT = "/users/logout";
    public static String GET_USER_INFO = "/users/info/";
    public static String GET_ALL_PRODUCTION = "/pdc";
    public static String POST_UPDATE_USER_MONEY_OR_SCORE = "/users/update/";
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

}
