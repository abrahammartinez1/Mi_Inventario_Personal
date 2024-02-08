package com.abraham.mi.inventario.miinventariopersonal

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abraham.mi.inventario.miinventariopersonal.ui.base.BaseActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.screens.AboutMeActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.screens.NewActiveActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.screens.SearchActiveActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.screens.StockActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.MiInventarioPersonalTheme

class MainActivity : BaseActivity() {
    @Composable
    override fun Content(paddingValues: PaddingValues, bundle: Bundle?) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonComponent(
                text = stringResource(R.string.main_activity_button_new),
                onClick = {
                    val intent = Intent(this@MainActivity, NewActiveActivity::class.java)
                    startActivity(intent)
                })
            Spacer(modifier = Modifier.height(8.dp))
            ButtonComponent(
                text = stringResource(R.string.main_activity_button_edit),
                onClick = {
                    val intent = Intent(this@MainActivity, SearchActiveActivity::class.java)
                    startActivity(intent)
                })
            Spacer(modifier = Modifier.height(8.dp))
            ButtonComponent(
                text = stringResource(R.string.main_activity_button_stock),
                onClick = {
                    val intent = Intent(this@MainActivity, StockActivity::class.java)
                    startActivity(intent)
                })
            Spacer(modifier = Modifier.height(8.dp))
            ButtonComponent(
                text = stringResource(R.string.main_activity_button_about),
                onClick = {
                    val intent = Intent(this@MainActivity, AboutMeActivity::class.java)
                    startActivity(intent)
                })
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMainActivity() {
        MiInventarioPersonalTheme {
            ViewContainer()
        }
    }
}