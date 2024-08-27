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

//WebUI.callTestCase(findTestCase('Test Requiment/Log in'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.navigateToUrl('http://localhost:3000/admin/group-category')

// Get first row states in list
String pre_GroupTypeName = WebUI.getText(findTestObject('CreateGroupType Objects/_71_GroupTypeName_CheckText_CGT'))

String pre_GroupTypeDescription = WebUI.getText(findTestObject('CreateGroupType Objects/_72_GroupTypeDescription_CheckText_CGT'))

// Open dialog
WebUI.click(findTestObject('CreateGroupType Objects/_1_AddType_Button_CGT'))

// Create new group type
WebUI.sendKeys(findTestObject('CreateGroupType Objects/_2_GroupTypeName_Input_CGT'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
WebUI.setText(findTestObject('CreateGroupType Objects/_2_GroupTypeName_Input_CGT'), GroupType_Name)

WebUI.sendKeys(findTestObject('CreateGroupType Objects/_3_GroupTypeDescription_Input_CGT'), Keys.chord(Keys.CONTROL, 'a', Keys.DELETE))
WebUI.setText(findTestObject('CreateGroupType Objects/_3_GroupTypeDescription_Input_CGT'), GroupType_Description)

if (GroupType_IconType == 'define') {
	if (GroupType_DefineIcons == '1') {
	}
	
	if (GroupType_DefineIcons == '2') {
		WebUI.click(findTestObject('CreateGroupType Objects/_4_PencilIcon_Button_CGT'))
		WebUI.click(findTestObject('CreateGroupType Objects/_41_AnotherDefined_Icon_CGT'))
	}
}
else if (GroupType_IconType == 'upload' && Message_Text != 'định dạng') {
	WebUI.click(findTestObject('CreateGroupType Objects/_4_PencilIcon_Button_CGT'))
	WebUI.click(findTestObject('CreateGroupType Objects/_42_UploadNewIcon_Button_CGT'))
	WebUI.uploadFile(findTestObject('CreateGroupType Objects/_43_UploadFile_Input_CGT'), GroupType_IconPath)
	WebUI.click(findTestObject('CreateGroupType Objects/_44_ConfirmUpload_Button_CGT'))
}
else if (GroupType_IconType == 'upload' && Message_Text == 'định dạng') {
	WebUI.click(findTestObject('CreateGroupType Objects/_4_PencilIcon_Button_CGT'))
	WebUI.click(findTestObject('CreateGroupType Objects/_42_UploadNewIcon_Button_CGT'))
	WebUI.uploadFile(findTestObject('CreateGroupType Objects/_43_UploadFile_Input_CGT'), GroupType_IconPath)

	WebUI.verifyElementPresent(findTestObject('CreateGroupType Objects/_7_SuccessMessage_CheckText_CGT'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	String actualMessage = WebUI.getText(findTestObject('CreateGroupType Objects/_7_SuccessMessage_CheckText_CGT'), FailureHandling.OPTIONAL)
	WebUI.verifyMatch(actualMessage, '.*' + Message_Text + '.*', true, FailureHandling.STOP_ON_FAILURE)
}

if (Message_Text != 'định dạng') {
	WebUI.click(findTestObject('CreateGroupType Objects/_5_Permission_ComboBox_CGT'))
	if (GroupType_Permissions != '0') {
		for (int i = 0; i < GroupType_Permissions.toInteger(); i++) {
			WebUI.sendKeys(findTestObject('CreateGroupType Objects/_5_Permission_ComboBox_CGT'), Keys.chord(Keys.DOWN, Keys.ENTER))
		}
	}
	WebUI.click(findTestObject('CreateGroupType Objects/_51_UnfocusCombobox_CGT'))

	WebUI.click(findTestObject('CreateGroupType Objects/_6_ConfirmCreate_Button_CGT'))
}



// Verification logic
boolean isGroupTypeNameValid = GroupType_Name.length() > 0 && GroupType_Name.length() <= 100
boolean isGroupTypeDescriptionValid = GroupType_Description.length() <= 200
boolean expectedResult = Expected_Output == 'true'
boolean isDuplicateNameError = Message_Text == 'đã tồn tại'
println('isGroupTypeNameValid: ' + isGroupTypeNameValid)
println('isGroupTypeDescriptionValid: ' + isGroupTypeDescriptionValid)
println('Expected_Output: ' + Expected_Output)
println('Message_Text: ' + Message_Text)
println('expectedResult: ' + expectedResult)
println('isDuplicateNameError: ' + isDuplicateNameError)

if (isGroupTypeNameValid && isGroupTypeDescriptionValid && expectedResult && !isDuplicateNameError) {
	// Check if the success or error message appears
	WebUI.verifyElementPresent(findTestObject('CreateGroupType Objects/_7_SuccessMessage_CheckText_CGT'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	String actualMessage = WebUI.getText(findTestObject('CreateGroupType Objects/_7_SuccessMessage_CheckText_CGT'), FailureHandling.OPTIONAL)
	WebUI.verifyMatch(actualMessage, '.*' + Message_Text + '.*', true, FailureHandling.OPTIONAL)

	// Verify the group type is correctly created in the list
	WebUI.verifyElementText(findTestObject('CreateGroupType Objects/_71_GroupTypeName_CheckText_CGT'), GroupType_Name, FailureHandling.CONTINUE_ON_FAILURE)
	WebUI.verifyElementText(findTestObject('CreateGroupType Objects/_72_GroupTypeDescription_CheckText_CGT'), GroupType_Description, FailureHandling.CONTINUE_ON_FAILURE)
} else {
	// Verify error messages are displayed
	if (!isGroupTypeNameValid) {
		WebUI.verifyElementPresent(findTestObject('CreateGroupType Objects/_21_GroupTypeName_ErrorText_CGT'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	}
	if (!isGroupTypeDescriptionValid) {
		WebUI.verifyElementPresent(findTestObject('CreateGroupType Objects/_22_GroupTypeDescription_ErrorText_CGT'), 0, FailureHandling.CONTINUE_ON_FAILURE)
	}
	if (isDuplicateNameError) {
		WebUI.verifyElementPresent(findTestObject('CreateGroupType Objects/_7_SuccessMessage_CheckText_CGT'), 0, FailureHandling.CONTINUE_ON_FAILURE)
		String actualMessage = WebUI.getText(findTestObject('CreateGroupType Objects/_7_SuccessMessage_CheckText_CGT'), FailureHandling.OPTIONAL)
		WebUI.verifyMatch(actualMessage, '.*' + Message_Text + '.*', true, FailureHandling.OPTIONAL)
	}

	// Ensure no incorrect creation
	WebUI.verifyElementText(findTestObject('CreateGroupType Objects/_71_GroupTypeName_CheckText_CGT'), pre_GroupTypeName, FailureHandling.CONTINUE_ON_FAILURE)
	WebUI.verifyElementText(findTestObject('CreateGroupType Objects/_72_GroupTypeDescription_CheckText_CGT'), pre_GroupTypeDescription, FailureHandling.CONTINUE_ON_FAILURE)
}

WebUI.refresh()