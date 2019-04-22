@chcp 65001
@echo Date: %1
@echo copy ".\deployed\g2d_studio.jar" "..\..\BattleSystemCSharp\Common\Tools\lib\"
@copy /Y   ".\deployed\g2d_studio.jar" "..\..\BattleSystemCSharp\Common\Tools\lib\"
@echo done