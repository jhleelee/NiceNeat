package jacklee_entertainment.niceneat;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import jacklee_entertainment.niceneat.fragment.main.PageFragment;

/**
 * Created by rizki on 4/15/15.
 */
public class TabOption implements Parcelable{
    private int pageCount;
    private String[] pageTitle;
    private int[] pageLayout;
    private int[] icon;
    private boolean iconHeader;
    private int indicatorColor = R.color.green_strong;
    private int dividerColor = R.color.green_strong;
    private int colorInactive = R.color.red__orange_style;
    private PageFragment pageFragment = new PageFragment();

    private int colorActive = Color.parseColor("#00a13b");

    public PageFragment getPageFragment() {
        return pageFragment;
    }

    public void setPageFragment(PageFragment pageFragment) {
        this.pageFragment = pageFragment;
    }

    public int[] getIcon() {
        return icon;
    }

    public void setIcon(int[] icon) {
        this.icon = icon;
    }

    public String[] getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String[] pageTitle) {
        this.pageTitle = pageTitle;
    }

    public int[] getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(int[] pageLayout) {
        this.pageLayout = pageLayout;
    }

    public boolean isIconHeader() {
        return iconHeader;
    }

    public void setIconHeader(boolean iconHeader) {
        this.iconHeader = iconHeader;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public int getColorInactive() {
        return colorInactive;
    }

    public void setColorInactive(int colorInactive) {
        this.colorInactive = colorInactive;
    }

    public int getColorActive() {
        return colorActive;
    }

    public void setColorActive(int colorActive) {
        this.colorActive = colorActive;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
