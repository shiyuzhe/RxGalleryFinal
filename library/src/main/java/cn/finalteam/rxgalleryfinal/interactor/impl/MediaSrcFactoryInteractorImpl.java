package cn.finalteam.rxgalleryfinal.interactor.impl;

import android.content.Context;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.interactor.MediaSrcFactoryInteractor;
import cn.finalteam.rxgalleryfinal.utils.MediaUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 上午11:08
 */
public class MediaSrcFactoryInteractorImpl implements MediaSrcFactoryInteractor {

    Context context;
    OnGenerateMediaListener onGenerateMediaListener;
    boolean hasImage;

    public MediaSrcFactoryInteractorImpl(Context context, boolean hasImage, OnGenerateMediaListener onGenerateMediaListener) {
        this.context = context;
        this.hasImage = hasImage;
        this.onGenerateMediaListener = onGenerateMediaListener;
    }

    @Override
    public void generateMeidas(final int page, final int limit) {
        Observable.create((Observable.OnSubscribe<List<MediaBean>>) subscriber -> {
            List<MediaBean> mediaBeanList = null;
            if(hasImage) {
                mediaBeanList = MediaUtils.getMediaWithImageList(context, page, limit);
            } else {
                mediaBeanList = MediaUtils.getMediaWithVideoList(context, page, limit);
            }
            subscriber.onNext(mediaBeanList);
            subscriber.onCompleted();
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<MediaBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                onGenerateMediaListener.onFinished(page, limit, null);
            }

            @Override
            public void onNext(List<MediaBean> mediaBeenList) {
                onGenerateMediaListener.onFinished(page, limit, mediaBeenList);
            }
        });
    }
}
