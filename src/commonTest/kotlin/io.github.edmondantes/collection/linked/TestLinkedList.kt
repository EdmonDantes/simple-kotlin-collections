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
package io.github.edmondantes.collection.linked

import io.github.edmondantes.collection.LinkedList
import io.github.edmondantes.collection.asLinkedList
import io.github.edmondantes.collection.linkedListOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestLinkedList {

    @Test
    fun testEmptyLinkedList() {
        val list = LinkedList<String>()

        assertEquals(0, list.size)
    }

    @Test
    fun testCreateLinkedListBySequence() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_2, list[2])
    }

    @Test
    fun testCreateLinkedListByList() {
        val list = LinkedList(OBJECTS)

        assertEquals(3, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_2, list[2])
    }

    @Test
    fun testCreateLinkedListByIterator() {
        val list = LinkedList(OBJECTS.iterator())

        assertEquals(3, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_2, list[2])
    }

    @Test
    fun testAddFirst() {
        val list = OBJECTS.asLinkedList()

        assertTrue(list.addFirst(OBJ_3))

        assertEquals(4, list.size)
        assertEquals(OBJ_3, list[0])
        assertEquals(OBJ_0, list[1])
        assertEquals(OBJ_1, list[2])
        assertEquals(OBJ_2, list[3])
    }

    @Test
    fun testAddLast() {
        val list = OBJECTS.asLinkedList()

        assertTrue(list.addLast(OBJ_3))

        assertEquals(4, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_2, list[2])
        assertEquals(OBJ_3, list[3])
    }

    @Test
    fun testRemoveFirst() {
        val list = OBJECTS.asLinkedList()

        assertTrue(list.removeFirst())

        assertEquals(2, list.size)
        assertEquals(OBJ_1, list[0])
        assertEquals(OBJ_2, list[1])
    }

    @Test
    fun testRemoveLast() {
        val list = OBJECTS.asLinkedList()

        assertTrue(list.removeLast())

        assertEquals(2, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
    }

    @Test
    fun testSetFirst() {
        val list = LinkedList(OBJECTS.iterator())

        assertEquals(OBJ_0, list.setFirst(OBJ_3))

        assertEquals(3, list.size)
        assertEquals(OBJ_3, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_2, list[2])
    }

    @Test
    fun testSetLast() {
        val list = LinkedList(OBJECTS.iterator())

        assertEquals(OBJ_2, list.setLast(OBJ_3))

        assertEquals(3, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_3, list[2])
    }

    @Test
    fun testClear() {
        val list = LinkedList(OBJECTS.iterator())

        list.clear()

        assertEquals(0, list.size)
    }

    @Test
    fun testAddAll() {
        val list = LinkedList(OBJECTS.iterator())

        assertTrue(list.addAll(OBJECTS))

        assertEquals(6, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_2, list[2])
        assertEquals(OBJ_0, list[3])
        assertEquals(OBJ_1, list[4])
        assertEquals(OBJ_2, list[5])
    }

    @Test
    fun testAddAllWithIndex() {
        val list = LinkedList(OBJECTS.iterator())

        assertTrue(list.addAll(1, OBJECTS))

        assertEquals(6, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_0, list[1])
        assertEquals(OBJ_1, list[2])
        assertEquals(OBJ_2, list[3])
        assertEquals(OBJ_1, list[4])
        assertEquals(OBJ_2, list[5])
    }

    @Test
    fun testAdd() {
        val list = LinkedList(OBJECTS.iterator())

        assertTrue(list.add(OBJ_3))

        assertEquals(4, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_1, list[1])
        assertEquals(OBJ_2, list[2])
        assertEquals(OBJ_3, list[3])
    }

    @Test
    fun testAddWithIndex() {
        val list = LinkedList(OBJECTS.iterator())

        list.add(1, OBJ_3)

        assertEquals(4, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_3, list[1])
        assertEquals(OBJ_1, list[2])
        assertEquals(OBJ_2, list[3])
    }

    @Test
    fun testIsEmpty() {
        val list = LinkedList(OBJECTS.iterator())

        assertFalse(list.isEmpty())
        assertTrue(LinkedList<String>().isEmpty())
    }

    @Test
    fun testIterator() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        val iterator = list.iterator()
        assertTrue(iterator.hasNext(), "Can not get first element by iterator. hasNext returned false")
        assertEquals(OBJ_0, iterator.next())
        assertTrue(iterator.hasNext(), "Can not get second element by iterator. hasNext returned false")
        assertEquals(OBJ_1, iterator.next())
        iterator.remove()
        assertTrue(iterator.hasNext(), "Can not get third element by iterator. hasNext returned false")
        assertEquals(OBJ_2, iterator.next())

        assertFalse(iterator.hasNext())
        assertEquals(2, list.size)
    }

    @Test
    fun testConcurrentModification() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        val iterator = list.iterator()
        iterator.next()

        list.add(OBJ_3)

        assertFailsWith<ConcurrentModificationException> {
            iterator.next()
        }
    }

    @Test
    fun testListIterator() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        val iterator = list.listIterator()

        assertTrue(iterator.hasNext())
        assertEquals(0, iterator.nextIndex())
        assertEquals(OBJ_0, iterator.next())
        iterator.set(OBJ_1)
        assertEquals(1, iterator.nextIndex())
        iterator.add(OBJ_3)
        assertEquals(2, iterator.nextIndex())
        assertTrue(iterator.hasNext())
        assertEquals(OBJ_1, iterator.next())
        assertEquals(3, iterator.nextIndex())
        iterator.remove()
        assertEquals(3, iterator.nextIndex())
        assertTrue(iterator.hasNext())
        assertEquals(OBJ_2, iterator.next())
        assertEquals(4, iterator.nextIndex())
        iterator.add(OBJ_4)
        assertEquals(5, iterator.nextIndex())
        assertTrue(iterator.hasPrevious())
        assertEquals(4, iterator.previousIndex())
        assertEquals(OBJ_4, iterator.previous())
        assertEquals(3, iterator.previousIndex())
        assertTrue(iterator.hasPrevious())
        assertEquals(OBJ_2, iterator.previous())

        assertEquals(4, list.size)
        assertEquals(OBJ_1, list[0])
        assertEquals(OBJ_3, list[1])
        assertEquals(OBJ_2, list[2])
        assertEquals(OBJ_4, list[3])
    }

    @Test
    fun testListIteratorWithIndex() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        val iterator = list.listIterator(1)

        assertTrue(iterator.hasNext())
        assertEquals(OBJ_1, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(OBJ_2, iterator.next())
    }

    @Test
    fun testRemoveAt() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        assertEquals(OBJ_1, list.removeAt(1))

        assertEquals(2, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_2, list[1])
    }

    @Test
    fun testSubList() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        val subList = list.subList(1, 3)

        assertEquals(2, subList.size)
        assertEquals(OBJ_1, subList[0])
        assertEquals(OBJ_2, subList[1])
    }

    @Test
    fun testSet() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        list[1] = OBJ_3

        assertEquals(3, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_3, list[1])
        assertEquals(OBJ_2, list[2])
    }

    @Test
    fun testRetainAll() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        assertTrue(list.retainAll(listOf(OBJ_0, OBJ_2)))

        assertEquals(2, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_2, list[1])
    }

    @Test
    fun testRemoveAll() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        assertTrue(list.removeAll(listOf(OBJ_0, OBJ_2)))

        assertEquals(1, list.size)
        assertEquals(OBJ_1, list[0])
    }

    @Test
    fun testRemove() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        assertTrue(list.remove(OBJ_1))

        assertEquals(2, list.size)
        assertEquals(OBJ_0, list[0])
        assertEquals(OBJ_2, list[1])
    }

    @Test
    fun testLastIndexOf() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2, OBJ_1)

        assertEquals(4, list.size)
        assertEquals(3, list.lastIndexOf(OBJ_1))
    }

    @Test
    fun testIndexOf() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2, OBJ_1)

        assertEquals(4, list.size)
        assertEquals(1, list.indexOf(OBJ_1))
    }

    @Test
    fun testContainsAll() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        assertTrue(list.containsAll(listOf(OBJ_1, OBJ_2)))
        assertFalse(list.containsAll(listOf(OBJ_1, OBJ_3)))
    }

    @Test
    fun testContains() {
        val list = linkedListOf(OBJ_0, OBJ_1, OBJ_2)

        assertEquals(3, list.size)

        assertTrue(list.contains(OBJ_2))
        assertFalse(list.contains(OBJ_3))
    }

    private companion object {
        const val OBJ_0 = "obj0"
        const val OBJ_1 = "obj1"
        const val OBJ_2 = "obj2"
        const val OBJ_3 = "obj3"
        const val OBJ_4 = "obj4"
        val OBJECTS = listOf(OBJ_0, OBJ_1, OBJ_2)
    }
}
