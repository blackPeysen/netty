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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeMap;

import java.net.InetSocketAddress;
import java.net.SocketAddress;


/**
 * 连接到网络套接字或能够进行读写、连接和绑定等IO操作的组件。
 * <p>
 * 通道为用户提供:
 * <ul>
 * <li>通道的当前状态(例如，它是打开的吗?它是连接?),</li>
 * <li>通道的{@linkplain ChannelConfig 配置参数}(例如接收缓冲区大小),</li>
 * <li>通道支持的I/O操作(例如读、写、连接和绑定)</li>
 * <li>{@link ChannelPipeline}处理所有I/O事件和请求与通道关联.</li>
 * </ul>
 *
 * <h3>所有的I/O操作都是异步的.</h3>
 * <p>
 * Netty中的所有I/O操作都是异步的。
 * 这意味着任何I/O调用都可以立即返回，但不保证所请求的I/O操作已在通话结束时完成。
 * 相反，你会被带回来一个{@link ChannelFuture}实例，它将在请求I/O时通知你操作已成功、失败或取消。
 *
 * <h3>通道是分层的</h3>
 * <p>
 * 一个{@link Channel}可以有一个{@linkplain #parent() parent}依赖于它是如何创建的。
 * 例如，一个被接受的{@link SocketChannel}by {@link ServerSocketChannel}，
 * 将返回{@link ServerSocketChannel}作为它的父类在{@link #parent()}。
 * <p>
 * 层次结构的语义依赖于传输实现，其中{@link Channel}属于。
 * 例如，你可以编写一个新的{@link Channel}实现来创建子通道
 * *共享一个套接字连接，如<a href="http://beepcore.org/">BEEP</a>和
 * * <a href="https://en.wikipedia.org/wiki/Secure_Shell">SSH</a> do。
 *
 * <h3>下行访问特定于传输的操作</h3>
 * <p>
 * 某些传输公开特定于的附加操作运输。
 * 将{@link Channel}向下转换为子类型以调用此类操作。
 * 例如，对于旧的I/O数据报传输，多播join / leave操作由{@link DatagramChannel}提供。
 *
 * <h3>Release resources</h3>
 * <p>
 * 重要的是调用{@link #close()}或{@link #close(ChannelPromise)}来释放所有资源，一旦你完成了{@link Channel}。
 *      这确保了所有资源都是以适当的方式释放，即文件句柄。
 */
public interface Channel extends AttributeMap, ChannelOutboundInvoker, Comparable<Channel> {

    /**
     * 返回此{@link ChannelId}的全局唯一标识符。
     */
    ChannelId id();

    /**
     * 返回这个{@link Channel}注册到的{@link EventLoop}。
     */
    EventLoop eventLoop();

    /**
     * 返回此通道的父通道。
     *
     * @return the parent channel.
     *         {@code null} if this channel does not have a parent channel.
     */
    Channel parent();

    /**
     * 返回此通道的配置。
     */
    ChannelConfig config();

    /**
     * 返回{@code true}如果{@link Channel}是打开的，可能会在稍后激活
     */
    boolean isOpen();

    /**
     *
     * 如果{@link Channel}在{@link EventLoop}中注册，则返回{@code true}。
     */
    boolean isRegistered();

    /**
     * 如果{@link Channel}是活动的并且是连接的，则返回{@code true}。
     */
    boolean isActive();

    /**
     * 返回{@link Channel}的{@link ChannelMetadata}，它描述了{@link Channel}的性质。
     */
    ChannelMetadata metadata();

    /**
     * 返回此通道绑定到的本地地址。
     * 返回的{@link SocketAddress}应该被向下转换成更具体的形式类型如{@link InetSocketAddress}检索详细的信息。
     *
     * @return the local address of this channel.
     *         {@code null} if this channel is not bound.
     */
    SocketAddress localAddress();

    /**
     * 返回此通道连接到的远程地址。
     * *回的{@link SocketAddress}应该被向下转换为更多具体类型，如{@link InetSocketAddress}检索详细信息。
     *
     * @return the remote address of this channel.
     *         {@code null} if this channel is not connected.
     *         If this channel is not connected but it can receive messages
     *         from arbitrary remote addresses (e.g. {@link DatagramChannel},
     *         use {@link DatagramPacket#recipient()} to determine
     *         the origination of the received message as this method will
     *         return {@code null}.
     */
    SocketAddress remoteAddress();

    /**
     * 返回{@link ChannelFuture}，它将被通知通道关闭。
     * 这个方法总是返回相同的未来实例。
     */
    ChannelFuture closeFuture();

    /**
     * 当且仅当I/O线程将执行请求立即写入操作。
     * 任何写请求时该方法返回{@code false}排队，直到I/O线程是准备处理排队的写请求。
     */
    boolean isWritable();

    /**
     * 获取在{@link #isWritable()}返回{@code false}之前可以写入多少字节。
     * 这个量总是非负的。
     * 如果{@link #isWritable()}是{@code false}，则为0。
     */
    long bytesBeforeUnwritable();

    /**
     * 获取在{@link #isWritable()}返回{@code true}之前必须从底层缓冲区中抽取多少字节。
     * 这个量总是非负的。
     * 如果{@link #isWritable()}是{@code true}，则0。
     */
    long bytesBeforeWritable();

    /**
     * 返回一个提供不安全操作的<em>内部使用的</em>对象。
     */
    Unsafe unsafe();

    /**
     * 返回指定的{@link ChannelPipeline}。
     */
    ChannelPipeline pipeline();

    /**
     * 返回已分配的{@link ByteBufAllocator}，它将用于分配{@link ByteBuf}。
     */
    ByteBufAllocator alloc();

    @Override
    Channel read();

    @Override
    Channel flush();

    /**
     * 不安全的操作应该永远不要从用户代码被调用。
     * 这些方法只提供来实现实际的传输，并且必须从I/O线程调用以下方法:
     * <ul>
     *   <li>{@link #localAddress()}</li>
     *   <li>{@link #remoteAddress()}</li>
     *   <li>{@link #closeForcibly()}</li>
     *   <li>{@link #register(EventLoop, ChannelPromise)}</li>
     *   <li>{@link #deregister(ChannelPromise)}</li>
     *   <li>{@link #voidPromise()}</li>
     * </ul>
     */
    interface Unsafe {

        /**
         * 返回指定的{@link RecvByteBufAllocator.Handle}将用于分配{@link ByteBuf}的时候接收数据。
         */
        RecvByteBufAllocator.Handle recvBufAllocHandle();

        /**
         * Return the {@link SocketAddress} to which is bound local or
         * {@code null} if none.
         */
        SocketAddress localAddress();

        /**
         * Return the {@link SocketAddress} to which is bound remote or
         * {@code null} if none is bound yet.
         */
        SocketAddress remoteAddress();

        /**
         * 注册{@link ChannelPromise}的{@link Channel}并通知注册完成后，{@link ChannelFuture}。
         */
        void register(EventLoop eventLoop, ChannelPromise promise);

        /**
         * Bind the {@link SocketAddress} to the {@link Channel} of the {@link ChannelPromise} and notify
         * it once its done.
         */
        void bind(SocketAddress localAddress, ChannelPromise promise);

        /**
         * Connect the {@link Channel} of the given {@link ChannelFuture} with the given remote {@link SocketAddress}.
         * If a specific local {@link SocketAddress} should be used it need to be given as argument. Otherwise just
         * pass {@code null} to it.
         *
         * The {@link ChannelPromise} will get notified once the connect operation was complete.
         */
        void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise);

        /**
         * Disconnect the {@link Channel} of the {@link ChannelFuture} and notify the {@link ChannelPromise} once the
         * operation was complete.
         */
        void disconnect(ChannelPromise promise);

        /**
         * Close the {@link Channel} of the {@link ChannelPromise} and notify the {@link ChannelPromise} once the
         * operation was complete.
         */
        void close(ChannelPromise promise);

        /**
         * Closes the {@link Channel} immediately without firing any events.  Probably only useful
         * when registration attempt failed.
         */
        void closeForcibly();

        /**
         * Deregister the {@link Channel} of the {@link ChannelPromise} from {@link EventLoop} and notify the
         * {@link ChannelPromise} once the operation was complete.
         */
        void deregister(ChannelPromise promise);

        /**
         * Schedules a read operation that fills the inbound buffer of the first {@link ChannelInboundHandler} in the
         * {@link ChannelPipeline}.  If there's already a pending read operation, this method does nothing.
         */
        void beginRead();

        /**
         * Schedules a write operation.
         */
        void write(Object msg, ChannelPromise promise);

        /**
         * Flush out all write operations scheduled via {@link #write(Object, ChannelPromise)}.
         */
        void flush();

        /**
         * Return a special ChannelPromise which can be reused and passed to the operations in {@link Unsafe}.
         * It will never be notified of a success or error and so is only a placeholder for operations
         * that take a {@link ChannelPromise} as argument but for which you not want to get notified.
         */
        ChannelPromise voidPromise();

        /**
         * Returns the {@link ChannelOutboundBuffer} of the {@link Channel} where the pending write requests are stored.
         */
        ChannelOutboundBuffer outboundBuffer();
    }
}
