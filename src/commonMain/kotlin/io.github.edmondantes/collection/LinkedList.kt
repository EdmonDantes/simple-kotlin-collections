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
package io.github.edmondantes.collection

import io.github.edmondantes.collection.linked.list.LinkedListIterator
import io.github.edmondantes.collection.linked.list.Node
import io.github.edmondantes.collection.linked.list.addToNext
import io.github.edmondantes.collection.linked.list.addToPrev
import io.github.edmondantes.collection.linked.list.forEachNextInclude
import io.github.edmondantes.collection.linked.list.forEachNextIndexedIncluded
import io.github.edmondantes.collection.linked.list.forEachPrevIndexedIncluded
import io.github.edmondantes.collection.linked.list.getNext
import io.github.edmondantes.collection.linked.list.getPrev
import io.github.edmondantes.collection.linked.list.remove

/**
 * Implementation linked list
 *
 * You can use it like queue, dequeue or stack
 *
 * _**WARNING!!!**_ This class it **NOT** thread-safe
 */
public open class LinkedList<E> : AbstractMutableList<E> {

    protected var first: Node<E>? = null
    protected var last: Node<E>? = null
    protected var _size: Int = 0
    protected var modificationCount: Int = 0

    override val size: Int
        get() = _size

    public constructor() {
        first = null
        last = null
        _size = 0
    }

    public constructor(iterable: Iterable<E>) : this(iterable.iterator())

    public constructor(iterator: Iterator<E>) {
        var i = 0
        var node =
            if (iterator.hasNext()) {
                i++
                Node(iterator.next())
            } else {
                null
            }

        first = node

        while (iterator.hasNext()) {
            node = node?.addToNext(iterator.next())
            i++
        }

        last = node
        _size = i
    }

    public constructor(size: Int, initializer: (Int) -> E) {
        var node = Node(initializer(0))

        first = node

        for (i in 1 until size) {
            node = node.addToNext(initializer(i))
        }

        last = node
        this._size = size
    }

    protected constructor(first: Node<E>?, last: Node<E>?) {
        if (first == null || last == null) {
            this.first = null
            this.last = null
            _size = 0
            return
        }

        var node: Node<E> = first
        while (node !== last && node.next != null) {
            addNode(null, node.value)
            node = node.next!!
        }
        addNode(null, node.value)
    }

    /**
     * Add [element] to the start of list
     *
     * @return True if additional was successful
     */
    public fun addFirst(element: E): Boolean {
        add(0, element)
        return true
    }

    /**
     * Add [element] to the end of list
     *
     * @return True if additional was successful
     */
    public fun addLast(element: E): Boolean {
        add(size, element)
        return true
    }

    /**
     * Remove first element
     *
     * @return True if removing was successful
     */
    public fun removeFirst(): Boolean {
        removeAt(0)
        return true
    }

    /**
     * Remove last element
     *
     * @return True if removing was successful
     */
    public fun removeLast(): Boolean {
        removeAt(size - 1)
        return true
    }

    /**
     * Replace first element
     *
     * @return True if replacing was successful
     */
    public fun setFirst(element: E): E =
        set(0, element)

    /**
     * Replace last element
     *
     * @return True if replacing was successful
     */
    public fun setLast(element: E): E =
        set(size - 1, element)

    override fun clear() {
        first = null
        last = null
        _size = 0
    }

    override fun addAll(elements: Collection<E>): Boolean {
        elements.forEach(::addLast)
        return true
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        checkElementIndex(index, size)

        elements.forEachIndexed { i, element ->
            add(index + i, element)
        }

        return true
    }

    override fun add(index: Int, element: E) {
        checkPositionIndex(index, size)

        addNode(if (index == size) null else getNode(index), element)
        modificationCount++
    }

    override fun add(element: E): Boolean =
        addLast(element)

    override fun get(index: Int): E {
        checkElementIndex(index, size)
        return first!!.getNext(index).value
    }

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): MutableIterator<E> =
        listIterator()

    override fun listIterator(): MutableListIterator<E> =
        listIterator(0)

    override fun listIterator(index: Int): MutableListIterator<E> {
        checkPositionIndex(index, size)
        val node = if (index == size) null else first!!.getNext(index)

        return LinkedListIterator(node, index, modificationCount, ::modificationCount::get, ::removeNode, ::addNode)
    }

    override fun removeAt(index: Int): E {
        checkElementIndex(index, size)

        val node = getNode(index)
        removeNode(node)

        return node.value
    }

    override fun subList(fromIndex: Int, toIndex: Int): LinkedList<E> {
        checkRangeIndexes(fromIndex, toIndex, size)

        val fromNode = first!!.getNext(fromIndex)
        val toNode = fromNode.getNext(toIndex - fromIndex - 1)

        return LinkedList(fromNode, toNode)
    }

    override fun set(index: Int, element: E): E {
        checkElementIndex(index, size)

        val node = getNode(index)
        val prev = node.value
        node.value = element
        return prev
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        var result = false
        first?.forEachNextInclude {
            if (!elements.contains(it.value)) {
                removeNode(it)
                result = true
            }
        }

        return result
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var result = false
        elements.forEach { result = result or remove(it) }
        return result
    }

    override fun remove(element: E): Boolean {
        var result = false
        first?.forEachNextInclude {
            if (it.value == element) {
                removeNode(it)
                result = true
            }
        }

        return result
    }

    override fun lastIndexOf(element: E): Int {
        last?.forEachPrevIndexedIncluded { index, node ->
            if (node.value == element) {
                return size - index - 1
            }
        }

        return -1
    }

    override fun indexOf(element: E): Int {
        first?.forEachNextIndexedIncluded { index, node ->
            if (node.value == element) {
                return index
            }
        }

        return -1
    }

    override fun containsAll(elements: Collection<E>): Boolean =
        elements.all { contains(it) }

    override fun contains(element: E): Boolean =
        indexOf(element) > -1

    protected fun getNode(index: Int): Node<E> {
        checkElementIndex(index, size)
        return if (index < size / 2) {
            first!!.getNext(index)
        } else {
            last!!.getPrev(size - index - 1)
        }
    }

    protected fun removeNode(node: Node<E>) {
        node.remove {
            when {
                prev == null -> first = next
                next == null -> last = prev
            }
        }
        _size--
        modificationCount++
    }

    protected fun addNode(node: Node<E>?, element: E): Node<E> {
        _size++
        modificationCount++
        return if (node == null) {
            if (last == null) {
                last = Node(element)
                first = last
            } else {
                last = last?.addToNext(element)
            }
            last!!
        } else {
            node.addToPrev(element).apply {
                when {
                    prev == null -> first = this
                    next == null -> last = this
                }
            }
        }
    }

    internal companion object {
        internal fun checkElementIndex(index: Int, size: Int) {
            if (index < 0 || index >= size) {
                throw IndexOutOfBoundsException("index: $index, size: $size")
            }
        }

        internal fun checkPositionIndex(index: Int, size: Int) {
            if (index < 0 || index > size) {
                throw IndexOutOfBoundsException("index: $index, size: $size")
            }
        }

        internal fun checkRangeIndexes(fromIndex: Int, toIndex: Int, size: Int) {
            if (fromIndex < 0 || toIndex > size) {
                throw IndexOutOfBoundsException("fromIndex: $fromIndex, toIndex: $toIndex, size: $size")
            }
            if (fromIndex > toIndex) {
                throw IllegalArgumentException("fromIndex: $fromIndex > toIndex: $toIndex")
            }
        }
    }
}

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun <T> Array<out T>.asLinkedList(): LinkedList<T> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun ByteArray.asLinkedList(): LinkedList<Byte> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun ShortArray.asLinkedList(): LinkedList<Short> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun IntArray.asLinkedList(): LinkedList<Int> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun LongArray.asLinkedList(): LinkedList<Long> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun FloatArray.asLinkedList(): LinkedList<Float> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun DoubleArray.asLinkedList(): LinkedList<Double> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun BooleanArray.asLinkedList(): LinkedList<Boolean> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original array.
 */
public fun CharArray.asLinkedList(): LinkedList<Char> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps the original list.
 */
public fun <T> List<T>.asLinkedList(): LinkedList<T> = LinkedList(size, ::get)

/**
 * Returns a [LinkedList] that wraps [elements]
 */
public fun <T> linkedListOf(vararg elements: T): LinkedList<T> =
    if (elements.isNotEmpty()) elements.asLinkedList() else linkedListOf()

/**
 * Returns an empty [LinkedList].
 */
public fun <T> linkedListOf(): LinkedList<T> = LinkedList()
