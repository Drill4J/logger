package com.epam.drill.logger.internal

import io.ktor.utils.io.streams.*
import platform.posix.*
import kotlin.native.concurrent.*

internal actual val platformName: String = Platform.osFamily.name

@SharedImmutable
internal val _fileDescriptor = AtomicReference<Int?>(null)

internal fun output(block: (Appendable) -> Unit) {
    _fileDescriptor.value?.let {
        Output(STDOUT_FILENO).flush()
        val out = Output(it)
        block(out)
        out.flush()
    } ?: block(StdOut)
}

private object StdOut : Appendable {
    override fun append(value: Char): Appendable = apply { print(value) }

    override fun append(value: CharSequence?): Appendable = apply { print(value) }

    override fun append(value: CharSequence?, startIndex: Int, endIndex: Int): Appendable = apply {
        print(value?.subSequence(startIndex, endIndex))
    }
}
