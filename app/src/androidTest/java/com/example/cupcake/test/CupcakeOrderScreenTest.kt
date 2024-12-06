
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cupcake.R
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.test.onNodeWithStringId
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {
    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeOrderUiState = OrderUiState(
        quantity = 6,
        flavor = "Vanilla",
        date = "Wed Jul 21",
        price = "$100",
        pickupOptions = listOf()
    )
    @Test
    fun selectOptionScreen_verifyContent(){
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        val subtotal = "$100"
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors)
        }
        flavors.forEach{flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()
        }
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.subtotal_price, subtotal)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
        composeTestRule.onNodeWithStringId(R.string.cancel).assertIsEnabled()
    }

    @Test
    fun startOrderScreen_verifyContent(){
        composeTestRule.setContent {
            StartOrderScreen(
                quantityOptions = DataSource.quantityOptions,
                onNextButtonClicked = {}
            )
        }
        DataSource.quantityOptions.forEach { option ->
            composeTestRule.onNodeWithStringId(option.first).assertIsDisplayed()
        }
    }

    @Test
    fun selectOptionScreen_optionSelected_NextButtonEnabled(){
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        val subtotal = "$100"
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors)
        }
        composeTestRule.onNodeWithText(flavors.first()).performClick()
        composeTestRule.onNodeWithStringId(R.string.next).assertIsEnabled()
    }
    @Test
    fun summaryScreen_verifyContent(){
        composeTestRule.setContent {
            OrderSummaryScreen(
                orderUiState = fakeOrderUiState,
                onCancelButtonClicked = { },
                onSendButtonClicked = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.quantity).uppercase()
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getQuantityString(
                R.plurals.cupcakes,
                fakeOrderUiState.quantity,
                fakeOrderUiState.quantity
            )
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.flavor).uppercase()
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeOrderUiState.flavor).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.pickup_date).uppercase()
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeOrderUiState.date).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.subtotal_price, fakeOrderUiState.price)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.send).assertIsEnabled()
        composeTestRule.onNodeWithStringId(R.string.cancel).assertIsEnabled()
    }
}