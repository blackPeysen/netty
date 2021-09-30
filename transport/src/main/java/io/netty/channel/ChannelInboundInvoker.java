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

public interface ChannelInboundInvoker {

    /**
     * 一个{@link Channel}被注册到它的{@link EventLoop}。
     *
     * 这将导致有{@link ChannelInboundHandler#channelRegistered(ChannelHandlerContext)}方法
     * 的{@link ChannelInboundHandler}中包含的下一个{@link ChannelPipeline}调用{@link Channel}。
     */
    ChannelInboundInvoker fireChannelRegistered();

    /**
     * 从其{@link EventLoop}中未注册{@link Channel}。
     *
     * This will result in having the  {@link ChannelInboundHandler#channelUnregistered(ChannelHandlerContext)} method
     * called of the next  {@link ChannelInboundHandler} contained in the  {@link ChannelPipeline} of the
     * {@link Channel}.
     */
    ChannelInboundInvoker fireChannelUnregistered();

    /**
     * 一个{@link通道}现在是活跃的，这意味着它已连接。
     *
     * This will result in having the  {@link ChannelInboundHandler#channelActive(ChannelHandlerContext)} method
     * called of the next  {@link ChannelInboundHandler} contained in the  {@link ChannelPipeline} of the
     * {@link Channel}.
     */
    ChannelInboundInvoker fireChannelActive();

    /**
     * {@link Channel}现在是非活动的，这意味着它是关闭的。
     *
     * This will result in having the  {@link ChannelInboundHandler#channelInactive(ChannelHandlerContext)} method
     * called of the next  {@link ChannelInboundHandler} contained in the  {@link ChannelPipeline} of the
     * {@link Channel}.
     */
    ChannelInboundInvoker fireChannelInactive();

    /**
     * {@link Channel}在其入站操作中收到{@link Throwable}。
     *
     * This will result in having the  {@link ChannelInboundHandler#exceptionCaught(ChannelHandlerContext, Throwable)}
     * method  called of the next  {@link ChannelInboundHandler} contained in the  {@link ChannelPipeline} of the
     * {@link Channel}.
     */
    ChannelInboundInvoker fireExceptionCaught(Throwable cause);

    /**
     * {@link Channel}收到用户定义的事件。
     *
     * This will result in having the  {@link ChannelInboundHandler#userEventTriggered(ChannelHandlerContext, Object)}
     * method  called of the next  {@link ChannelInboundHandler} contained in the  {@link ChannelPipeline} of the
     * {@link Channel}.
     */
    ChannelInboundInvoker fireUserEventTriggered(Object event);

    /**
     * 一个{@link Channel}收到一条消息。
     *
     * This will result in having the {@link ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}
     * method  called of the next {@link ChannelInboundHandler} contained in the  {@link ChannelPipeline} of the
     * {@link Channel}.
     */
    ChannelInboundInvoker fireChannelRead(Object msg);

    /**
     * 触发一个{@link ChannelInboundHandler#channelReadComplete(ChannelHandlerContext)}
     *  事件到{@link ChannelInboundHandler}中的下一个{@link ChannelPipeline}。
     */
    ChannelInboundInvoker fireChannelReadComplete();

    /**
     * 触发一个{@link ChannelInboundHandler #channelWritabilityChanged(ChannelHandlerContext)}
     * *事件到{@link ChannelInboundHandler}中的下一个{@link ChannelPipeline}
     */
    ChannelInboundInvoker fireChannelWritabilityChanged();
}
