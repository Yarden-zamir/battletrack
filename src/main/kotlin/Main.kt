// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
	var text by remember { mutableStateOf("") }
	var actors = remember { listOf(Actor("d"), Actor("d")).toMutableStateList() }

	MaterialTheme {
		Column {
			ChipCreator(onActorNameValueChange = {
				text = it
			}, text, addChip = {
				if (text.isEmpty()) return@ChipCreator
				actors.add(Actor(text))
				text = ""
			})
			LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
				actors.forEach {
					item {
						TrackingChip(it, Info(""))
					}
				}
			}
		}
	}
}

@Composable
fun ActorsList(actors: List<Actor>) {

}

fun main() = application {
	Window(onCloseRequest = ::exitApplication) {
		App()
	}
}

@Composable
fun TrackingChip(actor: Actor, info: Info) {
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()
	val cardElevation by animateDpAsState(if (isHovered) 5.dp else 1.dp)
	val cardSize by animateSizeAsState(if (isHovered) Size(100f, 100f) else Size.Zero)

	Card(
		modifier = Modifier
			.padding(10.dp)
			.hoverable(interactionSource)
			.defaultMinSize(cardSize.width.dp, cardSize.height.dp)
			.widthIn(100.dp, 1000.dp).fillMaxWidth(),
//		backgroundColor = MaterialTheme.colors.primarySurface,

		elevation = cardElevation
	) {
		Row {
			Image(
				painter = painterResource("hyena-head.png"),
				contentDescription = "Contact profile picture",
				modifier = Modifier
					// Set image size to 40 dp
					.size(80.dp)
					// Clip image to be shaped as a circle
					.padding(10.dp)
					.clip(CircleShape)
			)
			Text(actor.name)
		}

	}
}

@Preview
@Composable
fun ChipCreator(onActorNameValueChange: (String) -> Unit, actorName: String, addChip: () -> Unit) {
	Row(modifier = Modifier.fillMaxWidth().padding(20.dp).wrapContentWidth().wrapContentHeight()) {
		TextField(onValueChange = onActorNameValueChange, value = actorName, placeholder = { Text("Actor name") })
		TextField(onValueChange = {}, value = "Energy", modifier = Modifier.defaultMinSize(20.dp))
		Text("  X	", modifier = Modifier.align(Alignment.CenterVertically))
		TextField(onValueChange = {}, value = "Amount", modifier = Modifier.defaultMinSize(20.dp))
		Button(
			onClick = addChip,
			modifier = Modifier.align(Alignment.CenterVertically).padding(4.dp),
			shape = CircleShape
		) {
			Text("Add")

		}
	}
}

data class Info(val name: String)

data class Actor(val name: String)
