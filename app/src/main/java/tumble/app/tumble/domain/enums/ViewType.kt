package tumble.app.tumble.domain.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import tumble.app.tumble.R

enum class ViewType{
    LIST, CALENDAR, WEEK
}

@Composable
fun viewTypeToStringResource(viewType: ViewType): String {
    return when(viewType){
        ViewType.LIST -> {
            stringResource(id = R.string.list)
        }
        ViewType.CALENDAR -> {
            stringResource(id = R.string.calendar)
        }
        ViewType.WEEK -> {
            stringResource(id = R.string.week)
        }
    }
}