/*
 * Copyright 2019 The Netty Project
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
package io.netty.util.internal;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.FastThreadLocal;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * 允许为调用{@link Thread}检索{@link EventExecutor}。
 */
public final class ThreadExecutorMap {

    private static final FastThreadLocal<EventExecutor> mappings = new FastThreadLocal<EventExecutor>();

    private ThreadExecutorMap() { }

    /**
     * 返回当前使用{@link Thread}的{@link EventExecutor}，如果没有/ unknown则返回{@code null}。
     */
    public static EventExecutor currentExecutor() {
        return mappings.get();
    }

    /**
     * 设置当前{@link Thread}使用的{@link EventExecutor}。
     */
    private static void setCurrentEventExecutor(EventExecutor executor) {
        mappings.set(executor);
    }

    /**
     * 装饰给定的{@link Executor}，并确保{@link #currentExecutor()}将返回{@code eventExecutor}在执行过程中从{@link Runnable}内部调用。
     */
    public static Executor apply(final Executor executor, final EventExecutor eventExecutor) {
        ObjectUtil.checkNotNull(executor, "executor");
        ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
        return new Executor() {
            @Override
            public void execute(final Runnable command) {
                executor.execute(apply(command, eventExecutor));
            }
        };
    }

    /**
     * 装饰给定的{@link Runnable}并确保{@link #currentExecutor()}将返回{@code eventExecutor}在执行过程中从{@link Runnable}内部调用。
     */
    public static Runnable apply(final Runnable command, final EventExecutor eventExecutor) {
        ObjectUtil.checkNotNull(command, "command");
        ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
        return new Runnable() {
            @Override
            public void run() {
                setCurrentEventExecutor(eventExecutor);
                try {
                    command.run();
                } finally {
                    setCurrentEventExecutor(null);
                }
            }
        };
    }

    /**
     * 装饰给定的{@link ThreadFactory}，并确保{@link #currentExecutor()}将返回{@code eventExecutor}在执行过程中从{@link Runnable}内部调用。
     */
    public static ThreadFactory apply(final ThreadFactory threadFactory, final EventExecutor eventExecutor) {
        ObjectUtil.checkNotNull(threadFactory, "command");
        ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return threadFactory.newThread(apply(r, eventExecutor));
            }
        };
    }
}
