package com.example.clock.util

fun parseLong(`val`: String?): Long {
    return if (`val` == null || `val`.isEmpty()) 0L else `val`.toLong()
}