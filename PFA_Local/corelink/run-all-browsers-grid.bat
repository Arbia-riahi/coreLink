@echo off
title Selenium Standalone Grid - 15 Parallel Sessions

echo ================================================
echo 🚀 LANCEMENT SELENIUM GRID STANDALONE (15 sessions)
echo ================================================

start "SELENIUM GRID" cmd /k java -jar selenium-server-4.38.0.jar standalone --max-sessions 15

timeout /t 3 > nul

echo ================================================
echo 🚀 LANCEMENT DES TESTS
echo ================================================

start "Chrome GRID" cmd /k mvn test -Pchrome-grid
start "Firefox GRID" cmd /k mvn test -Pfirefox-grid
start "Edge GRID" cmd /k mvn test -Pedge-grid

pause
