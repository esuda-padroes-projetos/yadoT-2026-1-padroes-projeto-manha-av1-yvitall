package com.example.yadot.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Cadastrar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HabitosViewModel
) {
    var nome           by remember { mutableStateOf("") }
    var email          by remember { mutableStateOf("") }
    var senha          by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Branco)
            .padding(horizontal = 40.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.logoyadot),
            contentDescription = "Logo",
            modifier = Modifier.height(120.dp).size(80.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Cadastra-se",
            style = TextStyle(fontSize = 50.sp, color = Preto, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 7.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Digite seu Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Digite seu Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Digite sua Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            label = { Text("Confirme sua senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        if (uiState.erro != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = uiState.erro ?: "", color = Color.Red, fontSize = 13.sp)
        }

        Column(
            modifier = Modifier
                .background(Branco)
                .padding(horizontal = 40.dp, vertical = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (senha == confirmarSenha && nome.isNotBlank() && email.isNotBlank()) {
                        viewModel.cadastrar(nome, email, senha) {
                            navController.navigate(Rotas.HOME)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(1f).height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Preto),
                enabled = !uiState.carregando
            ) {
                if (uiState.carregando) {
                    CircularProgressIndicator(color = Branco, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Cadastrar", color = Branco, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(5f))

        Column(
            modifier = Modifier.fillMaxWidth().background(Branco),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "yadoT©", color = Preto, fontSize = 15.sp)
        }
    }
}

