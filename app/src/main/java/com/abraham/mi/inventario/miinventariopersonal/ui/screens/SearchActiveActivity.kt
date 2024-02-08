package com.abraham.mi.inventario.miinventariopersonal.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abraham.mi.inventario.miinventariopersonal.R
import com.abraham.mi.inventario.miinventariopersonal.data.BBDD.DatabaseCallback
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveEntity
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveTypeEntity
import com.abraham.mi.inventario.miinventariopersonal.ui.base.BaseActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.base.DatabaseSingleton
import com.abraham.mi.inventario.miinventariopersonal.ui.screens.EditActiveActivity.Companion.KEY_ACTIVE_ID
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.MiInventarioPersonalTheme
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.TextStyleGlobal
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.TextStyleTitle

class SearchActiveActivity : BaseActivity() {
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

            val (queryValue, onQueryValueChanged) = remember { mutableStateOf<String?>(null) }
            SearchBarComponent(queryValue, onQueryValueChanged)

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

            val (minPriceValue, onMinPriceValueChanged) = remember { mutableStateOf<String?>(null) }
            val (maxPriceValue, onMaxPriceValueChanged) = remember { mutableStateOf<String?>(null) }
            PriceRangeRowComponent(
                minPriceValue,
                onMinPriceValueChanged,
                maxPriceValue,
                onMaxPriceValueChanged
            )

            val (activesValue, onActivesValueChanged) = remember {
                mutableStateOf<List<ActiveEntity>>(emptyList())
            }
            SearchButtonComponent(
                typeValue?.id,
                isFavoriteValue,
                onActivesValueChanged,
                queryValue,
                isActiveValue,
                minPriceValue,
                maxPriceValue,
            )
            ActiveListComponent(activesValue)
        }
    }

    @Composable
    fun TitleComponent() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.activity_search_active_title).uppercase(),
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PriceRangeRowComponent(
        minPriceValue: String?,
        onMinPriceValueChanged: (String?) -> Unit,
        maxPriceValue: String?,
        onMaxPriceValueChanged: (String?) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.range_price),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = minPriceValue.orEmpty(),
                onValueChange = {
                    onMinPriceValueChanged(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                placeholder = { Text(text = "0.0") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textStyle = TextStyle(fontSize = 16.sp)
            )

            Text(
                "-",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color.Black
            )

            TextField(
                value = maxPriceValue.orEmpty(),
                onValueChange = {
                    onMaxPriceValueChanged(it)
                },
                placeholder = { Text(text = "100.0") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textStyle = TextStyle(fontSize = 16.sp)
            )
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
    private fun SearchButtonComponent(
        typeId: Long?,
        isFavorite: Boolean,
        onActivesValueChanged: (List<ActiveEntity>) -> Unit,
        queryValue: String?,
        isActive: Boolean,
        minPriceValue: String?,
        maxPriceValue: String?,
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp, 8.dp),
            horizontalArrangement = Arrangement.Center,

            ) {
            ButtonComponent(
                text = stringResource(R.string.search_active),
                modifier = Modifier.wrapContentWidth()
            ) {
                DatabaseSingleton.getActivesAsync(
                    this@SearchActiveActivity,
                    object :
                        DatabaseCallback<List<ActiveEntity>> {
                        override fun onResult(result: List<ActiveEntity>) {
                            onActivesValueChanged(
                                filterActives(
                                    result,
                                    typeId,
                                    isFavorite,
                                    queryValue,
                                    isActive,
                                    minPriceValue,
                                    maxPriceValue,
                                )
                            )
                        }
                    })
            }
        }
    }

    private fun filterActives(
        actives: List<ActiveEntity>,
        typeId: Long?,
        favorite: Boolean,
        queryValue: String?,
        active: Boolean,
        minPriceValue: String?,
        maxPriceValue: String?
    ): List<ActiveEntity> {
        var result = actives.toList()

        result = result.filter { it.favorite == favorite }
        result = result.filter { it.active == active }

        typeId?.apply {
            result = result.filter { active -> active.typeId == this }
        }

        if (!minPriceValue.isNullOrEmpty() && minPriceValue.toDouble() > 0.0) {
            result = result.filter { it.price >= minPriceValue.toDouble() }
        }

        if (!maxPriceValue.isNullOrEmpty() && maxPriceValue.toDouble() > 0.0) {
            result = result.filter { it.price <= maxPriceValue.toDouble() }
        }

        if (!queryValue.isNullOrEmpty()) {
            result = result.filter { it.description.contains(queryValue, ignoreCase = true) }
        }

        return result
    }

    @Composable
    private fun ActiveListComponent(
        activeList: List<ActiveEntity>,
    ) {
        LazyColumn(
            modifier = Modifier.padding(8.dp),
        ) {
            items(activeList) { active ->
                Text(
                    text = active.description,
                    style = TextStyleGlobal,
                    modifier = Modifier
                        .padding(0.dp, 8.dp)
                        .clickable {
                            val intent =
                                Intent(this@SearchActiveActivity, EditActiveActivity::class.java)
                            intent.putExtra(KEY_ACTIVE_ID, active.id)
                            startActivity(intent)
                        }
                )

                Divider()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun SearchBarComponent(queryValue: String?, onQueryValueChanged: (String?) -> Unit) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                TextField(
                    value = queryValue.orEmpty(),
                    onValueChange = {
                        onQueryValueChanged(it)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    textStyle = TextStyleGlobal,
                    placeholder = {
                        Text(text = stringResource(R.string.search_hint))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                        }
                    )
                )
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