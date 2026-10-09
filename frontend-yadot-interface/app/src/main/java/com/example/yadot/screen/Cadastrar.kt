package com.example.yadot.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.yadot.R
import com.example.yadot.Rotas
import com.example.yadot.ui.theme.Branco
import com.example.yadot.ui.theme.Preto
import com.example.yadot.viewmodel.HabitosViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation
import com.example.yadot.ui.theme.VermelhoErro

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Cadastrar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HabitosViewModel
) {
    var nome           by remember { mutableStateOf("") }
    var sobrenome      by remember { mutableStateOf("") }
    var email          by remember { mutableStateOf("") }
    var senha          by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var erroNome       by remember { mutableStateOf(false) }
    var erroSobrenome  by remember { mutableStateOf(false) }
    var erroEmail      by remember { mutableStateOf(false) }
    var erroSenha      by remember { mutableStateOf(false) }
    var erroSenhas     by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // 1. Column pai SEM scroll ocupando todo o ecrã
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Branco)
            .padding(horizontal = 40.dp, vertical = 32.dp)
    ) {
        
        // 2. Column filha COM scroll para o formulário.
        // O weight(1f) aqui faz com que ocupe todo o espaço disponível ACIMA do rodapé.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            
            // --- LOGO E TÍTULO ---
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Preto, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "ya.", color = Branco, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Cadastre-se",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Preto
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            // ---------------------

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it; erroNome = false },
                label = { Text("Digite seu Nome") },
                isError = erroNome,
                supportingText = { if (erroNome) Text("Nome é obrigatório", color = VermelhoErro) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = sobrenome,
                onValueChange = { sobrenome = it; erroSobrenome = false },
                label = { Text("Digite seu Sobrenome") },
                isError = erroSobrenome,
                supportingText = { if (erroSobrenome) Text("Sobrenome é obrigatório", color = VermelhoErro) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; erroEmail = false },
                label = { Text("Digite seu Email") },
                isError = erroEmail,
                supportingText = { if (erroEmail) Text("Email é obrigatório", color = VermelhoErro) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it; erroSenha = false; erroSenhas = false },
                label = { Text("Digite sua Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = erroSenha,
                supportingText = { if (erroSenha) Text("Senha é obrigatória", color = VermelhoErro) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it; erroSenhas = false },
                label = { Text("Confirme sua senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = erroSenhas,
                supportingText = { if (erroSenhas) Text("As senhas não conferem", color = VermelhoErro) }
            )

            if (uiState.erro != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = uiState.erro ?: "", color = VermelhoErro, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    erroNome = nome.isBlank()
                    erroSobrenome = sobrenome.isBlank()
                    erroEmail = email.isBlank()
                    erroSenha = senha.isBlank()
                    erroSenhas = senha != confirmarSenha

                    if (!erroNome && !erroSobrenome && !erroEmail && !erroSenha && !erroSenhas) {
                        viewModel.cadastrar(
                            nome = nome.trim(),
                            sobrenome = sobrenome.trim(),
                            email = email.trim(),
                            senha = senha,
                            onSucesso = { navController.navigate(Rotas.ENTRAR) },
                            onErro = { }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Preto),
                enabled = !uiState.carregando
            ) {
                if (uiState.carregando) {
                    CircularProgressIndicator(color = Branco, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Finalizar", color = Branco, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Tem conta? Realizar Login", color = Preto, fontSize = 14.sp)
            }
        }

        // 3. Rodapé fixo (não rola com o ecrã)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), 
            contentAlignment = Alignment.Center
        ) {
            Text(text = "yadoT©", color = Preto, fontSize = 15.sp)
        }
    }
}