package com.example.yadot.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yadot.model.Habito
import com.example.yadot.network.CheckinRequest
import com.example.yadot.network.RetrofitClient
import com.example.yadot.network.UsuarioCadastroRequest
import com.example.yadot.network.UsuarioLoginRequest
import com.example.yadot.network.UsuarioResponse
import com.example.yadot.network.ProgressoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

// ════════════════════════════════════════════════════════════
//  STRATEGY PATTERN
// ════════════════════════════════════════════════════════════

interface EstrategiaMsg {
    fun gerar(progresso: Int): String
}

class EstrategiaMsgPadrao : EstrategiaMsg {
    override fun gerar(progresso: Int) = when {
        progresso == 100 -> "Dia perfeito! Você arrasou! 🔥"
        progresso >= 80  -> "Dia super produtivo! O ritmo está excelente."
        progresso >= 50  -> "Bom progresso! Mais da metade concluída."
        progresso > 0    -> "Começou bem, amanhã vai melhor!"
        else             -> "Nenhuma tarefa registrada neste dia."
    }
}

class EstrategiaMsgIngles : EstrategiaMsg {
    override fun gerar(progresso: Int) = when {
        progresso == 100 -> "Perfect day! You crushed it! 🔥"
        progresso >= 80  -> "Super productive day! Keep the momentum."
        progresso >= 50  -> "Good progress! More than half done."
        progresso > 0    -> "Good start, tomorrow will be better!"
        else             -> "No tasks recorded for this day."
    }
}

// ════════════════════════════════════════════════════════════
//  ESTADO DA UI — Observer via StateFlow
// ════════════════════════════════════════════════════════════

data class RankingItem(
    val nome: String,
    val percentual: Int,
    val ofensivaDias: Int
)

data class HabitosUiState(
    val carregando: Boolean = false,
    val erro: String? = null,
    val usuarioLogado: UsuarioResponse? = null,
    val progressoHoje: ProgressoResponse? = null,
    val ranking: List<RankingItem> = emptyList()
)

@RequiresApi(Build.VERSION_CODES.O)
class HabitosViewModel(
    private val estrategiaMsg: EstrategiaMsg = EstrategiaMsgPadrao()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitosUiState())
    val uiState: StateFlow<HabitosUiState> = _uiState

    val diasDaSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
    private val indexDeHoje = LocalDate.now().dayOfWeek.value - 1

    var diaSelecionado by mutableStateOf(diasDaSemana[indexDeHoje])
        private set

    var habitosCadastrados by mutableStateOf(mapOf<String, List<Habito>>())
        private set

    var mostrarModal by mutableStateOf(false)
        private set

    var modoEdicao by mutableStateOf(false)
        private set

    val categorias = listOf(
        "Educação", "Saúde", "Trabalho", "Estudos",
        "Responsabilidades", "Finanças", "Casa", "Lazer"
    )

    val iconesDisponiveis: List<Pair<String, ImageVector>> = listOf(
        "Star"            to Icons.Filled.Star,
        "AttachMoney"     to Icons.Filled.AttachMoney,
        "FitnessCenter"   to Icons.Filled.FitnessCenter,
        "Book"            to Icons.Filled.Book,
        "Home"            to Icons.Filled.Home,
        "Work"            to Icons.Filled.Work,
        "ShoppingCart"    to Icons.Filled.ShoppingCart,
        "Favorite"        to Icons.Filled.Favorite,
        "School"          to Icons.Filled.School,
        "Code"            to Icons.Filled.Code,
        "MusicNote"       to Icons.Filled.MusicNote,
        "Restaurant"      to Icons.Filled.Restaurant,
        "Bedtime"         to Icons.Filled.Bedtime,
        "SelfImprovement" to Icons.Filled.SelfImprovement,
        "Brush"           to Icons.Filled.Brush,
        "Pets"            to Icons.Filled.Pets,
    )

    // Funções locais

    fun selecionarDia(diaClicado: String) { diaSelecionado = diaClicado }
    fun abrirModal()         { mostrarModal = true  }
    fun fecharModal()        { mostrarModal = false }
    fun alternarModoEdicao() { modoEdicao = !modoEdicao }

    fun adicionarHabito(nomeDigitado: String, categoria: String, icone: String) {
        val novoHabito = Habito(
            id = (0..1000).random(),
            nome = nomeDigitado,
            concluido = false,
            categoria = categoria,
            icone = icone
        )
        val lista = habitosCadastrados[diaSelecionado] ?: emptyList()
        habitosCadastrados = habitosCadastrados + (diaSelecionado to (lista + novoHabito))
        fecharModal()
    }

    fun alternarStatusDoHabito(idDoHabito: Int) {
        val lista = habitosCadastrados[diaSelecionado] ?: return
        val novaLista = lista.map { h ->
            if (h.id == idDoHabito) h.copy(concluido = !h.concluido) else h
        }
        habitosCadastrados = habitosCadastrados + (diaSelecionado to novaLista)
    }

    fun removerHabito(idDoHabito: Int) {
        val lista = habitosCadastrados[diaSelecionado] ?: return
        habitosCadastrados = habitosCadastrados + (diaSelecionado to lista.filter { it.id != idDoHabito })
    }

    // Cálculos locais

    fun calcularProgressoDoDia(): Int {
        val habitos = habitosCadastrados[diaSelecionado] ?: return 0
        if (habitos.isEmpty()) return 0
        return (habitos.count { it.concluido } * 100) / habitos.size
    }

    fun calcularProgressoDoDiaPorNome(dia: String): Int {
        val habitos = habitosCadastrados[dia] ?: return 0
        if (habitos.isEmpty()) return 0
        return (habitos.count { it.concluido } * 100) / habitos.size
    }

    fun calcularMensagemMotivacional(dia: String): String {
        return estrategiaMsg.gerar(calcularProgressoDoDiaPorNome(dia))
    }

    fun calcularOfensivaAteDia(diaAlvo: String): Int {
        val indexAlvo = diasDaSemana.indexOf(diaAlvo)
        if (indexAlvo < 0) return 0
        var ofensiva = 0
        for (i in (indexAlvo - 1) downTo 0) {
            val dia = diasDaSemana[i]
            val habitos = habitosCadastrados[dia] ?: emptyList()
            if (habitos.isEmpty()) break
            if (calcularProgressoDoDiaPorNome(dia) == 100) ofensiva++ else break
        }
        val habitosAlvo = habitosCadastrados[diaAlvo] ?: emptyList()
        if (habitosAlvo.isNotEmpty() && calcularProgressoDoDiaPorNome(diaAlvo) == 100) ofensiva++
        return ofensiva
    }

    fun calcularOfensiva(): Int = calcularOfensivaAteDia(diasDaSemana[indexDeHoje])

    // Chamadas à API

    /** POST /usuarios/login */
    fun login(email: String, senha: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            try {
                val usuario = RetrofitClient.api.login(UsuarioLoginRequest(email, senhaHash = senha))
                _uiState.value = _uiState.value.copy(usuarioLogado = usuario)
                onSucesso()
            } catch (e: Exception) { }
        }
    }

    fun cadastrar(nome: String, email: String, senha: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            try {
                val usuario = RetrofitClient.api.cadastrarUsuario(
                    UsuarioCadastroRequest(nome = nome, sobrenome = "", email = email, senhaHash = senha)
                )
                _uiState.value = _uiState.value.copy(usuarioLogado = usuario)
                onSucesso()
            } catch (e: Exception) { /* tratar erro */ }
        }
    }

    /** GET /checkins/progresso/{id} */
    fun carregarProgressoDeHoje() {
        val userId = _uiState.value.usuarioLogado?.id ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(carregando = true)
            try {
                val progresso = RetrofitClient.api.progressoDoDia(userId)
                _uiState.value = _uiState.value.copy(carregando = false, progressoHoje = progresso)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    carregando = false,
                    erro = "Erro ao carregar progresso: ${e.message}"
                )
            }
        }
    }


    /** GET /usuarios + GET /checkins/progresso/{id} para cada um */
    fun carregarRanking() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(carregando = true)
            try {
                val usuarios = RetrofitClient.api.listarUsuarios()
                val itens = usuarios.mapNotNull { usuario ->
                    try {
                        val prog = RetrofitClient.api.progressoDoDia(usuario.id)
                        // Cálculo manual já que o DTO dele mudou:
                        val percentual = if (prog.total > 0) ((prog.concluidos.toDouble() / prog.total) * 100).toInt() else 0

                        // Como o DTO dele não tem mais ofensivaDias, vamos usar 0 ou o total de concluidos por enquanto
                        RankingItem(usuario.nome, percentual, prog.concluidos.toInt())
                    } catch (e: Exception) { null }
                }.sortedByDescending { it.percentual } // Ordena pelo percentual
                _uiState.value = _uiState.value.copy(carregando = false, ranking = itens)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(carregando = false, erro = "Erro no ranking: ${e.message}")
            }
        }
    }

    /** POST /checkins */
    /** POST /checkins */
    fun realizarCheckin(habitoId: Long) {
        viewModelScope.launch {
            try {
                val dataHoje = LocalDate.now().toString()
                RetrofitClient.api.realizarCheckin(CheckinRequest(habitoId, dataHoje))
                // Recarrega os dados após marcar como feito
                carregarProgressoDeHoje()
            } catch (e: Exception) { /* erro */ }
        }
    }

    // No seu ViewModel, garanta que a verificação de "dia editável" considere o hoje real
    fun ehDiaEditavel(dia: String): Boolean {
        val hojeNome = LocalDate.now().dayOfWeek.value.let {
            // Converte o valor do dia da semana para o seu padrão (Seg, Ter...)
            diasDaSemana[it - 1]
        }
        return dia == hojeNome // Permite editar se o dia selecionado for o nome do dia de hoje
    }

}