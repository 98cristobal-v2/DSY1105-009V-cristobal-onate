@echo off
title CIM UC - Ejecutar Pruebas Unitarias
echo ================================================
echo   Ejecutando Pruebas Unitarias (JUnit 4)
echo ================================================
call gradlew.bat testDebugUnitTest --info
echo.
echo ================================================
echo   Pruebas finalizadas. Revisa los resultados arriba.
echo ================================================
pause
