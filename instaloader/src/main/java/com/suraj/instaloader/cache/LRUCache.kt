/*
*
* LRU cacheing alogorithm for saving key value pairs
* Refer LRU cache https://www.geeksforgeeks.org/lru-cache-implementation/
*
*
*
* */


package com.suraj.instaloader.cache

import java.util.LinkedHashMap

/*Created by suraj on 25/01/2020*/


class LRUCacheJson(private val capacity: Int) {

    private val map = hashMapOf<String, Node>()
    private val head: Node = Node("", null)
    private val tail: Node = Node("", null)

    init {
        head.next = tail
        tail.prev = head
    }

    fun get(key: String): String? {
        if (map.containsKey(key)) {
            val node = map[key]!!
            remove(node)
            addAtEnd(node)
            return node.value
        }
        return null
    }

    fun put(key: String, value: String?) {
        if (map.containsKey(key)) {
            remove(map[key]!!)
        }
        val node = Node(key, value)
        addAtEnd(node)
        map[key] = node
        if (map.size > capacity) {
            val first = head.next!!
            remove(first)
            map.remove(first.key)
        }
    }

    fun clearCache(){
        map.clear()
    }

    private fun remove(node: Node) {
        val next = node.next!!
        val prev = node.prev!!
        prev.next = next
        next.prev = prev
    }

    private fun addAtEnd(node: Node) {
        val prev = tail.prev!!
        prev.next = node
        node.prev = prev
        node.next = tail
        tail.prev = node
    }



    data class Node(val key: String?, val value: String?) {
        var next: Node? = null
        var prev: Node? = null
    }
}