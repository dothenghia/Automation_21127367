import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//WebUI.callTestCase(findTestCase('Test Requiment/Log in'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.navigateToUrl('http://localhost:3000/chat')

WebUI.click(findTestObject('EditAccount Objects/Avatar_SideBar_EA'))

// Get previous states
String pre_HoVaTen = WebUI.getText(findTestObject('EditAccount Objects/TextHoVaTen_EA'))
String pre_NgaySinh = WebUI.getText(findTestObject('EditAccount Objects/TextNgaySinh_EA'))
String pre_SoDienThoai = WebUI.getText(findTestObject('EditAccount Objects/TextSoDienThoai_EA'))

// Update information
WebUI.click(findTestObject('EditAccount Objects/NutMoDialogCapNhat_EA'))

WebUI.sendKeys(findTestObject('EditAccount Objects/InputHoVaTen_EA'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
WebUI.setText(findTestObject('EditAccount Objects/InputHoVaTen_EA'), HoVaTen_String)

WebUI.sendKeys(findTestObject('EditAccount Objects/InputSoDienThoai_EA'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
WebUI.setText(findTestObject('EditAccount Objects/InputSoDienThoai_EA'), SoDienThoai_String)

WebUI.sendKeys(findTestObject('EditAccount Objects/InputNgaySinh_EA'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))

String formattedNgaySinh = ''
if (NgaySinh_String != '') {
	String[] NgaySinhSubStrings = NgaySinh_String.split('/')
	formattedNgaySinh = NgaySinhSubStrings[0].toInteger() + '/' + NgaySinhSubStrings[1].toInteger() + '/' + NgaySinhSubStrings[2]
	for (def var : (0..2)) {
		WebUI.sendKeys(findTestObject('EditAccount Objects/InputNgaySinh_EA'), NgaySinhSubStrings[var])
	}
}

WebUI.click(findTestObject('EditAccount Objects/NutCapNhat_EA'))

// Verification logic
boolean isHoVaTenValid = HoVaTen_String.length() > 0 && HoVaTen_String.length() <= 50 && HoVaTen_String.matches('^[\\p{L}\\s\\\']+$')
boolean isSoDienThoaiValid = SoDienThoai_String == '' || SoDienThoai_String.matches('^0\\d{9}$')
boolean isNgaySinhValid = true
boolean isNgaySinhNotInFuture = true

if (NgaySinh_String != '') {
	isNgaySinhValid = formattedNgaySinh.matches('(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/\\d{4}')
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern('d/M/yyyy')
	LocalDate ngaySinhDate = LocalDate.parse(formattedNgaySinh, formatter)
	LocalDate today = LocalDate.now()
	isNgaySinhNotInFuture = !ngaySinhDate.isAfter(today)
}

if (isHoVaTenValid && isSoDienThoaiValid && isNgaySinhValid && isNgaySinhNotInFuture) {
	WebUI.verifyElementPresent(findTestObject('EditAccount Objects/ToastMessage_EA'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	String actualText = WebUI.getText(findTestObject('EditAccount Objects/ToastMessage_EA'), FailureHandling.OPTIONAL)
	String regex = ('.*' + message) + '.*'
	WebUI.verifyMatch(actualText, regex, true, FailureHandling.OPTIONAL)
	
	WebUI.verifyElementText(findTestObject('EditAccount Objects/TextHoVaTen_EA'), HoVaTen_String, FailureHandling.CONTINUE_ON_FAILURE)
	
	if (NgaySinh_String == '') {
		WebUI.verifyElementText(findTestObject('EditAccount Objects/TextNgaySinh_EA'), 'Chưa cập nhật', FailureHandling.CONTINUE_ON_FAILURE)
	} else {
		WebUI.verifyElementText(findTestObject('EditAccount Objects/TextNgaySinh_EA'), formattedNgaySinh, FailureHandling.CONTINUE_ON_FAILURE)
	}
	if (SoDienThoai_String == '') {
		WebUI.verifyElementText(findTestObject('EditAccount Objects/TextSoDienThoai_EA'), 'Chưa cập nhật', FailureHandling.CONTINUE_ON_FAILURE)
	} else {
		WebUI.verifyElementText(findTestObject('EditAccount Objects/TextSoDienThoai_EA'), SoDienThoai_String, FailureHandling.CONTINUE_ON_FAILURE)
	}
} else {
	if (!isHoVaTenValid) {
		WebUI.verifyElementPresent(findTestObject('EditAccount Objects/Text_Error_HoVaTen_EA'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	}
	if (!isSoDienThoaiValid) {
		WebUI.verifyElementPresent(findTestObject('EditAccount Objects/Text_Error_SoDienThoai'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	}
	if (!isNgaySinhValid || !isNgaySinhNotInFuture) {
		WebUI.verifyElementPresent(findTestObject('EditAccount Objects/Text_Error_SoDienThoai'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	}

	WebUI.verifyElementText(findTestObject('EditAccount Objects/TextHoVaTen_EA'), pre_HoVaTen, FailureHandling.CONTINUE_ON_FAILURE)
	WebUI.verifyElementText(findTestObject('EditAccount Objects/TextNgaySinh_EA'), pre_NgaySinh, FailureHandling.CONTINUE_ON_FAILURE)
	WebUI.verifyElementText(findTestObject('EditAccount Objects/TextSoDienThoai_EA'), pre_SoDienThoai, FailureHandling.CONTINUE_ON_FAILURE)
}

WebUI.refresh()
