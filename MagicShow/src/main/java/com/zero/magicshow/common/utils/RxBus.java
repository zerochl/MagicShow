package com.zero.magicshow.common.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zero.zerolib.manager.PostManager;

import java.util.Vector;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.util.ActionSubscriber;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by hongli on 2017/8/31.
 */

public class RxBus {
    private static RxBus instance;
    private Vector<ZeroSubject> subjectList = new Vector<>();

    private RxBus() {
    }

    public static synchronized RxBus getInstance() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }

//    public synchronized <T> ZeroSubject<T> register(int actionType) {
//        Subject<T, T> subject = PublishSubject.create();
//        ZeroSubject<T> zeroSubject = new ZeroSubject(subject,actionType,null);
//        subjectList.add(zeroSubject);
//        return zeroSubject;
//    }
//
//    public synchronized void unregister(Object object) {
//        subjectList.remove(object);
//    }

    public synchronized <T> void registerMain(int actionType, Action1<T> action){
        Subject<T, T> subject = PublishSubject.create();
        subject.observeOn(AndroidSchedulers.mainThread()).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                Log.e("RxBus","post error");
            }
        }).subscribe(new ActionSubscriber<T>(action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                Log.e("RxBus","post error");
            }
        }, Actions.empty()));
        ZeroSubject<T> zeroSubject = new ZeroSubject<T>(subject,actionType,action);
        subjectList.add(zeroSubject);
    }

    public synchronized<T> void unregisterMain(Action1<T> action) {
        for(ZeroSubject zeroSubject : subjectList){
            if(zeroSubject.getAction() == action){
                subjectList.remove(zeroSubject);
                break;
            }
        }
    }

    public synchronized void unregisterMain(int actionType) {
        for(ZeroSubject zeroSubject : subjectList){
            if(zeroSubject.getActionType() == actionType){
                subjectList.remove(zeroSubject);
                break;
            }
        }
    }
    public synchronized void unregisterAllMain(int actionType) {
        Vector<ZeroSubject> subjectRemoveList = new Vector<>();
        for(ZeroSubject zeroSubject : subjectList){
            if(zeroSubject.getActionType() == actionType){
                subjectRemoveList.add(zeroSubject);
            }
        }
        subjectList.removeAll(subjectRemoveList);
    }

    public synchronized<T> void unregisterCategory(int actionType) {
        for(ZeroSubject zeroSubject : subjectList){
            if(zeroSubject.getActionType() == actionType){
                subjectList.remove(zeroSubject);
                break;
            }
        }
    }

    public void post(@NonNull final Object content, final int actionType) {
        PostManager.getInstance().postMain(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try{
                        for (ZeroSubject subject : subjectList) {
                            if (subject != null && subject.getActionType() == actionType) {
                                try{
                                    subject.getSubject().onNext(content);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        },0);
    }
}
