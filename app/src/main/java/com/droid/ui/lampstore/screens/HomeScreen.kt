package com.droid.ui.lampstore.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.droid.ui.lampstore.components.PopularCard
import com.droid.ui.lampstore.components.RecommendedCard
import com.droid.ui.lampstore.components.TitleWithSellAll
import com.droid.ui.lampstore.model.LampInfo
import com.droid.ui.lampstore.model.LampType
import com.droid.ui.lampstore.ui.theme.CharcoalGrey
import com.droid.ui.lampstore.ui.theme.DarkCharcoalGrey
import com.droid.ui.lampstore.ui.theme.Orange
import com.droid.ui.lampstore.viewmodel.LampStoreViewModel

/**
 * This function creates the HomeScreen.
 * Contains top sections, with recommended lamp section and popular lamp section.
 * @param navController parent navController to navigate between the screen.
 * @param viewModel common LampStoreViewModel object.
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: LampStoreViewModel,
    paddingValues: PaddingValues
) {
    // The var selectedChip variable is used to store the selected chip.
    var selectedChip = remember {
        mutableStateOf("All")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCharcoalGrey)
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {
        HeaderSession()
        ChipsSession(chips = viewModel.getChipList(), selectedChip = selectedChip)
        RecommendedSection(if (selectedChip.value != LampType.ALL.lampChipName) viewModel.getLampLists().filter { it.lampType.lampChipName == selectedChip.value && it.isRecommended } else viewModel.getLampLists().filter { it.isRecommended }) {
            viewModel.addSelectedLamp(it)
            navController.navigate("detail")
        }
        PopularSection(if (selectedChip.value != LampType.ALL.lampChipName) viewModel.getLampLists().filter { it.lampType.lampChipName == selectedChip.value && it.isPopular } else viewModel.getLampLists().filter { it.isPopular }) {
            viewModel.addSelectedLamp(it)
            navController.navigate("detail")
        }
    }
}

/**
 * The HeaderSession() function is used to create the header section of the screen.
 * Contains the title and sub title and user icon.
 */
@Composable
private fun HeaderSession() {
    Row(
        modifier = Modifier.padding(25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Exclusive Lights", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "for your house", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Light)
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = CharcoalGrey,
            ),
            elevation = CardDefaults.cardElevation(5.dp),
            modifier = Modifier.size(35.dp),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Icon(Icons.Filled.AccountBox, contentDescription = null, modifier = Modifier.fillMaxSize())
        }
    }
}

/**
 * The ChipsSession() function is used to create the chips section of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipsSession(chips: ArrayList<String>, selectedChip: MutableState<String>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 25.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(chips) { item ->
            FilterChip(
                selected = selectedChip.value == item,
                onClick = { selectedChip.value = item },
                label = { Text(text = item, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) },
                colors = FilterChipDefaults.filterChipColors(containerColor = CharcoalGrey, selectedContainerColor = Orange),
                border = FilterChipDefaults.filterChipBorder(CharcoalGrey),
                elevation = FilterChipDefaults.filterChipElevation(2.dp)
            )
        }
    }
}

/**
 * The RecommendedSection() function is used to create the recommended section of the screen.
 * Contains the Recommended title and See ALL button.
 * @param lamps recommended lamp lists, to display recommended lamp card
 * @param onLampSelected callback function to call the detail screen with selected lamp info
 */
@Composable
private fun RecommendedSection(lamps: List<LampInfo>, onLampSelected: (lampInfo: LampInfo) -> Unit) {
    Column {
        TitleWithSellAll(title = "Recommended")
        RecommendedCard(lamps = lamps) {
            onLampSelected(it)
        }
    }
}

/**
 * The PopularSection() function is used to create the popular section of the screen.
 * Contains the Popular title and See ALL button.
 * @param lamps Popular lamp lists, to display Popular lamp card
 * @param onLampSelected callback function to call the detail screen with selected lamp info
 */
@Composable
private fun PopularSection(lamps: List<LampInfo>, onLampSelected: (lampInfo: LampInfo) -> Unit) {
    Column {
        TitleWithSellAll(title = "Popular Lamps")
        PopularCard(lamps) {
            onLampSelected(it)
        }
    }
}