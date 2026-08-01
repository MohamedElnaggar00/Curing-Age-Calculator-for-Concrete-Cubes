package com.example.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {

    fun formatArabicDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault()))
    }

    fun formatShortDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.getDefault()))
    }

    data class DateDiffResult(
        val totalDays: Long,
        val weeks: Long,
        val remainingDaysInWeek: Long,
        val months: Long,
        val remainingDaysInMonth: Long,
        val isFuture: Boolean
    )

    fun calculateDifference(startDate: LocalDate, endDate: LocalDate): DateDiffResult {
        val totalDays = ChronoUnit.DAYS.between(startDate, endDate)
        val absDays = Math.abs(totalDays)

        val weeks = absDays / 7
        val remDaysWeek = absDays % 7

        val months = ChronoUnit.MONTHS.between(startDate, endDate)
        val absMonths = Math.abs(months)
        val datePlusMonths = if (totalDays >= 0) startDate.plusMonths(absMonths) else startDate.minusMonths(absMonths)
        val remDaysMonth = Math.abs(ChronoUnit.DAYS.between(datePlusMonths, endDate))

        return DateDiffResult(
            totalDays = totalDays,
            weeks = weeks,
            remainingDaysInWeek = remDaysWeek,
            months = absMonths,
            remainingDaysInMonth = remDaysMonth,
            isFuture = totalDays >= 0
        )
    }
}
