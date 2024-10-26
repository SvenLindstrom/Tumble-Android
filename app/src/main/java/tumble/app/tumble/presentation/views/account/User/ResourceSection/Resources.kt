package tumble.app.tumble.presentation.views.account.User.ResourceSection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.AccountViewModel

@Composable
fun Resources(
    parentViewModel: AccountViewModel = hiltViewModel(),
    getResourcesAndEvents: () -> Unit,
    collapsedHeader: MutableState<Boolean>,
    navController: NavHostController
){
    val scrollState = rememberScrollState()
    val coroutinScope = rememberCoroutineScope()
    var scrollOffset by remember {
        mutableStateOf(0f)
    }
    var showingConfirmationDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        getResourcesAndEvents()
    }
    val userBookings = parentViewModel.userBookings.collectAsState()
    val userEvents = parentViewModel.completeUserEvent.collectAsState()
    val registeredBookingsState = parentViewModel.registeredBookingsSectionState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colors.background)
    ) {
        //TODO pull to refresh
        ResourceSectionDivider(title = "User options"){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Automatic exam signup"
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = parentViewModel.autoSignUpEnabled(),
                    onCheckedChange = {
                        if (it) {
                            showingConfirmationDialog = true
                        } else {
                            parentViewModel.toggleAutoSignup(false)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.onPrimary,
                        checkedTrackColor = MaterialTheme.colors.primary
                    )
                )
            }
            if(showingConfirmationDialog){
                AlertDialog(
                    onDismissRequest = { showingConfirmationDialog = false },
                    title = { Text(text = "Confirm Action")},
                    text = { Text(text = "are you sure you want to enable this experimental feature?")},
                    confirmButton = {
                        TextButton(onClick = {
                            parentViewModel.toggleAutoSignup(true)
                            showingConfirmationDialog = false
                        }) {
                            Text(text = "Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            parentViewModel.toggleAutoSignup(false)
                            showingConfirmationDialog = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
        ResourceSectionDivider(
            title = "Your bookings", // string resource
            resourceType = ResourceType.RESOURCE,
            destination = { navController.navigate(Routes.accountResources) }
        ) {
            RegisteredBooking(
                onClickResource = { parentViewModel.setBooking(it) },
                state = registeredBookingsState,
                bookings = userBookings.value?.bookings?: emptyList(),
                confirmBooking = {resourceId, bookingId -> parentViewModel.confirmResource(resourceId, bookingId)},
                unBook = {bookingId -> parentViewModel.unBookResource(bookingId)}
            )
        }
        ResourceSectionDivider(
            title = "Your event", // string resource
            resourceType = ResourceType.EVENT,
            destination = { navController.navigate(Routes.accountEvents) }
        ) {
            RegisteredEvent(
                onClickEvent = { parentViewModel.setEvent(it) },
                state = parentViewModel.registeredEventSectionState.collectAsState(),
                registeredEvents = userEvents.value?.registeredEvents?: emptyList()
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
    LaunchedEffect(key1 = scrollState.value) {
        scrollOffset = scrollState.value.toFloat()
        if(scrollOffset >= 80){
            coroutinScope.launch {
                collapsedHeader.value = true
            }
        } else if (scrollOffset == 0f){
            coroutinScope.launch {
                collapsedHeader.value = false
            }
        }
    }
}