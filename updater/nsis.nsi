; example1.nsi
;
; This script is perhaps one of the simplest NSIs you can make. All of the
; optional settings are left to their default settings. The installer simply 
; prompts the user asking them where to install, and drops a copy of example1.nsi
; there. 

;--------------------------------

; The name of the installer
Name "Forest Defense"

; The file to write
OutFile "fdsetup.exe"

; Request application privileges for Windows Vista
RequestExecutionLevel user

; Build Unicode installer
Unicode True

; The default installation directory
InstallDir $APPDATA\ienokih\ForestDefense\

; Text above setup
DirText "This will install Forest Defense on your computer. Forest Defense requires Java to be installed. Get it from java.com/download"

;--------------------------------

; Pages

Page directory
Page instfiles

;--------------------------------

; JRE Stuff
Section "JRE" SEC01 
  Call DetectJRE
SectionEnd

;Functions
Function DetectJRE
  StrCpy $1 "SOFTWARE\JavaSoft\Java Runtime Environment"  
  StrCpy $2 0  
  ReadRegStr $2 HKLM "$1" "CurrentVersion"  
  StrCmp $2 "" DetectTry2   
  ReadRegStr $5 HKLM "$1\$2" "JavaHome"  
  StrCmp $5 "" DetectTry2  
  goto done  
  
  DetectTry2:  
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"  
  StrCmp $2 "" NoJava  
  ReadRegStr $5 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "JavaHome"  
  StrCmp $5 "" NoJava done  
  
  done:  
  ;All done.   
  ;MessageBox MB_OK "$2 JRE installed"
  
  NoJava:  
  ;Write the script to install Java here
  ;SetOutPath $TEMP 
  ;File "jreinstall.exe"  
  ;DetailPrint "Starting the JRE installation"     
  ;ExecWait "$TEMP\jreinstall.exe"
  DetailPrint "No java installation found."
 FunctionEnd


; The stuff to install
Section "" ;No components page, name is not important

  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
; Tell the compiler to write an uninstaller and to look for a "Uninstall" section 
WriteUninstaller $INSTDIR\Uninstall.exe

; Shortcuts
CreateDirectory "$SMPROGRAMS\ienokih"
CreateShortCut "$SMPROGRAMS\ienokih\Run ForestDefense.lnk" "$INSTDIR\start.exe"
CreateShortCut "$SMPROGRAMS\ienokih\Uninstall ForestDefense.lnk" "$INSTDIR\Uninstall.exe"

  ; Put file there
  File ForestDefense\start.bat
  File ForestDefense\start.exe
  File ForestDefense\background.png
  File ForestDefense\getdown-launcher.jar
  File ForestDefense\getdown.txt

  
SectionEnd ; end the section

Section "Uninstall"

Delete "$SMPROGRAMS\ienokih\Run ForestDefense.lnk"
Delete "$SMPROGRAMS\ienokih\Uninstall ForestDefense.lnk"
RMDIR "$SMPROGRAMS\ienokih"
Delete $INSTDIR\Uninstall.exe
RMDir /r $INSTDIR
;RMDir /r $INSTDIR


SectionEnd 
