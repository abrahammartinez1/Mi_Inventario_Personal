package com.abraham.mi.inventario.miinventariopersonal.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abraham.mi.inventario.miinventariopersonal.R
import com.abraham.mi.inventario.miinventariopersonal.data.BBDD.DatabaseCallback
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveEntity
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveTypeEntity
import com.abraham.mi.inventario.miinventariopersonal.ui.base.BaseActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.base.DatabaseSingleton
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.MiInventarioPersonalTheme
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.TextStyleGlobal
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.TextStyleTitle


class StockActivity : BaseActivity() {
    @Composable
    override fun Content(paddingValues: PaddingValues, bundle: Bundle?) {
        var types by remember { mutableStateOf<List<ActiveTypeEntity>>(emptyList()) }
        val (typeValue, onTypeValueChanged) = remember { mutableStateOf<ActiveTypeEntity?>(null) }

        LaunchedEffect(types) {
            DatabaseSingleton.getTypesAsync(
                applicationContext,
                object : DatabaseCallback<List<ActiveTypeEntity>> {
                    override fun onResult(result: List<ActiveTypeEntity>) {
                        types = result
                        onTypeValueChanged(types.firstOrNull())
                    }
                })
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            TitleComponent()

            val (isFavoriteValue, onIsFavoriteValueChanged) = remember { mutableStateOf(false) }
            val (isActiveValue, onIsActiveValueChanged) = remember { mutableStateOf(true) }
            FilterComponent(
                types,
                typeValue,
                onTypeValueChanged,
                isFavoriteValue,
                onIsFavoriteValueChanged,
                isActiveValue,
                onIsActiveValueChanged
            )

            val (rateStockValue, onRateStockValueChanged) = remember { mutableStateOf<String?>(null) }
            ButtonShowRateComponent(
                typeValue?.id,
                isFavoriteValue,
                isActiveValue,
                onRateStockValueChanged
            )
            BorderedTextViewComponent(rateStockValue)
            ButtonOpenCalculatorComponent(stringResource(R.string.calculator_not_found))
        }
    }

    @Composable
    fun TitleComponent() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.activity_stock_title).uppercase(),
                style = TextStyleTitle,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    @Composable
    private fun FilterComponent(
        types: List<ActiveTypeEntity>,
        onTypeValue: ActiveTypeEntity?,
        onTypeValueChanged: (ActiveTypeEntity) -> Unit,
        favoriteValue: Boolean,
        onFavoriteValueChanged: (Boolean) -> Unit,
        activeValue: Boolean,
        onActiveValueChanged: (Boolean) -> Unit,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpinnerComponent(types, onTypeValue, onTypeValueChanged)
            Column {
                CheckboxWithText(
                    stringResource(R.string.favorite),
                    favoriteValue,
                    onFavoriteValueChanged
                )
                CheckboxWithText(
                    stringResource(R.string.active),
                    activeValue,
                    onActiveValueChanged
                )
            }
        }
    }

    @Composable
    fun SpinnerComponent(
        types: List<ActiveTypeEntity>,
        onTypeValue: ActiveTypeEntity?,
        onTypeValueChanged: (ActiveTypeEntity) -> Unit
    ) {
        Box {
            var expanded by remember { mutableStateOf(false) }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
            ) {
                types.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            onTypeValueChanged(item)
                            expanded = false
                        },
                        text = {
                            Text(text = item.name)
                        }
                    )
                }
            }

            TransparentButtonComponent(
                onClick = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                Text(text = onTypeValue?.name.orEmpty(), style = TextStyleGlobal)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }

    @Composable
    fun CheckboxWithText(
        text: String,
        checkedValue: Boolean,
        onCheckedValueChanged: (Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedValue,
                onCheckedChange = { newChecked ->
                    onCheckedValueChanged(newChecked)
                }
            )

            Text(
                text = text,
                style = TextStyleGlobal,
            )
        }
    }

    @Composable
    private fun ButtonShowRateComponent(
        typeIdValue: Long?,
        isFavoriteValue: Boolean,
        isActiveValue: Boolean,
        onRateStockValueChanged: (String?) -> Unit
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp, 8.dp),
            horizontalArrangement = Arrangement.Center,

            ) {
            ButtonComponent(
                text = stringResource(R.string.show_rate),
                modifier = Modifier.wrapContentWidth()
            ) {
                DatabaseSingleton.getActivesAsync(
                    this@StockActivity,
                    object :
                        DatabaseCallback<List<ActiveEntity>> {
                        override fun onResult(result: List<ActiveEntity>) {
                            onRateStockValueChanged(
                                filterActives(
                                    result,
                                    typeIdValue,
                                    isFavoriteValue,
                                    isActiveValue,
                                )
                            )
                        }
                    })
            }
        }
    }

    @Composable
    fun BorderedTextViewComponent(text: String? = null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(1.dp, Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text ?: stringResource(R.string.stock_default_value),
                    style = TextStyleGlobal
                )
            }
        }
    }

    private fun filterActives(
        actives: List<ActiveEntity>,
        typeId: Long?,
        favorite: Boolean,
        active: Boolean,
    ): String {
        var result = actives.toList()

        result = result.filter { it.favorite == favorite }
        result = result.filter { it.active == active }

        typeId?.apply {
            result = result.filter { active -> active.typeId == this }
        }


        return result.map { it.price }.sum().toString()
    }

    @Composable
    private fun ButtonOpenCalculatorComponent(textError: String) {
        Row(
            modifier = Modifier
                .padding(0.dp, 8.dp),
            horizontalArrangement = Arrangement.Center,

            ) {
            ButtonComponent(
                text = stringResource(R.string.open_calculator),
                modifier = Modifier.wrapContentWidth()
            ) {
                val intent = Intent()
                intent.setAction(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_APP_CALCULATOR)

                if (intent.resolveActivity(packageManager) == null) {
                    Toast.makeText(
                        this@StockActivity,
                        textError,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    startActivity(intent);
                }
            }
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