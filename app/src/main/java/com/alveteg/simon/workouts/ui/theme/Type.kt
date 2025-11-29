package com.alveteg.simon.workouts.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.alveteg.simon.workouts.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val archivoFontName = GoogleFont("Archivo")
val archivoBlackFontName = GoogleFont("Archivo Black")


val Archivo = FontFamily(
    Font(googleFont = archivoFontName, fontProvider = provider)
)

val ArchivoBlack = FontFamily(
    Font(googleFont = archivoBlackFontName, fontProvider = provider)
)

val Roboto = FontFamily.Default

val AppTypography = Typography(
	displayLarge = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 57.sp,
		lineHeight = 64.sp,
		letterSpacing = (-0.25).sp,
	),
	displayMedium = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 45.sp,
		lineHeight = 52.sp,
		letterSpacing = 0.sp,
	),
	displaySmall = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 36.sp,
		lineHeight = 44.sp,
		letterSpacing = 0.sp,
	),
	headlineLarge = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 32.sp,
		lineHeight = 40.sp,
		letterSpacing = 0.sp,
	),
	headlineMedium = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 28.sp,
		lineHeight = 36.sp,
		letterSpacing = 0.sp,
	),
	headlineSmall = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 24.sp,
		lineHeight = 32.sp,
		letterSpacing = 0.sp,
	),
	titleLarge = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.sp,
	),
	titleMedium = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.1.sp,
	),
	titleSmall = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp,
	),
	labelLarge = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp,
	),
	bodyLarge = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp,
	),
	bodyMedium = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.25.sp,
	),
	bodySmall = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.W400,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.4.sp,
	),
	labelMedium = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.Medium,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp,
	),
	labelSmall = TextStyle(
		fontFamily = Roboto,
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp,
	),
)
