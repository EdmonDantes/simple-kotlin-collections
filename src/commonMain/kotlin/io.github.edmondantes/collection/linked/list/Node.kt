/*
 * Copyright (c) 2023. Ilia Loginov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.edmondantes.collection.linked.list

import io.github.edmondantes.collection.LinkedList

/**
 * Node for class [LinkedList]
 * @see LinkedList
 */
public class Node<E>(
    public var value: E,
    public var prev: Node<E>?,
    public var next: Node<E>?,
) {
    public constructor(value: E) : this(value, null, null)
}

public fun <E> Node<E>.getNext(index: Int): Node<E> {
    var i = 0
    var node: Node<E> = this
    while (i < index && node.next != null) {
        node = node.next!!
        i++
    }

    if (i < index) {
        throw IndexOutOfBoundsException("index: $index, size: $i")
    }

    return node
}

public fun <E> Node<E>.getPrev(index: Int): Node<E> {
    var i = 0
    var node: Node<E> = this
    while (i < index && node.prev != null) {
        node = node.prev!!
        i++
    }

    if (i < index) {
        throw IndexOutOfBoundsException("index: $index, size: $i")
    }

    return node
}

public inline fun <E> Node<E>.forEachPrev(action: (Node<E>) -> Unit) {
    var node = this
    while (node.prev != null) {
        action(node.prev as Node<E>)
        node = node.prev as Node<E>
    }
}

public inline fun <E> Node<E>.forEachPrevInclude(action: (Node<E>) -> Unit) {
    var node: Node<E>? = this
    while (node != null) {
        action(node)
        node = node.prev
    }
}

public inline fun <E> Node<E>.forEachNext(action: (Node<E>) -> Unit) {
    var node = this
    while (node.next != null) {
        action(node.next as Node<E>)
        node = node.next as Node<E>
    }
}

public inline fun <E> Node<E>.forEachNextInclude(action: (Node<E>) -> Unit) {
    var node: Node<E>? = this
    while (node != null) {
        action(node)
        node = node.next
    }
}

public inline fun <E> Node<E>.forEachPrevIndexed(action: (Int, Node<E>) -> Unit) {
    var node = this
    var index = 1
    while (node.prev != null) {
        action(index++, node.prev as Node<E>)
        node = node.prev as Node<E>
    }
}

public inline fun <E> Node<E>.forEachPrevIndexedIncluded(action: (Int, Node<E>) -> Unit) {
    var node: Node<E>? = this
    var index = 0
    while (node != null) {
        action(index++, node)
        node = node.prev
    }
}

public inline fun <E> Node<E>.forEachNextIndexed(action: (Int, Node<E>) -> Unit) {
    var node = this
    var index = 1
    while (node.next != null) {
        action(index++, node.next as Node<E>)
        node = node.next as Node<E>
    }
}

public inline fun <E> Node<E>.forEachNextIndexedIncluded(action: (Int, Node<E>) -> Unit) {
    var node: Node<E>? = this
    var index = 0
    while (node != null) {
        action(index++, node)
        node = node.next
    }
}

public inline fun <E> Node<E>.remove(afterDelete: Node<E>.() -> Unit = {}) {
    prev?.next = next
    next?.prev = prev
    afterDelete()
    prev = null
    next = null
}

public fun <E> Node<E>.addToNext(element: E): Node<E> = Node(element).also { node ->
    node.prev = this
    node.next = next

    next?.prev = node
    next = node

    return node
}

public fun <E> Node<E>.addToPrev(element: E): Node<E> = Node(element).also { node ->
    node.next = this
    node.prev = prev

    prev?.next = node
    prev = node

    return node
}
