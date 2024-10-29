package com.example.diaryprogram

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.diaryprogram.Page.BrowseDetailPage
import com.example.diaryprogram.Page.BrowsePage
import com.example.diaryprogram.Page.EnrollPage
import com.example.diaryprogram.Page.LoginPage
import com.example.diaryprogram.Page.MainPage
import com.example.diaryprogram.Page.MapPage
import com.example.diaryprogram.Page.SettingPage
import com.example.diaryprogram.Page.WritePage

/**
 *  앱의 전반적인 흐름 통제함
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        //로그인 페이지
        composable(route = "login") {
            LoginPage(navController)
        }
        //회원 가입 페이지
        composable(route = "enroll") {
            EnrollPage(navController)
        }
        //메인 페이지
        composable(route = "main") {
            MainPage(navController)
        }
        //지도 페이지
        composable(route = "map") {
            MapPage(navController)
        }
        //설정 페이지
        composable(route="setting") {
            SettingPage(navController)
        }
        //일기 작성 페이지
        composable(route="write") {
            WritePage(navController)
        }
        // 일기 조회 페이지
        composable(route = "browse") {
            BrowsePage(navController)
        }
        //일기 상세 조회 페이지
        composable(route = "browseDetail") {
            BrowseDetailPage(navController)
        }

    }
}