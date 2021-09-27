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

/**
 * 字节缓冲区的抽象-基本的数据结构, 表示低级二进制和文本消息。
 *
 * Netty使用自己的缓冲区API而不是NIO {@link java.nio.ByteBuffer}表示一个字节序列。
 * 这种方法具有显著的优势,使用{@link java.nio.ByteBuffer}的新缓冲类型，{@link io.netty.buffer.ByteBuf}，
 * 已经从地面设计up来解决{@link java.nio.ByteBuffer}和满足网络应用开发者的日常需求。
 * 以下是一些很酷的功能:
 * <ul>
 *   <li>如果需要，可以定义缓冲区类型.</li>
 *   <li>透明零拷贝是通过内置的复合缓冲区类型实现的。</li>
 *   <li>提供了一种开箱即用的动态缓冲区类型，其容量为按需扩展，就像{@link java.lang.StringBuffer}.</li>
 *   <li>没有必要再调用{@code flip()}方法了.</li>
 *   <li>它通常比{@link java.nio.ByteBuffer}快。.</li>
 * </ul>
 *
 * <h3>可扩展性</h3>
 *
 * {@link io.netty.buffer.ByteBuf}有丰富的操作集为快速协议实现而优化。
 * 例如, {@link io.netty.buffer.ByteBuf}提供各种操作用于访问无符号值和字符串并搜索特定字节缓冲区中的序列。
 * 您还可以扩展或包装现有的缓冲区类型添加方便的访问器。
 * 自定义缓冲区类型仍然实现{@link io.netty.buffer.ByteBuf}接口而不是引入不兼容的类型。
 *
 *
 * <h3>透明的零拷贝</h3>
 * 为了将网络应用程序的性能提高到极致，您需要减少内存复制操作的次数。
 * 你可能有一套缓冲区可以被切片和组合成一个完整的消息。
 * 网状的提供一个复合缓冲区，它允许您从任意数量的现有缓冲区，没有内存拷贝。
 * 例如,一个信息可由两部分组成;头和身体。
 * 在一个模块化的应用时，这两部分可以由不同的模块和稍后在发送消息时组装。
 * <pre>
 * +--------+----------+
 * | header |   body   |
 * +--------+----------+
 * </pre>
 * 如果使用了{@link java.nio.ByteBuffer}，则必须创建一个新的大缓冲区并复制这两部分到新的缓冲区。
 * 或者,您可以在NIO中执行收集写操作，但它限制了表示缓冲区的组合为{@link java.nio.ByteBuffer}年代,
 * 而不是单个缓冲区，打破了抽象并引入了复杂性状态管理。
 * 此外，如果你不打算阅读或从NIO通道写入。
 * <pre>
 *
 * // The composite type is incompatible with the component type.
 * ByteBuffer[] message = new ByteBuffer[] { header, body };
 * </pre>
 * By contrast, {@link io.netty.buffer.ByteBuf} does not have such
 * caveats because it is fully extensible and has a built-in composite buffer
 * type.
 * <pre>
 * // The composite type is compatible with the component type.
 * {@link io.netty.buffer.ByteBuf} message = {@link io.netty.buffer.Unpooled}.wrappedBuffer(header, body);
 *
 * // Therefore, you can even create a composite by mixing a composite and an
 * // ordinary buffer.
 * {@link io.netty.buffer.ByteBuf} messageWithFooter = {@link io.netty.buffer.Unpooled}.wrappedBuffer(message, footer);
 *
 * // Because the composite is still a {@link io.netty.buffer.ByteBuf}, you can access its content
 * // easily, and the accessor method will behave just like it's a single buffer
 * // even if the region you want to access spans over multiple components.  The
 * // unsigned integer being read here is located across body and footer.
 * messageWithFooter.getUnsignedInt(
 *     messageWithFooter.readableBytes() - footer.readableBytes() - 1);
 * </pre>
 *
 * <h3>自动扩展能力</h3>
 *
 * 许多协议定义了可变长度的消息，这意味着没有办法确定消息的长度，直到您构造消息或它是精确计算长度困难且不便。
 * 这只是就像当你构建一个{@link java.lang.String}。
 * 你经常估计长度的结果字符串，并让{@link java.lang.StringBuffer}扩展本身需求。
 * <pre>
 * // A new dynamic buffer is created.  Internally, the actual buffer is created
 * // lazily to avoid potentially wasted memory space.
 * {@link io.netty.buffer.ByteBuf} b = {@link io.netty.buffer.Unpooled}.buffer(4);
 *
 * // When the first write attempt is made, the internal buffer is created with
 * // the specified initial capacity (4).
 * b.writeByte('1');
 *
 * b.writeByte('2');
 * b.writeByte('3');
 * b.writeByte('4');
 *
 * // When the number of written bytes exceeds the initial capacity (4), the
 * // internal buffer is reallocated automatically with a larger capacity.
 * b.writeByte('5');
 * </pre>
 *
 * <h3>更好的性能</h3>
 *
 * 最常用的缓冲区实现{@link io.netty.buffer.ByteBuf}是一个非常薄的包装字节数组(即{@code byte[]})。
 * 与{@link nio。ByteBuffer},它没有复杂的边界检查和指标补偿，因此是JVM更容易优化缓冲区访问。
 * 更复杂的缓冲实现只用于切片或复合缓冲区，它执行以及{@link java.nio.ByteBuffer}。
 */
package io.netty.buffer;
