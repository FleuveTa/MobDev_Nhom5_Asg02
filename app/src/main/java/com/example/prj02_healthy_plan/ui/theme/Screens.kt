package com.example.prj02_healthy_plan.ui.theme

sealed class Screens (val screen: String) {
    data object Home : Screens("home")
    data object Diary : Screens("diary")
    data object Explore : Screens("explore")
    data object DetailRecipe: Screens("detailRecipe")
    data object ViewRecipeResult: Screens("viewRecipeResult")
    data object Scan: Screens("scan")
    data object SearchChoice: Screens("searchChoice")
    data object SearchResult: Screens("searchResult")
    data object More : Screens("more")
    data object UserInfor: Screens("userInfor")
    data object UserAddFood: Screens("userAddFood")

    data object UserAddIngredient: Screens("userAddIngredient")
    data object CalendarUI: Screens("calendar")
    data object SecurityUI: Screens("security")
}