package com.example.prj02_healthy_plan.ui.theme

sealed class Screens (val screen: String) {
    data object Home : Screens("home")
    data object Diary : Screens("diary")
    data object Explore : Screens("explore")
    data object More : Screens("more")
}