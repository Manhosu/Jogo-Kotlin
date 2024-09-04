package com.example.meujogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.meujogo.ui.theme.MeuJogoTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeuJogoTheme {
                JourneyScreen()
            }
        }
    }
}

@Composable
fun JourneyScreen() {
    // Estado para armazenar o número aleatório N
    var randomClicksNeeded by remember { mutableIntStateOf(Random.nextInt(1, 51)) }
    // Estado para armazenar a contagem de cliques
    var clickCount by remember { mutableIntStateOf(0) }
    // Estado para controlar a exibição do diálogo
    var showDialog by remember { mutableStateOf(false) }
    // Estado para controlar se é uma mensagem de desistência ou congratulações
    var isGiveUp by remember { mutableStateOf(false) }

    // Determina a imagem atual com base na contagem de cliques
    val currentImage: Painter = when {
        clickCount < (randomClicksNeeded * 0.33).toInt() -> painterResource(R.drawable.imagem_inicial)
        clickCount < (randomClicksNeeded * 0.66).toInt() -> painterResource(R.drawable.imagem_mediana)
        clickCount < randomClicksNeeded -> painterResource(R.drawable.imagem_final)
        else -> painterResource(R.drawable.imagem_conquista)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Imagem de fundo ajustada para preencher a tela inteira
        Image(
            painter = currentImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize() // Preenche toda a tela
        )

        // Botão redondo clicável ajustado
        Button(
            onClick = {
                if (clickCount < randomClicksNeeded) {
                    clickCount++
                } else {
                    showDialog = true
                    isGiveUp = false // Exibir diálogo de congratulações
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter) // Posiciona o botão mais para cima
                .padding(top = 32.dp) // Adiciona espaçamento superior menor
                .size(120.dp) // Tamanho ajustado do botão
                .background(Color.Red, CircleShape), // Cor de fundo vermelha e formato redondo
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(
                text = "Clique Aqui",
                color = Color.White, // Cor do texto
                fontSize = 16.sp, // Tamanho da fonte ajustado
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centraliza o texto
            )
        }
    }

    // Diálogo para novo jogo ou saída
    if (showDialog) {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { /* Nada acontece ao clicar fora do diálogo */ },
            title = {
                Text(text = if (isGiveUp) "Novo jogo?" else "Parabéns!")
            },
            text = {
                Text(text = if (isGiveUp) "Você desistiu. Deseja iniciar um novo jogo?" else "Você atingiu a conquista! Deseja jogar novamente?")
            },
            confirmButton = {
                Button(onClick = {
                    randomClicksNeeded = Random.nextInt(1, 51)
                    clickCount = 0
                    showDialog = false
                }) {
                    Text("Sim")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    // Finalizar a aplicação corretamente usando LocalContext.current
                    (context as? ComponentActivity)?.finish()
                }) {
                    Text("Não")
                }
            }
        )
    }
}
