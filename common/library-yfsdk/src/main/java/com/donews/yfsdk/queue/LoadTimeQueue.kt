package com.donews.yfsdk.queue

import com.tencent.mmkv.MMKV

class LoadTimeQueue(var mmkv: MMKV) {
    private var TAG = ""
    private var elements: LongArray = longArrayOf()
    private var length: Int = 20
    private var reported: Boolean = false

    init {
        read()
    }

    fun initSize(l: Int, tag: String) {
        length = l
        TAG = tag
        read()
    }

    fun reported() {
        reported = true
        mmkv.encode("LoadTimeQuenu${TAG}report", reported)
    }

    fun isReported(): Boolean {
        return reported
    }

    private fun read() {
        val els = mmkv.decodeString("LoadTimeQuenu$TAG")
        reported = mmkv.decodeBool("LoadTimeQuenu${TAG}report", false)
        val list: List<String> = els?.split(",") ?: return

        removeAll()

        for (s: String in list) {
            if (s.isEmpty()) {
                continue
            }
            put(s.toLong())
        }
    }

    private fun save() {
        var els = ""
        for (l: Long in elements) {
            els += l.toString()
            els += ","
        }
        mmkv.encode("LoadTimeQuenu$TAG", els)
        mmkv.encode("LoadTimeQuenu${TAG}report", reported)
    }

    fun getSize(): Int {
        return elements.size
    }

    fun getCapacity(): Int {
        return length
    }

    fun isFull(): Boolean {
        return length == elements.size
    }

    fun put(e: Long) {
        if (elements.size == length) {
            removeHead()
        }
        val newArr = LongArray(elements.size + 1)
        for (i in elements.indices) {
            newArr[i] = elements[i]
        }
        newArr[elements.size] = e
        elements = newArr

        save()
    }

    fun getHead(): Long {
        if (elements.isEmpty()) {
            return 0
        }

        return elements[0]
    }

    fun getLast(): Long {
        if (elements.isEmpty()) {
            return 0
        }

        return elements[elements.size - 1]
    }

    private fun removeHead() {
        if (elements.isEmpty()) {
            return
        }
        val newArr = LongArray(elements.size - 1)
        for (i in newArr.indices) {
            newArr[i] = elements[i + 1]
        }
        elements = newArr
    }

    fun removeAll() {
        while (elements.isNotEmpty()) {
            removeHead()
        }
        reported = false
        save()
        mmkv.encode("LoadTimeQuenu${TAG}report", reported)
    }

}