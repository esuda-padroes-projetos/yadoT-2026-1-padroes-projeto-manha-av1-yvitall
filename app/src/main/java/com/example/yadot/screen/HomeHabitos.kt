package com.example.yadot.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.yadot.Rotas
import com.example.yadot.ui.theme.Branco
import com.example.yadot.ui.theme.CinzaInativo
import com.example.yadot.ui.theme.FundoCinzaClaro
import com.example.yadot.ui.theme.Preto
import com.example.yadot.ui.theme.Transparente
import com.example.yadot.ui.theme.VerdeConcluido
import com.example.yadot.ui.theme.VermelhoErro
import com.example.yadot.viewmodel.HabitosViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SemHabitos(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HabitosViewModel
) {
    val habitosDoDia = viewModel.habitosCadastrados[viewModel.diaSelecionado] ?: emptyList()
    val progresso = viewModel.calcularProgressoDoDia()

    // ── Lógica: abre ranking automaticamente quando semana completa ──
    // Verifica se TODOS os 7 dias têm pelo menos 1 hábito concluído.
    // Quando isso acontece, navega para a tela de ranking automaticamente.
    // Isso simula o "ranking aparece no último dia da semana".
    LaunchedEffect(viewModel.habitosCadastrados) {
        val diasDaSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
        val semanaCompleta = diasDaSemana.all { dia ->
            val habitos = viewModel.habitosCadastrados[dia] ?: emptyList()
            habitos.isNotEmpty() && habitos.any { it.concluido }
        }
        if (semanaCompleta) {
            navController.navigate(Rotas.RANKING) {
                // Não empilha várias vezes — substitui se já estiver no ranking
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Branco)
            .padding(horizontal = 15.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título + botão editar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Week 1",
                style = TextStyle(fontSize = 50.sp, color = Preto, fontWeight = FontWeight.Bold)
            )
            if (habitosDoDia.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Preto)
                        .clickable { viewModel.alternarModoEdicao() }
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = if (viewModel.modoEdicao) Icons.Filled.Check else Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = Branco,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // ── Barra de dias com lógica do dia virar ────────────
        // Dias ANTERIORES a hoje → TelaDiaAnterior (somente leitura)
        // Hoje e futuros → fica nesta tela normalmente
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            BarraDeDias(
                diaAtual = viewModel.diaSelecionado,
                aoClicarNoDia = { diaClicado ->
                    val diasDaSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
                    val indexHoje = LocalDate.now().dayOfWeek.value - 1
                    val indexClicado = diasDaSemana.indexOf(diaClicado)

                    if (indexClicado < indexHoje) {
                        // Dia anterior → abre TelaDiaAnterior congelada
                        navController.navigate(Rotas.diaAnterior(diaClicado))
                    } else {
                        // Hoje ou futuro → troca dia nesta tela
                        viewModel.selecionarDia(diaClicado)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(3f))

        // Lista de hábitos ou estado vazio
        if (habitosDoDia.isEmpty()) {
            Column(
                modifier = Modifier.padding(top = 40.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nenhum hábito encontrado", style = TextStyle(fontSize = 25.sp, color = Preto, fontWeight = FontWeight.Bold))
                Text("Cadastre agora!", style = TextStyle(fontSize = 25.sp, color = Preto, fontWeight = FontWeight.Normal))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 50.dp, bottomEnd = 50.dp))
                        .clickable { viewModel.abrirModal() }
                        .background(Preto)
                        .padding(horizontal = 60.dp, vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", style = TextStyle(fontSize = 80.sp, color = Branco, fontWeight = FontWeight.Bold))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(18f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(habitosDoDia) { habito ->
                    val vetorIcone = viewModel.iconesDisponiveis
                        .firstOrNull { it.first == habito.icone }?.second

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Branco)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (viewModel.modoEdicao) {
                            Icon(
                                imageVector = Icons.Filled.RemoveCircle,
                                contentDescription = "Remover",
                                tint = VermelhoErro,
                                modifier = Modifier.size(26.dp)
                                    .clickable { viewModel.removerHabito(habito.id) }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }

                        if (vetorIcone != null) {
                            Icon(imageVector = vetorIcone, contentDescription = habito.nome, tint = Preto, modifier = Modifier.size(26.dp))
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = habito.nome,
                            style = TextStyle(fontSize = 18.sp, color = Preto, fontWeight = FontWeight.Medium),
                            modifier = Modifier.weight(1f)
                        )

                        if (!viewModel.modoEdicao) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (habito.concluido) VerdeConcluido else Transparente)
                                    .border(2.dp, if (habito.concluido) VerdeConcluido else CinzaInativo, CircleShape)
                                    .clickable { viewModel.alternarStatusDoHabito(habito.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (habito.concluido) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Concluído", tint = Branco, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(5f))

        if (viewModel.modoEdicao) {
            androidx.compose.material3.Button(
                onClick = { viewModel.abrirModal() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Preto)
            ) {
                Text(text = "Cadastrar Hábito", color = Branco, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        GraficoProgresso(porcentagem = progresso)

        Spacer(modifier = Modifier.weight(5f))

        Column(
            modifier = Modifier.fillMaxWidth().background(Branco),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "yadoT©", color = Preto, fontSize = 15.sp)
        }
    }

    if (viewModel.mostrarModal) {
        ModalAdicionarHabito(
            viewModel = viewModel,
            aoFechar = { viewModel.fecharModal() },
            aoSalvar = { nome, categoria, icone -> viewModel.adicionarHabito(nome, categoria, icone) }
        )
    }
}

// ════════════════════════════════════════════════════════════
//  BarraDeDias — compartilhada entre SemHabitos e DiaAnterior
// ════════════════════════════════════════════════════════════

@Composable
fun BarraDeDias(diaAtual: String, aoClicarNoDia: (String) -> Unit) {
    val diasDaSemana = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 1.dp)
    ) {
        items(diasDaSemana) { dia ->
            val corFundo = if (dia == diaAtual) Preto else CinzaInativo
            val corTexto = if (dia == diaAtual) Branco else Preto

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { aoClicarNoDia(dia) }
                    .background(corFundo)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = dia, style = TextStyle(fontSize = 20.sp, color = corTexto, fontWeight = FontWeight.Bold))
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
//  GraficoProgresso
// ════════════════════════════════════════════════════════════

@Composable
fun GraficoProgresso(porcentagem: Int) {
    val progressoAnimado by animateFloatAsState(
        targetValue = porcentagem.toFloat(),
        animationSpec = tween(durationMillis = 1000),
        label = "animacao_progresso"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Progresso Atual", style = TextStyle(fontSize = 28.sp, color = Preto, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(20.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val espessura = 24.dp.toPx()
                drawArc(color = FundoCinzaClaro, startAngle = 0f, sweepAngle = 360f, useCenter = false, style = Stroke(width = espessura))
                if (progressoAnimado > 0f) {
                    drawArc(color = VerdeConcluido, startAngle = -90f, sweepAngle = (progressoAnimado / 100f) * 360f, useCenter = false, style = Stroke(width = espessura, cap = StrokeCap.Round))
                }
            }
            Text(text = "$porcentagem%", style = TextStyle(fontSize = 20.sp, color = Preto, fontWeight = FontWeight.Bold))
        }
    }
}