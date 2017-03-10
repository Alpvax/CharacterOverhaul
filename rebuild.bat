@echo off
:;if /I "%COMSPEC%" == %CMDCMDLINE% "gradle setupdecompworkspace eclipse --refresh-dependencies"
gradle setupdecompworkspace eclipse --refresh-dependencies
if /I not "%COMSPEC%" == %CMDCMDLINE% pause