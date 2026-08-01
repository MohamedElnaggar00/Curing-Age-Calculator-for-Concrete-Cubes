import os
import re

files_to_update = [
    "app/src/main/java/com/example/ui/screens/ConcreteCalcScreen.kt",
    "app/src/main/java/com/example/ui/screens/DateDiffScreen.kt",
    "app/src/main/java/com/example/ui/screens/SavedBatchesScreen.kt",
    "app/src/main/java/com/example/MainActivity.kt",
    "app/src/main/java/com/example/ui/MainViewModel.kt",
    "app/src/main/java/com/example/util/DateUtils.kt"
]

replacements = {
    '"موافق"': 'stringResource(R.string.ok)',
    '"إلغاء"': 'stringResource(R.string.cancel)',
    '"عنوان المشروع (اختياري)"': 'stringResource(R.string.project_title_hint)',
    '"اختر تاريخ الصب"': 'stringResource(R.string.select_casting_date)',
    '"اليوم"': 'stringResource(R.string.today)',
    '"الأمس"': 'stringResource(R.string.yesterday)',
    '"فترة الاختبار"': 'stringResource(R.string.test_period)',
    'option.label': 'stringResource(option.labelResId)',
    '"تاريخ الكسر"': 'stringResource(R.string.breaking_date)',
    '"حفظ"': 'stringResource(R.string.save)',
    
    '"تاريخ البداية"': 'stringResource(R.string.start_date)',
    '"تاريخ النهاية"': 'stringResource(R.string.end_date)',
    '"الفرق بين التاريخين"': 'stringResource(R.string.date_diff_title)',
    '"$daysDiff يوم"': 'stringResource(R.string.diff_result, daysDiff)',
    
    '"حذف العينة"': 'stringResource(R.string.delete_batch_title)',
    '"هل أنت متأكد من حذف هذه العينة؟"': 'stringResource(R.string.delete_batch_msg)',
    '"حذف"': 'stringResource(R.string.delete)',
    '"بحث..."': 'stringResource(R.string.search_hint)',
    '"الكل"': 'stringResource(R.string.filter_all)',
    '"قادمة"': 'stringResource(R.string.filter_upcoming)',
    '"مكتملة"': 'stringResource(R.string.filter_completed)',
    '"كل العينات"': 'stringResource(R.string.all_batches)',
    '"لا يوجد عينات"': 'stringResource(R.string.no_batches)',
    '"عينة غير مسماة"': 'stringResource(R.string.unnamed_batch)',
    
    '"تاريخ الصب: ${DateUtils.formatArabicDate(LocalDate.ofEpochDay(batch.castingDateEpochDay))}"': 'stringResource(R.string.casting_date_prefix, DateUtils.formatArabicDate(LocalDate.ofEpochDay(batch.castingDateEpochDay)))',
    '"تاريخ الكسر: ${DateUtils.formatArabicDate(LocalDate.ofEpochDay(batch.castingDateEpochDay).plusDays(28))} (28 أيام)"': 'stringResource(R.string.breaking_date_prefix, DateUtils.formatArabicDate(LocalDate.ofEpochDay(batch.castingDateEpochDay).plusDays(28)), stringResource(R.string.days_28))',
    '"مشاركة بيانات العينة"': 'context.getString(R.string.share_chooser)',
    
    'val tabs = listOf("الصب", "الفروقات", "السجل")': 'val tabs = listOf(stringResource(R.string.tab_casting), stringResource(R.string.tab_diff), stringResource(R.string.tab_history))',
    
    '"تم حفظ العينة بنجاح في السجل! 💾"': 'getApplication<Application>().getString(R.string.batch_saved_success)',
    '"تم حذف العينة من السجل"': 'getApplication<Application>().getString(R.string.batch_deleted)',
    
    '"اليوم موعد الاختبار (الكسر)!"': 'context.getString(R.string.status_today)',
    '"متبقي $diff يوم علي الكسر"': 'context.getString(R.string.status_upcoming, diff)',
    '"تم الكسر منذ ${Math.abs(diff)} يوم"': 'context.getString(R.string.status_overdue, Math.abs(diff))'
}

for file_path in files_to_update:
    if not os.path.exists(file_path):
        continue
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    # Needs some manual changes for DateUtils.kt since it doesn't have Context and strings are passed from ViewModel usually, but let's see. 
    # Actually DateUtils doesn't have Context in its format methods. We'll modify DateUtils.kt manually if needed.
    
    if "com.example.ui.screens" in file_path or "MainActivity" in file_path:
        if "import androidx.compose.ui.res.stringResource" not in content:
            content = content.replace("import androidx.compose.runtime.Composable", "import androidx.compose.ui.res.stringResource\nimport com.example.R\nimport androidx.compose.runtime.Composable")
            
    for k, v in replacements.items():
        content = content.replace(k, v)
        
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)

