package com.abraham.mi.inventario.miinventariopersonal.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.abraham.mi.inventario.miinventariopersonal.R
import com.abraham.mi.inventario.miinventariopersonal.data.BBDD.DatabaseCallback
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveTypeEntity
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.MiInventarioPersonalTheme
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.TextStyleButton

abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receivedBundle: Bundle? = intent.extras

        DatabaseSingleton.getTypesAsync(
            applicationContext,
            object : DatabaseCallback<List<ActiveTypeEntity>> {
                override fun onResult(result: List<ActiveTypeEntity>) {
                    /* Instance DataBase */
                }
            })

        setContent {
            MiInventarioPersonalTheme {
                ViewContainer(receivedBundle)
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    protected fun ViewContainer(receivedBundle: Bundle? = null) {
        Scaffold(
            topBar = { ToolbarComponent() },
            content = { paddingValues ->
                Content(paddingValues, receivedBundle)
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ToolbarComponent() {
        MiInventarioPersonalTheme {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .wrapContentSize(Alignment.Center),
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
            )
        }
    }

    @Composable
    abstract fun Content(paddingValues: PaddingValues, bundle: Bundle?)

    @Composable
    open fun ButtonComponent(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
        MiInventarioPersonalTheme {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .then(modifier),

                ) {
                Text(text.uppercase(), style = TextStyleButton)
            }
        }
    }

    @Composable
    open fun TransparentButtonComponent(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable RowScope.() -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            content()
        }
    }
}