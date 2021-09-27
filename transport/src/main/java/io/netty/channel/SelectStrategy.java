/*
 * Copyright 2016 The Netty Project
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

import io.netty.util.IntSupplier;

/**
 * 选择策略接口。
 *
 * 提供控制选择循环行为的能力。
 * 例如，阻塞选择如果有需要立即处理的事件，操作可能会被延迟或完全跳过。
 */
public interface SelectStrategy {

    /**
     * 指示应该跟随一个阻塞选择。
     */
    int SELECT = -1;
    /**
     * 指示IO循环应该重试，没有阻塞选择直接跟随。
     */
    int CONTINUE = -2;
    /**
     * 表示IO循环不阻塞地轮询新事件。
     */
    int BUSY_WAIT = -3;

    /**
     *
     * {@link SelectStrategy}可以用来引导一个潜在的选择调用的结果。
     *
     * @param selectSupplier 带有选择结果结果的供应商。
     * @param hasTasks 如果任务等待处理，则为True。
     * @return {@link #SELECT} if the next step should be blocking select {@link #CONTINUE} if
     *         the next step should be to not select but rather jump back to the IO loop and try
     *         again. Any value >= 0 is treated as an indicator that work needs to be done.
     */
    int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception;
}
