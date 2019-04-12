package com.mirage.gamelogic

import me.khol.reactive.subjects.EventSubject
import rx.Subscription

internal class EventSubjectAdapter<T>(private val subj: EventSubject<T> = EventSubject.create<T>()) : rx.subjects.Subject<T, T> ({

    it.add(object : Subscription {
        val subs = subj.subscribe(it::onNext, it::onError, it::onCompleted)

        override fun isUnsubscribed(): Boolean = subs.isDisposed
        override fun unsubscribe() = subs.dispose()
    })

}) {

    override fun hasObservers(): Boolean = subj.hasObservers()

    override fun onError(e: Throwable?) = subj.onError(e ?: Exception("WTF"))

    override fun onCompleted() = subj.onComplete()

    override fun onNext(t: T) = subj.onNext(t)
}