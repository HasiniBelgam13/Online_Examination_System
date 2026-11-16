
@echo off
title Online Examination System

cd /d "%~dp0"

echo =====================================
echo    ONLINE EXAMINATION SYSTEM
echo =====================================

set /p DB_PASSWORD=Enter MySQL exam_app password: 

echo Starting application...

mvn exec:java "-Dexec.mainClass=com.example.App"

pause
