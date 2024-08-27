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

// Get previous states
String pre_GroupName = WebUI.getText(findTestObject('EditGroupInformation Objects/GroupName_Text_EGI'))
String pre_GroupType = WebUI.getText(findTestObject('EditGroupInformation Objects/GroupType_Text_EGI'))
String pre_GroupStart = WebUI.getText(findTestObject('EditGroupInformation Objects/GroupStart_Text_EGI'))
String pre_GroupEnd = WebUI.getText(findTestObject('EditGroupInformation Objects/GroupEnd_Text_EGI'))

// Open menu
WebUI.click(findTestObject('EditGroupInformation Objects/OpenMenu_3DotsButton_EGI'))
WebUI.click(findTestObject('EditGroupInformation Objects/OpenDialog_EditButton_EGI'))

// Edit group information
WebUI.sendKeys(findTestObject('EditGroupInformation Objects/GroupName_Input_EGI'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
WebUI.setText(findTestObject('EditGroupInformation Objects/GroupName_Input_EGI'), Group_Name)

WebUI.sendKeys(findTestObject('EditGroupInformation Objects/GroupDescription_TextArea_EGI'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
WebUI.setText(findTestObject('EditGroupInformation Objects/GroupDescription_TextArea_EGI'), Group_Description)

WebUI.click(findTestObject('EditGroupInformation Objects/GroupType_ComboBox_EGI'))
if (Group_Type == '') {
    WebUI.sendKeys(findTestObject('EditGroupInformation Objects/GroupType_ComboBox_EGI'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
}


WebUI.sendKeys(findTestObject('EditGroupInformation Objects/GroupStartDate_Input_EGI'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
if (Start_Time != '') {
    String[] StartTimeSubStrings = Start_Time.split('/')
    for (def var : (0..2)) {
        WebUI.sendKeys(findTestObject('EditGroupInformation Objects/GroupStartDate_Input_EGI'), StartTimeSubStrings[var])
    }
}

WebUI.sendKeys(findTestObject('EditGroupInformation Objects/GroupEndDate_Input_EGI'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
if (End_Time != '') {
    String[] EndTimeSubStrings = End_Time.split('/')
    for (def var : (0..2)) {
        WebUI.sendKeys(findTestObject('EditGroupInformation Objects/GroupEndDate_Input_EGI'), EndTimeSubStrings[var])
    }
}

// Verification logic
boolean isGroupNameValid = Group_Name.length() > 0 && Group_Name.length() <= 100
boolean isGroupDescriptionValid = Group_Description.length() <= 200
boolean isGroupTypeValid = Group_Type.length() != 0
boolean isStartDateValid = Start_Time.matches('(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/\\d{4}')
boolean isEndDateValid = End_Time.matches('(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/\\d{4}')
boolean isDateOrderValid = true

if (isStartDateValid && isEndDateValid) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern('dd/MM/yyyy')
    LocalDate startDate = LocalDate.parse(Start_Time, formatter)
    LocalDate endDate = LocalDate.parse(End_Time, formatter)
    isDateOrderValid = !startDate.isAfter(endDate)
}

if (isGroupNameValid && isGroupDescriptionValid && isStartDateValid && isEndDateValid && isDateOrderValid && isGroupTypeValid) {
    // Confirm the changes
    WebUI.click(findTestObject('EditGroupInformation Objects/Confirm_Button_EGI'))
    
    WebUI.verifyElementPresent(findTestObject('EditGroupInformation Objects/Success_Message_EGI'), 0, FailureHandling.CONTINUE_ON_FAILURE)
    String actualText = WebUI.getText(findTestObject('EditGroupInformation Objects/Success_Message_EGI'), FailureHandling.OPTIONAL)
    String regex = ('.*' + Message_Text) + '.*'
    WebUI.verifyMatch(actualText, regex, true, FailureHandling.OPTIONAL)

    // Check if the group information is updated
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupName_Text_EGI'), Group_Name, FailureHandling.CONTINUE_ON_FAILURE)
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupType_Text_EGI'), Group_Type, FailureHandling.CONTINUE_ON_FAILURE)
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupStart_Text_EGI'), Start_Time, FailureHandling.CONTINUE_ON_FAILURE)
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupEnd_Text_EGI'), End_Time, FailureHandling.CONTINUE_ON_FAILURE)
} else {
    // Verify that the confirm button is disabled
    boolean isConfirmButtonEnabled = WebUI.verifyElementNotClickable(findTestObject('EditGroupInformation Objects/Confirm_Button_EGI'), FailureHandling.OPTIONAL)

    // If the confirm button is enabled, the test should fail
    if (isConfirmButtonEnabled) {
        WebUI.comment('Error: The confirm button should be disabled for invalid inputs.')
        assert false : 'Confirm button is clickable despite invalid inputs.'
    }

    // Verify error messages if applicable
    if (!isGroupNameValid) {
        WebUI.verifyElementPresent(findTestObject('EditGroupInformation Objects/GroupName_ErrorText_EGI'), 0, FailureHandling.CONTINUE_ON_FAILURE)
    }
    if (!isGroupDescriptionValid) {
        WebUI.verifyElementPresent(findTestObject('EditGroupInformation Objects/GroupDescription_ErrorText_EGI'), 0, FailureHandling.CONTINUE_ON_FAILURE)
    }
    if (!isGroupTypeValid) {
        WebUI.verifyElementPresent(findTestObject('EditGroupInformation Objects/GroupType_ErrorText_EGI'), 0, FailureHandling.CONTINUE_ON_FAILURE)
    }
    if (!isStartDateValid) {
        WebUI.verifyElementPresent(findTestObject('EditGroupInformation Objects/GroupStart_ErrorText_EGI'), 0, FailureHandling.CONTINUE_ON_FAILURE)
    }
    if (!isEndDateValid) {
        WebUI.verifyElementPresent(findTestObject('EditGroupInformation Objects/GroupEnd_ErrorText_EGI'), 0, FailureHandling.CONTINUE_ON_FAILURE)
    }
    if (!isDateOrderValid) {
        WebUI.verifyElementPresent(findTestObject('EditGroupInformation Objects/GroupDateOrder_ErrorText_EGI'), 0, FailureHandling.CONTINUE_ON_FAILURE)
    }

    // Verify that the group information remains unchanged
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupName_Text_EGI'), pre_GroupName, FailureHandling.CONTINUE_ON_FAILURE)
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupType_Text_EGI'), pre_GroupType, FailureHandling.CONTINUE_ON_FAILURE)
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupStart_Text_EGI'), pre_GroupStart, FailureHandling.CONTINUE_ON_FAILURE)
    WebUI.verifyElementText(findTestObject('EditGroupInformation Objects/GroupEnd_Text_EGI'), pre_GroupEnd, FailureHandling.CONTINUE_ON_FAILURE)
}

WebUI.refresh()
