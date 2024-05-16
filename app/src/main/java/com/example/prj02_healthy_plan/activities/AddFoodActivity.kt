package com.example.prj02_healthy_plan.activities

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.prj02_healthy_plan.Ingredient
import com.example.prj02_healthy_plan.ui.theme.Prj02_Healthy_PlanTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage

class AddFoodActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
//    private val context: Context = this
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val ingredientList = arrayListOf<Ingredient>()
    //Nếu Ingredient không thay đổi thì không cần tham chiếu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val ingredientRef = db.collection("ingredients")
        val storageRef = storage.reference
        val recipeRef = db.collection("recipes")
//        ingredientRef.get()
//            .addOnSuccessListener {documents ->
//                for (document in documents) {
//                    val ingredient = document.toObject<Ingredient>()
//                    ingredientList.add(ingredient)
//                    Log.d("TAG", "${ingredient.name} ${ingredient.unit} ${ingredient.nutrition}")
//                }
//            }
        getIngredientListFromDb(ingredientRef, ingredientList)
        setContent {
            Prj02_Healthy_PlanTheme {

                val context = LocalContext.current
                val ingredients = remember {
                    mutableIntStateOf(0)
                }
                val nameState = remember { mutableStateOf("Chicken Salad") }
                val descriptionState = remember { mutableStateOf("Mon ngon moi ngay") }
                val buttonEnabled = remember { mutableStateOf(true) }
                val ingredientStates = remember {
                    mutableStateListOf<MutableState<Double>>()
                }
                val savingRecipeState =  remember { mutableStateOf(false) }
                val nutritionStates = remember {
                    mutableStateListOf<MutableState<Int>>()
                }
                val reload = remember {
                    mutableStateOf(0)
                }
                val calories = remember {
                    mutableDoubleStateOf(0.0)
                }
                val protein = remember {
                    mutableDoubleStateOf(0.0)
                }
                val carb = remember {
                    mutableDoubleStateOf(0.0)
                }
                val fat = remember {
                    mutableDoubleStateOf(0.0)
                }

                LaunchedEffect(ingredientStates.toList(), nutritionStates.toList(), reload.value) {
                    calories.doubleValue = 0.0
                    protein.doubleValue = 0.0
                    carb.doubleValue = 0.0
                    fat.doubleValue = 0.0

                    for (i in 0 until ingredients.intValue) {
                        if (ingredientList.isNotEmpty() && nutritionStates[i].value < ingredientList.size) {
                            val nutrition = ingredientList[nutritionStates[i].value].nutrition
                            if (nutrition != null) {
                                calories.doubleValue += nutrition[0] * ingredientStates[i].value
                                protein.doubleValue += nutrition[1] * ingredientStates[i].value
                                carb.doubleValue += nutrition[2] * ingredientStates[i].value
                                fat.doubleValue += nutrition[3] * ingredientStates[i].value
                            }
                        }
                    }

                }

                Column (
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Add New Recipe", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                    RecipeInputBox(title = "Name", des = "Chicken Salad", nameState)
                    RecipeInputBox(title = "Description", des = "", descriptionState)
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Ingredients", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Green))
                        IconButton(onClick = {
                            ingredients.intValue++
                            ingredientStates.add(mutableDoubleStateOf(1.0))
                            nutritionStates.add(mutableIntStateOf(0))
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.AddCircle,
                                contentDescription = "Logout Icon",
                                modifier = Modifier
                                    .size(30.dp),
                                tint = Color.Green
                            )
                        }
                    }
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        for (i in 0 until ingredients.intValue) {
                            IngredientRow(ingredientList, ingredientStates[i], nutritionStates[i], reload)
                        }
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Start,
                        ){
                            Text(text = "Nutrition", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Green))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Total Calories: ${calories.doubleValue}")
                            Text(text = "Protein: ${protein.doubleValue}g")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Carbs: ${carb.doubleValue}g")
                            Text(text = "Fat: ${fat.doubleValue}g")
                        }
                        Text(text = "Detail : ", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black))
                        for (i in 0 until ingredients.intValue) {
                            if (ingredientList.isNotEmpty() && nutritionStates[i].value < ingredientList.size) {
                                Text(text = "${ingredientList[nutritionStates[i].value].name} : ${ingredientStates[i].value }, nutrition : ${ingredientList[nutritionStates[i].value].nutrition.toString()}")
                            } else {
                                Text(text = "Add more Ingredient and Quantity to see total nutrition")
                            }
                        }

                        var imageUri by remember {
                            mutableStateOf<Uri?>(null)
                        }
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) {
                            imageUri = it
                        }

                        var fileUri by remember { mutableStateOf<Uri?>(null) }
                        val filePickerLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) {
                            fileUri = it
                        }

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Start,
                        ){
                            Text(text = "Image", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Green))
                        }
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .height(200.dp)
                                    .width(200.dp),
                                contentScale = ContentScale.Crop
                            )
                            Button(onClick = {
                                launcher.launch("image/*")
                            }) {
                                Text(text = "Choose Image")
                            }
                        }

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Start,
                        ){
                            Text(text = "Instruction", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Green))
                        }
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "File : $fileUri")
                            Button(onClick = {
                                filePickerLauncher.launch("text/plain")
                            }) {
                                Text(text = "Choose File")
                            }
                        }

                        Button(
                            onClick = {
                                if (imageUri == null || fileUri == null) {
                                    Toast.makeText(context, "Please choose image and file", Toast.LENGTH_SHORT).show()
                                    return@Button
                                } else {
                                    savingRecipeState.value = true
                                    val ingredientInputList = ArrayList<Map<String, Any>>()
                                    for (i in 0 until ingredients.intValue) {
                                        if (ingredientList.isNotEmpty() && nutritionStates[i].value < ingredientList.size) {
                                            val ingredient = hashMapOf(
                                                "name" to (ingredientList[nutritionStates[i].value].name
                                                    ?: ""),
                                                "unit" to (ingredientList[nutritionStates[i].value].unit
                                                    ?: ""),
                                                "quantity" to ingredientStates[i].value
                                            )
                                            ingredientInputList.add(ingredient)
                                        }
                                    }
                                    val recipeData = hashMapOf(
                                        "name" to nameState.value,
                                        "description" to descriptionState.value,
                                        "ingredients" to ingredientInputList,
                                        "nutrition" to arrayListOf(
                                            calories.doubleValue,
                                            protein.doubleValue,
                                            carb.doubleValue,
                                            fat.doubleValue
                                        ),
                                        "imageUrl" to "images/chicken_salad.jpg",
                                        "instructionUrl" to "Cook the chicken, mix with corn and lettuce"
                                    )

                                    val imageRef =
                                        storageRef.child("images/${imageUri?.lastPathSegment}")
                                    val fileTextRef =
                                        storageRef.child("texts/${fileUri?.lastPathSegment}")

                                    val uploadImage = imageRef.putFile(imageUri!!)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Upload Image Success, uploading file text",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                                recipeData["imageUrl"] = uri.toString()
                                                val uploadFileText = fileTextRef.putFile(fileUri!!)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Upload File Text Success, saving recipe to database",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        fileTextRef.downloadUrl.addOnSuccessListener { uri ->
                                                            recipeData["instructionUrl"] =
                                                                uri.toString()
                                                            recipeRef.add(recipeData)
                                                                .addOnSuccessListener { documentReference ->
                                                                    savingRecipeState.value = false
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Recipe saved to Firestore with ID: ${documentReference.id}",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                                .addOnFailureListener { e ->
                                                                    Log.w(
                                                                        "TAG",
                                                                        "Error saving document",
                                                                        e
                                                                    )
                                                                }
                                                        }
                                                    }
                                                    .addOnFailureListener {
                                                        Log.d("TAG", "Upload File Text Failed")
                                                    }
                                            }
                                        }
                                        .addOnFailureListener {
                                            Log.d("TAG", "Upload Image Failed")
                                        }
                                }
                            },
                            modifier = Modifier
                                .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
                                .padding(top = 20.dp),
                            enabled = buttonEnabled.value,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (buttonEnabled.value) Color.Green else Color.Gray
                            )
                        ){
                            Icon(Icons.Filled.Add, contentDescription = "Localized description")
                            Text(text = "Add Recipe", modifier = Modifier.padding(start = 8.dp))
                            if (savingRecipeState.value) {
                                CircularProgressIndicator(modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getIngredientListFromDb(
    ingredientRef: CollectionReference,
    ingredientList: ArrayList<Ingredient>
) {
    ingredientRef.get()
        .addOnSuccessListener {documents ->
            for (document in documents) {
                val ingredient = document.toObject<Ingredient>()
                ingredientList.add(ingredient)
                Log.d("TAG", "${ingredient.name} ${ingredient.unit} ${ingredient.nutrition}")
            }
        }
}


@Composable
fun RecipeInputBox(title: String, des: String, textState: MutableState<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        var value by remember {
            mutableStateOf("Ex: $des")
        }
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                textState.value = it
                            },
            label = { Text(title) },
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(60.dp)
                .align(Alignment.Center)
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientRow(ingredient: List<Ingredient>, ingredientState: MutableState<Double>, nutritionIndexState: MutableState<Int>, reload: MutableState<Int>) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var level by remember {
        mutableStateOf("Chicken Breast, 100 grams")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 60.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth())
        {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {isExpanded = it},
                modifier =  Modifier.weight(3F)
            ) {
                OutlinedTextField(
                    value = level,
                    label = {Text("Ingredient")},
                    onValueChange = {},
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    })
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {isExpanded = false},
                ) {
                    for (i in ingredient) {
                        DropdownMenuItem(
                            text = { Text(text = "${i.name!!}, ${i.unit ?: "null"}") },
                            onClick = {
                                level = "${i.name!!}, ${i.unit ?: "null"}"
                                nutritionIndexState.value = ingredient.indexOf(i)
                                isExpanded = false
                                reload.value++
                            })
                    }
                }
            }
            var value by remember {
                mutableStateOf("1")
            }
            OutlinedTextField(
                value = value,
                onValueChange = {
                    value = it
                    reload.value--
                    if (
                        it.isNotEmpty() &&
                        it.toDouble() >= 0
                    )
                    ingredientState.value = it.toDouble()
                                },
                label = { Text("Qty") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .requiredHeight(60.dp)
                    .weight(1F)
            )
        }
    }
}

@Composable
fun IngredientInput() {
    Row {
        var value by remember {
            mutableStateOf("Ex: Chicken")
        }
        OutlinedTextField(
            value = value,
            onValueChange = {value = it},
            label = { Text("Ingredient") },
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(60.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun Previeww() {
    Prj02_Healthy_PlanTheme {
        Column (

            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Add New Recipe", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Ingredients")
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "Logout Icon",
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
    }
}