package com.moong.programers.net.compose;

import com.moong.programers.data.Res;
import com.moong.programers.net.exception.ResultCodeException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RWComposer<T> extends Transformer<T> {
    private Function mFlatMapFunction;

    public RWComposer(boolean showPopup) {
        mFlatMapFunction = (Function<Res<T>, ObservableSource<?>>) value -> {
            if (value != null && value.getMessage().isEmpty()) {
                return Observable.just(value);
            } else {
                return Observable.error(ResultCodeException.Companion.create(value, showPopup));
            }
        };
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.flatMap(mFlatMapFunction);
    }
}
