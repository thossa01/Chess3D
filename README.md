With this prohject, I:

•	Developed a chess engine with move highlighters and board flipping capabilities to enhance player interaction and provide a dynamic, customizable user interface.
•	Implemented a computer AI algorithm capable of mimicking human-like moves, enabling users to play against a simulated computer opponent with adjustable difficulty levels.
•	Engineered a real-time move visualization feature, allowing users to easily track and highlight legal and invalid moves on the chessboard.


To compile the project, you can do either:

1. Put the directory into a folder (named src) and use the following terminal command while in the parent folder (note that the art folder needs to be outside the src folder and in the parent folder to work):
   
  javac -d out src\com\Chess3D\*.java src\com\Chess3D\core\board\*.java src\com\Chess3D\gui\*.java src\com\Chess3D\core\pieces\*.java src\com\Chess3D\core\player\*.java src\com\Chess3D\core\pgn\*.java src\com\Chess3D\core\*.java     src\com\Chess3D\core\player\ai\*.java
  
  java -cp out com.Chess3D.ChessGame 
  
2. Or simply open the Chess3D folder in VSCODE and click run


