package link.harbon.work.flipperbird;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harbon on 16/1/3.
 */
public class Production implements Parcelable {
    String mName;
    float mPrice;
    float mScorePrice;
    int mPicture;
    String mIcon_url;
    String mDescription;


    public Production(String name, float price, float scorePrice, int picture){
        this.mName = name;
        this.mPrice = price;
        this.mScorePrice = scorePrice;
        this.mPicture = picture;
    }
    public Production(String name, float price, float scorePrice, String icon_url){
        this.mName = name;
        this.mPrice = price;
        this.mScorePrice = scorePrice;
        this.mIcon_url = icon_url;
    }

    public String getName() {
        return mName;
    }

    public float getPrice() {
        return mPrice;
    }

    public float getScorePrice() {
        return mScorePrice;
    }

    public int getPicture() {
        return mPicture;
    }
    public String getDescription() {
        return this.mDescription;
    }
    public void setDescription(String description) {
        this.mDescription = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeFloat(this.mPrice);
        dest.writeFloat(this.mScorePrice);
        dest.writeInt(this.mPicture);
        dest.writeString(this.mIcon_url);
        dest.writeString(this.mDescription);
    }

    protected Production(Parcel in) {
        this.mName = in.readString();
        this.mPrice = in.readFloat();
        this.mScorePrice = in.readFloat();
        this.mPicture = in.readInt();
        this.mIcon_url = in.readString();
        this.mDescription = in.readString();
    }

    public static final Creator<Production> CREATOR = new Creator<Production>() {
        public Production createFromParcel(Parcel source) {
            return new Production(source);
        }

        public Production[] newArray(int size) {
            return new Production[size];
        }
    };
}
