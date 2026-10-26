package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ExerciseMode
import com.example.model.MathSection
import com.example.ui.components.LiquidGlassPanel
import com.example.ui.components.LiquidGlassPillButton
import com.example.ui.theme.FlatBackgroundIvory
import com.example.ui.theme.FlatCoral
import com.example.ui.theme.FlatIndigo
import com.example.ui.theme.FlatInkPrimary
import com.example.ui.theme.FlatInkSecondary
import com.example.ui.theme.FlatSurfaceWhite
import com.example.ui.theme.FredokaFontFamily

/**
 * Dedicated Submenu screen for each section ("Sumas y Restas" or "Fracciones y Gráficas")
 */
@Composable
fun SectionSubMenuScreen(
    section: MathSection,
    onSelectMode: (ExerciseMode) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val isGrade2 = section == MathSection.GRADE_2_ADD_SUB
    val sectionTitle = if (isGrade2) {
        stringResource(R.string.module_grade_2_title)
    } else {
        stringResource(R.string.module_grade_4_title)
    }
    val gradeBadge = if (isGrade2) {
        stringResource(R.string.module_grade_2_badge)
    } else {
        stringResource(R.string.module_grade_4_badge)
    }
    val accentColor = if (isGrade2) FlatIndigo else FlatCoral

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FlatBackgroundIvory)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LiquidGlassPanel(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp),
            cornerRadius = 20.dp,
            tintColor = FlatSurfaceWhite,
            shadowElevation = 4.dp,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidGlassPillButton(
                    onClick = onBack,
                    accentColor = accentColor,
                    testTag = "submenu_back_button",
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back_button),
                        tint = FlatInkPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = gradeBadge,
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = accentColor
                    )
                    Text(
                        text = sectionTitle,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 22.sp,
                            color = FlatInkPrimary
                        )
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.submenu_title),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                color = FlatInkPrimary
            ),
            textAlign = TextAlign.Center
        )

        SubMenuModeCard(
            title = stringResource(R.string.mode_system_title),
            description = stringResource(R.string.mode_system_desc),
            buttonLabel = "Iniciar Modo Sistema",
            accentColor = FlatIndigo,
            icon = {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            },
            testTag = "submenu_mode_system",
            onClick = { onSelectMode(ExerciseMode.SYSTEM_RANDOM) },
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
        )

        SubMenuModeCard(
            title = stringResource(R.string.mode_creator_title),
            description = stringResource(R.string.mode_creator_desc),
            buttonLabel = "Abrir Modo Creador",
            accentColor = FlatCoral,
            icon = {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            },
            testTag = "submenu_mode_creator",
            onClick = { onSelectMode(ExerciseMode.CREATOR_MANUAL) },
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
        )
    }
}

@Composable
private fun SubMenuModeCard(
    title: String,
    description: String,
    buttonLabel: String,
    accentColor: Color,
    icon: @Composable () -> Unit,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = FlatSurfaceWhite),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 20.sp,
                        color = FlatInkPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = FlatInkSecondary
                )
            )

            LiquidGlassPillButton(
                onClick = onClick,
                accentColor = accentColor,
                filledGlass = true,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = buttonLabel,
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
