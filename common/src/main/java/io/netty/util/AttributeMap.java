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
package io.netty.util;

/**
 * 持有{@link Attribute}，可以通过{@link AttributeKey}访问。
 *
 * 实现必须是线程安全的。
 */
public interface AttributeMap {
    /**
     * 获取给定的{@link AttributeKey}的{@link Attribute}。
     * 这个方法永远不会返回null，但是可以返回一个尚未设置值的{@link Attribute}。
     */
    <T> Attribute<T> attr(AttributeKey<T> key);

    /**
     * 当且仅当给定的{@link Attribute}存在于这个{@link AttributeMap}中时返回{@code true}。
     */
    <T> boolean hasAttr(AttributeKey<T> key);
}
