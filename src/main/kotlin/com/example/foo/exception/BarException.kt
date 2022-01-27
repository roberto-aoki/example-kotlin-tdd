package com.example.foo.exception

class BarException(cause: Throwable) : RuntimeException(cause.message)
