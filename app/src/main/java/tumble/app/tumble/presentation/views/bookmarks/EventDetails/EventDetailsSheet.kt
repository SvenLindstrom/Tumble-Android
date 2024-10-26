package tumble.app.tumble.presentation.views.bookmarks.EventDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.toColor
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.components.sheets.SheetHeader

@Composable
fun EventDetailsSheet(
    event: Event,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        SheetHeader(title = "Details", onClose = {onClose()})

        //Spacer(modifier = Modifier.height(20.dp))
        Box {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colors.background)
            ) {
                EventDetailsCard(
                    openColorPicker = { openColorPicker() },
                    event = event,
                    color = event.course?.color?.toColor() ?: Color.Gray
                )
                EventDetailsBody(event)
                Spacer(modifier = Modifier.height(60.dp))
            }
            //ColorPicker()
        }
    }
}

private fun onClose(){
    AppController.shared.eventSheet = null
}

private fun openColorPicker(){}