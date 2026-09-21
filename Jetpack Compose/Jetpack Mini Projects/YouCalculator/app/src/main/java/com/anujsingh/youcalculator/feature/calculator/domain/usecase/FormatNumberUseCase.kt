package com.anujsingh.youcalculator.feature.calculator.domain.usecase

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class FormatNumberUseCase(
    private val locale: Locale = Locale.getDefault()
) {
    constructor() : this(Locale.getDefault())

    private val symbols = DecimalFormatSymbols.getInstance(locale)
    val decimalSeparator: String = symbols.decimalSeparator.toString()
    val groupingSeparator: String = symbols.groupingSeparator.toString()

    fun format(numberString: String): String {
        if (numberString.isEmpty()) return ""
        if (numberString == "-") return "-"

        val clean = numberString.replace(groupingSeparator, "")
        val isNegative = clean.startsWith("-")
        val withoutSign = if (isNegative) clean.substring(1) else clean

        val hasDecimal = withoutSign.contains(decimalSeparator)
        val integerPart = if (hasDecimal) withoutSign.substringBefore(decimalSeparator) else withoutSign
        val fractionPart = if (hasDecimal) withoutSign.substringAfter(decimalSeparator) else ""

        val formattedInt = try {
            if (integerPart.isEmpty()) "0"
            else {
                val bd = BigDecimal(integerPart)
                val formatter = DecimalFormat("#,###", symbols)
                formatter.format(bd)
            }
        } catch (_: Exception) {
            integerPart
        }

        val sign = if (isNegative) "-" else ""
        return if (hasDecimal) {
            "$sign$formattedInt$decimalSeparator$fractionPart"
        } else {
            "$sign$formattedInt"
        }
    }

    fun formatResult(bigDecimal: BigDecimal): String {
        val symbols = DecimalFormatSymbols.getInstance(locale)
        val formatter = DecimalFormat("#,##0.##########", symbols)
        return formatter.format(bigDecimal)
    }

    fun cleanToParsable(formatted: String): String {
        return formatted
            .replace(groupingSeparator, "")
            .replace(decimalSeparator, ".")
    }
}
