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
package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 特殊的{@link EventExecutorGroup}允许注册{@link Channel}获取处理后的选择在事件循环期间。
 *
 */
public interface EventLoopGroup extends EventExecutorGroup {
    /**
     * 返回下一个要使用的{@link EventLoop}
     */
    @Override
    EventLoop next();

    /**
     * 在这个{@link EventLoop}中注册一个{@link Channel}。
     * 返回的{@link ChannelFuture}将在注册完成后收到通知。
     */
    ChannelFuture register(Channel channel);

    /**
     * 使用{@link ChannelFuture}在这个{@link EventLoop}中注册一个{@link Channel}。
     * 传递的{@link ChannelFuture}将得到通知，一旦注册完成，也将得到返回。
     */
    ChannelFuture register(ChannelPromise promise);

    /**
     * 在这个{@link EventLoop}中注册一个{@link Channel}。
     * 已传递的{@link ChannelFuture}将在注册完成后得到通知，并将被退回。
     *
     * @deprecated Use {@link #register(ChannelPromise)} instead.
     */
    @Deprecated
    ChannelFuture register(Channel channel, ChannelPromise promise);
}
