package codes.carl.gallery.model.views;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.utils.sort.SortType;
import io.reactivex.disposables.CompositeDisposable;

public class GalleryViewModel extends ViewModel {

    private List<Picture> pictures = new ArrayList<>();
    private SortType sortType = SortType.NORMAL;
    private boolean isRefreshing = false;
    private CompositeDisposable rxDisposables = new CompositeDisposable();

    public GalleryViewModel() {
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public CompositeDisposable getRxDisposables() {
        return rxDisposables;
    }

    public void setRxDisposables(CompositeDisposable rxDisposables) {
        this.rxDisposables = rxDisposables;
    }
}
