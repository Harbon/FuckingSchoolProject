package link.harbon.work.flipperbird;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by harbon on 16/1/3.
 */
public class User implements Parcelable {
    float mMoney;
    String mName;
    String mAvatar;
    String mEmail;
    float mScore;
    String mPassword;

    public static User mCurrentUser;



    public User() {}

    public float getMoney() {
        return mMoney;
    }

    public void setMoney(float money) {
        this.mMoney = money;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public float getScore() {
        return mScore;
    }

    public void setScore(float score) {
        this.mScore = score;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public static User getUserByName(String name, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        String userPropertiesString = sharedPreferences.getString(name, null);
        if (userPropertiesString != null) {
            User user = new User();
            String[] properties = userPropertiesString.split(",");
            user.setName(properties[0]);
            user.setPassword(properties[1]);
            user.setAvatar(properties[2]);
            user.setEmail(properties[3]);
            user.setMoney(Float.parseFloat(properties[4]));
            user.setScore(Float.parseFloat(properties[5]));
            return user;
        }else {
            return null;
        }
    }
    public void update(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Users",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String propertiesString = ""+this.mName+","+this.mPassword+","+this.mEmail+","+this.mAvatar+","+this.mMoney+","+this.mScore;
        editor.putString(mName, propertiesString);
        editor.commit();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mMoney);
        dest.writeString(this.mName);
        dest.writeString(this.mAvatar);
        dest.writeString(this.mEmail);
        dest.writeFloat(this.mScore);
        dest.writeString(this.mPassword);
    }

    protected User(Parcel in) {
        this.mMoney = in.readFloat();
        this.mName = in.readString();
        this.mAvatar = in.readString();
        this.mEmail = in.readString();
        this.mScore = in.readFloat();
        this.mPassword = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };



}
