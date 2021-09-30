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
package io.netty.channel;

import java.util.Queue;

/**
 * 用于创建{@link Queue}实例的工厂，该实例将用于存储{@link EventLoop}的任务。
 *
 * 一般来说，返回的{@link Queue}必须是线程安全的，取决于{@link EventLoop}实现必须是{@link java.util.concurrent.BlockingQueue}类型。
 */
public interface EventLoopTaskQueueFactory {

    /**
     * 返回一个新的{@link Queue}来使用。
     * @param maxCapacity 给定点时间上可以存储在{@link Queue}中的元素的最大数量
     * *
     * @return the new queue.
     */
    Queue<Runnable> newTaskQueue(int maxCapacity);
}
