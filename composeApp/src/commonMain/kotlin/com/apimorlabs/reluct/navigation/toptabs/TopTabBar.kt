package com.apimorlabs.reluct.navigation.toptabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apimorlabs.reluct.compose.ui.components.topBar.ReluctPageHeading
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TopTabBar(
    title: String,
    tabPage: State<TabDestinations<out Any>>,
    tabs: ImmutableList<TabBar<out Any>>,
    updateTabPage: (TabDestinations<out Any>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        ReluctPageHeading(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = title,
            extraItems = {}
        )

        LazyRow {
            item {
                TabBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    selectedTabPage = tabPage,
                    tabs = tabs,
                    onTabSelected = { updateTabPage(it) }
                )
            }
        }
    }
}
