package com.example.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {

    private val ARABIC_LOCALE = Locale("ar")

    fun getArabicDayName(date: LocalDate): String {
        return when (date.dayOfWeek) {
            DayOfWeek.SATURDAY -> "السبت"
            DayOfWeek.SUNDAY -> "الأحد"
            DayOfWeek.MONDAY -> "الاثنين"
            DayOfWeek.TUESDAY -> "الثلاثاء"
            DayOfWeek.WEDNESDAY -> "الأربعاء"
            DayOfWeek.THURSDAY -> "الخميس"
            DayOfWeek.FRIDAY -> "الجمعة"
            else -> date.dayOfWeek.getDisplayName(TextStyle.FULL, ARABIC_LOCALE)
        }
    }

    fun formatArabicDate(date: LocalDate): String {
        val dayName = getArabicDayName(date)
        val dayOfMonth = date.dayOfMonth
        val monthName = getArabicMonthName(date.monthValue)
        val year = date.year
        return "$dayName، $dayOfMonth $monthName $year"
    }

    fun formatShortDate(date: LocalDate): String {
        return "${date.year}/${date.monthValue}/${date.dayOfMonth}"
    }

    fun getArabicMonthName(month: Int): String {
        return when (month) {
            1 -> "يناير"
            2 -> "فبراير"
            3 -> "مارس"
            4 -> "أبريل"
            5 -> "مايو"
            6 -> "يونيو"
            7 -> "يوليو"
            8 -> "أغسطس"
            9 -> "سبتمبر"
            10 -> "أكتوبر"
            11 -> "نوفمبر"
            12 -> "ديسمبر"
            else -> ""
        }
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

    data class TestBreakStatus(
        val targetDate: LocalDate,
        val daysUntilTest: Long,
        val statusText: String,
        val statusType: StatusType
    )

    enum class StatusType {
        TODAY,
        UPCOMING,
        OVERDUE
    }

    fun getTestBreakStatus(castingDate: LocalDate, daysOffset: Long, today: LocalDate = LocalDate.now()): TestBreakStatus {
        val targetDate = castingDate.plusDays(daysOffset)
        val diff = ChronoUnit.DAYS.between(today, targetDate)

        val (statusText, statusType) = when {
            diff == 0L -> "اليوم موعد الاختبار (الكسر)!" to StatusType.TODAY
            diff > 0L -> "متبقي $diff يوم علي الكسر" to StatusType.UPCOMING
            else -> "تم الكسر منذ ${Math.abs(diff)} يوم" to StatusType.OVERDUE
        }

        return TestBreakStatus(
            targetDate = targetDate,
            daysUntilTest = diff,
            statusText = statusText,
            statusType = statusType
        )
    }
}
