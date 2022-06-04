package com.example.clock.util

fun String?.parseLong(): Long {
    return if (this == null || this.isEmpty()) 0L else this.toLong()
}

fun String?.parseInt(): Int {
    return if (this == null || this.isEmpty()) 0 else this.toInt()
}

