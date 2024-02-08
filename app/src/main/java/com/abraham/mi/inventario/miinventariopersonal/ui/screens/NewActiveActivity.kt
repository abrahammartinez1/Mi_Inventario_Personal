package com.abraham.mi.inventario.miinventariopersonal.ui.screens

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.abraham.mi.inventario.miinventariopersonal.R
import com.abraham.mi.inventario.miinventariopersonal.data.BBDD.DatabaseCallback
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveEntity
import com.abraham.mi.inventario.miinventariopersonal.data.model.ActiveTypeEntity
import com.abraham.mi.inventario.miinventariopersonal.ui.base.BaseActivity
import com.abraham.mi.inventario.miinventariopersonal.ui.base.DatabaseSingleton
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.MiInventarioPersonalTheme
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.TextStyleGlobal
import com.abraham.mi.inventario.miinventariopersonal.ui.theme.TextStyleTitle
import java.io.ByteArrayOutputStream

class NewActiveActivity : BaseActivity() {
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
            TitleComponent()

            val (favoriteValue, onFavoriteValueChanged) = remember { mutableStateOf(false) }
            FilterComponent(
                types,
                typeValue,
                onTypeValueChanged,
                favoriteValue,
                onFavoriteValueChanged
            )

            val (descriptionValue, onDescriptionValueChanged) = remember { mutableStateOf("") }
            val (priceValue, onPriceValueChanged) = remember { mutableStateOf("") }
            EditTextComponents(
                descriptionValue,
                onDescriptionValueChanged,
                priceValue,
                onPriceValueChanged
            )

            val (capturedImageValue, onCapturedImageValueChanged) = remember {
                mutableStateOf<ImageBitmap?>(
                    null
                )
            }

            CameraCaptureComponent(capturedImageValue, onCapturedImageValueChanged)
            SaveButtonComponent(
                paddingValues,
                typeValue?.id,
                favoriteValue,
                if (priceValue.isEmpty()) 0.0 else priceValue.toDouble(),
                descriptionValue,
                capturedImageValue?.let { convertImageBitmapToByteArray(it) } ?: run { null },
            ) {
                onFavoriteValueChanged(false)
                onDescriptionValueChanged("")
                onPriceValueChanged("")
                onCapturedImageValueChanged(null)
            }
        }
    }

    @Composable
    fun TitleComponent() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.activity_new_active_title).uppercase(),
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
        onFavoriteValueChanged: (Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpinnerComponent(types, onTypeValue, onTypeValueChanged)
            CheckboxWithText(favoriteValue, onFavoriteValueChanged)
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
    fun CheckboxWithText(favoriteValue: Boolean, onFavoriteValueChanged: (Boolean) -> Unit) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = favoriteValue,
                onCheckedChange = { newChecked ->
                    onFavoriteValueChanged(newChecked)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.favorite),
                style = TextStyleGlobal,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTextComponents(
        descriptionValue: String,
        onDescriptionValueChanged: (String) -> Unit,
        priceValue: String,
        onPriceValueChanged: (String) -> Unit
    ) {
        val verticalSpacer = 16.dp

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            TextField(
                value = descriptionValue,
                onValueChange = { onDescriptionValueChanged(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.description),
                        style = TextStyleGlobal
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .padding(bottom = verticalSpacer)
            )

            TextField(
                value = priceValue,
                onValueChange = { onPriceValueChanged(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.price),
                        style = TextStyleGlobal
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
            )
        }
    }

    @Composable
    fun CameraCaptureComponent(
        capturedImageValue: ImageBitmap?,
        onCapturedImageValueChanged: (ImageBitmap?) -> Unit,
    ) {
        var permissionResult by remember { mutableStateOf<Boolean?>(null) }
        val (showAlertValue, onShowAlertValueChanged) = remember { mutableStateOf(false) }

        val takePictureLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onCapturedImageValueChanged(handleCameraResult(result))
            }

        val requestPermissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                permissionResult = it

                if (permissionResult == true) {
                    launchCameraIntent(takePictureLauncher)
                } else {
                    onShowAlertValueChanged(true)
                }
            }

        if (showAlertValue) {
            PermissionAlert(onShowAlertValueChanged)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {

            Text(
                text = stringResource(R.string.take_photo),
                style = TextStyleGlobal,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            capturedImageValue?.let { image ->
                Image(
                    bitmap = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
                            if (permissionResult == true) {
                                launchCameraIntent(takePictureLauncher)
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                )
            } ?: run {
                SquareContainerWithImage {
                    if (permissionResult == true) {
                        launchCameraIntent(takePictureLauncher)
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }
        }
    }

    private fun convertImageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        imageBitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @Composable
    fun SquareContainerWithImage(onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(1.dp, Color.Black)
                .padding(16.dp)
                .clickable { onClick.invoke() }
        ) {
            Text(text = stringResource(R.string.no_image), color = Color.Gray, fontSize = 18.sp)
        }
    }

    @Composable
    private fun SaveButtonComponent(
        paddingValues: PaddingValues,
        typeId: Long?,
        favorite: Boolean,
        price: Double?,
        description: String?,
        imageByteArray: ByteArray?,
        onClearForm: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonComponent(
                text = stringResource(R.string.save),
                modifier = Modifier.wrapContentWidth()
            ) {
                typeId?.apply {
                    val activeEntity = ActiveEntity(
                        typeId = this,
                        favorite = favorite,
                        active = true,
                        price = price ?: 0.0,
                        description = description ?: "",
                        image = imageByteArray
                    )

                    DatabaseSingleton.insertActiveAsync(
                        this@NewActiveActivity,
                        activeEntity,
                        object :
                            DatabaseCallback<Unit> {
                            override fun onResult(result: Unit) {
                                onClearForm.invoke()
                            }
                        })
                }
            }
        }
    }

    private fun launchCameraIntent(takePictureLauncher: ActivityResultLauncher<Intent>) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }

    private fun handleCameraResult(result: ActivityResult): ImageBitmap? {
        return if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val bitmap: Bitmap? = data?.extras?.get("data") as? Bitmap

            bitmap?.asImageBitmap()
        } else {
            null
        }
    }

    @Composable
    fun PermissionAlert(onShowAlertValueChanged: (Boolean) -> Unit) {
        AlertDialog(
            onDismissRequest = { onShowAlertValueChanged(false) },
            title = {
                Text(text = stringResource(R.string.request_permission_title))
            },
            text = {
                Text(text = stringResource(R.string.request_permission_description))
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val intent = Intent()
                            intent.action =
                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            ContextCompat.startActivity(this@NewActiveActivity, intent, null)
                            onShowAlertValueChanged(false)
                        }
                    ) {
                        Text(text = stringResource(R.string.open_app_settings))
                    }
                }
            }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMainActivity() {
        MiInventarioPersonalTheme {
            ViewContainer()
        }
    }
}