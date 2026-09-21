package com.anujsingh.calculator.core.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = emptyArray()
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is StringResource) return false
            return id == other.id && args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            return 31 * id + args.contentHashCode()
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }
}