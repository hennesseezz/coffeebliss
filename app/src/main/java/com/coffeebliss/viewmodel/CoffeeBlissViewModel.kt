package com.coffeebliss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.data.database.AppDatabase
import com.coffeebliss.data.entity.Member
import com.coffeebliss.data.entity.Transaction
import com.coffeebliss.data.repository.CoffeeBlissRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false
)

class CoffeeBlissViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = CoffeeBlissRepository(db.memberDao(), db.transactionDao())

    // ===== Members =====
    val allMembers: StateFlow<List<Member>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ===== Transactions =====
    val allTransactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ===== Selected Member =====
    private val _selectedMember = MutableStateFlow<Member?>(null)
    val selectedMember: StateFlow<Member?> = _selectedMember.asStateFlow()

    private val _memberTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val memberTransactions: StateFlow<List<Transaction>> = _memberTransactions.asStateFlow()

    // ===== UI State =====
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // ===== Rewards =====
    val rewards = repository.rewards

    // ==================== ACTIONS ====================

    fun registerMember(name: String, email: String, phone: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            try {
                // Validate inputs
                if (name.isBlank()) { onError("Nama tidak boleh kosong"); return@launch }
                if (email.isBlank()) { onError("Email tidak boleh kosong"); return@launch }
                if (phone.isBlank()) { onError("No HP tidak boleh kosong"); return@launch }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    onError("Format email tidak valid"); return@launch
                }

                // Check duplicate email
                val existing = repository.getMemberByEmail(email)
                if (existing != null) { onError("Email sudah terdaftar"); return@launch }

                val member = Member(name = name.trim(), email = email.trim().lowercase(), phone = phone.trim())
                repository.insertMember(member)
                _uiState.value = UiState(message = "Registrasi berhasil!")
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = UiState(isError = true, message = e.message)
                onError(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun selectMember(member: Member) {
        _selectedMember.value = member
        viewModelScope.launch {
            repository.getTransactionsByMember(member.id).collect { transactions ->
                _memberTransactions.value = transactions
            }
        }
    }

    fun refreshSelectedMember(memberId: Int) {
        viewModelScope.launch {
            val updated = repository.getMemberById(memberId)
            _selectedMember.value = updated
        }
    }

    fun addTransaction(
        memberId: Int,
        amount: Double,
        description: String = "",
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (amount <= 0) { onError("Nominal harus lebih dari 0"); return@launch }
                val transaction = repository.addTransaction(memberId, amount, description)
                refreshSelectedMember(memberId)
                onSuccess(transaction.pointEarned)
            } catch (e: Exception) {
                onError(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun redeemReward(
        member: Member,
        reward: CoffeeBlissRepository.Reward,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val success = repository.redeemReward(member, reward)
                if (success) {
                    refreshSelectedMember(member.id)
                    onSuccess()
                } else {
                    onError("Poin tidak cukup untuk redeem reward ini")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
