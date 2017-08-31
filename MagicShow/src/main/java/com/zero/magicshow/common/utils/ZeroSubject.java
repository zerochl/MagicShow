package com.zero.magicshow.common.utils;

import rx.functions.Action1;
import rx.subjects.Subject;

/**
 * Created by hongli on 2017/8/31.
 */

public class ZeroSubject<T>{
    private int actionType;
    private Subject<T,T> subject;
    private Action1<T> action;
    public ZeroSubject(Subject<T,T> subject, int actionType, Action1<T> action) {
        this.actionType = actionType;
        this.subject = subject;
        this.action = action;
    }

    public int getActionType(){
        return actionType;
    }

    public Subject getSubject(){
        return subject;
    }

    public Action1<T> getAction() {
        return action;
    }
}
