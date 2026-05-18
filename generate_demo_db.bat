@echo off
setlocal

cd /d "%~dp0"
echo [1/2] Compiling Java sources...
javac Main.java SeedDemoData.java academic\*.java datastorage\*.java enums\*.java exceptions\*.java research\*.java users\*.java
if errorlevel 1 (
  echo Compilation failed.
  exit /b 1
)

echo [2/2] Generating demo database mini_wsp_db.ser...
java SeedDemoData
if errorlevel 1 (
  echo Seed generation failed.
  exit /b 1
)

echo Done.
endlocal
