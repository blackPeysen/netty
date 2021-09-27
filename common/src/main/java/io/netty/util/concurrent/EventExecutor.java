/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.util.concurrent;

/**
 * {@link EventExecutor}是一个特殊的{@link EventExecutorGroup}
 *      使用一些方便的方法来查看{@link Thread}是否在事件循环中执行。
 *      除此之外，它还扩展了{@link EventExecutorGroup}以允许泛型访问方法的方法。
 */
public interface EventExecutor extends EventExecutorGroup {

    /**
     * 返回对自身的引用。
     */
    @Override
    EventExecutor next();

    /**
     * 返回{@link EventExecutor}的父类{@link EventExecutor}，
     */
    EventExecutorGroup parent();

    /**
     * 以{@link Thread#currentThread()}作为参数调用{@link #inEventLoop(Thread)}
     */
    boolean inEventLoop();

    /**
     * 如果给定的{@link Thread}在事件循环中执行，则返回{@code true}，{@code false}否则。
     */
    boolean inEventLoop(Thread thread);

    /**
     * Return a new {@link Promise}.
     */
    <V> Promise<V> newPromise();

    /**
     * Create a new {@link ProgressivePromise}.
     */
    <V> ProgressivePromise<V> newProgressivePromise();

    /**
     * Create a new {@link Future} which is marked as succeeded already. So {@link Future#isSuccess()}
     * will return {@code true}. All {@link FutureListener} added to it will be notified directly. Also
     * every call of blocking methods will just return without blocking.
     */
    <V> Future<V> newSucceededFuture(V result);

    /**
     * Create a new {@link Future} which is marked as failed already. So {@link Future#isSuccess()}
     * will return {@code false}. All {@link FutureListener} added to it will be notified directly. Also
     * every call of blocking methods will just return without blocking.
     */
    <V> Future<V> newFailedFuture(Throwable cause);
}
