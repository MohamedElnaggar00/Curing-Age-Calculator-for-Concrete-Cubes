import os
import re

# English strings (default)
strings_en = """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Concrete Tracker</string>
    <string name="tab_casting">Casting</string>
    <string name="tab_diff">Differences</string>
    <string name="tab_history">History</string>
    <string name="batch_saved_success">Batch saved successfully! 💾</string>
    <string name="batch_deleted">Batch deleted</string>
    <string name="today">Today</string>
    <string name="yesterday">Yesterday</string>
    <string name="date_diff_title">Difference</string>
    <string name="delete_batch_title">Delete Batch</string>
    <string name="delete_batch_msg">Are you sure you want to delete this batch?</string>
    <string name="filter_all">All</string>
    <string name="filter_upcoming">Upcoming</string>
    <string name="filter_completed">Completed</string>
    <string name="all_batches">All Batches</string>
    <string name="no_batches">No batches</string>
    <string name="search_hint">Search...</string>
    <string name="casting_date_prefix">Casting Date: %1$s</string>
    <string name="breaking_date_prefix">Breaking Date: %1$s (%2$s)</string>
    <string name="days">days</string>
    <string name="day">day</string>
    <string name="unnamed_batch">Unnamed batch</string>
    <string name="share_title">Concrete Batch Data\n-----------------</string>
    <string name="share_project">Project: %1$s</string>
    <string name="share_casting">Casting Date: %1$s</string>
    <string name="share_breaking">Breaking Date: %1$s</string>
    <string name="share_chooser">Share batch data</string>
    <string name="project_title_hint">Project Title (Optional)</string>
    <string name="select_casting_date">Select Casting Date</string>
    <string name="test_period">Test Period</string>
    <string name="breaking_date">Breaking Date</string>
    <string name="cancel">Cancel</string>
    <string name="save">Save</string>
    <string name="ok">OK</string>
    <string name="delete">Delete</string>
    <string name="start_date">Start Date</string>
    <string name="end_date">End Date</string>
    <string name="status_today">Today is the testing day!</string>
    <string name="status_upcoming">%1$d days remaining</string>
    <string name="status_overdue">Tested %1$d days ago</string>
    <string name="days_7">7 Days</string>
    <string name="days_14">14 Days</string>
    <string name="days_28">28 Days</string>
    <string name="days_56">56 Days</string>
    <string name="diff_result">%1$d days</string>
</resources>
"""

# Arabic strings
strings_ar = """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">حاسبة الخرسانة</string>
    <string name="tab_casting">الصب</string>
    <string name="tab_diff">الفروقات</string>
    <string name="tab_history">السجل</string>
    <string name="batch_saved_success">تم حفظ العينة بنجاح في السجل! 💾</string>
    <string name="batch_deleted">تم حذف العينة من السجل</string>
    <string name="today">اليوم</string>
    <string name="yesterday">الأمس</string>
    <string name="date_diff_title">الفرق بين التاريخين</string>
    <string name="delete_batch_title">حذف العينة</string>
    <string name="delete_batch_msg">هل أنت متأكد من حذف هذه العينة؟</string>
    <string name="filter_all">الكل</string>
    <string name="filter_upcoming">قادمة</string>
    <string name="filter_completed">مكتملة</string>
    <string name="all_batches">كل العينات</string>
    <string name="no_batches">لا يوجد عينات</string>
    <string name="search_hint">بحث...</string>
    <string name="casting_date_prefix">تاريخ الصب: %1$s</string>
    <string name="breaking_date_prefix">تاريخ الكسر: %1$s (%2$s)</string>
    <string name="days">أيام</string>
    <string name="day">يوم</string>
    <string name="unnamed_batch">عينة غير مسماة</string>
    <string name="share_title">بيانات عينة الصب الخرساني\n-----------------</string>
    <string name="share_project">المشروع: %1$s</string>
    <string name="share_casting">تاريخ الصب: %1$s</string>
    <string name="share_breaking">تاريخ الكسر: %1$s</string>
    <string name="share_chooser">مشاركة بيانات العينة</string>
    <string name="project_title_hint">عنوان المشروع (اختياري)</string>
    <string name="select_casting_date">اختر تاريخ الصب</string>
    <string name="test_period">فترة الاختبار</string>
    <string name="breaking_date">تاريخ الكسر</string>
    <string name="cancel">إلغاء</string>
    <string name="save">حفظ</string>
    <string name="ok">موافق</string>
    <string name="delete">حذف</string>
    <string name="start_date">تاريخ البداية</string>
    <string name="end_date">تاريخ النهاية</string>
    <string name="status_today">اليوم موعد الاختبار (الكسر)!</string>
    <string name="status_upcoming">متبقي %1$d يوم علي الكسر</string>
    <string name="status_overdue">تم الكسر منذ %1$d يوم</string>
    <string name="days_7">7 أيام</string>
    <string name="days_14">14 يوم</string>
    <string name="days_28">28 يوم</string>
    <string name="days_56">56 يوم</string>
    <string name="diff_result">%1$d يوم</string>
</resources>
"""

os.makedirs("app/src/main/res/values", exist_ok=True)
with open("app/src/main/res/values/strings.xml", "w", encoding="utf-8") as f:
    f.write(strings_en)

os.makedirs("app/src/main/res/values-ar", exist_ok=True)
with open("app/src/main/res/values-ar/strings.xml", "w", encoding="utf-8") as f:
    f.write(strings_ar)

print("Strings created successfully.")
