package com.moong.programers.net

import io.reactivex.ObservableTransformer

abstract class Transformer<T> : ObservableTransformer<T, T>