package tw.idv.louislee.accountingbook.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Preview(
    group = "Phone-TW",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "zh"
)
@Preview(
    group = "Phone-TW",
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "zh"
)
@Preview(
    group = "Tablet-TW",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.TABLET,
    locale = "zh"
)
@Preview(
    group = "Tablet-TW",
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.TABLET,
    locale = "zh"
)
annotation class ChinesePreview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Preview(
    group = "Phone-EN",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Preview(
    group = "Phone-EN",
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "en"
)
@Preview(
    group = "Tablet-EN",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.TABLET,
    locale = "en"
)
@Preview(
    group = "Tablet-EN",
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.TABLET,
    locale = "en"
)
annotation class EnglishPreview


@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@ChinesePreview
@EnglishPreview
annotation class AppPreview
