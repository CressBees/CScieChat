# CScieChat


# Undocumented tests (Not on the main testing doc)
### 16/05/23 - Crashes while offline
Expected outcome: no expected outcome
Actual outcome: Throws error and crashes
An attempt to connect to a URL while offline causes the program to crash, as an error is thrown with "throw new RuntimeException(e);" which exits the program

### 16/05/23 - Getting only local IP while offline
Expected outcome: Give private IP and explanation why a public IP can't be found
Actual outcome: Give private IP and explanation why a public IP can't be found