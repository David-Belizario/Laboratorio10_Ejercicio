package com.androidcourse.laboratorio10_ejercicio.Screens

import com.androidcourse.laboratorio10_ejercicio.Model.ProductModel

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.androidcourse.laboratorio10_ejercicio.data.ProductApiService
import kotlinx.coroutines.delay

// Función para listar los elementos obtenidos del API
@Composable
fun ContenidoProductosListado(navController: NavHostController, servicio: ProductApiService) {
    var listaProductos: SnapshotStateList<ProductModel> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        val listado = servicio.selectProducts()
        listado.forEach { listaProductos.add(it) }
    }

    LazyColumn {
        item {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.1f)
                )
                Text(
                    text = "Producto",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.7f)
                )
                Text(
                    text = "Acción",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.2f)
                )
            }
        }

        items(listaProductos) { item ->
            Row(
                modifier = Modifier.padding(start = 8.dp).fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.id}", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.1f)
                )
                Text(
                    text = item.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.6f)
                )
                IconButton(
                    onClick = {
                        navController.navigate("productView/${item.id}")
                        Log.e("PRODUCT-VIEW", "ID = ${item.id}")
                    },
                    Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Ver")
                }
                IconButton(
                    onClick = {
                        navController.navigate("productDelete/${item.id}")
                        Log.e("PRODUCT-DELETE", "ID = ${item.id}")
                    },
                    Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

// Función para editar los elementos obtenidos del API
@Composable
fun ContenidoProductoEditar(navController: NavHostController, servicio: ProductApiService, pid: Int = 0) {
    var id by remember { mutableStateOf(pid) }
    var name by remember { mutableStateOf<String?>("") }
    var price by remember { mutableStateOf<String?>("") }
    var category by remember { mutableStateOf<String?>("") }
    var grabar by remember { mutableStateOf(false) }

    if (id != 0) {
        LaunchedEffect(Unit) {
            val objProduct = servicio.selectProduct(id.toString())
            delay(100)
            name = objProduct.body()?.nombre
            price = objProduct.body()?.precio.toString()
            category = objProduct.body()?.categoria
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = id.toString(),
            onValueChange = { },
            label = { Text("ID (solo lectura)") },
            readOnly = true,
            singleLine = true
        )
        TextField(
            value = name!!,
            onValueChange = { name = it },
            label = { Text("Nombre del Producto: ") },
            singleLine = true
        )
        TextField(
            value = price!!,
            onValueChange = { price = it },
            label = { Text("Precio:") },
            singleLine = true
        )
        TextField(
            value = category!!,
            onValueChange = { category = it },
            label = { Text("Categoría:") },
            singleLine = true
        )
        Button(
            onClick = {
                grabar = true
            }
        ) {
            Text("Guardar", fontSize = 16.sp)
        }
    }

    if (grabar) {
        val objProduct = ProductModel(id, name!!, "", price!!.toDouble().toString(), category!!)
        LaunchedEffect(Unit) {
            if (id == 0)
                servicio.insertProduct(objProduct)
            else
                servicio.updateProduct(id.toString(), objProduct)
        }
        grabar = false
        navController.navigate("products")
    }
}

// Función para eliminar los elementos obtenidos del API
@Composable
fun ContenidoProductoEliminar(navController: NavHostController, servicio: ProductApiService, id: Int) {
    var showDialog by remember { mutableStateOf(true) }
    var borrar by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar Eliminación") },
            text = { Text("¿Está seguro de eliminar el Producto?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        borrar = true
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (borrar) {
        LaunchedEffect(Unit) {
            servicio.deleteProduct(id.toString())
            borrar = false
            navController.navigate("products")
        }
    }
}
