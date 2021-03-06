package com.sito.bugs360

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sito.bugs360.ui.theme.Bugs360Theme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bugs360Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ShowImage()
                }
            }
        }
    }
}

@Preview
@Composable
fun ShowImage() {
    var image by rememberSaveable {
        mutableStateOf(R.drawable.ixodus_ricinus_a)
    }
    val start = R.drawable.ixodus_ricinus_a

    //var offsetX by remember { mutableStateOf(0f) }
    var del by rememberSaveable {
        mutableStateOf(0f)
    }

    var d by rememberSaveable {
        mutableStateOf(R.drawable.ixodus_ricinus_a)
    }

    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }

    // set up all transformation states
    var scale by rememberSaveable { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        if (scale < 1f) {
            scale = 1f
            offset = Offset(0f, 0f)
        }
    }

    Column() {
        Image(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            scale = if (scale == 1f) {
                                2f
                            } else 1f
                        },
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (scale > 1f) {
                            change.consumeAllChanges()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        } else {
                            offsetX += 0
                            offsetY += 0
                        }
                    }

                }
                // apply other transformations like rotation and zoom
                // on the pizza slice emoji
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    /*translationX = offset.x,
                    translationY = offset.y*/
                )
                // add transformable to listen to multitouch transformation events
                // after offset
                .transformable(state = state)
                .fillMaxSize()
                //drag
                .padding(5.dp)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (scale == 1f) {
                            d = image
                            del = delta
                            if (del > 0) {
                                d++
                            } else {
                                d--
                            }
                            if (d > start + 17)
                                d = start
                            else if (d < start)
                                d = start + 17
                            image = d
                            del = delta
                        }
                        delta
                    }
                ),
            contentDescription = "",
            painter = painterResource(id = image)
        )
        /*Text(text=image.toString())
        Text(text=start.toString())
        Text(text=d.toString())*/
        /*Button(onClick = {
            image++
            if (image > start + 17)
                image = start
            else if (image < start - 17)
                image = start + 18
        }) {
            Text(text = "add")
        }*/
    }
}