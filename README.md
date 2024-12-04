This project was done using references from amir650's BlackWidow chess.

Repo link: https://github.com/amir650/BlackWidow-Chess

To compile the project, you can do either:

1. Put the directory into a folder (named src) and use the following terminal command while in the parent folder (note that the art folder needs to be outside the src folder and in the parent folder to work):
   
  javac -d out src\com\Chess3D\*.java src\com\Chess3D\core\board\*.java src\com\Chess3D\gui\*.java src\com\Chess3D\core\pieces\*.java src\com\Chess3D\core\player\*.java src\com\Chess3D\core\pgn\*.java src\com\Chess3D\core\*.java     src\com\Chess3D\core\player\ai\*.java
  
  java -cp out com.Chess3D.ChessGame 
  
2. Or simply open the Chess3D folder in VSCODE and click run


