package com.moong.programers.net.compose;

import com.moong.programers.data.Res;
import com.moong.programers.net.exception.ResultCodeException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Appg-EzBalance
 * Class: RWComposer
 * Created by d on 2019-09-30.
 * <p>
 * Description:
 */
public class RWComposer<T> extends Transformer<T> {
    private Function mFlatMapFunction;

    public RWComposer(boolean showPopup) {
        mFlatMapFunction = (Function<Res<T>, ObservableSource<?>>) value -> {
            if (value != null && value.getStatusCode() == 200) {
                return Observable.just(value);
            } else {
                return Observable.error(ResultCodeException.create(value, showPopup));
            }
        };
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.flatMap(mFlatMapFunction);
    }
}
