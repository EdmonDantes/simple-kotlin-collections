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
 * [MutableListIterator] for class [LinkedList]
 * @see LinkedList
 */
public open class LinkedListIterator<E> public constructor(
    private var next: Node<E>?,
    private var nextIndex: Int,
    private var expectedModificationCount: Int,
    private var modificationCount: () -> Int,
    private var removeNodeFunc: (Node<E>) -> Unit,
    private var addNodeFunc: (Node<E>?, E) -> Node<E>,
) : MutableListIterator<E> {

    private var prev: Node<E>? = null
    private var lastRet: Node<E>? = null

    override fun hasNext(): Boolean = next != null

    override fun hasPrevious(): Boolean = prev != null

    override fun next(): E {
        checkForComodification()
        if (!hasNext()) throw NoSuchElementException()

        lastRet = next
        prev = next
        next = next?.next
        nextIndex++

        return lastRet!!.value
    }

    override fun nextIndex(): Int = nextIndex

    override fun previous(): E {
        checkForComodification()
        if (!hasPrevious()) throw NoSuchElementException()

        lastRet = prev
        next = prev
        prev = prev?.prev
        nextIndex--
        return lastRet!!.value
    }

    override fun previousIndex(): Int = nextIndex - 1

    override fun add(element: E) {
        checkForComodification()
        checkNotNull(lastRet)

        lastRet = null
        prev = addNodeFunc(next, element)
        nextIndex++
        expectedModificationCount++
    }

    override fun remove() {
        checkForComodification()
        checkNotNull(lastRet)
        if (lastRet === prev) {
            prev = lastRet?.prev
        } else if (lastRet === next) {
            next = lastRet?.next
        } else {
            nextIndex--
        }
        removeNodeFunc(lastRet!!)
        lastRet = null
        expectedModificationCount++
    }

    override fun set(element: E) {
        checkForComodification()
        checkNotNull(lastRet)
        lastRet!!.value = element
    }

    private fun checkForComodification() {
        if (modificationCount() != expectedModificationCount) {
            throw ConcurrentModificationException()
//        }
        }
    }
}
