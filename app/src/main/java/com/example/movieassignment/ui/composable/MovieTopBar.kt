package com.example.movieassignment.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movieassignment.R

@Composable
fun MovieTopBar(
    title: String,
    onQueryChange: (String) -> Unit,
) {
    val (isSearchMode, setSearchMode) = remember { mutableStateOf(false) }
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    if (isSearchMode) {
        TopAppBar(
            backgroundColor = Color.Black,
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                ) {
                    TextField(
                        value = searchQuery, onValueChange = {
                            setSearchQuery(it)
                            onQueryChange(it)
                        }, modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search_cancel),
                                contentDescription = "Close Search", modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 8.dp)
                                    .clickable {
                                        setSearchMode(false)
                                        setSearchQuery("")
                                        onQueryChange("")
                                    }, tint = Color.White
                            )
                        },
                        placeholder = { Text(text = "Enter atleast 3 characters to search",
                            color = Color.White, fontWeight = FontWeight.Thin
                        )}
                    )
                }
            },
        )
    } else {
        Box(
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.nav_bar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.8f

            )
            TopAppBar(
                backgroundColor = Color.Transparent,
                title = { Text(text = title, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search",
                        modifier = Modifier.clickable { setSearchMode(true) },
                        tint = Color.White
                    )
                }
            )
        }
    }
}
