package com.example.restopass.common

internal inline fun Any?.orElse(f: ()-> Unit): Any?{
    if (this == null){
        f()
    }
    return this
}