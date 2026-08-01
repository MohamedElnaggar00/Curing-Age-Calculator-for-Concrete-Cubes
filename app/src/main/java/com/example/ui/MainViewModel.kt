package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ConcreteBatch
import com.example.data.ConcreteBatchRepository
import com.example.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

import com.example.R

enum class ConcreteOption(val days: Long, val labelResId: Int) {
    SEVEN_DAYS(7L, R.string.days_7),
    FOURTEEN_DAYS(14L, R.string.days_14),
    TWENTY_EIGHT_DAYS(28L, R.string.days_28),
    FIFTY_SIX_DAYS(56L, R.string.days_56)
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ConcreteBatchRepository

    init {
        val dao = AppDatabase.getDatabase(application).concreteBatchDao()
        repository = ConcreteBatchRepository(dao)
    }

    // Navigation Tab Index (0: Concrete Calc, 1: Date Diff Calc, 2: Saved Batches)
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    // --- Concrete Casting Mode State ---
    private val _castingDate = MutableStateFlow<LocalDate?>(null)
    val castingDate: StateFlow<LocalDate?> = _castingDate.asStateFlow()

    private val _selectedOption = MutableStateFlow(ConcreteOption.TWENTY_EIGHT_DAYS)
    val selectedOption: StateFlow<ConcreteOption> = _selectedOption.asStateFlow()

    private val _customDaysInput = MutableStateFlow("14")
    val customDaysInput: StateFlow<String> = _customDaysInput.asStateFlow()

    // Dialog & Form Inputs for Saving Batch
    private val _showSaveDialog = MutableStateFlow(false)
    val showSaveDialog: StateFlow<Boolean> = _showSaveDialog.asStateFlow()

    private val _projectName = MutableStateFlow("")
    val projectName: StateFlow<String> = _projectName.asStateFlow()

    private val _elementName = MutableStateFlow("")
    val elementName: StateFlow<String> = _elementName.asStateFlow()

    private val _concreteGrade = MutableStateFlow("C30")
    val concreteGrade: StateFlow<String> = _concreteGrade.asStateFlow()

    private val _cubeCount = MutableStateFlow("6")
    val cubeCount: StateFlow<String> = _cubeCount.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun setCastingDate(date: LocalDate?) {
        _castingDate.value = date
    }

    fun setSelectedOption(option: ConcreteOption) {
        _selectedOption.value = option
    }

    fun setCustomDaysInput(input: String) {
        if (input.isEmpty() || input.all { it.isDigit() }) {
            _customDaysInput.value = input
        }
    }

    fun openSaveDialog() {
        _showSaveDialog.value = true
    }

    fun dismissSaveDialog() {
        _showSaveDialog.value = false
    }

    fun setProjectName(value: String) { _projectName.value = value }
    fun setElementName(value: String) { _elementName.value = value }
    fun setConcreteGrade(value: String) { _concreteGrade.value = value }
    fun setCubeCount(value: String) { _cubeCount.value = value }
    fun setNotes(value: String) { _notes.value = value }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun saveBatch() {
        val proj = _projectName.value
        val elem = _elementName.value
        val count = _cubeCount.value.toIntOrNull() ?: 6

        val newBatch = ConcreteBatch(
            projectName = proj,
            elementName = elem,
            castingDateEpochDay = _castingDate.value?.toEpochDay() ?: LocalDate.now().toEpochDay(),
            concreteGrade = _concreteGrade.value.ifBlank { "C30" },
            cubeCount = count,
            notes = _notes.value
        )

        viewModelScope.launch {
            repository.insert(newBatch)
            _showSaveDialog.value = false
            _snackbarMessage.value = getApplication<Application>().getString(R.string.batch_saved_success)
            // Reset fields
            _projectName.value = ""
            _elementName.value = ""
            _notes.value = ""
        }
    }

    // --- Date Difference Calculator State ---
    private val _diffStartDate = MutableStateFlow(LocalDate.now())
    val diffStartDate: StateFlow<LocalDate> = _diffStartDate.asStateFlow()

    private val _diffEndDate = MutableStateFlow(LocalDate.now().plusDays(28))
    val diffEndDate: StateFlow<LocalDate> = _diffEndDate.asStateFlow()

    fun setDiffStartDate(date: LocalDate) {
        _diffStartDate.value = date
    }

    fun setDiffEndDate(date: LocalDate) {
        _diffEndDate.value = date
    }

    fun setPresetPeriod(days: Long) {
        val start = _diffStartDate.value
        _diffEndDate.value = start.plusDays(days)
    }

    // --- Saved Batches State & Search/Filter ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterStatus = MutableStateFlow("ALL") // ALL, UPCOMING, COMPLETED
    val filterStatus: StateFlow<String> = _filterStatus.asStateFlow()

    val batches: StateFlow<List<ConcreteBatch>> = combine(
        repository.allBatches,
        _searchQuery,
        _filterStatus
    ) { allBatches, query, filter ->
        val today = LocalDate.now()
        allBatches.filter { batch ->
            val matchesQuery = batch.projectName.contains(query, ignoreCase = true) ||
                    batch.elementName.contains(query, ignoreCase = true) ||
                    batch.concreteGrade.contains(query, ignoreCase = true)

            val castingLocalDate = LocalDate.ofEpochDay(batch.castingDateEpochDay)
            val day28 = castingLocalDate.plusDays(28)

            val matchesFilter = when (filter) {
                "UPCOMING" -> day28.isAfter(today) || day28.isEqual(today)
                "COMPLETED" -> day28.isBefore(today)
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterStatus(status: String) {
        _filterStatus.value = status
    }

    fun deleteBatch(batch: ConcreteBatch) {
        viewModelScope.launch {
            repository.delete(batch)
            _snackbarMessage.value = getApplication<Application>().getString(R.string.batch_deleted)
        }
    }
}
